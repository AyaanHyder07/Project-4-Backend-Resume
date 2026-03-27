package com.resume.dashboard.service;

import com.resume.dashboard.entity.BillingCycle;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.Subscription;
import com.resume.dashboard.entity.SubscriptionPlan;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.SubscriptionPlanRepository;
import com.resume.dashboard.repository.SubscriptionRepository;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               SubscriptionPlanRepository subscriptionPlanRepository,
                               TemplateRepository templateRepository,
                               UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
    }

    public Subscription getActiveSubscription(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .map(this::syncAndValidateActiveSubscription)
                .orElseGet(() -> createFreeIfEligible(user));
    }

    public List<Subscription> getSubscriptionHistory(String userId) {
        return subscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Subscription createSubscription(String userId, PlanType plan, Instant endDate) {
        return createSubscription(userId, plan, null, endDate);
    }

    public Subscription createSubscription(String userId, PlanType plan, BillingCycle billingCycle, Instant endDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        SubscriptionPlan planConfig = getPlanConfig(plan);
        if (plan == PlanType.FREE && user.isFreePlanConsumed()) {
            throw new IllegalStateException("Free plan has already been used for this account.");
        }

        subscriptionRepository.findByUserIdAndActiveTrue(userId).ifPresent(existing -> {
            existing.setActive(false);
            existing.setUpdatedAt(Instant.now());
            subscriptionRepository.save(existing);
        });

        Subscription sub = new Subscription();
        sub.setId(UUID.randomUUID().toString());
        sub.setUserId(userId);
        sub.setPlan(plan);
        sub.setBillingCycle(billingCycle);
        sub.setStartDate(Instant.now());
        sub.setEndDate(resolveEndDate(planConfig, billingCycle, endDate));
        sub.setActive(true);
        applyPlanLimits(sub, planConfig);
        sub.setCreatedAt(Instant.now());
        sub.setUpdatedAt(Instant.now());
        Subscription saved = subscriptionRepository.save(sub);

        if (plan == PlanType.FREE) {
            user.setFreePlanConsumed(true);
            if (user.getFreePlanConsumedAt() == null) {
                user.setFreePlanConsumedAt(Instant.now());
            }
            userRepository.save(user);
        }

        return saved;
    }

    public void expireEndedSubscriptions() {
        List<Subscription> expired = subscriptionRepository.findByActiveTrueAndEndDateBefore(Instant.now());
        for (Subscription sub : expired) {
            sub.setActive(false);
            sub.setUpdatedAt(Instant.now());
            subscriptionRepository.save(sub);
        }
    }

    public void validateResumeCreation(String userId, long currentCount) {
        Subscription sub = getActiveSubscription(userId);
        if (currentCount >= sub.getResumeLimit()) {
            throw new RuntimeException("Portfolio limit reached for plan: " + sub.getPlan() + ". Upgrade to create more portfolios.");
        }
    }

    public void validatePublicPublish(String userId, long publishedCount) {
        Subscription sub = getActiveSubscription(userId);
        if (sub.getPublicLinkLimit() == 0) {
            throw new RuntimeException("Public portfolio links are not available on your current plan. Please upgrade.");
        }
        if (publishedCount >= sub.getPublicLinkLimit()) {
            throw new RuntimeException("Public portfolio limit reached for plan: " + sub.getPlan() + ". Upgrade to publish more portfolios.");
        }
    }

    public void validateVersioning(String userId) {
        Subscription sub = getActiveSubscription(userId);
        if (!sub.isVersioningEnabled()) {
            throw new RuntimeException("Versioning is not available on the " + sub.getPlan() + " plan.");
        }
    }

    public void validateThemeCustomization(String userId) {
        Subscription sub = getActiveSubscription(userId);
        if (!sub.isThemeCustomizationEnabled()) {
            throw new RuntimeException("Public page customization is only available on the PREMIUM plan.");
        }
    }

    public void validateTemplateChange(String userId) {
        Subscription sub = getActiveSubscription(userId);
        if (!sub.isTemplateChangeEnabled()) {
            throw new RuntimeException("Template changing is available only on PRO and PREMIUM plans.");
        }
    }

    public void validateCustomSlug(String userId) {
        Subscription sub = getActiveSubscription(userId);
        if (!sub.isCustomSlugEnabled()) {
            throw new RuntimeException("Custom public slug is available only on PRO and PREMIUM plans.");
        }
    }

    public boolean isTemplateAllowed(String userId, String templateId) {
        Template template = templateRepository.findByIdAndActiveTrue(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        Subscription sub = getActiveSubscription(userId);
        return sub.getPlan().ordinal() >= template.getPlanLevel().ordinal();
    }

    public boolean isSubscriptionActive(String userId) {
        try {
            Subscription sub = getActiveSubscription(userId);
            return sub.isActive() && (sub.getEndDate() == null || sub.getEndDate().isAfter(Instant.now()));
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public PlanType getCurrentPlan(String userId) {
        return getActiveSubscription(userId).getPlan();
    }

    public Subscription getSubscriptionDetails(String userId) {
        return getActiveSubscription(userId);
    }

    private Subscription syncAndValidateActiveSubscription(Subscription sub) {
        Subscription active = deactivateIfExpired(sub);
        SubscriptionPlan planConfig = getPlanConfig(active.getPlan());
        boolean changed = false;

        if (active.getResumeLimit() != planConfig.getResumeLimit()) {
            active.setResumeLimit(planConfig.getResumeLimit());
            changed = true;
        }
        if (active.getPublicLinkLimit() != planConfig.getPublicLinkLimit()) {
            active.setPublicLinkLimit(planConfig.getPublicLinkLimit());
            changed = true;
        }
        if (active.isVersioningEnabled() != planConfig.isVersioningEnabled()) {
            active.setVersioningEnabled(planConfig.isVersioningEnabled());
            changed = true;
        }
        if (active.isThemeCustomizationEnabled() != planConfig.isThemeCustomizationEnabled()) {
            active.setThemeCustomizationEnabled(planConfig.isThemeCustomizationEnabled());
            changed = true;
        }
        if (active.isTemplateChangeEnabled() != planConfig.isTemplateChangeEnabled()) {
            active.setTemplateChangeEnabled(planConfig.isTemplateChangeEnabled());
            changed = true;
        }
        if (active.isCustomSlugEnabled() != planConfig.isCustomSlugEnabled()) {
            active.setCustomSlugEnabled(planConfig.isCustomSlugEnabled());
            changed = true;
        }
        if (active.isOneTimeOnly() != planConfig.isOneTimeOnly()) {
            active.setOneTimeOnly(planConfig.isOneTimeOnly());
            changed = true;
        }
        if ((active.getTrialDurationDays() == null ? null : active.getTrialDurationDays()).equals(planConfig.getTrialDurationDays()) == false) {
            active.setTrialDurationDays(planConfig.getTrialDurationDays());
            changed = true;
        }

        if (changed) {
            active.setUpdatedAt(Instant.now());
            return subscriptionRepository.save(active);
        }
        return active;
    }

    private Subscription deactivateIfExpired(Subscription sub) {
        if (sub.getEndDate() != null && sub.getEndDate().isBefore(Instant.now())) {
            sub.setActive(false);
            sub.setUpdatedAt(Instant.now());
            subscriptionRepository.save(sub);
            throw new RuntimeException("Subscription expired. Please upgrade your plan.");
        }
        return sub;
    }

    private Subscription createFreeIfEligible(User user) {
        if (user.isFreePlanConsumed()) {
            throw new RuntimeException("No active subscription. Please choose a paid plan.");
        }
        return createSubscription(user.getId(), PlanType.FREE, null, null);
    }

    private SubscriptionPlan getPlanConfig(PlanType plan) {
        return subscriptionPlanRepository.findByPlanType(plan)
                .orElseThrow(() -> new RuntimeException("Plan config missing for: " + plan));
    }

    private void applyPlanLimits(Subscription sub, SubscriptionPlan planConfig) {
        sub.setResumeLimit(planConfig.getResumeLimit());
        sub.setPublicLinkLimit(planConfig.getPublicLinkLimit());
        sub.setVersioningEnabled(planConfig.isVersioningEnabled());
        sub.setThemeCustomizationEnabled(planConfig.isThemeCustomizationEnabled());
        sub.setTemplateChangeEnabled(planConfig.isTemplateChangeEnabled());
        sub.setCustomSlugEnabled(planConfig.isCustomSlugEnabled());
        sub.setOneTimeOnly(planConfig.isOneTimeOnly());
        sub.setTrialDurationDays(planConfig.getTrialDurationDays());
    }

    private Instant resolveEndDate(SubscriptionPlan planConfig, BillingCycle billingCycle, Instant explicitEndDate) {
        if (explicitEndDate != null) return explicitEndDate;
        if (billingCycle == BillingCycle.YEARLY) {
            return Instant.now().plus(365, ChronoUnit.DAYS);
        }
        if (billingCycle == BillingCycle.MONTHLY) {
            return Instant.now().plus(30, ChronoUnit.DAYS);
        }
        if (planConfig.getTrialDurationDays() != null && planConfig.getTrialDurationDays() > 0) {
            return Instant.now().plus(planConfig.getTrialDurationDays(), ChronoUnit.DAYS);
        }
        return null;
    }
}
