package com.resume.dashboard.dto.customization;

import com.resume.dashboard.entity.ThemeBackground;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

/**
 * PRO/ENTERPRISE only.
 * All fields optional except baseThemeId.
 * Only the fields the user actually changed need to be sent.
 * Null fields = keep the base theme's default value.
 */
public class SaveCustomizationRequest {

    @NotBlank(message = "Base theme ID is required")
    private String baseThemeId;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^rgba?\\(.*\\)$",
             message = "primaryColor must be a valid hex or rgba color")
    private String primaryColor;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^rgba?\\(.*\\)$")
    private String secondaryColor;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^rgba?\\(.*\\)$")
    private String accentColor;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^rgba?\\(.*\\)$")
    private String textPrimaryColor;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^rgba?\\(.*\\)$")
    private String textSecondaryColor;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^rgba?\\(.*\\)$")
    private String pageBackgroundColor;

    private ThemeBackground.GradientConfig customGradient;

    private String headingFont;
    private String bodyFont;

    @Min(value = 12, message = "Base font size must be at least 12px equivalent")
    @Max(value = 24, message = "Base font size must not exceed 24px equivalent")
    private Double baseFontSize;

    @Min(100)
    @Max(900)
    private Integer headingWeight;

    private Boolean enableGrain;

    @Min(0)
    @Max(100)
    private Integer grainIntensity;

    private String cardBorderRadius;
    private Map<String, String> templateOptions;
    private Map<String, String> templateLabels;

    public String getBaseThemeId() { return baseThemeId; }
    public void setBaseThemeId(String baseThemeId) { this.baseThemeId = baseThemeId; }
    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }
    public String getAccentColor() { return accentColor; }
    public void setAccentColor(String accentColor) { this.accentColor = accentColor; }
    public String getTextPrimaryColor() { return textPrimaryColor; }
    public void setTextPrimaryColor(String textPrimaryColor) { this.textPrimaryColor = textPrimaryColor; }
    public String getTextSecondaryColor() { return textSecondaryColor; }
    public void setTextSecondaryColor(String textSecondaryColor) { this.textSecondaryColor = textSecondaryColor; }
    public String getPageBackgroundColor() { return pageBackgroundColor; }
    public void setPageBackgroundColor(String pageBackgroundColor) { this.pageBackgroundColor = pageBackgroundColor; }
    public ThemeBackground.GradientConfig getCustomGradient() { return customGradient; }
    public void setCustomGradient(ThemeBackground.GradientConfig customGradient) { this.customGradient = customGradient; }
    public String getHeadingFont() { return headingFont; }
    public void setHeadingFont(String headingFont) { this.headingFont = headingFont; }
    public String getBodyFont() { return bodyFont; }
    public void setBodyFont(String bodyFont) { this.bodyFont = bodyFont; }
    public Double getBaseFontSize() { return baseFontSize; }
    public void setBaseFontSize(Double baseFontSize) { this.baseFontSize = baseFontSize; }
    public Integer getHeadingWeight() { return headingWeight; }
    public void setHeadingWeight(Integer headingWeight) { this.headingWeight = headingWeight; }
    public Boolean getEnableGrain() { return enableGrain; }
    public void setEnableGrain(Boolean enableGrain) { this.enableGrain = enableGrain; }
    public Integer getGrainIntensity() { return grainIntensity; }
    public void setGrainIntensity(Integer grainIntensity) { this.grainIntensity = grainIntensity; }
    public String getCardBorderRadius() { return cardBorderRadius; }
    public void setCardBorderRadius(String cardBorderRadius) { this.cardBorderRadius = cardBorderRadius; }
    public Map<String, String> getTemplateOptions() { return templateOptions; }
    public void setTemplateOptions(Map<String, String> templateOptions) { this.templateOptions = templateOptions; }
    public Map<String, String> getTemplateLabels() { return templateLabels; }
    public void setTemplateLabels(Map<String, String> templateLabels) { this.templateLabels = templateLabels; }
}
