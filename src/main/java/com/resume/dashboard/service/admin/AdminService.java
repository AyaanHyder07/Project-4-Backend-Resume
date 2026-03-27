package com.resume.dashboard.service.admin;

import com.resume.dashboard.entity.ApprovalStatus;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.entity.VisibilityType;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.UserRepository;
import com.resume.dashboard.service.ResumeVersionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AdminService {

    private final ResumeRepository resumeRepository;
    private final ResumeVersionService resumeVersionService;
    private final UserRepository userRepository;

    public AdminService(ResumeRepository resumeRepository,
                        ResumeVersionService resumeVersionService,
                        UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.resumeVersionService = resumeVersionService;
        this.userRepository = userRepository;
    }

    public List<Resume> getAll() {
        return resumeRepository.findAll();
    }

    public List<Resume> getByStatus(ApprovalStatus status) {
        return resumeRepository.findByApprovalStatus(status);
    }

    public List<Resume> getPending() {
        return resumeRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }

    @Transactional
    public Resume approve(String resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (resume.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException("Only PENDING resumes can be approved");
        }

        resume.setApprovalStatus(ApprovalStatus.APPROVED);

        try {
            resumeVersionService.createVersion(
                    resume.getUserId(),
                    resume.getId(),
                    "Auto snapshot on admin approval"
            );
        } catch (RuntimeException ignored) {
        }

        String finalSlug = resolveApprovedSlug(resume);
        resume.setSlug(finalSlug);
        resume.setPublished(true);
        resume.setVisibility(VisibilityType.PUBLIC);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    public Resume reject(String resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (resume.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException("Only PENDING resumes can be rejected");
        }

        resume.setApprovalStatus(ApprovalStatus.REJECTED);
        resume.setPublished(false);
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setSlug(null);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    public Resume forceUnpublish(String resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        resume.setPublished(false);
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setSlug(null);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    public Resume updateSlug(String resumeId, String requestedSlug) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        String slug = normalizeSlugCandidate(requestedSlug);
        if (slug.length() < 3 || slug.length() > 40) {
            throw new IllegalArgumentException("Public URL must be between 3 and 40 characters.");
        }
        if (resumeRepository.existsBySlugAndIdNot(slug, resumeId)) {
            throw new IllegalStateException("This public URL is already taken.");
        }

        resume.setSlug(slug);
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public void delete(String resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        resumeRepository.delete(resume);
    }

    private String resolveApprovedSlug(Resume resume) {
        String existing = normalizeSlugCandidate(resume.getSlug());
        if (!existing.isBlank()) {
            if (resumeRepository.existsBySlugAndIdNot(existing, resume.getId())) {
                throw new IllegalStateException("This public URL is already taken.");
            }
            return existing;
        }

        User user = userRepository.findById(resume.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long publishedCount = resumeRepository.countByUserIdAndPublishedTrue(user.getId());
        String baseSlug = normalizeSlugCandidate(user.getUsername());
        String finalSlug = publishedCount == 0 ? baseSlug : baseSlug + "-v" + (publishedCount + 1);

        int counter = (int) publishedCount + 1;
        while (resumeRepository.existsBySlugAndIdNot(finalSlug, resume.getId())) {
            counter++;
            finalSlug = baseSlug + "-v" + counter;
        }
        return finalSlug;
    }

    private String normalizeSlugCandidate(String slug) {
        if (slug == null) {
            return "";
        }
        return slug.trim().toLowerCase()
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
