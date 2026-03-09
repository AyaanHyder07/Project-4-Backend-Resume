package com.resume.dashboard.dto.experience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class CreateExperienceRequest {

    @NotBlank
    private String resumeId;

    @NotBlank
    private String organizationName;

    @NotBlank
    private String employmentType;

    @NotBlank
    private String roleTitle;

    private String location;

    @NotNull
    private String startDate;

    private String endDate;

    private Boolean currentlyWorking;

    private String roleDescription;

    private List<String> keyAchievements;

    private List<String> skillsUsed;

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

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getCurrentlyWorking() {
		return currentlyWorking;
	}

	public void setCurrentlyWorking(Boolean currentlyWorking) {
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

    // getters & setters
}