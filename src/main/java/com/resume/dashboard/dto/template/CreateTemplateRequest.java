package com.resume.dashboard.dto.template;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.VisualMood;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateTemplateRequest {

    @NotBlank(message = "Template name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    // Short marketing hook shown on the template picker card
    @Size(max = 100, message = "Tagline must be 100 characters or less")
    private String tagline;

    private String previewImageUrl;
    private String previewVideoUrl;

    // ─── Plan gating ─────────────────────────────────────────────────
    @NotNull(message = "Plan level is required")
    private PlanType planLevel;

    // ─── Composition ─────────────────────────────────────────────────
    @NotBlank(message = "Layout ID is required")
    private String layoutId;

    @NotBlank(message = "Default theme ID is required")
    private String defaultThemeId;

    // ─── Classification ──────────────────────────────────────────────
    @NotNull(message = "Target audiences are required")
    @Size(min = 1, message = "At least one target audience is required")
    private List<LayoutAudience> targetAudiences;

    // Free-text search tags e.g. "musician", "front-end dev", "neurosurgeon"
    private List<String> professionTags;

    @NotNull(message = "Primary mood is required")
    private VisualMood primaryMood;

    // ─── Sections ────────────────────────────────────────────────────
    // All section type keys this template can display
    @NotNull(message = "Supported sections are required")
    @Size(min = 1)
    private List<String> supportedSections;

    // Section keys user cannot remove (e.g. "header", "contact")
    private List<String> requiredSections;

    // ─── Marketing flags ─────────────────────────────────────────────
    private Boolean featured;
    private Boolean isNew;
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
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
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
	public PlanType getPlanLevel() {
		return planLevel;
	}
	public void setPlanLevel(PlanType planLevel) {
		this.planLevel = planLevel;
	}
	public String getLayoutId() {
		return layoutId;
	}
	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}
	public String getDefaultThemeId() {
		return defaultThemeId;
	}
	public void setDefaultThemeId(String defaultThemeId) {
		this.defaultThemeId = defaultThemeId;
	}
	public List<LayoutAudience> getTargetAudiences() {
		return targetAudiences;
	}
	public void setTargetAudiences(List<LayoutAudience> targetAudiences) {
		this.targetAudiences = targetAudiences;
	}
	public List<String> getProfessionTags() {
		return professionTags;
	}
	public void setProfessionTags(List<String> professionTags) {
		this.professionTags = professionTags;
	}
	public VisualMood getPrimaryMood() {
		return primaryMood;
	}
	public void setPrimaryMood(VisualMood primaryMood) {
		this.primaryMood = primaryMood;
	}
	public List<String> getSupportedSections() {
		return supportedSections;
	}
	public void setSupportedSections(List<String> supportedSections) {
		this.supportedSections = supportedSections;
	}
	public List<String> getRequiredSections() {
		return requiredSections;
	}
	public void setRequiredSections(List<String> requiredSections) {
		this.requiredSections = requiredSections;
	}
	public Boolean getFeatured() {
		return featured;
	}
	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
}