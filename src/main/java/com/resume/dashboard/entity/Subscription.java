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

    private PlanType plan; // FREE, BASIC, PRO, PREMIUM

    private Instant startDate;
    private Instant endDate;

    private boolean active;

    // ─── PLAN LIMITS ────────────────────────────────────────────────
    private int resumeLimit;
    private int publicLinkLimit;
    private boolean versioningEnabled;
    private boolean themeCustomizationEnabled; // ✅ NEW — PRO and PREMIUM only

    private Instant createdAt;
    private Instant updatedAt;

    // ─── GETTERS & SETTERS ───────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public PlanType getPlan() { return plan; }
    public void setPlan(PlanType plan) { this.plan = plan; }

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
    public void setThemeCustomizationEnabled(boolean themeCustomizationEnabled) {
        this.themeCustomizationEnabled = themeCustomizationEnabled;
    }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}