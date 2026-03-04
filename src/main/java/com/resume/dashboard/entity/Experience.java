package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "experiences")
public class Experience {

    @Id
    private String id;

    @Indexed
    private String resumeId;

    private String organizationName;

    private EmploymentType employmentType; // Full-time / Part-time / Contract / Freelance / Internship

    private String roleTitle;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentlyWorking;

    private String roleDescription;

    private List<String> keyAchievements;

    private List<String> skillsUsed;

    private int displayOrder;

    private Instant createdAt;

    private Instant updatedAt;

    public Experience() {
    }

    public Experience(String resumeId, String organizationName, String roleTitle) {
        this.resumeId = resumeId;
        this.organizationName = organizationName;
        this.roleTitle = roleTitle;
        this.currentlyWorking = false;
        this.displayOrder = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Getters and Setters
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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }



    public EmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public boolean isCurrentlyWorking() {
        return currentlyWorking;
    }

    public void setCurrentlyWorking(boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }


    public List<String> getKeyAchievements() {
		return keyAchievements;
	}

	public void setKeyAchievements(List<String> keyAchievements) {
		this.keyAchievements = keyAchievements;
	}

	public List<String> getSkillsUsed() {
        return skillsUsed;
    }

    public void setSkillsUsed(List<String> skillsUsed) {
        this.skillsUsed = skillsUsed;
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
