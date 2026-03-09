package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "layouts")
public class Layout {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    private LayoutType layoutType;

    // ─── Classification ─────────────────────────────────────────────
    private List<LayoutAudience> targetAudiences;  // e.g. [ARTIST, PHOTOGRAPHER]
    private List<VisualMood> compatibleMoods;       // moods this layout suits
    private List<String> professionTags;            // free-text: "musician", "surgeon"

    // ─── Structure Config ────────────────────────────────────────────
    private LayoutStructureConfig structureConfig;

    // ─── Plan gating ────────────────────────────────────────────────
    private PlanType requiredPlan;

    // ─── Admin-defined preview ──────────────────────────────────────
    private String previewImageUrl;

    // ─── Status ─────────────────────────────────────────────────────
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