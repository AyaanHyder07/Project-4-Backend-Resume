package com.resume.dashboard.dto.publicview;

import java.time.LocalDate;
import java.util.List;

public class PublicExperienceDTO {

    private String organizationName;
    private String roleTitle;
    private String location;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean currentlyWorking;

    private String roleDescription;
    private List<String> skillsUsed;
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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
	public List<String> getSkillsUsed() {
		return skillsUsed;
	}
	public void setSkillsUsed(List<String> skillsUsed) {
		this.skillsUsed = skillsUsed;
	}

    // getters setters
}