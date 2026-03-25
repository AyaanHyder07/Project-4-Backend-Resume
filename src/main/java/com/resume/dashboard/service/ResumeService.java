package com.resume.dashboard.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resume.dashboard.dto.resume.CreateResumeRequest;
import com.resume.dashboard.entity.ApprovalStatus;
import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.Theme;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.entity.VisibilityType;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.LayoutRepository;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.ThemeRepository;
import com.resume.dashboard.repository.UserRepository;

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
    private final UserProfileService userProfileService;
    private final ProfessionTaxonomyService professionTaxonomyService;

    public ResumeService(
            ResumeRepository resumeRepository,
            SubscriptionService subscriptionService,
            TemplateRepository templateRepository,
            LayoutRepository layoutRepository,
            ThemeRepository themeRepository,
            ResumeVersionService resumeVersionService,
            UserRepository userRepository,
            PortfolioSectionConfigService sectionService,
            UserProfileService userProfileService,
            ProfessionTaxonomyService professionTaxonomyService) {
        this.resumeRepository = resumeRepository;
        this.subscriptionService = subscriptionService;
        this.templateRepository = templateRepository;
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
        this.resumeVersionService = resumeVersionService;
        this.userRepository = userRepository;
        this.sectionService = sectionService;
        this.userProfileService = userProfileService;
        this.professionTaxonomyService = professionTaxonomyService;
    }

    @Transactional
    public Resume createResume(String userId, CreateResumeRequest request) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long existingCount = resumeRepository.countByUserId(userId);
        subscriptionService.validateResumeCreation(userId, existingCount);

        if (!subscriptionService.isTemplateAllowed(userId, request.getTemplateId())) {
            throw new IllegalStateException("Your subscription plan does not allow this template. Please upgrade.");
        }

        Template template = templateRepository.findByIdAndActiveTrue(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        Layout layout = layoutRepository.findById(template.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        String themeId = request.getThemeOverrideId() != null ? request.getThemeOverrideId() : template.getDefaultThemeId();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        ProfessionType professionType = professionTaxonomyService.resolveProfessionType(request.getProfessionType());
        MotionPreset selectedMotion = request.getMotionPreset() != null
                ? request.getMotionPreset()
                : (template.getDefaultMotionPreset() != null ? template.getDefaultMotionPreset() : layout.getDefaultMotionPreset());

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle(request.getTitle().trim());
        resume.setProfessionType(professionType.name());
        resume.setProfessionCategory(professionType.getCategory());
        resume.setTemplateId(template.getId());
        resume.setTemplateVersion(template.getVersion());
        resume.setLayoutId(layout.getId());
        resume.setLayoutVersion(layout.getVersion());
        resume.setThemeId(theme.getId());
        resume.setThemeVersion(theme.getVersion());
        resume.setMotionPreset(selectedMotion);
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setPublished(false);
        resume.setApprovalStatus(ApprovalStatus.DRAFT);
        resume.setVersion(1);
        resume.setViewCount(0L);
        resume.setContent(request.getContent());
        resume.setCreatedAt(Instant.now());
        resume.setUpdatedAt(Instant.now());

        Resume saved = resumeRepository.save(resume);
        sectionService.initializeDefaultSections(saved.getId());
        userProfileService.createEmptyProfile(saved.getId());
        return saved;
    }

    @Transactional
    public Resume publishResume(String userId, String resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);

        if (resume.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new IllegalStateException("Resume must be approved before publishing");
        }
        if (!subscriptionService.isSubscriptionActive(userId)) {
            throw new IllegalStateException("Your subscription is inactive or expired");
        }

        long publishedCount = resumeRepository.countByUserIdAndPublishedTrue(userId);
        subscriptionService.validatePublicPublish(userId, publishedCount);
        resumeVersionService.createVersionInternal(resumeId, resume.getUserId(), "Auto snapshot on publish");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String baseSlug = user.getUsername().toLowerCase();
        String finalSlug = publishedCount == 0 ? baseSlug : baseSlug + "-v" + (publishedCount + 1);

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

    public Resume updateMeta(String userId, String resumeId, String title, String profession) {
        Resume resume = getOwnedResume(userId, resumeId);
        ProfessionType professionType = professionTaxonomyService.resolveProfessionType(profession);
        resume.setTitle(title);
        resume.setProfessionType(professionType.name());
        resume.setProfessionCategory(professionType.getCategory());
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

    public List<Resume> getAllByUser(String userId) {
        return resumeRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
