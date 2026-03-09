package com.resume.dashboard.dto.layout;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutStructureConfig;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.VisualMood;


import java.time.Instant;
import java.util.List;

public class LayoutResponse {

    private String id;
    private String name;
    private String description;
    private LayoutType layoutType;

    private List<LayoutAudience> targetAudiences;
    private List<VisualMood> compatibleMoods;
    private List<String> professionTags;

    private LayoutStructureConfig structureConfig;

    private PlanType requiredPlan;
    private String previewImageUrl;

    private boolean active;
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
	public LayoutType getLayoutType() {
		return layoutType;
	}
	public void setLayoutType(LayoutType layoutType) {
		this.layoutType = layoutType;
	}
	public List<LayoutAudience> getTargetAudiences() {
		return targetAudiences;
	}
	public void setTargetAudiences(List<LayoutAudience> targetAudiences) {
		this.targetAudiences = targetAudiences;
	}
	public List<VisualMood> getCompatibleMoods() {
		return compatibleMoods;
	}
	public void setCompatibleMoods(List<VisualMood> compatibleMoods) {
		this.compatibleMoods = compatibleMoods;
	}
	public List<String> getProfessionTags() {
		return professionTags;
	}
	public void setProfessionTags(List<String> professionTags) {
		this.professionTags = professionTags;
	}
	public LayoutStructureConfig getStructureConfig() {
		return structureConfig;
	}
	public void setStructureConfig(LayoutStructureConfig structureConfig) {
		this.structureConfig = structureConfig;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
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