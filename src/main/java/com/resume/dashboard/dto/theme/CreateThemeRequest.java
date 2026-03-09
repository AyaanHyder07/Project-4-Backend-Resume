package com.resume.dashboard.dto.theme;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ThemeBackground;
import com.resume.dashboard.entity.ThemeColorPalette;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.ThemeTypography;
import com.resume.dashboard.entity.VisualMood;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateThemeRequest {

    @NotBlank(message = "Theme name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    // ─── Visual Identity ─────────────────────────────────────────────
    @NotNull(message = "Color palette is required")
    @Valid
    private ThemeColorPalette colorPalette;

    @NotNull(message = "Background definition is required")
    @Valid
    private ThemeBackground background;

    @NotNull(message = "Typography is required")
    @Valid
    private ThemeTypography typography;

    // Effects are optional — can be null (no special effects)
    @Valid
    private ThemeEffects effects;

    // ─── Classification ──────────────────────────────────────────────
    @NotNull(message = "Visual mood is required")
    private VisualMood mood;

    @NotNull(message = "Target audiences are required")
    @Size(min = 1, message = "At least one target audience is required")
    private List<LayoutAudience> targetAudiences;

    // ─── Plan gating ─────────────────────────────────────────────────
    private PlanType requiredPlan;         // defaults to FREE in service

    // ─── Preview ─────────────────────────────────────────────────────
    private String previewImageUrl;
    private String previewVideoUrl;

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

	public Boolean getFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}
}