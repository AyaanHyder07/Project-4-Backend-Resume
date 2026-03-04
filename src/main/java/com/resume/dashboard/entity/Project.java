package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "projects")
@CompoundIndex(def = "{'resumeId': 1, 'displayOrder': 1}")
public class Project {

    @Id
    private String id;

    @Indexed
    private String resumeId;

    private String title;

    @Indexed
    private ProjectType projectType;

    private boolean ndaRestricted;

    private String slug;

    private String description;
    
    private List<String> keyFeatures;

    private String roleInProject;

    private String clientName;

    private String industry;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus projectStatus;

    private List<String> technologiesUsed;

    private List<String> toolsUsed;

    private String liveUrl;

    private String sourceCodeUrl;

    private String caseStudyUrl;

    private boolean featured;

    private VisibilityType visibility;

    private int displayOrder;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public boolean isNdaRestricted() {
		return ndaRestricted;
	}

	public void setNdaRestricted(boolean ndaRestricted) {
		this.ndaRestricted = ndaRestricted;
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

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public List<String> getToolsUsed() {
		return toolsUsed;
	}

	public void setToolsUsed(List<String> toolsUsed) {
		this.toolsUsed = toolsUsed;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}

	public String getSourceCodeUrl() {
		return sourceCodeUrl;
	}

	public void setSourceCodeUrl(String sourceCodeUrl) {
		this.sourceCodeUrl = sourceCodeUrl;
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

	public VisibilityType getVisibility() {
		return visibility;
	}

	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
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
}
