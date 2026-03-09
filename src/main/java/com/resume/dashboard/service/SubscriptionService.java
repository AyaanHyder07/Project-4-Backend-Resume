package com.resume.dashboard.service;

import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.Subscription;
import com.resume.dashboard.entity.SubscriptionPlan;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.SubscriptionPlanRepository;
import com.resume.dashboard.repository.SubscriptionRepository;
import com.resume.dashboard.repository.TemplateRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository; // ✅ reads limits from DB
    private final TemplateRepository templateRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               SubscriptionPlanRepository subscriptionPlanRepository,
                               TemplateRepository templateRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.templateRepository = templateRepository;
    }

    /* =======================================================
       CORE: GET ACTIVE SUBSCRIPTION (AUTO FREE FALLBACK)
    ======================================================= */
    public Subscription getActiveSubscription(String userId) {

        Subscription sub = subscriptionRepository
                .findByUserIdAndActiveTrue(userId)
                .orElseGet(() -> createSubscription(userId, PlanType.FREE, null));

        if (sub.getEndDate() != null && sub.getEndDate().isBefore(Instant.now())) {
            sub.setActive(false);
            sub.setUpdatedAt(Instant.now());
            subscriptionRepository.save(sub);
            throw new RuntimeException("Subscription expired. Please upgrade your plan.");
        }

        return sub;
    }

    /* =======================================================
       CREATE / UPGRADE SUBSCRIPTION
       ⚠️  INTERNAL USE ONLY — called by PaymentService after
       confirmed payment. Never expose this directly to users.
    ======================================================= */
    public Subscription createSubscription(String userId,
                                           PlanType plan,
                                           Instant endDate) {

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

        applyPlanLimits(sub, plan); // reads from DB — no hardcoded limits

        sub.setCreatedAt(Instant.now());
        sub.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(sub);
    }

    /* =======================================================
       PLAN LIMIT CONFIGURATION — DB DRIVEN
       Previously limits were hardcoded here. Now they are
       read from the subscription_plans collection.
       Admin can update price/limits via API without redeploying.
    ======================================================= */
    private void applyPlanLimits(Subscription sub, PlanType plan) {

        SubscriptionPlan planConfig = subscriptionPlanRepository
                .findByPlanType(plan)
                .orElseThrow(() -> new RuntimeException(
                        "Plan config missing for: " + plan
                        + ". Seed the subscription_plans collection."));

        sub.setResumeLimit(planConfig.getResumeLimit());
        sub.setPublicLinkLimit(planConfig.getPublicLinkLimit());
        sub.setVersioningEnabled(planConfig.isVersioningEnabled());
        sub.setThemeCustomizationEnabled(planConfig.isThemeCustomizationEnabled());
    }

    /* =======================================================
       VALIDATIONS
    ======================================================= */
    public void validateResumeCreation(String userId, long currentCount) {
        Subscription sub = getActiveSubscription(userId);
        if (currentCount >= sub.getResumeLimit()) {
            throw new RuntimeException(
                    "Resume limit reached for plan: " + sub.getPlan()
                    + ". Upgrade to create more resumes.");
        }
    }

    public void validatePublicPublish(String userId, long publishedCount) {
        Subscription sub = getActiveSubscription(userId);
        if (sub.getPublicLinkLimit() == 0) {
            throw new RuntimeException(
                    "Public portfolio links are not available on the FREE plan. Please upgrade.");
        }
        if (publishedCount >= sub.getPublicLinkLimit()) {
            throw new RuntimeException(
                    "Public portfolio limit reached for plan: " + sub.getPlan()
                    + ". Upgrade to publish more portfolios.");
        }
    }

    public void validateVersioning(String userId) {
        Subscription sub = getActiveSubscription(userId);
        if (!sub.isVersioningEnabled()) {
            throw new RuntimeException(
                    "Resume versioning is not available on the " + sub.getPlan()
                    + " plan. Upgrade to PRO or PREMIUM.");
        }
    }

    public void validateThemeCustomization(String userId) {
        Subscription sub = getActiveSubscription(userId);
        if (!sub.isThemeCustomizationEnabled()) {
            throw new RuntimeException(
                    "Theme customization is not available on the " + sub.getPlan()
                    + " plan. Upgrade to PRO or PREMIUM.");
        }
    }

    public boolean isTemplateAllowed(String userId, String templateId) {
        Template template = templateRepository
                .findByIdAndActiveTrue(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        Subscription sub = getActiveSubscription(userId);
        return sub.getPlan().ordinal() >= template.getPlanLevel().ordinal();
    }

    public boolean isSubscriptionActive(String userId) {
        return subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .filter(sub -> sub.getEndDate() == null ||
                        sub.getEndDate().isAfter(Instant.now()))
                .isPresent();
    }

    public PlanType getCurrentPlan(String userId) {
        return getActiveSubscription(userId).getPlan();
    }

    public Subscription getSubscriptionDetails(String userId) {
        return getActiveSubscription(userId);
    }
}