package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "resumes")
public class Resume {

    @Id
    private String id;
    @Indexed
    private String userId;
    private String title;
    @Indexed
    private ResumeState state;
    private int activeVersion;
    private Instant createdAt;
    private Instant updatedAt;

    public enum ResumeState {
        DRAFT, SUBMITTED, APPROVED, PUBLISHED, DISABLED
    }

    public Resume() {
    }

    public Resume(String id, String userId, String title, ResumeState state, int activeVersion,
                  Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.state = state;
        this.activeVersion = activeVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ResumeState getState() {
        return state;
    }

    public void setState(ResumeState state) {
        this.state = state;
    }

    public int getActiveVersion() {
        return activeVersion;
    }

    public void setActiveVersion(int activeVersion) {
        this.activeVersion = activeVersion;
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
