package com.resume.dashboard.controller;

import com.resume.dashboard.entity.*;
import com.resume.dashboard.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PAYMENT CONTROLLER
 *
 * POST /api/payments/initiate   → start a purchase, get back an orderId
 * POST /api/payments/confirm    → confirm payment done, upgrades subscription
 * GET  /api/payments/history    → user's past orders / receipts
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /* ================================================================
       INITIATE — creates a pending PaymentOrder
       Returns orderId which frontend uses to confirm later
    ================================================================ */
    @PostMapping("/initiate")
    public ResponseEntity<PaymentOrderResponse> initiate(
            @AuthenticationPrincipal String userId,
            @RequestBody InitiatePaymentRequest request) {

        PaymentOrder order = paymentService.initiatePayment(
                userId, request.getPlan(), request.getBillingCycle());

        return ResponseEntity.ok(toResponse(order));
    }

    /* ================================================================
       CONFIRM — mark payment as done, upgrades the user's plan
       When Razorpay is added: body will also include razorpay_signature
    ================================================================ */
    @PostMapping("/confirm")
    public ResponseEntity<PaymentOrderResponse> confirm(
            @AuthenticationPrincipal String userId,
            @RequestBody ConfirmPaymentRequest request) {

        PaymentOrder order = paymentService.confirmPayment(
                userId, request.getOrderId(), request.getTransactionRef());

        return ResponseEntity.ok(toResponse(order));
    }

    /* ================================================================
       HISTORY — user's past payment orders
    ================================================================ */
    @GetMapping("/history")
    public ResponseEntity<List<PaymentOrderResponse>> history(
            @AuthenticationPrincipal String userId) {

        List<PaymentOrderResponse> history = paymentService
                .getPaymentHistory(userId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(history);
    }

    // ─── MAPPER ──────────────────────────────────────────────────────
    private PaymentOrderResponse toResponse(PaymentOrder o) {
        PaymentOrderResponse r = new PaymentOrderResponse();
        r.setOrderId(o.getId());
        r.setTargetPlan(o.getTargetPlan());
        r.setBillingCycle(o.getBillingCycle());
        r.setDisplayAmount(o.getDisplayAmount());
        r.setCurrency(o.getCurrency());
        r.setStatus(o.getStatus());
        r.setTransactionRef(o.getTransactionRef());
        r.setExpiresAt(o.getExpiresAt());
        r.setCreatedAt(o.getCreatedAt());
        return r;
    }

    // ─── INLINE DTOs ─────────────────────────────────────────────────

    public static class InitiatePaymentRequest {
        private PlanType plan;
        private BillingCycle billingCycle;
        public PlanType getPlan() { return plan; }
        public void setPlan(PlanType plan) { this.plan = plan; }
        public BillingCycle getBillingCycle() { return billingCycle; }
        public void setBillingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; }
    }

    public static class ConfirmPaymentRequest {
        private String orderId;
        private String transactionRef; // Razorpay paymentId goes here later
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public String getTransactionRef() { return transactionRef; }
        public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }
    }

    public static class PaymentOrderResponse {
        private String orderId;
        private PlanType targetPlan;
        private BillingCycle billingCycle;
        private String displayAmount;
        private String currency;
        private PaymentStatus status;
        private String transactionRef;
        private java.time.Instant expiresAt;
        private java.time.Instant createdAt;

        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public PlanType getTargetPlan() { return targetPlan; }
        public void setTargetPlan(PlanType targetPlan) { this.targetPlan = targetPlan; }
        public BillingCycle getBillingCycle() { return billingCycle; }
        public void setBillingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; }
        public String getDisplayAmount() { return displayAmount; }
        public void setDisplayAmount(String displayAmount) { this.displayAmount = displayAmount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public PaymentStatus getStatus() { return status; }
        public void setStatus(PaymentStatus status) { this.status = status; }
        public String getTransactionRef() { return transactionRef; }
        public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }
        public java.time.Instant getExpiresAt() { return expiresAt; }
        public void setExpiresAt(java.time.Instant expiresAt) { this.expiresAt = expiresAt; }
        public java.time.Instant getCreatedAt() { return createdAt; }
        public void setCreatedAt(java.time.Instant createdAt) { this.createdAt = createdAt; }
    }
}