package com.resume.dashboard.dto.admin;

import com.resume.dashboard.entity.PlanType;

import java.time.Instant;

public class AdminSubscriptionUserSummaryResponse {

    private String userId;
    private String username;
    private String role;
    private boolean freePlanConsumed;
    private Instant joinedAt;
    private Instant lastLogin;
    private long portfolioCount;
    private long publishedPortfolioCount;
    private PlanType currentPlan;
    private boolean subscriptionActive;
    private Instant subscriptionEndDate;
    private String billingCycle;
    private long totalPaidOrders;
    private long totalRevenueInSmallestUnit;
    private String latestDisplayAmount;
    private Instant latestPaymentAt;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isFreePlanConsumed() { return freePlanConsumed; }
    public void setFreePlanConsumed(boolean freePlanConsumed) { this.freePlanConsumed = freePlanConsumed; }
    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }
    public long getPortfolioCount() { return portfolioCount; }
    public void setPortfolioCount(long portfolioCount) { this.portfolioCount = portfolioCount; }
    public long getPublishedPortfolioCount() { return publishedPortfolioCount; }
    public void setPublishedPortfolioCount(long publishedPortfolioCount) { this.publishedPortfolioCount = publishedPortfolioCount; }
    public PlanType getCurrentPlan() { return currentPlan; }
    public void setCurrentPlan(PlanType currentPlan) { this.currentPlan = currentPlan; }
    public boolean isSubscriptionActive() { return subscriptionActive; }
    public void setSubscriptionActive(boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }
    public Instant getSubscriptionEndDate() { return subscriptionEndDate; }
    public void setSubscriptionEndDate(Instant subscriptionEndDate) { this.subscriptionEndDate = subscriptionEndDate; }
    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }
    public long getTotalPaidOrders() { return totalPaidOrders; }
    public void setTotalPaidOrders(long totalPaidOrders) { this.totalPaidOrders = totalPaidOrders; }
    public long getTotalRevenueInSmallestUnit() { return totalRevenueInSmallestUnit; }
    public void setTotalRevenueInSmallestUnit(long totalRevenueInSmallestUnit) { this.totalRevenueInSmallestUnit = totalRevenueInSmallestUnit; }
    public String getLatestDisplayAmount() { return latestDisplayAmount; }
    public void setLatestDisplayAmount(String latestDisplayAmount) { this.latestDisplayAmount = latestDisplayAmount; }
    public Instant getLatestPaymentAt() { return latestPaymentAt; }
    public void setLatestPaymentAt(Instant latestPaymentAt) { this.latestPaymentAt = latestPaymentAt; }
}
