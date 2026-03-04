package com.resume.dashboard.dto.resume;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

import com.resume.dashboard.entity.VisibilityType;

public class CreateResumeRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String professionType;

    @NotBlank
    private String templateId;

    private String themeOverrideId; // optional

    private VisibilityType visibility; // optional

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProfessionType() {
		return professionType;
	}

	public void setProfessionType(String professionType) {
		this.professionType = professionType;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getThemeOverrideId() {
		return themeOverrideId;
	}

	public void setThemeOverrideId(String themeOverrideId) {
		this.themeOverrideId = themeOverrideId;
	}

	public VisibilityType getVisibility() {
		return visibility;
	}

	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	private Map<String, Object> content;

    // getters setters
}