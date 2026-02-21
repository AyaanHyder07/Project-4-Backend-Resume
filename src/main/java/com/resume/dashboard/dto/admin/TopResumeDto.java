package com.resume.dashboard.dto.admin;

public class TopResumeDto {

    private String resumeId;
    private String title;
    private long viewCount;

    public TopResumeDto() {
    }

    public TopResumeDto(String resumeId, String title, long viewCount) {
        this.resumeId = resumeId;
        this.title = title;
        this.viewCount = viewCount;
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
