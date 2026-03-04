package com.resume.dashboard.dto.template;

import com.resume.dashboard.entity.PlanType;

import java.time.Instant;
import java.util.List;

public class TemplateResponse {

    private String id;
    private String name;
    private String description;
    private String previewImageUrl;
    private PlanType planLevel;
    private boolean active;
    private List<String> professionTags;
    private String layoutId;
    private String defaultThemeId;
    private List<String> supportedSections;
    private int version;
    private boolean featured;
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
	public String getPreviewImageUrl() {
		return previewImageUrl;
	}
	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}
	public PlanType getPlanLevel() {
		return planLevel;
	}
	public void setPlanLevel(PlanType planLevel) {
		this.planLevel = planLevel;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<String> getProfessionTags() {
		return professionTags;
	}
	public void setProfessionTags(List<String> professionTags) {
		this.professionTags = professionTags;
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
	public List<String> getSupportedSections() {
		return supportedSections;
	}
	public void setSupportedSections(List<String> supportedSections) {
		this.supportedSections = supportedSections;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public boolean isFeatured() {
		return featured;
	}
	public void setFeatured(boolean featured) {
		this.featured = featured;
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

    // getters & setters
}