package com.resume.dashboard.dto.admin;

import com.resume.dashboard.entity.BillingCycle;
import com.resume.dashboard.entity.PlanType;

public class AdminAssignSubscriptionRequest {

    private PlanType plan;
    private BillingCycle billingCycle;
    private Integer durationDays;

    public PlanType getPlan() { return plan; }
    public void setPlan(PlanType plan) { this.plan = plan; }
    public BillingCycle getBillingCycle() { return billingCycle; }
    public void setBillingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; }
    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
}
