package com.resume.dashboard.dto.resume;

import java.util.Map;

import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.ProfessionCategory;

public class PublicResumeResponse {
    private String title;
    private String professionType;
    private ProfessionCategory professionCategory;
    private String slug;
    private String templateId;
    private String layoutId;
    private String themeId;
    private MotionPreset motionPreset;
    private long viewCount;
    private Map<String, Object> content;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getProfessionType() { return professionType; }
    public void setProfessionType(String professionType) { this.professionType = professionType; }
    public ProfessionCategory getProfessionCategory() { return professionCategory; }
    public void setProfessionCategory(ProfessionCategory professionCategory) { this.professionCategory = professionCategory; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getLayoutId() { return layoutId; }
    public void setLayoutId(String layoutId) { this.layoutId = layoutId; }
    public String getThemeId() { return themeId; }
    public void setThemeId(String themeId) { this.themeId = themeId; }
    public MotionPreset getMotionPreset() { return motionPreset; }
    public void setMotionPreset(MotionPreset motionPreset) { this.motionPreset = motionPreset; }
    public long getViewCount() { return viewCount; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
    public Map<String, Object> getContent() { return content; }
    public void setContent(Map<String, Object> content) { this.content = content; }
}
