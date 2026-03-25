package com.resume.dashboard.dto.template;

import com.resume.dashboard.entity.BlockType;
import com.resume.dashboard.entity.ContentMode;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
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
    @Size(max = 100, message = "Tagline must be 100 characters or less")
    private String tagline;
    private String previewImageUrl;
    private String previewVideoUrl;
    @NotNull(message = "Plan level is required")
    private PlanType planLevel;
    @NotBlank(message = "Layout ID is required")
    private String layoutId;
    @NotBlank(message = "Default theme ID is required")
    private String defaultThemeId;
    @NotNull(message = "Target audiences are required")
    @Size(min = 1, message = "At least one target audience is required")
    private List<LayoutAudience> targetAudiences;
    private List<String> professionTags;
    private List<ProfessionCategory> professionCategories;
    private List<ProfessionType> professionTypes;
    @NotNull(message = "Primary mood is required")
    private VisualMood primaryMood;
    private List<ContentMode> supportedContentModes;
    @NotNull(message = "Supported sections are required")
    @Size(min = 1)
    private List<String> supportedSections;
    private List<String> requiredSections;
    private List<BlockType> supportedBlockTypes;
    private List<BlockType> recommendedBlockTypes;
    private List<MotionPreset> allowedMotionPresets;
    private MotionPreset defaultMotionPreset;
    private Boolean featured;
    private Boolean isNew;
    private Integer premiumRank;
    private Boolean globallySelectable;

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
    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }
    public Boolean getIsNew() { return isNew; }
    public void setIsNew(Boolean isNew) { this.isNew = isNew; }
    public Integer getPremiumRank() { return premiumRank; }
    public void setPremiumRank(Integer premiumRank) { this.premiumRank = premiumRank; }
    public Boolean getGloballySelectable() { return globallySelectable; }
    public void setGloballySelectable(Boolean globallySelectable) { this.globallySelectable = globallySelectable; }
}
