package com.resume.dashboard.dto.publicview;

import com.resume.dashboard.entity.BlockType;
import com.resume.dashboard.entity.ContentMode;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutStructureConfig;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.VisualMood;

import java.util.List;

public class LayoutDTO {
    private String layoutType;
    private int layoutVersion;
    private LayoutStructureConfig structureConfig;
    private List<LayoutAudience> targetAudiences;
    private List<VisualMood> compatibleMoods;
    private List<String> professionTags;
    private List<ProfessionCategory> supportedProfessionCategories;
    private List<ProfessionType> supportedProfessionTypes;
    private List<ContentMode> supportedContentModes;
    private List<MotionPreset> supportedMotionPresets;
    private List<BlockType> recommendedBlockTypes;
    private MotionPreset defaultMotionPreset;

    public String getLayoutType() { return layoutType; }
    public void setLayoutType(String layoutType) { this.layoutType = layoutType; }
    public int getLayoutVersion() { return layoutVersion; }
    public void setLayoutVersion(int layoutVersion) { this.layoutVersion = layoutVersion; }
    public LayoutStructureConfig getStructureConfig() { return structureConfig; }
    public void setStructureConfig(LayoutStructureConfig structureConfig) { this.structureConfig = structureConfig; }
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
}
