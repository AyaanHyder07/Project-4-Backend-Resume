package com.resume.dashboard.service;

import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.Subscription;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.SubscriptionRepository;
import com.resume.dashboard.repository.TemplateRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TemplateRepository templateRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               TemplateRepository templateRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.templateRepository = templateRepository;
    }

    /* =======================================================
       CORE: GET ACTIVE SUBSCRIPTION (AUTO FREE FALLBACK)
    ======================================================= */
    public Subscription getActiveSubscription(String userId) {

        Subscription sub = subscriptionRepository
                .findByUserIdAndActiveTrue(userId)
                .orElseGet(() -> getOrCreateFree(userId));

        if (sub.getEndDate() != null &&
                sub.getEndDate().isBefore(Instant.now())) {

            sub.setActive(false);
            sub.setUpdatedAt(Instant.now());
            subscriptionRepository.save(sub);

            throw new RuntimeException("Subscription expired. Upgrade required.");
        }

        return sub;
    }

    /* =======================================================
       AUTO CREATE FREE PLAN
    ======================================================= */
    public Subscription getOrCreateFree(String userId) {

        return subscriptionRepository
                .findByUserIdAndActiveTrue(userId)
                .orElseGet(() -> createSubscription(userId, PlanType.FREE, null));
    }

    /* =======================================================
       CREATE / UPGRADE SUBSCRIPTION
    ======================================================= */
    public Subscription createSubscription(String userId,
                                           PlanType plan,
                                           Instant endDate) {

        // deactivate existing
        subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .ifPresent(existing -> {
                    existing.setActive(false);
                    existing.setUpdatedAt(Instant.now());
                    subscriptionRepository.save(existing);
                });

        Subscription sub = new Subscription();
        sub.setId(UUID.randomUUID().toString());
        sub.setUserId(userId);
        sub.setPlan(plan);
        sub.setStartDate(Instant.now());
        sub.setEndDate(endDate);
        sub.setActive(true);

        applyPlanLimits(sub, plan);

        sub.setCreatedAt(Instant.now());
        sub.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(sub);
    }

    /* =======================================================
       PLAN LIMIT CONFIGURATION
    ======================================================= */
    private void applyPlanLimits(Subscription sub, PlanType plan) {

        switch (plan) {

            case FREE -> {
                sub.setResumeLimit(1);
                sub.setPublicLinkLimit(0);
                sub.setVersioningEnabled(false);
            }

            case BASIC -> {
                sub.setResumeLimit(1);
                sub.setPublicLinkLimit(1);
                sub.setVersioningEnabled(false);
            }

            case PRO -> {
                sub.setResumeLimit(2);
                sub.setPublicLinkLimit(1);
                sub.setVersioningEnabled(true);
            }

            case PREMIUM -> {
                sub.setResumeLimit(3);
                sub.setPublicLinkLimit(2);
                sub.setVersioningEnabled(true);
            }
        }
    }

    /* =======================================================
       RESUME LIMIT VALIDATION
    ======================================================= */
    public void validateResumeCreation(String userId, long currentCount) {

        Subscription sub = getActiveSubscription(userId);

        if (currentCount >= sub.getResumeLimit()) {
            throw new RuntimeException(
                    "Resume limit reached for plan: " + sub.getPlan());
        }
    }

    /* =======================================================
       PUBLIC LINK VALIDATION
    ======================================================= */
    public void validatePublicPublish(String userId, long publishedCount) {

        Subscription sub = getActiveSubscription(userId);

        if (publishedCount >= sub.getPublicLinkLimit()) {
            throw new RuntimeException(
                    "Public portfolio limit reached for plan: " + sub.getPlan());
        }
    }

    /* =======================================================
       VERSIONING VALIDATION
    ======================================================= */
    public void validateVersioning(String userId) {

        Subscription sub = getActiveSubscription(userId);

        if (!sub.isVersioningEnabled()) {
            throw new RuntimeException(
                    "Versioning not allowed in your current plan");
        }
    }

    /* =======================================================
       TEMPLATE GATING
    ======================================================= */
    public boolean isTemplateAllowed(String userId, String templateId) {

        Template template = templateRepository
                .findByIdAndActiveTrue(templateId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Template not found"));

        Subscription sub = getActiveSubscription(userId);

        PlanType userPlan = sub.getPlan();
        PlanType requiredPlan = template.getPlanLevel();

        return switch (requiredPlan) {
            case FREE -> true;
            case BASIC -> userPlan == PlanType.BASIC
                    || userPlan == PlanType.PRO
                    || userPlan == PlanType.PREMIUM;
            case PRO -> userPlan == PlanType.PRO
                    || userPlan == PlanType.PREMIUM;
            case PREMIUM -> userPlan == PlanType.PREMIUM;
        };
    }

    /* =======================================================
       SIMPLE ACTIVE CHECK
    ======================================================= */
    public boolean isSubscriptionActive(String userId) {
        return subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .filter(sub -> sub.getEndDate() == null ||
                        sub.getEndDate().isAfter(Instant.now()))
                .isPresent();
    }

    /* =======================================================
       GET CURRENT PLAN (FOR FRONTEND USE)
    ======================================================= */
    public PlanType getCurrentPlan(String userId) {
        return getActiveSubscription(userId).getPlan();
    }
}