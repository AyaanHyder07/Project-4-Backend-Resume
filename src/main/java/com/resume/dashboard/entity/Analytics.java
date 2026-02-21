package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "analytics")
public class Analytics {

    @Id
    private String id;
    @Indexed
    private String resumeId;
    private AnalyticsAction action;
    private String viewerIp;
    private Instant timestamp;

    public enum AnalyticsAction {
        VIEW, DOWNLOAD
    }

    public Analytics() {
    }

    public Analytics(String id, String resumeId, AnalyticsAction action, String viewerIp, Instant timestamp) {
        this.id = id;
        this.resumeId = resumeId;
        this.action = action;
        this.viewerIp = viewerIp;
        this.timestamp = timestamp;
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

    public AnalyticsAction getAction() {
        return action;
    }

    public void setAction(AnalyticsAction action) {
        this.action = action;
    }

    public String getViewerIp() {
        return viewerIp;
    }

    public void setViewerIp(String viewerIp) {
        this.viewerIp = viewerIp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
