package com.resume.dashboard.dto.resume;

import java.time.Instant;

public class ResumeVersionResponse {

    private String id;
    private String resumeId;
    private int version;
    private ResumeResponse.ResumeContentResponse content;
    private Instant createdAt;

    public ResumeVersionResponse() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getResumeId() { return resumeId; }
    public void setResumeId(String resumeId) { this.resumeId = resumeId; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public ResumeResponse.ResumeContentResponse getContent() { return content; }
    public void setContent(ResumeResponse.ResumeContentResponse content) { this.content = content; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
