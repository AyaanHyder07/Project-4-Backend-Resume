package com.resume.dashboard.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resume.dashboard.dto.resume.CreateResumeRequest;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.*;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final SubscriptionService subscriptionService;
    private final TemplateRepository templateRepository;
    private final LayoutRepository layoutRepository;
    private final ThemeRepository themeRepository;
    private final ResumeVersionService resumeVersionService;
    private final UserRepository userRepository;
    private final PortfolioSectionConfigService sectionService;

    public ResumeService(
            ResumeRepository resumeRepository,
            SubscriptionService subscriptionService,
            TemplateRepository templateRepository,
            LayoutRepository layoutRepository,
            ThemeRepository themeRepository,
            ResumeVersionService resumeVersionService,
            UserRepository userRepository,
            PortfolioSectionConfigService sectionService) {

        this.resumeRepository = resumeRepository;
        this.subscriptionService = subscriptionService;
        this.templateRepository = templateRepository;
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
        this.resumeVersionService = resumeVersionService;
        this.userRepository = userRepository;
        this.sectionService = sectionService;
    }

    /* =========================================================
       CREATE RESUME
    ========================================================= */
    @Transactional
    public Resume createResume(String userId, CreateResumeRequest request) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long existingCount = resumeRepository.countByUserId(userId);

        // 🔥 Enforce resume limit per plan (FREE=1, BASIC=1, PRO=2, PREMIUM=3)
        subscriptionService.validateResumeCreation(userId, existingCount);

        // 🔥 Enforce template plan gating — uses single canonical logic in SubscriptionService
        if (!subscriptionService.isTemplateAllowed(userId, request.getTemplateId())) {
            throw new IllegalStateException(
                    "Your subscription plan does not allow this template. Please upgrade.");
        }

        Template template = templateRepository
                .findByIdAndActiveTrue(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        Layout layout = layoutRepository.findById(template.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        String themeId = request.getThemeOverrideId() != null
                ? request.getThemeOverrideId()
                : template.getDefaultThemeId();

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle(request.getTitle().trim());
        resume.setProfessionType(request.getProfessionType());

        resume.setTemplateId(template.getId());
        resume.setTemplateVersion(template.getVersion());
        resume.setLayoutId(layout.getId());
        resume.setLayoutVersion(layout.getVersion());
        resume.setThemeId(theme.getId());
        resume.setThemeVersion(theme.getVersion());

        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setPublished(false);
        resume.setApprovalStatus(ApprovalStatus.DRAFT);
        resume.setVersion(1);
        resume.setViewCount(0L);

        resume.setCreatedAt(Instant.now());
        resume.setUpdatedAt(Instant.now());

        Resume saved = resumeRepository.save(resume);

        sectionService.initializeDefaultSections(saved.getId());

        return saved;
    }

    /* =========================================================
       PUBLISH RESUME
    ========================================================= */
    @Transactional
    public Resume publishResume(String userId, String resumeId) {

        Resume resume = getOwnedResume(userId, resumeId);

        if (resume.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new IllegalStateException("Resume must be approved before publishing");
        }

        // 🔥 Check active subscription before publishing
        if (!subscriptionService.isSubscriptionActive(userId)) {
            throw new IllegalStateException("Your subscription is inactive or expired");
        }

        long publishedCount = resumeRepository.countByUserIdAndPublishedTrue(userId);

        // 🔥 Enforce public link limit per plan (FREE=0, BASIC=1, PRO=1, PREMIUM=2)
        subscriptionService.validatePublicPublish(userId, publishedCount);

        // Create an auto-snapshot on publish — NOTE: this is an INTERNAL system snapshot,
        // not a user-triggered version save, so it bypasses validateVersioning intentionally.
        // User-facing version creation (ResumeVersionService.createVersion) enforces the plan check.
        resumeVersionService.createVersionInternal(
                resumeId,
                resume.getUserId(),
                "Auto snapshot on publish"
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String baseSlug = user.getUsername().toLowerCase();
        String finalSlug;

        if (publishedCount == 0) {
            finalSlug = baseSlug;
        } else {
            finalSlug = baseSlug + "-v" + (publishedCount + 1);
        }

        int counter = (int) publishedCount + 1;
        while (resumeRepository.existsBySlug(finalSlug)) {
            counter++;
            finalSlug = baseSlug + "-v" + counter;
        }

        resume.setSlug(finalSlug);
        resume.setPublished(true);
        resume.setVisibility(VisibilityType.PUBLIC);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    /* =========================================================
       OTHER METHODS
    ========================================================= */

    public Resume updateMeta(String userId, String resumeId, String title, String profession) {

        Resume resume = getOwnedResume(userId, resumeId);
        resume.setTitle(title);
        resume.setProfessionType(profession);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    public Resume changeTheme(String userId, String resumeId, String newThemeId) {

        Resume resume = getOwnedResume(userId, resumeId);

        Theme theme = themeRepository.findById(newThemeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        resume.setThemeId(theme.getId());
        resume.setThemeVersion(theme.getVersion());
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    public Resume submitForApproval(String userId, String resumeId) {

        Resume resume = getOwnedResume(userId, resumeId);

        if (resume.getApprovalStatus() != ApprovalStatus.DRAFT) {
            throw new IllegalStateException("Only draft resumes can be submitted");
        }

        resume.setApprovalStatus(ApprovalStatus.PENDING);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    public Resume unpublishResume(String userId, String resumeId) {

        Resume resume = getOwnedResume(userId, resumeId);
        resume.setPublished(false);
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    public Resume getByIdForOwner(String userId, String resumeId) {
        return getOwnedResume(userId, resumeId);
    }

    public Resume getPublicBySlug(String slug) {

        Resume resume = resumeRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        incrementViewCount(resume);
        return resume;
    }

    public void deleteResume(String userId, String resumeId) {

        Resume resume = getOwnedResume(userId, resumeId);
        resumeRepository.delete(resume);
    }

    private Resume getOwnedResume(String userId, String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return resume;
    }

    private void incrementViewCount(Resume resume) {
        resume.setViewCount(resume.getViewCount() + 1);
        resumeRepository.save(resume);
    }
}