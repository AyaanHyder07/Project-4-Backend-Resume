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
    private List<LayoutAudience> targetAudiences;
    private List<VisualMood> compatibleMoods;
    private List<String> professionTags;
    private List<ProfessionCategory> supportedProfessionCategories;
    private List<ProfessionType> supportedProfessionTypes;
    private List<ContentMode> supportedContentModes;
    private List<MotionPreset> supportedMotionPresets;
    private List<BlockType> recommendedBlockTypes;
    private MotionPreset defaultMotionPreset;

    private LayoutStructureConfig structureConfig;
    private PlanType requiredPlan;
    private String previewImageUrl;
    private boolean active;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LayoutType getLayoutType() { return layoutType; }
    public void setLayoutType(LayoutType layoutType) { this.layoutType = layoutType; }
    public List<LayoutAudience> getTargetAudiences() { return targetAudiences; }
    public void setTargetAudiences(List<LayoutAudience> targetAudiences) { this.targetAudiences = targetAudiences; }
    public List<VisualMood> getCompatibleMoods() { return compatibleMoods; }
    public void setCompatibleMoods(List<VisualMood> compatibleMoods) { this.compatibleMoods = compatibleMoods; }
    public List<String> getProfessionTags() { return professionTags; }
    public void setProfessionTags(List<String> professionTags) { this.professionTags = professionTags; }
    public List<ProfessionCategory> getSupportedProfessionCategories() { return supportedProfessionCategories; }
    public void setSupportedProfessionCategories(List<ProfessionCategory> supportedProfessionCategories) { this.supportedProfessionCategories = supportedProfessionCategories; }
    public List<ProfessionType> getSupportedProfessionTypes() { return supportedProfessionTypes; }
    public void setSupportedProfessionTypes(List<ProfessionType> supportedProfessionTypes) { this.supportedProfessionTypes = supportedProfessionTypes; }
    public List<ContentMode> getSupportedContentModes() { return supportedContentModes; }
    public void setSupportedContentModes(List<ContentMode> supportedContentModes) { this.supportedContentModes = supportedContentModes; }
    public List<MotionPreset> getSupportedMotionPresets() { return supportedMotionPresets; }
    public void setSupportedMotionPresets(List<MotionPreset> supportedMotionPresets) { this.supportedMotionPresets = supportedMotionPresets; }
    public List<BlockType> getRecommendedBlockTypes() { return recommendedBlockTypes; }
    public void setRecommendedBlockTypes(List<BlockType> recommendedBlockTypes) { this.recommendedBlockTypes = recommendedBlockTypes; }
    public MotionPreset getDefaultMotionPreset() { return defaultMotionPreset; }
    public void setDefaultMotionPreset(MotionPreset defaultMotionPreset) { this.defaultMotionPreset = defaultMotionPreset; }
    public LayoutStructureConfig getStructureConfig() { return structureConfig; }
    public void setStructureConfig(LayoutStructureConfig structureConfig) { this.structureConfig = structureConfig; }
    public PlanType getRequiredPlan() { return requiredPlan; }
    public void setRequiredPlan(PlanType requiredPlan) { this.requiredPlan = requiredPlan; }
    public String getPreviewImageUrl() { return previewImageUrl; }
    public void setPreviewImageUrl(String previewImageUrl) { this.previewImageUrl = previewImageUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
