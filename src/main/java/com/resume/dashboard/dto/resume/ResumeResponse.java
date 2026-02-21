package com.resume.dashboard.dto.resume;

import java.time.Instant;

public class ResumeResponse {

    private String id;
    private String userId;
    private String title;
    private String state;
    private int activeVersion;
    private Instant createdAt;
    private Instant updatedAt;
    private ResumeContentResponse content;

    public ResumeResponse() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public int getActiveVersion() { return activeVersion; }
    public void setActiveVersion(int activeVersion) { this.activeVersion = activeVersion; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public ResumeContentResponse getContent() { return content; }
    public void setContent(ResumeContentResponse content) { this.content = content; }

    public static class ResumeContentResponse {
        private ResumeRequest.PersonalInfoDto personalInfo;
        private String summary;
        private java.util.List<ResumeRequest.ExperienceDto> experience;
        private java.util.List<ResumeRequest.EducationDto> education;
        private java.util.List<String> skills;
        private java.util.List<ResumeRequest.ProjectDto> projects;
        private ResumeRequest.LinksDto links;

        public ResumeContentResponse() { }
        public ResumeRequest.PersonalInfoDto getPersonalInfo() { return personalInfo; }
        public void setPersonalInfo(ResumeRequest.PersonalInfoDto personalInfo) { this.personalInfo = personalInfo; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public java.util.List<ResumeRequest.ExperienceDto> getExperience() { return experience; }
        public void setExperience(java.util.List<ResumeRequest.ExperienceDto> experience) { this.experience = experience; }
        public java.util.List<ResumeRequest.EducationDto> getEducation() { return education; }
        public void setEducation(java.util.List<ResumeRequest.EducationDto> education) { this.education = education; }
        public java.util.List<String> getSkills() { return skills; }
        public void setSkills(java.util.List<String> skills) { this.skills = skills; }
        public java.util.List<ResumeRequest.ProjectDto> getProjects() { return projects; }
        public void setProjects(java.util.List<ResumeRequest.ProjectDto> projects) { this.projects = projects; }
        public ResumeRequest.LinksDto getLinks() { return links; }
        public void setLinks(ResumeRequest.LinksDto links) { this.links = links; }
    }
}
