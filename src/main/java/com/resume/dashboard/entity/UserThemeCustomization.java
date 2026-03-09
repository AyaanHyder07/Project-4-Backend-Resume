package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * PRO/ENTERPRISE ONLY.
 * Stores a user's personal overrides on top of a base theme.
 * The base theme stays untouched — this is a DELTA / PATCH layer.
 *
 * Resolution order at render time:
 *   Base Theme defaults
 *     → UserThemeCustomization overrides (if plan allows)
 */
@Document(collection = "user_theme_customizations")
@CompoundIndex(def = "{'userId': 1, 'resumeId': 1}", unique = true)
public class UserThemeCustomization {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String resumeId;         // which resume/portfolio this applies to

    private String baseThemeId;      // the theme being overridden

    // ─── Color overrides (partial — only what user changed) ──────────
    private String primaryColor;
    private String secondaryColor;
    private String accentColor;
    private String textPrimaryColor;
    private String textSecondaryColor;
    private String pageBackgroundColor;

    // ─── Gradient override ───────────────────────────────────────────
    private ThemeBackground.GradientConfig customGradient;  // if user picks custom gradient

    // ─── Typography overrides ────────────────────────────────────────
    private String headingFont;          // override heading font
    private String bodyFont;             // override body font
    private Double baseFontSize;         // override base size (rem)
    private Integer headingWeight;       // override heading weight

    // ─── Effect toggles ─────────────────────────────────────────────
    private Boolean enableGrain;
    private Integer grainIntensity;
    private String cardBorderRadius;

    // ─── Metadata ────────────────────────────────────────────────────
    private Instant createdAt;
    private Instant updatedAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getResumeId() {
		return resumeId;
	}
	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}
	public String getBaseThemeId() {
		return baseThemeId;
	}
	public void setBaseThemeId(String baseThemeId) {
		this.baseThemeId = baseThemeId;
	}
	public String getPrimaryColor() {
		return primaryColor;
	}
	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = primaryColor;
	}
	public String getSecondaryColor() {
		return secondaryColor;
	}
	public void setSecondaryColor(String secondaryColor) {
		this.secondaryColor = secondaryColor;
	}
	public String getAccentColor() {
		return accentColor;
	}
	public void setAccentColor(String accentColor) {
		this.accentColor = accentColor;
	}
	public String getTextPrimaryColor() {
		return textPrimaryColor;
	}
	public void setTextPrimaryColor(String textPrimaryColor) {
		this.textPrimaryColor = textPrimaryColor;
	}
	public String getTextSecondaryColor() {
		return textSecondaryColor;
	}
	public void setTextSecondaryColor(String textSecondaryColor) {
		this.textSecondaryColor = textSecondaryColor;
	}
	public String getPageBackgroundColor() {
		return pageBackgroundColor;
	}
	public void setPageBackgroundColor(String pageBackgroundColor) {
		this.pageBackgroundColor = pageBackgroundColor;
	}
	public ThemeBackground.GradientConfig getCustomGradient() {
		return customGradient;
	}
	public void setCustomGradient(ThemeBackground.GradientConfig customGradient) {
		this.customGradient = customGradient;
	}
	public String getHeadingFont() {
		return headingFont;
	}
	public void setHeadingFont(String headingFont) {
		this.headingFont = headingFont;
	}
	public String getBodyFont() {
		return bodyFont;
	}
	public void setBodyFont(String bodyFont) {
		this.bodyFont = bodyFont;
	}
	public Double getBaseFontSize() {
		return baseFontSize;
	}
	public void setBaseFontSize(Double baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	public Integer getHeadingWeight() {
		return headingWeight;
	}
	public void setHeadingWeight(Integer headingWeight) {
		this.headingWeight = headingWeight;
	}
	public Boolean getEnableGrain() {
		return enableGrain;
	}
	public void setEnableGrain(Boolean enableGrain) {
		this.enableGrain = enableGrain;
	}
	public Integer getGrainIntensity() {
		return grainIntensity;
	}
	public void setGrainIntensity(Integer grainIntensity) {
		this.grainIntensity = grainIntensity;
	}
	public String getCardBorderRadius() {
		return cardBorderRadius;
	}
	public void setCardBorderRadius(String cardBorderRadius) {
		this.cardBorderRadius = cardBorderRadius;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    
}