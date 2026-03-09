package com.resume.dashboard.dto.layout;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutStructureConfig;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.VisualMood;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateLayoutRequest {

    @NotBlank(message = "Layout name is required")
    @Size(max = 100, message = "Name must be 100 characters or less")
    private String name;

    @Size(max = 500, message = "Description must be 500 characters or less")
    private String description;

    @NotNull(message = "Layout type is required")
    private LayoutType layoutType;

    // Who this layout is designed for — at least one required
    @NotNull(message = "Target audiences are required")
    @Size(min = 1, message = "At least one target audience is required")
    private List<LayoutAudience> targetAudiences;

    // Which visual moods pair well with this layout
    private List<VisualMood> compatibleMoods;

    // Free-text search tags e.g. "musician", "surgeon", "startup"
    private List<String> professionTags;

    // Typed structure — zones, columns, grid config etc.
    @NotNull(message = "Structure config is required")
    @Valid
    private LayoutStructureConfig structureConfig;

    // Plan required to use this layout — defaults to FREE in service
    private PlanType requiredPlan;

    private String previewImageUrl;

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
}