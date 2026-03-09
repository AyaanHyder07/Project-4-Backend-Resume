package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * SubscriptionPlan — the SOURCE OF TRUTH for what each plan costs and allows.
 *
 * WHY THIS ENTITY EXISTS:
 *   Previously plan limits were hardcoded in SubscriptionService.applyPlanLimits().
 *   That means to change BASIC price from ₹99 to ₹149, or give PRO 3 resumes instead
 *   of 2, you'd have to edit code and redeploy. This entity moves all of that to the DB
 *   so an admin API call is all it takes.
 *
 * HOW IT CONNECTS:
 *   PlanType (enum) is still the identifier — FREE, BASIC, PRO, PREMIUM.
 *   SubscriptionService.applyPlanLimits() now fetches the matching SubscriptionPlan
 *   record and reads limits from it instead of hardcoding them.
 *
 * SEEDING:
 *   On first startup, seed 4 records — one per PlanType — via a DataSeeder component.
 */
@Document(collection = "subscription_plans")
public class SubscriptionPlan {

    @Id
    private String id;

    // Matches PlanType enum — unique per plan
    @Indexed(unique = true)
    private PlanType planType; // FREE, BASIC, PRO, PREMIUM

    private String displayName;   // e.g. "Pro Plan"
    private String description;   // shown on pricing page

    // ─── PRICING ─────────────────────────────────────────────────────
    // Store in smallest currency unit to avoid floating point issues
    // e.g. ₹199 = 19900 paise, $9.99 = 999 cents
    private long priceMonthlyInSmallestUnit;  // 0 for FREE
    private long priceYearlyInSmallestUnit;   // 0 for FREE
    private String currency;                  // "INR" or "USD"

    // Human-readable display prices (e.g. "₹199/mo") — set by admin, shown on UI
    private String displayPriceMonthly;
    private String displayPriceYearly;

    // ─── FEATURE LIMITS ──────────────────────────────────────────────
    private int resumeLimit;
    private int publicLinkLimit;
    private boolean versioningEnabled;
    private boolean themeCustomizationEnabled;

    // ─── UI / MARKETING ──────────────────────────────────────────────
    private boolean isPopular;       // shows "Most Popular" badge
    private boolean isActive;        // false = plan hidden from pricing page (not purchasable)
    private int displayOrder;        // sort order on pricing page (0=FREE, 1=BASIC, 2=PRO, 3=PREMIUM)

    private Instant createdAt;
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

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}