package com.resume.dashboard.dto.admin;

import com.resume.dashboard.entity.PaymentOrder;
import com.resume.dashboard.entity.Subscription;

import java.util.List;

public class AdminUserBillingDetailsResponse {

    private AdminSubscriptionUserSummaryResponse user;
    private Subscription activeSubscription;
    private List<Subscription> subscriptionHistory;
    private List<PaymentOrder> paymentHistory;

    public AdminSubscriptionUserSummaryResponse getUser() { return user; }
    public void setUser(AdminSubscriptionUserSummaryResponse user) { this.user = user; }
    public Subscription getActiveSubscription() { return activeSubscription; }
    public void setActiveSubscription(Subscription activeSubscription) { this.activeSubscription = activeSubscription; }
    public List<Subscription> getSubscriptionHistory() { return subscriptionHistory; }
    public void setSubscriptionHistory(List<Subscription> subscriptionHistory) { this.subscriptionHistory = subscriptionHistory; }
    public List<PaymentOrder> getPaymentHistory() { return paymentHistory; }
    public void setPaymentHistory(List<PaymentOrder> paymentHistory) { this.paymentHistory = paymentHistory; }
}
