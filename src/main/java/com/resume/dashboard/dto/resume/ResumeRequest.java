package com.resume.dashboard.dto.resume;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ResumeRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @Valid
    private PersonalInfoDto personalInfo;
    private String summary;
    @Valid
    private List<ExperienceDto> experience;
    @Valid
    private List<EducationDto> education;
    private List<String> skills;
    @Valid
    private List<ProjectDto> projects;
    private LinksDto links;

    public ResumeRequest() {
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public PersonalInfoDto getPersonalInfo() { return personalInfo; }
    public void setPersonalInfo(PersonalInfoDto personalInfo) { this.personalInfo = personalInfo; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<ExperienceDto> getExperience() { return experience; }
    public void setExperience(List<ExperienceDto> experience) { this.experience = experience; }
    public List<EducationDto> getEducation() { return education; }
    public void setEducation(List<EducationDto> education) { this.education = education; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public List<ProjectDto> getProjects() { return projects; }
    public void setProjects(List<ProjectDto> projects) { this.projects = projects; }
    public LinksDto getLinks() { return links; }
    public void setLinks(LinksDto links) { this.links = links; }

    public static class PersonalInfoDto {
        @NotBlank(message = "Full name is required")
        private String fullName;
        private String title;
        @NotBlank(message = "Email is required")
        private String email;
        private String phone;
        private String location;

        public PersonalInfoDto() { }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }

    public static class ExperienceDto {
        @NotBlank(message = "Company is required")
        private String company;
        @NotBlank(message = "Role is required")
        private String role;
        private String duration;
        private String description;

        public ExperienceDto() { }
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class EducationDto {
        @NotBlank(message = "Institution is required")
        private String institution;
        @NotBlank(message = "Degree is required")
        private String degree;
        private String year;

        public EducationDto() { }
        public String getInstitution() { return institution; }
        public void setInstitution(String institution) { this.institution = institution; }
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
    }

    public static class ProjectDto {
        @NotBlank(message = "Project name is required")
        private String name;
        private String description;
        private String link;

        public ProjectDto() { }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getLink() { return link; }
        public void setLink(String link) { this.link = link; }
    }

    public static class LinksDto {
        private String github;
        private String linkedin;
        private String portfolio;

        public LinksDto() { }
        public String getGithub() { return github; }
        public void setGithub(String github) { this.github = github; }
        public String getLinkedin() { return linkedin; }
        public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
        public String getPortfolio() { return portfolio; }
        public void setPortfolio(String portfolio) { this.portfolio = portfolio; }
    }
}
