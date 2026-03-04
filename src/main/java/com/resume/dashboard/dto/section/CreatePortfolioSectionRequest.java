package com.resume.dashboard.dto.section;

import com.resume.dashboard.entity.PortfolioSectionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePortfolioSectionRequest {

    @NotBlank
    private String resumeId;

    @NotNull
    private PortfolioSectionType sectionName;

    private boolean enabled = true;

    private String customTitle;

	public String getResumeId() {
		return resumeId;
	}

	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}

	public PortfolioSectionType getSectionName() {
		return sectionName;
	}

	public void setSectionName(PortfolioSectionType sectionName) {
		this.sectionName = sectionName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCustomTitle() {
		return customTitle;
	}

	public void setCustomTitle(String customTitle) {
		this.customTitle = customTitle;
	}

    // getters & setters
}