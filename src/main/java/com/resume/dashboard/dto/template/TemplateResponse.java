package com.resume.dashboard.dto.template;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.VisualMood;


import java.time.Instant;
import java.util.List;

public class TemplateResponse {

    private String id;
    private String name;
    private String description;
    private String tagline;

    private String previewImageUrl;
    private String previewVideoUrl;

    private PlanType planLevel;

    // References — IDs only in response (frontend fetches full objects separately if needed)
    private String layoutId;
    private String defaultThemeId;
    
    // Add enriched objects for frontend visual preview
    private Object layout;
    private Object theme;

    private List<LayoutAudience> targetAudiences;
    private List<String> professionTags;
    private VisualMood primaryMood;

    private List<String> supportedSections;
    private List<String> requiredSections;

    private boolean active;
    private boolean featured;
    private boolean isNew;
    private Integer popularityScore;

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
	public Object getLayout() {
		return layout;
	}
	public void setLayout(Object layout) {
		this.layout = layout;
	}
	public Object getTheme() {
		return theme;
	}
	public void setTheme(Object theme) {
		this.theme = theme;
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
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public Integer getPopularityScore() {
		return popularityScore;
	}
	public void setPopularityScore(Integer popularityScore) {
		this.popularityScore = popularityScore;
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