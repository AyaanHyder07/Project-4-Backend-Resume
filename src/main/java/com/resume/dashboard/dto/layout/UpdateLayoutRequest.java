package com.resume.dashboard.dto.layout;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutStructureConfig;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.VisualMood;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * All fields are optional — only non-null fields will be applied.
 * Changing structureConfig increments the version number.
 */
public class UpdateLayoutRequest {

    @Size(max = 100, message = "Name must be 100 characters or less")
    private String name;

    @Size(max = 500)
    private String description;

    private LayoutType layoutType;

    private List<LayoutAudience> targetAudiences;

    private List<VisualMood> compatibleMoods;

    private List<String> professionTags;

    @Valid
    private LayoutStructureConfig structureConfig;   // triggers version bump

    private PlanType requiredPlan;

    private String previewImageUrl;

    private Boolean active;

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}