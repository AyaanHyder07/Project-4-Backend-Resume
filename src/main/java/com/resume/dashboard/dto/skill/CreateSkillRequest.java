package com.resume.dashboard.dto.skill;

import com.resume.dashboard.entity.SkillCategory;
import com.resume.dashboard.entity.SkillProficiency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSkillRequest {

    @NotBlank
    private String resumeId;

    @NotBlank
    private String skillName;

    @NotNull
    private SkillCategory category;

    private SkillProficiency proficiency;

    private Integer yearsOfExperience;

    private boolean featured;

	public String getResumeId() {
		return resumeId;
	}

	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public SkillCategory getCategory() {
		return category;
	}

	public void setCategory(SkillCategory category) {
		this.category = category;
	}

	public SkillProficiency getProficiency() {
		return proficiency;
	}

	public void setProficiency(SkillProficiency proficiency) {
		this.proficiency = proficiency;
	}

	public int getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(int yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

    // getters & setters
}