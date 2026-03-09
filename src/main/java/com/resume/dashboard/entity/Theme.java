package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "themes")
public class Theme {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    // ─── Visual Identity ────────────────────────────────────────────
    private ThemeColorPalette colorPalette;
    private ThemeBackground background;
    private ThemeTypography typography;
    private ThemeEffects effects;

    // ─── Classification ─────────────────────────────────────────────
    private VisualMood mood;
    private List<LayoutAudience> targetAudiences;  // who this theme suits

    // ─── Plan gating ────────────────────────────────────────────────
    private PlanType requiredPlan;  // FREE, PRO, ENTERPRISE

    // ─── Admin-defined preview ──────────────────────────────────────
    private String previewImageUrl;
    private String previewVideoUrl;   // optional short loop

    // ─── Status ─────────────────────────────────────────────────────
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
