package com.resume.dashboard.dto.skill;

import com.resume.dashboard.entity.SkillCategory;
import com.resume.dashboard.entity.SkillProficiency;

public class UpdateSkillRequest {

    private String skillName;
    private SkillCategory category;
    private SkillProficiency proficiency;
    private Integer yearsOfExperience;
    private Boolean featured;
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
	public Integer getYearsOfExperience() {
		return yearsOfExperience;
	}
	public void setYearsOfExperience(Integer yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}
	public Boolean getFeatured() {
		return featured;
	}
	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

    // getters & setters
}