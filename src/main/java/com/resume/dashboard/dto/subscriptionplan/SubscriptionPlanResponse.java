package com.resume.dashboard.dto.subscriptionplan;

import com.resume.dashboard.entity.PlanType;
import java.time.Instant;

public class SubscriptionPlanResponse {

    private String id;
    private PlanType planType;
    private String displayName;
    private String description;

    private long priceMonthlyInSmallestUnit;
    private long priceYearlyInSmallestUnit;
    private String currency;
    private String displayPriceMonthly;
    private String displayPriceYearly;

    private int resumeLimit;
    private int publicLinkLimit;
    private boolean versioningEnabled;
    private boolean themeCustomizationEnabled;

    private boolean isPopular;
    private boolean isActive;
    private int displayOrder;

    private Instant updatedAt;

    // ─── GETTERS & SETTERS ───────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public PlanType getPlanType() { return planType; }
    public void setPlanType(PlanType planType) { this.planType = planType; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getPriceMonthlyInSmallestUnit() { return priceMonthlyInSmallestUnit; }
    public void setPriceMonthlyInSmallestUnit(long v) { this.priceMonthlyInSmallestUnit = v; }

    public long getPriceYearlyInSmallestUnit() { return priceYearlyInSmallestUnit; }
    public void setPriceYearlyInSmallestUnit(long v) { this.priceYearlyInSmallestUnit = v; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDisplayPriceMonthly() { return displayPriceMonthly; }
    public void setDisplayPriceMonthly(String displayPriceMonthly) { this.displayPriceMonthly = displayPriceMonthly; }

    public String getDisplayPriceYearly() { return displayPriceYearly; }
    public void setDisplayPriceYearly(String displayPriceYearly) { this.displayPriceYearly = displayPriceYearly; }

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

    public boolean isPopular() { return isPopular; }
    public void setPopular(boolean popular) { isPopular = popular; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}