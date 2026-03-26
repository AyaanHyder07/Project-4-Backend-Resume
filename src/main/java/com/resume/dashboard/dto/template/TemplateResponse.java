package com.resume.dashboard.dto.template;

import com.resume.dashboard.entity.BlockType;
import com.resume.dashboard.entity.ContentMode;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.TemplateDefaultTheme;
import com.resume.dashboard.entity.VisualMood;

import java.time.Instant;
import java.util.List;

public class TemplateResponse {
    private String id;
    private String name;
    private String templateKey;
    private String renderFamily;
    private String variantKey;
    private String profession;
    private String description;
    private String tagline;
    private String previewImageUrl;
    private String previewVideoUrl;
    private PlanType planLevel;
    private String layoutId;
    private String defaultThemeId;
    private Object layout;
    private Object theme;
    private List<LayoutAudience> targetAudiences;
    private List<String> professionTags;
    private List<ProfessionCategory> professionCategories;
    private List<ProfessionType> professionTypes;
    private TemplateDefaultTheme defaultTheme;
    private VisualMood primaryMood;
    private List<ContentMode> supportedContentModes;
    private List<String> supportedSections;
    private List<String> enabledSections;
    private List<String> sectionOrder;
    private List<String> requiredSections;
    private List<BlockType> supportedBlockTypes;
    private List<BlockType> recommendedBlockTypes;
    private List<MotionPreset> allowedMotionPresets;
    private MotionPreset defaultMotionPreset;
    private String navStyle;
    private boolean active;
    private boolean featured;
    private boolean isNew;
    private Integer popularityScore;
    private Integer premiumRank;
    private boolean globallySelectable;
    private boolean systemTemplate;
    private boolean editableByAdmin;
    private boolean supportsPremiumCustomization;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTemplateKey() { return templateKey; }
    public void setTemplateKey(String templateKey) { this.templateKey = templateKey; }
    public String getRenderFamily() { return renderFamily; }
    public void setRenderFamily(String renderFamily) { this.renderFamily = renderFamily; }
    public String getVariantKey() { return variantKey; }
    public void setVariantKey(String variantKey) { this.variantKey = variantKey; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
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
    public Object getLayout() { return layout; }
    public void setLayout(Object layout) { this.layout = layout; }
    public Object getTheme() { return theme; }
    public void setTheme(Object theme) { this.theme = theme; }
    public List<LayoutAudience> getTargetAudiences() { return targetAudiences; }
    public void setTargetAudiences(List<LayoutAudience> targetAudiences) { this.targetAudiences = targetAudiences; }
    public List<String> getProfessionTags() { return professionTags; }
    public void setProfessionTags(List<String> professionTags) { this.professionTags = professionTags; }
    public List<ProfessionCategory> getProfessionCategories() { return professionCategories; }
    public void setProfessionCategories(List<ProfessionCategory> professionCategories) { this.professionCategories = professionCategories; }
    public List<ProfessionType> getProfessionTypes() { return professionTypes; }
    public void setProfessionTypes(List<ProfessionType> professionTypes) { this.professionTypes = professionTypes; }
    public TemplateDefaultTheme getDefaultTheme() { return defaultTheme; }
    public void setDefaultTheme(TemplateDefaultTheme defaultTheme) { this.defaultTheme = defaultTheme; }
    public VisualMood getPrimaryMood() { return primaryMood; }
    public void setPrimaryMood(VisualMood primaryMood) { this.primaryMood = primaryMood; }
    public List<ContentMode> getSupportedContentModes() { return supportedContentModes; }
    public void setSupportedContentModes(List<ContentMode> supportedContentModes) { this.supportedContentModes = supportedContentModes; }
    public List<String> getSupportedSections() { return supportedSections; }
    public void setSupportedSections(List<String> supportedSections) { this.supportedSections = supportedSections; }
    public List<String> getEnabledSections() { return enabledSections; }
    public void setEnabledSections(List<String> enabledSections) { this.enabledSections = enabledSections; }
    public List<String> getSectionOrder() { return sectionOrder; }
    public void setSectionOrder(List<String> sectionOrder) { this.sectionOrder = sectionOrder; }
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
    public String getNavStyle() { return navStyle; }
    public void setNavStyle(String navStyle) { this.navStyle = navStyle; }
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
    public boolean isSystemTemplate() { return systemTemplate; }
    public void setSystemTemplate(boolean systemTemplate) { this.systemTemplate = systemTemplate; }
    public boolean isEditableByAdmin() { return editableByAdmin; }
    public void setEditableByAdmin(boolean editableByAdmin) { this.editableByAdmin = editableByAdmin; }
    public boolean isSupportsPremiumCustomization() { return supportsPremiumCustomization; }
    public void setSupportsPremiumCustomization(boolean supportsPremiumCustomization) { this.supportsPremiumCustomization = supportsPremiumCustomization; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
