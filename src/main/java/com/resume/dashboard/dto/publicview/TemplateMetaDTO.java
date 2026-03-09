package com.resume.dashboard.dto.publicview;

/**
 * Template metadata included in the public portfolio response.
 * Frontend uses planLevel to decide if upgrade prompts should show.
 * primaryMood helps the renderer pick animation/effect defaults.
 */
public class TemplateMetaDTO {

    private String templateId;
    private String templateName;
    private int templateVersion;     // snapshot version stored on resume at creation
    private String planLevel;        // FREE / BASIC / PRO / PREMIUM
    private String primaryMood;      // e.g. "DARK", "MINIMAL", "VIBRANT"

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public int getTemplateVersion() { return templateVersion; }
    public void setTemplateVersion(int templateVersion) { this.templateVersion = templateVersion; }

    public String getPlanLevel() { return planLevel; }
    public void setPlanLevel(String planLevel) { this.planLevel = planLevel; }

    public String getPrimaryMood() { return primaryMood; }
    public void setPrimaryMood(String primaryMood) { this.primaryMood = primaryMood; }
}