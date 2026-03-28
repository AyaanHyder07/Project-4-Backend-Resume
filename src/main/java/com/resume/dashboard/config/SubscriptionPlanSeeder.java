package com.resume.dashboard.config;

import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.SubscriptionPlan;
import com.resume.dashboard.repository.SubscriptionPlanRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class SubscriptionPlanSeeder implements ApplicationRunner {

    private final SubscriptionPlanRepository planRepository;

    public SubscriptionPlanSeeder(SubscriptionPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        savePlan(buildPlan(PlanType.FREE, "Free Trial", "One portfolio draft for 7 days. Free can only be used once.", 0, 0, "?0", "?0", 1, 0, false, false, false, false, true, 7, false, true, 0));
        savePlan(buildPlan(PlanType.BASIC, "Basic", "One portfolio, one public link, limited templates.", 19900, 199000, "?199/mo", "?1990/yr", 1, 1, false, false, false, false, false, null, false, true, 1));
        savePlan(buildPlan(PlanType.PRO, "Pro", "Everything in Basic plus template switching and custom slugs.", 39900, 399000, "?399/mo", "?3990/yr", 1, 1, true, false, true, true, false, null, true, true, 2));
        savePlan(buildPlan(PlanType.PREMIUM, "Premium", "Everything in Pro plus full public page customization.", 69900, 699000, "?699/mo", "?6990/yr", 1, 1, true, true, true, true, false, null, false, true, 3));
    }

    private void savePlan(SubscriptionPlan plan) {
        planRepository.findByPlanType(plan.getPlanType()).ifPresent(existing -> plan.setId(existing.getId()));
        planRepository.save(plan);
    }

    private SubscriptionPlan buildPlan(PlanType planType, String displayName, String description, long priceMonthly, long priceYearly, String displayMonthly, String displayYearly, int resumeLimit, int publicLinkLimit, boolean versioning, boolean themeCustomization, boolean templateChange, boolean customSlugEnabled, boolean oneTimeOnly, Integer trialDurationDays, boolean isPopular, boolean isActive, int displayOrder) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(UUID.randomUUID().toString());
        plan.setPlanType(planType);
        plan.setDisplayName(displayName);
        plan.setDescription(description);
        plan.setPriceMonthlyInSmallestUnit(priceMonthly);
        plan.setPriceYearlyInSmallestUnit(priceYearly);
        plan.setCurrency("INR");
        plan.setDisplayPriceMonthly(displayMonthly);
        plan.setDisplayPriceYearly(displayYearly);
        plan.setResumeLimit(resumeLimit);
        plan.setPublicLinkLimit(publicLinkLimit);
        plan.setVersioningEnabled(versioning);
        plan.setThemeCustomizationEnabled(themeCustomization);
        plan.setTemplateChangeEnabled(templateChange);
        plan.setCustomSlugEnabled(customSlugEnabled);
        plan.setOneTimeOnly(oneTimeOnly);
        plan.setTrialDurationDays(trialDurationDays);
        plan.setPopular(isPopular);
        plan.setActive(isActive);
        plan.setDisplayOrder(displayOrder);
        plan.setCreatedAt(Instant.now());
        plan.setUpdatedAt(Instant.now());
        return plan;
    }
}
