package com.resume.dashboard.dto.template;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.VisualMood;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * All fields optional.
 * Changing layoutId, defaultThemeId, or supportedSections increments version.
 */
public class UpdateTemplateRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @Size(max = 100)
    private String tagline;

    private String previewImageUrl;
    private String previewVideoUrl;

    private PlanType planLevel;

    private String layoutId;            // triggers version bump
    private String defaultThemeId;      // triggers version bump

    private List<LayoutAudience> targetAudiences;
    private List<String> professionTags;
    private VisualMood primaryMood;

    private List<String> supportedSections;  // triggers version bump
    private List<String> requiredSections;

    private Boolean active;
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
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
}