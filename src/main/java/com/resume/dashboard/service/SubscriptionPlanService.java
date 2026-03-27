package com.resume.dashboard.service;

import com.resume.dashboard.dto.subscriptionplan.SubscriptionPlanResponse;
import com.resume.dashboard.dto.subscriptionplan.UpdateSubscriptionPlanRequest;
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

    public List<SubscriptionPlanResponse> getActivePlans() {
        return planRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<SubscriptionPlanResponse> getAllPlans() {
        return planRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SubscriptionPlanResponse updatePlan(PlanType planType, UpdateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = planRepository.findByPlanType(planType)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + planType));

        if (request.getDisplayName() != null) plan.setDisplayName(request.getDisplayName());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        if (request.getPriceMonthlyInSmallestUnit() != null) plan.setPriceMonthlyInSmallestUnit(request.getPriceMonthlyInSmallestUnit());
        if (request.getPriceYearlyInSmallestUnit() != null) plan.setPriceYearlyInSmallestUnit(request.getPriceYearlyInSmallestUnit());
        if (request.getCurrency() != null) plan.setCurrency(request.getCurrency());
        if (request.getDisplayPriceMonthly() != null) plan.setDisplayPriceMonthly(request.getDisplayPriceMonthly());
        if (request.getDisplayPriceYearly() != null) plan.setDisplayPriceYearly(request.getDisplayPriceYearly());
        if (request.getResumeLimit() != null) plan.setResumeLimit(request.getResumeLimit());
        if (request.getPublicLinkLimit() != null) plan.setPublicLinkLimit(request.getPublicLinkLimit());
        if (request.getVersioningEnabled() != null) plan.setVersioningEnabled(request.getVersioningEnabled());
        if (request.getThemeCustomizationEnabled() != null) plan.setThemeCustomizationEnabled(request.getThemeCustomizationEnabled());
        if (request.getTemplateChangeEnabled() != null) plan.setTemplateChangeEnabled(request.getTemplateChangeEnabled());
        if (request.getCustomSlugEnabled() != null) plan.setCustomSlugEnabled(request.getCustomSlugEnabled());
        if (request.getOneTimeOnly() != null) plan.setOneTimeOnly(request.getOneTimeOnly());
        if (request.getTrialDurationDays() != null) plan.setTrialDurationDays(request.getTrialDurationDays());
        if (request.getIsPopular() != null) plan.setPopular(request.getIsPopular());
        if (request.getIsActive() != null) plan.setActive(request.getIsActive());
        if (request.getDisplayOrder() != null) plan.setDisplayOrder(request.getDisplayOrder());

        plan.setUpdatedAt(Instant.now());
        return toResponse(planRepository.save(plan));
    }

    private SubscriptionPlanResponse toResponse(SubscriptionPlan plan) {
        SubscriptionPlanResponse response = new SubscriptionPlanResponse();
        response.setId(plan.getId());
        response.setPlanType(plan.getPlanType());
        response.setDisplayName(plan.getDisplayName());
        response.setDescription(plan.getDescription());
        response.setPriceMonthlyInSmallestUnit(plan.getPriceMonthlyInSmallestUnit());
        response.setPriceYearlyInSmallestUnit(plan.getPriceYearlyInSmallestUnit());
        response.setCurrency(plan.getCurrency());
        response.setDisplayPriceMonthly(plan.getDisplayPriceMonthly());
        response.setDisplayPriceYearly(plan.getDisplayPriceYearly());
        response.setResumeLimit(plan.getResumeLimit());
        response.setPublicLinkLimit(plan.getPublicLinkLimit());
        response.setVersioningEnabled(plan.isVersioningEnabled());
        response.setThemeCustomizationEnabled(plan.isThemeCustomizationEnabled());
        response.setTemplateChangeEnabled(plan.isTemplateChangeEnabled());
        response.setCustomSlugEnabled(plan.isCustomSlugEnabled());
        response.setOneTimeOnly(plan.isOneTimeOnly());
        response.setTrialDurationDays(plan.getTrialDurationDays());
        response.setPopular(plan.isPopular());
        response.setActive(plan.isActive());
        response.setDisplayOrder(plan.getDisplayOrder());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}
