package com.resume.dashboard.dto.publicview;

import java.util.Map;

/**
 * Top-level response returned by GET /api/public/portfolios/{slug}
 * This is the single object the frontend renderer consumes to draw
 * the entire public portfolio page.
 */
public class PublicPortfolioResponse {

    private String resumeId;
    private String title;
    private String professionType;
    private String slug;
    private long viewCount;

    private TemplateMetaDTO templateMeta;  // plan level, template name/version
    private LayoutDTO layout;              // layout type + structureConfig for renderer
    private ResolvedThemeDTO theme;        // fully merged theme (base + PRO/PREMIUM overrides)

    // Ordered map: sectionName → section data object
    // e.g. "HERO" → HeroSectionData, "EXPERIENCE" → List<ExperienceItem>
    private Map<String, Object> sections;

    public String getResumeId() { return resumeId; }
    public void setResumeId(String resumeId) { this.resumeId = resumeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getProfessionType() { return professionType; }
    public void setProfessionType(String professionType) { this.professionType = professionType; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public long getViewCount() { return viewCount; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }

    public TemplateMetaDTO getTemplateMeta() { return templateMeta; }
    public void setTemplateMeta(TemplateMetaDTO templateMeta) { this.templateMeta = templateMeta; }

    public LayoutDTO getLayout() { return layout; }
    public void setLayout(LayoutDTO layout) { this.layout = layout; }

    public ResolvedThemeDTO getTheme() { return theme; }
    public void setTheme(ResolvedThemeDTO theme) { this.theme = theme; }

    public Map<String, Object> getSections() { return sections; }
    public void setSections(Map<String, Object> sections) { this.sections = sections; }
}