package com.resume.dashboard.dto.theme;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ThemeBackground;
import com.resume.dashboard.entity.ThemeColorPalette;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.ThemeTypography;
import com.resume.dashboard.entity.VisualMood;


import java.time.Instant;
import java.util.List;

public class ThemeResponse {

    private String id;
    private String name;
    private String description;

    // Full structured visual identity
    private ThemeColorPalette colorPalette;
    private ThemeBackground background;
    private ThemeTypography typography;
    private ThemeEffects effects;

    private VisualMood mood;
    private List<LayoutAudience> targetAudiences;

    private PlanType requiredPlan;
    private String previewImageUrl;
    private String previewVideoUrl;

    private boolean active;
    private boolean featured;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isFeatured() {
		return featured;
	}
	public void setFeatured(boolean featured) {
		this.featured = featured;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
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