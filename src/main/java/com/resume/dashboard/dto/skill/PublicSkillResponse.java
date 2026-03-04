package com.resume.dashboard.dto.skill;

import com.resume.dashboard.entity.SkillCategory;
import com.resume.dashboard.entity.SkillProficiency;

public class PublicSkillResponse {

    private String skillName;
    private SkillCategory category;
    private SkillProficiency proficiency;
    private int yearsOfExperience;
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

    // getters & setters
}