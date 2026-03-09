package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * PaymentOrder — tracks every purchase attempt.
 *
 * CURRENT FLOW (no gateway yet):
 *   POST /api/payments/initiate  → creates PaymentOrder (PENDING)
 *   POST /api/payments/confirm   → marks PAID → upgrades subscription
 *
 * WHEN RAZORPAY IS ADDED:
 *   initiate → also creates Razorpay order, stores razorpayOrderId in transactionRef
 *   confirm  → verifies HMAC signature FIRST, then upgrades subscription
 *   Only one line changes in PaymentService.confirmPayment()
 */
@Document(collection = "payment_orders")
public class PaymentOrder {

    @Id
    private String id;

    @Indexed
    private String userId;

    private PlanType targetPlan;
    private BillingCycle billingCycle; // MONTHLY or YEARLY

    // Snapshotted from SubscriptionPlan at order creation time
    // so price changes don't retroactively affect existing orders
    private long amountInSmallestUnit;
    private String currency;
    private String displayAmount; // e.g. "₹199" — for UI/receipts

    private PaymentStatus status; // PENDING → PAID or FAILED

    // Gateway transaction reference — empty now, Razorpay paymentId goes here later
    private String transactionRef;

    // Order expires after 30 mins — prevents stale orders being confirmed later
    private Instant expiresAt;

    private Instant createdAt;
    private Instant updatedAt;

    // ─── GETTERS & SETTERS ───────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public PlanType getTargetPlan() { return targetPlan; }
    public void setTargetPlan(PlanType targetPlan) { this.targetPlan = targetPlan; }
    public BillingCycle getBillingCycle() { return billingCycle; }
    public void setBillingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; }
    public long getAmountInSmallestUnit() { return amountInSmallestUnit; }
    public void setAmountInSmallestUnit(long v) { this.amountInSmallestUnit = v; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getDisplayAmount() { return displayAmount; }
    public void setDisplayAmount(String displayAmount) { this.displayAmount = displayAmount; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}