package com.resume.dashboard.service;

import com.resume.dashboard.dto.subscriptionplan.*;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.SubscriptionPlan;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.SubscriptionPlanRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;

    public SubscriptionPlanService(SubscriptionPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    // Public pricing page — only active plans
    public List<SubscriptionPlanResponse> getActivePlans() {
        return planRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Admin — see all plans including inactive
    public List<SubscriptionPlanResponse> getAllPlans() {
        return planRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Admin — update a plan's price, limits, display text
    public SubscriptionPlanResponse updatePlan(PlanType planType,
                                                UpdateSubscriptionPlanRequest request) {

        SubscriptionPlan plan = planRepository.findByPlanType(planType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan not found: " + planType));

        if (request.getDisplayName() != null) plan.setDisplayName(request.getDisplayName());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        if (request.getPriceMonthlyInSmallestUnit() != null)
            plan.setPriceMonthlyInSmallestUnit(request.getPriceMonthlyInSmallestUnit());
        if (request.getPriceYearlyInSmallestUnit() != null)
            plan.setPriceYearlyInSmallestUnit(request.getPriceYearlyInSmallestUnit());
        if (request.getCurrency() != null) plan.setCurrency(request.getCurrency());
        if (request.getDisplayPriceMonthly() != null)
            plan.setDisplayPriceMonthly(request.getDisplayPriceMonthly());
        if (request.getDisplayPriceYearly() != null)
            plan.setDisplayPriceYearly(request.getDisplayPriceYearly());
        if (request.getResumeLimit() != null) plan.setResumeLimit(request.getResumeLimit());
        if (request.getPublicLinkLimit() != null) plan.setPublicLinkLimit(request.getPublicLinkLimit());
        if (request.getVersioningEnabled() != null)
            plan.setVersioningEnabled(request.getVersioningEnabled());
        if (request.getThemeCustomizationEnabled() != null)
            plan.setThemeCustomizationEnabled(request.getThemeCustomizationEnabled());
        if (request.getIsPopular() != null) plan.setPopular(request.getIsPopular());
        if (request.getIsActive() != null) plan.setActive(request.getIsActive());
        if (request.getDisplayOrder() != null) plan.setDisplayOrder(request.getDisplayOrder());

        plan.setUpdatedAt(Instant.now());
        return toResponse(planRepository.save(plan));
    }

    private SubscriptionPlanResponse toResponse(SubscriptionPlan p) {
        SubscriptionPlanResponse r = new SubscriptionPlanResponse();
        r.setId(p.getId());
        r.setPlanType(p.getPlanType());
        r.setDisplayName(p.getDisplayName());
        r.setDescription(p.getDescription());
        r.setPriceMonthlyInSmallestUnit(p.getPriceMonthlyInSmallestUnit());
        r.setPriceYearlyInSmallestUnit(p.getPriceYearlyInSmallestUnit());
        r.setCurrency(p.getCurrency());
        r.setDisplayPriceMonthly(p.getDisplayPriceMonthly());
        r.setDisplayPriceYearly(p.getDisplayPriceYearly());
        r.setResumeLimit(p.getResumeLimit());
        r.setPublicLinkLimit(p.getPublicLinkLimit());
        r.setVersioningEnabled(p.isVersioningEnabled());
        r.setThemeCustomizationEnabled(p.isThemeCustomizationEnabled());
        r.setPopular(p.isPopular());
        r.setActive(p.isActive());
        r.setDisplayOrder(p.getDisplayOrder());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}