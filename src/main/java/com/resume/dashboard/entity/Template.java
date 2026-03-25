package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "templates")
@CompoundIndex(def = "{'name': 1}", unique = true)
public class Template {

    @Id
    private String id;

    @Indexed
    private String name;

    private String description;
    private String tagline;
    private String previewImageUrl;
    private String previewVideoUrl;

    @Indexed
    private PlanType planLevel;

    @Indexed
    private String layoutId;
    @Indexed
    private String defaultThemeId;

    @Indexed
    private List<LayoutAudience> targetAudiences;
    @Indexed
    private List<String> professionTags;
    private List<ProfessionCategory> professionCategories;
    private List<ProfessionType> professionTypes;
    private VisualMood primaryMood;
    private List<ContentMode> supportedContentModes;

    private List<String> supportedSections;
    private List<String> requiredSections;
    private List<BlockType> supportedBlockTypes;
    private List<BlockType> recommendedBlockTypes;
    private List<MotionPreset> allowedMotionPresets;
    private MotionPreset defaultMotionPreset;

    private boolean active;
    private boolean featured;
    private boolean isNew;
    private Integer popularityScore;
    private Integer premiumRank;
    private boolean globallySelectable;

    private int version;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }
    public String getPreviewImageUrl() { return previewImageUrl; }
    public void setPreviewImageUrl(String previewImageUrl) { this.previewImageUrl = previewImageUrl; }
    public String getPreviewVideoUrl() { return previewVideoUrl; }
    public void setPreviewVideoUrl(String previewVideoUrl) { this.previewVideoUrl = previewVideoUrl; }
    public PlanType getPlanLevel() { return planLevel; }
    public void setPlanLevel(PlanType planLevel) { this.planLevel = planLevel; }
    public String getLayoutId() { return layoutId; }
    public void setLayoutId(String layoutId) { this.layoutId = layoutId; }
    public String getDefaultThemeId() { return defaultThemeId; }
    public void setDefaultThemeId(String defaultThemeId) { this.defaultThemeId = defaultThemeId; }
    public List<LayoutAudience> getTargetAudiences() { return targetAudiences; }
    public void setTargetAudiences(List<LayoutAudience> targetAudiences) { this.targetAudiences = targetAudiences; }
    public List<String> getProfessionTags() { return professionTags; }
    public void setProfessionTags(List<String> professionTags) { this.professionTags = professionTags; }
    public List<ProfessionCategory> getProfessionCategories() { return professionCategories; }
    public void setProfessionCategories(List<ProfessionCategory> professionCategories) { this.professionCategories = professionCategories; }
    public List<ProfessionType> getProfessionTypes() { return professionTypes; }
    public void setProfessionTypes(List<ProfessionType> professionTypes) { this.professionTypes = professionTypes; }
    public VisualMood getPrimaryMood() { return primaryMood; }
    public void setPrimaryMood(VisualMood primaryMood) { this.primaryMood = primaryMood; }
    public List<ContentMode> getSupportedContentModes() { return supportedContentModes; }
    public void setSupportedContentModes(List<ContentMode> supportedContentModes) { this.supportedContentModes = supportedContentModes; }
    public List<String> getSupportedSections() { return supportedSections; }
    public void setSupportedSections(List<String> supportedSections) { this.supportedSections = supportedSections; }
    public List<String> getRequiredSections() { return requiredSections; }
    public void setRequiredSections(List<String> requiredSections) { this.requiredSections = requiredSections; }
    public List<BlockType> getSupportedBlockTypes() { return supportedBlockTypes; }
    public void setSupportedBlockTypes(List<BlockType> supportedBlockTypes) { this.supportedBlockTypes = supportedBlockTypes; }
    public List<BlockType> getRecommendedBlockTypes() { return recommendedBlockTypes; }
    public void setRecommendedBlockTypes(List<BlockType> recommendedBlockTypes) { this.recommendedBlockTypes = recommendedBlockTypes; }
    public List<MotionPreset> getAllowedMotionPresets() { return allowedMotionPresets; }
    public void setAllowedMotionPresets(List<MotionPreset> allowedMotionPresets) { this.allowedMotionPresets = allowedMotionPresets; }
    public MotionPreset getDefaultMotionPreset() { return defaultMotionPreset; }
    public void setDefaultMotionPreset(MotionPreset defaultMotionPreset) { this.defaultMotionPreset = defaultMotionPreset; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
    public Integer getPopularityScore() { return popularityScore; }
    public void setPopularityScore(Integer popularityScore) { this.popularityScore = popularityScore; }
    public Integer getPremiumRank() { return premiumRank; }
    public void setPremiumRank(Integer premiumRank) { this.premiumRank = premiumRank; }
    public boolean isGloballySelectable() { return globallySelectable; }
    public void setGloballySelectable(boolean globallySelectable) { this.globallySelectable = globallySelectable; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
