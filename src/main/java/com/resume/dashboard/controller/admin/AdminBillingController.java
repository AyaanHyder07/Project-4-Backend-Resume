package com.resume.dashboard.controller.admin;

import com.resume.dashboard.dto.admin.AdminAssignSubscriptionRequest;
import com.resume.dashboard.dto.admin.AdminRevenueSummaryResponse;
import com.resume.dashboard.dto.admin.AdminSubscriptionUserSummaryResponse;
import com.resume.dashboard.dto.admin.AdminUserBillingDetailsResponse;
import com.resume.dashboard.entity.Subscription;
import com.resume.dashboard.service.admin.AdminBillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/billing")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBillingController {

    private final AdminBillingService adminBillingService;

    public AdminBillingController(AdminBillingService adminBillingService) {
        this.adminBillingService = adminBillingService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminSubscriptionUserSummaryResponse>> getUsers() {
        return ResponseEntity.ok(adminBillingService.getUserSummaries());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminUserBillingDetailsResponse> getUserDetails(@PathVariable String userId) {
        return ResponseEntity.ok(adminBillingService.getUserDetails(userId));
    }

    @PostMapping("/users/{userId}/subscription")
    public ResponseEntity<Subscription> assignSubscription(@PathVariable String userId,
                                                           @RequestBody AdminAssignSubscriptionRequest request) {
        return ResponseEntity.ok(adminBillingService.assignSubscription(userId, request));
    }

    @GetMapping("/revenue")
    public ResponseEntity<AdminRevenueSummaryResponse> getRevenue() {
        return ResponseEntity.ok(adminBillingService.getRevenueSummary());
    }
}
