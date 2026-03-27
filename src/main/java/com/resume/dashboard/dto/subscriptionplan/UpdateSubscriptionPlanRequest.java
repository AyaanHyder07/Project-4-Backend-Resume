package com.resume.dashboard.dto.subscriptionplan;

public class UpdateSubscriptionPlanRequest {
    private String displayName;
    private String description;
    private Long priceMonthlyInSmallestUnit;
    private Long priceYearlyInSmallestUnit;
    private String currency;
    private String displayPriceMonthly;
    private String displayPriceYearly;
    private Integer resumeLimit;
    private Integer publicLinkLimit;
    private Boolean versioningEnabled;
    private Boolean themeCustomizationEnabled;
    private Boolean templateChangeEnabled;
    private Boolean customSlugEnabled;
    private Boolean oneTimeOnly;
    private Integer trialDurationDays;
    private Boolean isPopular;
    private Boolean isActive;
    private Integer displayOrder;
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getPriceMonthlyInSmallestUnit() { return priceMonthlyInSmallestUnit; }
    public void setPriceMonthlyInSmallestUnit(Long priceMonthlyInSmallestUnit) { this.priceMonthlyInSmallestUnit = priceMonthlyInSmallestUnit; }
    public Long getPriceYearlyInSmallestUnit() { return priceYearlyInSmallestUnit; }
    public void setPriceYearlyInSmallestUnit(Long priceYearlyInSmallestUnit) { this.priceYearlyInSmallestUnit = priceYearlyInSmallestUnit; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getDisplayPriceMonthly() { return displayPriceMonthly; }
    public void setDisplayPriceMonthly(String displayPriceMonthly) { this.displayPriceMonthly = displayPriceMonthly; }
    public String getDisplayPriceYearly() { return displayPriceYearly; }
    public void setDisplayPriceYearly(String displayPriceYearly) { this.displayPriceYearly = displayPriceYearly; }
    public Integer getResumeLimit() { return resumeLimit; }
    public void setResumeLimit(Integer resumeLimit) { this.resumeLimit = resumeLimit; }
    public Integer getPublicLinkLimit() { return publicLinkLimit; }
    public void setPublicLinkLimit(Integer publicLinkLimit) { this.publicLinkLimit = publicLinkLimit; }
    public Boolean getVersioningEnabled() { return versioningEnabled; }
    public void setVersioningEnabled(Boolean versioningEnabled) { this.versioningEnabled = versioningEnabled; }
    public Boolean getThemeCustomizationEnabled() { return themeCustomizationEnabled; }
    public void setThemeCustomizationEnabled(Boolean themeCustomizationEnabled) { this.themeCustomizationEnabled = themeCustomizationEnabled; }
    public Boolean getTemplateChangeEnabled() { return templateChangeEnabled; }
    public void setTemplateChangeEnabled(Boolean templateChangeEnabled) { this.templateChangeEnabled = templateChangeEnabled; }
    public Boolean getCustomSlugEnabled() { return customSlugEnabled; }
    public void setCustomSlugEnabled(Boolean customSlugEnabled) { this.customSlugEnabled = customSlugEnabled; }
    public Boolean getOneTimeOnly() { return oneTimeOnly; }
    public void setOneTimeOnly(Boolean oneTimeOnly) { this.oneTimeOnly = oneTimeOnly; }
    public Integer getTrialDurationDays() { return trialDurationDays; }
    public void setTrialDurationDays(Integer trialDurationDays) { this.trialDurationDays = trialDurationDays; }
    public Boolean getIsPopular() { return isPopular; }
    public void setIsPopular(Boolean isPopular) { this.isPopular = isPopular; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
