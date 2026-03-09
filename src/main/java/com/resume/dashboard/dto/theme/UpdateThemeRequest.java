package com.resume.dashboard.dto.theme;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ThemeBackground;
import com.resume.dashboard.entity.ThemeColorPalette;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.ThemeTypography;
import com.resume.dashboard.entity.VisualMood;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * All fields optional — only non-null fields are applied.
 * Changing colorPalette, background, typography, or effects increments version.
 */
public class UpdateThemeRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @Valid
    private ThemeColorPalette colorPalette;    // triggers version bump

    @Valid
    private ThemeBackground background;         // triggers version bump

    @Valid
    private ThemeTypography typography;         // triggers version bump

    @Valid
    private ThemeEffects effects;               // triggers version bump

    private VisualMood mood;

    private List<LayoutAudience> targetAudiences;

    private PlanType requiredPlan;

    private String previewImageUrl;
    private String previewVideoUrl;

    private Boolean active;
    private Boolean featured;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ThemeColorPalette getColorPalette() {
		return colorPalette;
	}
	public void setColorPalette(ThemeColorPalette colorPalette) {
		this.colorPalette = colorPalette;
	}
	public ThemeBackground getBackground() {
		return background;
	}
	public void setBackground(ThemeBackground background) {
		this.background = background;
	}
	public ThemeTypography getTypography() {
		return typography;
	}
	public void setTypography(ThemeTypography typography) {
		this.typography = typography;
	}
	public ThemeEffects getEffects() {
		return effects;
	}
	public void setEffects(ThemeEffects effects) {
		this.effects = effects;
	}
	public VisualMood getMood() {
		return mood;
	}
	public void setMood(VisualMood mood) {
		this.mood = mood;
	}
	public List<LayoutAudience> getTargetAudiences() {
		return targetAudiences;
	}
	public void setTargetAudiences(List<LayoutAudience> targetAudiences) {
		this.targetAudiences = targetAudiences;
	}
	public PlanType getRequiredPlan() {
		return requiredPlan;
	}
	public void setRequiredPlan(PlanType requiredPlan) {
		this.requiredPlan = requiredPlan;
	}
	public String getPreviewImageUrl() {
		return previewImageUrl;
	}
	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}
	public String getPreviewVideoUrl() {
		return previewVideoUrl;
	}
	public void setPreviewVideoUrl(String previewVideoUrl) {
		this.previewVideoUrl = previewVideoUrl;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean getFeatured() {
		return featured;
	}
	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}
}