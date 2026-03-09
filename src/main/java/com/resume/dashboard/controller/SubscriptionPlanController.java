package com.resume.dashboard.controller;

import com.resume.dashboard.dto.subscriptionplan.*;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.service.SubscriptionPlanService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SUBSCRIPTION PLAN CONTROLLER
 *
 * Public (pricing page — no auth needed):
 *   GET /api/plans                        → all active plans with prices + features
 *
 * Admin:
 *   GET /api/admin/plans                  → all plans including inactive
 *   PUT /api/admin/plans/{planType}       → update price / limits / display text
 */
@RestController
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    public SubscriptionPlanController(SubscriptionPlanService planService) {
        this.planService = planService;
    }

    // Public pricing page
    @GetMapping("/api/plans")
    public ResponseEntity<List<SubscriptionPlanResponse>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }

    // Admin — all plans
    @GetMapping("/api/admin/plans")
    public ResponseEntity<List<SubscriptionPlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    // Admin — update a plan
    @PutMapping("/api/admin/plans/{planType}")
    public ResponseEntity<SubscriptionPlanResponse> updatePlan(
            @PathVariable PlanType planType,
            @RequestBody UpdateSubscriptionPlanRequest request) {
        return ResponseEntity.ok(planService.updatePlan(planType, request));
    }
}