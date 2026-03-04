package com.resume.dashboard.service.admin;

import com.resume.dashboard.entity.*;
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

    /* =========================================================
       GET ALL
    ========================================================= */
    public List<Resume> getAll() {
        return resumeRepository.findAll();
    }

    public List<Resume> getByStatus(ApprovalStatus status) {
        return resumeRepository.findByApprovalStatus(status);
    }

    public List<Resume> getPending() {
        return resumeRepository.findByApprovalStatus(ApprovalStatus.PENDING);
    }

    /* =========================================================
       APPROVE + AUTO PUBLISH
    ========================================================= */
    @Transactional
    public Resume approve(String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        if (resume.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING resumes can be approved");
        }

        // 1️⃣ Set Approved
        resume.setApprovalStatus(ApprovalStatus.APPROVED);

        // 2️⃣ Snapshot version before publish
        resumeVersionService.createVersion(
                resume.getUserId(),
                resume.getId(),
                "Auto snapshot on admin approval"
        );

        // 3️⃣ Generate slug
        User user = userRepository.findById(resume.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        long publishedCount =
                resumeRepository.countByUserIdAndPublishedTrue(user.getId());

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

        // 4️⃣ Auto Publish
        resume.setSlug(finalSlug);
        resume.setPublished(true);
        resume.setVisibility(VisibilityType.PUBLIC);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    /* =========================================================
       REJECT
    ========================================================= */
    public Resume reject(String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        if (resume.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING resumes can be rejected");
        }

        resume.setApprovalStatus(ApprovalStatus.REJECTED);
        resume.setPublished(false);
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setSlug(null);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    /* =========================================================
       FORCE UNPUBLISH
    ========================================================= */
    public Resume forceUnpublish(String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        resume.setPublished(false);
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setSlug(null);
        resume.setUpdatedAt(Instant.now());

        return resumeRepository.save(resume);
    }

    /* =========================================================
       DELETE
    ========================================================= */
    public void delete(String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        resumeRepository.delete(resume);
    }
}