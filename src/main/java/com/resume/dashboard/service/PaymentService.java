package com.resume.dashboard.service;

import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.PaymentOrderRepository;
import com.resume.dashboard.repository.SubscriptionPlanRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionService subscriptionService;

    public PaymentService(PaymentOrderRepository paymentOrderRepository,
                          SubscriptionPlanRepository subscriptionPlanRepository,
                          SubscriptionService subscriptionService) {
        this.paymentOrderRepository = paymentOrderRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionService = subscriptionService;
    }

    /* =======================================================
       STEP 1 — INITIATE PAYMENT
       User clicks "Buy PRO / BASIC / PREMIUM".
       Creates a PaymentOrder record and returns it to frontend.

       When Razorpay is added:
         → Also call razorpayClient.createOrder(amount, currency)
         → Store razorpayOrderId in order.transactionRef
         → Return razorpayOrderId + key to frontend for checkout
    ======================================================= */
    public PaymentOrder initiatePayment(String userId,
                                        PlanType targetPlan,
                                        BillingCycle billingCycle) {

        // Prevent FREE plan "purchase"
        if (targetPlan == PlanType.FREE) {
            throw new IllegalArgumentException("FREE plan cannot be purchased.");
        }

        // Prevent duplicate pending orders
        if (paymentOrderRepository.existsByUserIdAndStatus(userId, PaymentStatus.PENDING)) {
            throw new IllegalStateException(
                    "You already have a pending payment. Complete or cancel it first.");
        }

        // Load plan config from DB to get the correct price
        SubscriptionPlan planConfig = subscriptionPlanRepository
                .findByPlanType(targetPlan)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan not found: " + targetPlan));

        if (!planConfig.isActive()) {
            throw new IllegalStateException("This plan is currently unavailable.");
        }

        // Pick monthly or yearly price
        long amount = billingCycle == BillingCycle.YEARLY
                ? planConfig.getPriceYearlyInSmallestUnit()
                : planConfig.getPriceMonthlyInSmallestUnit();

        String displayAmount = billingCycle == BillingCycle.YEARLY
                ? planConfig.getDisplayPriceYearly()
                : planConfig.getDisplayPriceMonthly();

        // Calculate when the plan should expire
        Instant planEndDate = billingCycle == BillingCycle.YEARLY
                ? Instant.now().plus(365, ChronoUnit.DAYS)
                : Instant.now().plus(30, ChronoUnit.DAYS);

        PaymentOrder order = new PaymentOrder();
        order.setId(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setTargetPlan(targetPlan);
        order.setBillingCycle(billingCycle);
        order.setAmountInSmallestUnit(amount);
        order.setCurrency(planConfig.getCurrency());
        order.setDisplayAmount(displayAmount);
        order.setStatus(PaymentStatus.PENDING);
        order.setExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES)); // 30-min window
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        // ── RAZORPAY SLOT ──────────────────────────────────────────
        // When Razorpay is integrated, add here:
        //   RazorpayOrder rzpOrder = razorpayClient.orders.create(amount, currency, receiptId);
        //   order.setTransactionRef(rzpOrder.get("id")); // store razorpay order id
        // ──────────────────────────────────────────────────────────

        return paymentOrderRepository.save(order);
    }

    /* =======================================================
       STEP 2 — CONFIRM PAYMENT
       Frontend calls this after user completes payment.
       Verifies the order is valid, marks it PAID, upgrades plan.

       When Razorpay is added, replace the TODO block with:
         razorpayVerifier.verifySignature(
             order.getTransactionRef(),  // razorpay_order_id
             transactionRef,             // razorpay_payment_id
             razorpaySignature           // razorpay_signature
         );
         If signature invalid → mark FAILED, throw exception
         If valid → proceed to upgrade below
    ======================================================= */
    @Transactional
    public PaymentOrder confirmPayment(String userId,
                                       String orderId,
                                       String transactionRef) {

        // Load and validate order
        PaymentOrder order = paymentOrderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment order not found"));

        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Order is not in PENDING state. Current status: " + order.getStatus());
        }

        if (Instant.now().isAfter(order.getExpiresAt())) {
            order.setStatus(PaymentStatus.EXPIRED);
            order.setUpdatedAt(Instant.now());
            paymentOrderRepository.save(order);
            throw new IllegalStateException("Payment order has expired. Please start a new purchase.");
        }

        // ── RAZORPAY SIGNATURE VERIFICATION SLOT ──────────────────
        // TODO: When Razorpay is integrated, verify HMAC here before proceeding.
        // If verification fails:
        //   order.setStatus(PaymentStatus.FAILED);
        //   paymentOrderRepository.save(order);
        //   throw new IllegalStateException("Payment verification failed.");
        // ──────────────────────────────────────────────────────────

        // Calculate plan end date based on billing cycle
        Instant planEndDate = order.getBillingCycle() == BillingCycle.YEARLY
                ? Instant.now().plus(365, ChronoUnit.DAYS)
                : Instant.now().plus(30, ChronoUnit.DAYS);

        // Mark order as PAID
        order.setStatus(PaymentStatus.PAID);
        order.setTransactionRef(transactionRef);
        order.setUpdatedAt(Instant.now());
        paymentOrderRepository.save(order);

        // ✅ THIS is the only place a subscription plan gets upgraded
        // SubscriptionService.createSubscription is package-accessible but
        // should only be called from here (after payment verification)
        subscriptionService.createSubscription(
                userId,
                order.getTargetPlan(),
                order.getBillingCycle(),
                planEndDate
        );

        return order;
    }

    /* =======================================================
       GET PAYMENT HISTORY
       User can view their past orders/receipts.
    ======================================================= */
    public List<PaymentOrder> getPaymentHistory(String userId) {
        return paymentOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}

