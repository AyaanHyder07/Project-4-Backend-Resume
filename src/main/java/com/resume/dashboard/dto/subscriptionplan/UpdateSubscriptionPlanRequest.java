package com.resume.dashboard.dto.subscriptionplan;

public class UpdateSubscriptionPlanRequest {

    // ─── DISPLAY ─────────────────────────────────────────────────────
    private String displayName;
    private String description;

    // ─── PRICING ─────────────────────────────────────────────────────
    // Use Long (boxed) not long (primitive) so null = "don't update this field"
    private Long priceMonthlyInSmallestUnit;
    private Long priceYearlyInSmallestUnit;
    private String currency;
    private String displayPriceMonthly;  // e.g. "₹199/mo"
    private String displayPriceYearly;   // e.g. "₹1990/yr"

    // ─── FEATURE LIMITS ──────────────────────────────────────────────
    // Use Integer/Boolean (boxed) so null = "don't update this field"
    private Integer resumeLimit;
    private Integer publicLinkLimit;
    private Boolean versioningEnabled;
    private Boolean themeCustomizationEnabled;

    // ─── UI FLAGS ────────────────────────────────────────────────────
    private Boolean isPopular;     // shows "Most Popular" badge on pricing page
    private Boolean isActive;      // false = hidden from pricing page
    private Integer displayOrder;  // sort position on pricing page

    // ─── GETTERS & SETTERS ───────────────────────────────────────────

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getPriceMonthlyInSmallestUnit() { return priceMonthlyInSmallestUnit; }
    public void setPriceMonthlyInSmallestUnit(Long priceMonthlyInSmallestUnit) {
        this.priceMonthlyInSmallestUnit = priceMonthlyInSmallestUnit;
    }

    public Long getPriceYearlyInSmallestUnit() { return priceYearlyInSmallestUnit; }
    public void setPriceYearlyInSmallestUnit(Long priceYearlyInSmallestUnit) {
        this.priceYearlyInSmallestUnit = priceYearlyInSmallestUnit;
    }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDisplayPriceMonthly() { return displayPriceMonthly; }
    public void setDisplayPriceMonthly(String displayPriceMonthly) {
        this.displayPriceMonthly = displayPriceMonthly;
    }

    public String getDisplayPriceYearly() { return displayPriceYearly; }
    public void setDisplayPriceYearly(String displayPriceYearly) {
        this.displayPriceYearly = displayPriceYearly;
    }

    public Integer getResumeLimit() { return resumeLimit; }
    public void setResumeLimit(Integer resumeLimit) { this.resumeLimit = resumeLimit; }

    public Integer getPublicLinkLimit() { return publicLinkLimit; }
    public void setPublicLinkLimit(Integer publicLinkLimit) { this.publicLinkLimit = publicLinkLimit; }

    public Boolean getVersioningEnabled() { return versioningEnabled; }
    public void setVersioningEnabled(Boolean versioningEnabled) {
        this.versioningEnabled = versioningEnabled;
    }

    public Boolean getThemeCustomizationEnabled() { return themeCustomizationEnabled; }
    public void setThemeCustomizationEnabled(Boolean themeCustomizationEnabled) {
        this.themeCustomizationEnabled = themeCustomizationEnabled;
    }

    public Boolean getIsPopular() { return isPopular; }
    public void setIsPopular(Boolean isPopular) { this.isPopular = isPopular; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}