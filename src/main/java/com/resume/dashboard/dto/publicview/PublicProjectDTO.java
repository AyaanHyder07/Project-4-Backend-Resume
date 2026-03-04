package com.resume.dashboard.dto.publicview;

import java.time.LocalDate;
import java.util.List;

public class PublicProjectDTO {

    private String title;
    private String slug;
    private String description;

    private String roleInProject;
    private String industry;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<String> technologiesUsed;

    private String liveUrl;

    private boolean featured;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleInProject() {
		return roleInProject;
	}

	public void setRoleInProject(String roleInProject) {
		this.roleInProject = roleInProject;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public List<String> getTechnologiesUsed() {
		return technologiesUsed;
	}

	public void setTechnologiesUsed(List<String> technologiesUsed) {
		this.technologiesUsed = technologiesUsed;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

    // getters setters
}