package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "resumes")
public class Resume {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String title;
    private String professionType;
    private ProfessionCategory professionCategory;

    @Indexed(unique = true)
    private String slug;

    private String templateId;
    private String templateKey;
    private int templateVersion;
    private String layoutId;
    private int layoutVersion;
    private String themeId;
    private int themeVersion;
    private MotionPreset motionPreset;
    private ResolvedTheme resolvedTheme;
    private String rejectionReason;

    private boolean published;
    private VisibilityType visibility;
    private int version;
    private long viewCount;
    private Map<String, Object> content;
    private ApprovalStatus approvalStatus;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
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
    public String getTemplateKey() { return templateKey; }
    public void setTemplateKey(String templateKey) { this.templateKey = templateKey; }
    public int getTemplateVersion() { return templateVersion; }
    public void setTemplateVersion(int templateVersion) { this.templateVersion = templateVersion; }
    public String getLayoutId() { return layoutId; }
    public void setLayoutId(String layoutId) { this.layoutId = layoutId; }
    public int getLayoutVersion() { return layoutVersion; }
    public void setLayoutVersion(int layoutVersion) { this.layoutVersion = layoutVersion; }
    public String getThemeId() { return themeId; }
    public void setThemeId(String themeId) { this.themeId = themeId; }
    public int getThemeVersion() { return themeVersion; }
    public void setThemeVersion(int themeVersion) { this.themeVersion = themeVersion; }
    public MotionPreset getMotionPreset() { return motionPreset; }
    public void setMotionPreset(MotionPreset motionPreset) { this.motionPreset = motionPreset; }
    public ResolvedTheme getResolvedTheme() { return resolvedTheme; }
    public void setResolvedTheme(ResolvedTheme resolvedTheme) { this.resolvedTheme = resolvedTheme; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
    public VisibilityType getVisibility() { return visibility; }
    public void setVisibility(VisibilityType visibility) { this.visibility = visibility; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public long getViewCount() { return viewCount; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
    public Map<String, Object> getContent() { return content; }
    public void setContent(Map<String, Object> content) { this.content = content; }
    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
