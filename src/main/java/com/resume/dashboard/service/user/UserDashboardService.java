package com.resume.dashboard.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resume.dashboard.dto.dashboard.*;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Subscription;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.service.SubscriptionService;

@Service
public class UserDashboardService {

    private final ResumeRepository resumeRepository;
    private final SubscriptionService subscriptionService;

    public UserDashboardService(
            ResumeRepository resumeRepository,
            SubscriptionService subscriptionService) {

        this.resumeRepository = resumeRepository;
        this.subscriptionService = subscriptionService;
    }

    public DashboardResponse getDashboard(String userId) {

        // Always fetch active subscription properly
        Subscription subscription =
                subscriptionService.getActiveSubscription(userId);

        List<Resume> resumes = resumeRepository.findByUserId(userId);

        int resumeLimit = subscription.getResumeLimit();
        int publicLimit = subscription.getPublicLinkLimit();

        long publishedCount = resumes.stream()
                .filter(Resume::isPublished)
                .count();

        long totalViews = resumes.stream()
                .mapToLong(Resume::getViewCount)
                .sum();

        int remainingResumes =
                Math.max(resumeLimit - resumes.size(), 0);

        int remainingPublicSlots =
                Math.max(publicLimit - (int) publishedCount, 0);

        DashboardResponse response = new DashboardResponse();

        response.setCurrentPlan(subscription.getPlan().name());

        response.setResumeLimit(resumeLimit);
        response.setTotalResumes(resumes.size());
        response.setRemainingResumes(remainingResumes);

        response.setPublicLinkLimit(publicLimit);
        response.setPublishedCount(publishedCount);
        response.setRemainingPublicSlots(remainingPublicSlots);

        response.setTotalViews(totalViews);

        List<ResumeSummaryDTO> allResumes = resumes.stream()
                .map(this::mapSummary)
                .collect(Collectors.toList());

        response.setResumes(allResumes);

        // Set recent resumes (last 5 by updated date)
        response.setRecentResumes(
                allResumes.stream()
                        .sorted((a, b) -> {
                            java.time.Instant aTime = a.getUpdatedAt() != null ? a.getUpdatedAt() : java.time.Instant.EPOCH;
                            java.time.Instant bTime = b.getUpdatedAt() != null ? b.getUpdatedAt() : java.time.Instant.EPOCH;
                            return bTime.compareTo(aTime);
                        })
                        .limit(5)
                        .collect(Collectors.toList())
        );

        return response;
    }

    private ResumeSummaryDTO mapSummary(Resume resume) {

        ResumeSummaryDTO dto = new ResumeSummaryDTO();
        dto.setId(resume.getId());
        dto.setTitle(resume.getTitle());
        dto.setSlug(resume.getSlug());
        dto.setPublished(resume.isPublished());
        dto.setVisibility(resume.getVisibility());
        dto.setApprovalStatus(resume.getApprovalStatus());
        dto.setViewCount(resume.getViewCount());
        dto.setUpdatedAt(resume.getUpdatedAt());

        return dto;
    }
}