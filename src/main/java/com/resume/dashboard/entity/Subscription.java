package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;

    @Indexed
    private String userId;

    private PlanType plan;
    private BillingCycle billingCycle;

    private Instant startDate;
    private Instant endDate;

    private boolean active;

    private int resumeLimit;
    private int publicLinkLimit;
    private boolean versioningEnabled;
    private boolean themeCustomizationEnabled;
    private boolean templateChangeEnabled;
    private boolean customSlugEnabled;
    private boolean oneTimeOnly;
    private Integer trialDurationDays;

    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public PlanType getPlan() { return plan; }
    public void setPlan(PlanType plan) { this.plan = plan; }
    public BillingCycle getBillingCycle() { return billingCycle; }
    public void setBillingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; }
    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }
    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getResumeLimit() { return resumeLimit; }
    public void setResumeLimit(int resumeLimit) { this.resumeLimit = resumeLimit; }
    public int getPublicLinkLimit() { return publicLinkLimit; }
    public void setPublicLinkLimit(int publicLinkLimit) { this.publicLinkLimit = publicLinkLimit; }
    public boolean isVersioningEnabled() { return versioningEnabled; }
    public void setVersioningEnabled(boolean versioningEnabled) { this.versioningEnabled = versioningEnabled; }
    public boolean isThemeCustomizationEnabled() { return themeCustomizationEnabled; }
    public void setThemeCustomizationEnabled(boolean themeCustomizationEnabled) { this.themeCustomizationEnabled = themeCustomizationEnabled; }
    public boolean isTemplateChangeEnabled() { return templateChangeEnabled; }
    public void setTemplateChangeEnabled(boolean templateChangeEnabled) { this.templateChangeEnabled = templateChangeEnabled; }
    public boolean isCustomSlugEnabled() { return customSlugEnabled; }
    public void setCustomSlugEnabled(boolean customSlugEnabled) { this.customSlugEnabled = customSlugEnabled; }
    public boolean isOneTimeOnly() { return oneTimeOnly; }
    public void setOneTimeOnly(boolean oneTimeOnly) { this.oneTimeOnly = oneTimeOnly; }
    public Integer getTrialDurationDays() { return trialDurationDays; }
    public void setTrialDurationDays(Integer trialDurationDays) { this.trialDurationDays = trialDurationDays; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
