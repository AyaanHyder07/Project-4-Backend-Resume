package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "resume_versions")
@CompoundIndex(name = "resume_version_idx", def = "{'resumeId': 1, 'version': 1}", unique = true)
public class ResumeVersion {

    @Id
    private String id;
    private String resumeId;
    private int version;
    private ResumeContent content;
    private Instant createdAt;

    public ResumeVersion() {
    }

    public ResumeVersion(String id, String resumeId, int version, ResumeContent content, Instant createdAt) {
        this.id = id;
        this.resumeId = resumeId;
        this.version = version;
        this.content = content;
        this.createdAt = createdAt;
    }

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ResumeContent getContent() {
        return content;
    }

    public void setContent(ResumeContent content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static class ResumeContent {
        private PersonalInfo personalInfo;
        private String summary;
        private List<Experience> experience;
        private List<Education> education;
        private List<String> skills;
        private List<Project> projects;
        private Links links;

        public ResumeContent() {
        }

        public PersonalInfo getPersonalInfo() { return personalInfo; }
        public void setPersonalInfo(PersonalInfo personalInfo) { this.personalInfo = personalInfo; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public List<Experience> getExperience() { return experience; }
        public void setExperience(List<Experience> experience) { this.experience = experience; }
        public List<Education> getEducation() { return education; }
        public void setEducation(List<Education> education) { this.education = education; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
        public List<Project> getProjects() { return projects; }
        public void setProjects(List<Project> projects) { this.projects = projects; }
        public Links getLinks() { return links; }
        public void setLinks(Links links) { this.links = links; }
    }

    public static class PersonalInfo {
        private String fullName;
        private String title;
        private String email;
        private String phone;
        private String location;

        public PersonalInfo() { }
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

    public static class Experience {
        private String company;
        private String role;
        private String duration;
        private String description;

        public Experience() { }
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class Education {
        private String institution;
        private String degree;
        private String year;

        public Education() { }
        public String getInstitution() { return institution; }
        public void setInstitution(String institution) { this.institution = institution; }
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
    }

    public static class Project {
        private String name;
        private String description;
        private String link;

        public Project() { }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getLink() { return link; }
        public void setLink(String link) { this.link = link; }
    }

    public static class Links {
        private String github;
        private String linkedin;
        private String portfolio;

        public Links() { }
        public String getGithub() { return github; }
        public void setGithub(String github) { this.github = github; }
        public String getLinkedin() { return linkedin; }
        public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
        public String getPortfolio() { return portfolio; }
        public void setPortfolio(String portfolio) { this.portfolio = portfolio; }
    }
}
