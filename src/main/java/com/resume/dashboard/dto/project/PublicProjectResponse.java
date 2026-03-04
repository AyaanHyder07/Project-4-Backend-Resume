package com.resume.dashboard.dto.project;

import java.time.LocalDate;
import java.util.List;

import com.resume.dashboard.entity.ProjectStatus;
import com.resume.dashboard.entity.ProjectType;

public class PublicProjectResponse {

    private String title;
    private String slug;
    private ProjectType projectType;

    private String description;
    private List<String> keyFeatures;
    private String roleInProject;
    private String industry;

    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus projectStatus;

    private List<String> technologiesUsed;

    private String liveUrl;
    private String caseStudyUrl;

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

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getKeyFeatures() {
		return keyFeatures;
	}

	public void setKeyFeatures(List<String> keyFeatures) {
		this.keyFeatures = keyFeatures;
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

	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
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

	public String getCaseStudyUrl() {
		return caseStudyUrl;
	}

	public void setCaseStudyUrl(String caseStudyUrl) {
		this.caseStudyUrl = caseStudyUrl;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

    // getters and setters
}