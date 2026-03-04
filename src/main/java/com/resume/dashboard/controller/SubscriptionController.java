package com.resume.dashboard.controller;

import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.Subscription;
import com.resume.dashboard.service.SubscriptionService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /* =====================================================
       GET CURRENT SUBSCRIPTION (FULL OBJECT)
    ===================================================== */
    @GetMapping("/me")
    public ResponseEntity<Subscription> getMySubscription(
            @AuthenticationPrincipal String userId) {

        Subscription sub = subscriptionService.getActiveSubscription(userId);
        return ResponseEntity.ok(sub);
    }

    /* =====================================================
       GET CURRENT PLAN ONLY (LIGHTWEIGHT)
    ===================================================== */
    @GetMapping("/plan")
    public ResponseEntity<PlanType> getCurrentPlan(
            @AuthenticationPrincipal String userId) {

        return ResponseEntity.ok(
                subscriptionService.getCurrentPlan(userId)
        );
    }

    /* =====================================================
       CHECK IF SUBSCRIPTION ACTIVE
    ===================================================== */
    @GetMapping("/active")
    public ResponseEntity<Boolean> isActive(
            @AuthenticationPrincipal String userId) {

        return ResponseEntity.ok(
                subscriptionService.isSubscriptionActive(userId)
        );
    }

    /* =====================================================
       ADMIN / PAYMENT CONFIRM UPGRADE
       (You will call this after payment success)
    ===================================================== */
    @PostMapping("/upgrade")
    public ResponseEntity<Subscription> upgradePlan(
            @AuthenticationPrincipal String userId,
            @RequestParam PlanType plan,
            @RequestParam(required = false) Instant endDate) {

        Subscription sub =
                subscriptionService.createSubscription(userId, plan, endDate);

        return ResponseEntity.ok(sub);
    }
}