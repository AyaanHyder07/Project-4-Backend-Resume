package com.resume.dashboard.dto.admin;

import org.springframework.data.domain.Page;

public class AnalyticsResponse {

    private Page<AnalyticsItemDto> items;

    public AnalyticsResponse() {
    }

    public AnalyticsResponse(Page<AnalyticsItemDto> items) {
        this.items = items;
    }

    public Page<AnalyticsItemDto> getItems() {
        return items;
    }

    public void setItems(Page<AnalyticsItemDto> items) {
        this.items = items;
    }

    public static class AnalyticsItemDto {
        private String id;
        private String resumeId;
        private String action;
        private String viewerIp;
        private java.time.Instant timestamp;

        public AnalyticsItemDto() { }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getResumeId() { return resumeId; }
        public void setResumeId(String resumeId) { this.resumeId = resumeId; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getViewerIp() { return viewerIp; }
        public void setViewerIp(String viewerIp) { this.viewerIp = viewerIp; }
        public java.time.Instant getTimestamp() { return timestamp; }
        public void setTimestamp(java.time.Instant timestamp) { this.timestamp = timestamp; }
    }
}
