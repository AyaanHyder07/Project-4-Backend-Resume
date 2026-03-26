package com.resume.dashboard.dto.publicview;

import java.util.List;
import java.util.Map;

import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.dto.userprofile.PublicUserProfileResponse;

public class PublicPortfolioResponse {
    private String resumeId;
    private String title;
    private String professionType;
    private ProfessionCategory professionCategory;
    private String slug;
    private long viewCount;

    private String templateKey;
    private String renderFamily;
    private PublicThemeDataDTO themeData;
    private List<String> sectionOrder;
    private boolean openToWork;

    private TemplateMetaDTO templateMeta;
    private LayoutDTO layout;
    private ResolvedThemeDTO theme;
    private Map<String, Object> sections;
    private Map<String, String> sectionTitles;
    private List<PublicPortfolioBlockDTO> customBlocks;
    private PublicUserProfileResponse profile;

    public String getResumeId() { return resumeId; }
    public void setResumeId(String resumeId) { this.resumeId = resumeId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getProfessionType() { return professionType; }
    public void setProfessionType(String professionType) { this.professionType = professionType; }
    public ProfessionCategory getProfessionCategory() { return professionCategory; }
    public void setProfessionCategory(ProfessionCategory professionCategory) { this.professionCategory = professionCategory; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public long getViewCount() { return viewCount; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
    public String getTemplateKey() { return templateKey; }
    public void setTemplateKey(String templateKey) { this.templateKey = templateKey; }
    public String getRenderFamily() { return renderFamily; }
    public void setRenderFamily(String renderFamily) { this.renderFamily = renderFamily; }
    public PublicThemeDataDTO getThemeData() { return themeData; }
    public void setThemeData(PublicThemeDataDTO themeData) { this.themeData = themeData; }
    public List<String> getSectionOrder() { return sectionOrder; }
    public void setSectionOrder(List<String> sectionOrder) { this.sectionOrder = sectionOrder; }
    public boolean isOpenToWork() { return openToWork; }
    public void setOpenToWork(boolean openToWork) { this.openToWork = openToWork; }
    public TemplateMetaDTO getTemplateMeta() { return templateMeta; }
    public void setTemplateMeta(TemplateMetaDTO templateMeta) { this.templateMeta = templateMeta; }
    public LayoutDTO getLayout() { return layout; }
    public void setLayout(LayoutDTO layout) { this.layout = layout; }
    public ResolvedThemeDTO getTheme() { return theme; }
    public void setTheme(ResolvedThemeDTO theme) { this.theme = theme; }
    public Map<String, Object> getSections() { return sections; }
    public void setSections(Map<String, Object> sections) { this.sections = sections; }
    public Map<String, String> getSectionTitles() { return sectionTitles; }
    public void setSectionTitles(Map<String, String> sectionTitles) { this.sectionTitles = sectionTitles; }
    public List<PublicPortfolioBlockDTO> getCustomBlocks() { return customBlocks; }
    public void setCustomBlocks(List<PublicPortfolioBlockDTO> customBlocks) { this.customBlocks = customBlocks; }
    public PublicUserProfileResponse getProfile() { return profile; }
    public void setProfile(PublicUserProfileResponse profile) { this.profile = profile; }
}
