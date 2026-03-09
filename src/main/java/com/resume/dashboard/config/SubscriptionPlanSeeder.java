package com.resume.dashboard.config;

import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.SubscriptionPlan;
import com.resume.dashboard.repository.SubscriptionPlanRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * SubscriptionPlanSeeder
 *
 * Runs once on startup. If the subscription_plans collection is empty,
 * seeds the 4 default plan records (FREE, BASIC, PRO, PREMIUM).
 *
 * After seeding, admin can update prices/limits via:
 *   PUT /api/admin/plans/{planType}
 * without touching code.
 */
@Component
public class SubscriptionPlanSeeder implements ApplicationRunner {

    private final SubscriptionPlanRepository planRepository;

    public SubscriptionPlanSeeder(SubscriptionPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (planRepository.count() > 0) {
            return; // Already seeded — skip
        }

        planRepository.save(buildPlan(
                PlanType.FREE,
                "Free",
                "Get started with the basics",
                0, 0,
                "₹0", "₹0",
                1, 0, false, false,
                false, true, 0
        ));

        planRepository.save(buildPlan(
                PlanType.BASIC,
                "Basic",
                "Perfect for job seekers",
                9900, 99000,         // ₹99/mo, ₹990/yr
                "₹99/mo", "₹990/yr",
                1, 1, false, false,
                false, true, 1
        ));

        planRepository.save(buildPlan(
                PlanType.PRO,
                "Pro",
                "For professionals who want more",
                19900, 199000,       // ₹199/mo, ₹1990/yr
                "₹199/mo", "₹1990/yr",
                2, 1, true, true,
                true, true, 2       // isPopular = true
        ));

        planRepository.save(buildPlan(
                PlanType.PREMIUM,
                "Premium",
                "Everything, unlimited",
                39900, 399000,       // ₹399/mo, ₹3990/yr
                "₹399/mo", "₹3990/yr",
                3, 2, true, true,
                false, true, 3
        ));

        System.out.println("[SubscriptionPlanSeeder] Seeded 4 subscription plans.");
    }

    private SubscriptionPlan buildPlan(
            PlanType planType,
            String displayName,
            String description,
            long priceMonthly,
            long priceYearly,
            String displayMonthly,
            String displayYearly,
            int resumeLimit,
            int publicLinkLimit,
            boolean versioning,
            boolean themeCustomization,
            boolean isPopular,
            boolean isActive,
            int displayOrder) {

        SubscriptionPlan p = new SubscriptionPlan();
        p.setId(UUID.randomUUID().toString());
        p.setPlanType(planType);
        p.setDisplayName(displayName);
        p.setDescription(description);
        p.setPriceMonthlyInSmallestUnit(priceMonthly);
        p.setPriceYearlyInSmallestUnit(priceYearly);
        p.setCurrency("INR");
        p.setDisplayPriceMonthly(displayMonthly);
        p.setDisplayPriceYearly(displayYearly);
        p.setResumeLimit(resumeLimit);
        p.setPublicLinkLimit(publicLinkLimit);
        p.setVersioningEnabled(versioning);
        p.setThemeCustomizationEnabled(themeCustomization);
        p.setPopular(isPopular);
        p.setActive(isActive);
        p.setDisplayOrder(displayOrder);
        p.setCreatedAt(Instant.now());
        p.setUpdatedAt(Instant.now());
        return p;
    }
}