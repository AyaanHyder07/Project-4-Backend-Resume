package com.resume.dashboard.dto.section;

import java.time.Instant;

import com.resume.dashboard.entity.PortfolioSectionType;

public class PortfolioSectionResponse {

    private String id;
    private String resumeId;
    private PortfolioSectionType sectionName;
    private boolean enabled;
    private int displayOrder;
    private String customTitle;
    private Instant createdAt;
    private Instant updatedAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getCustomTitle() {
		return customTitle;
	}
	public void setCustomTitle(String customTitle) {
		this.customTitle = customTitle;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

    // getters & setters
}