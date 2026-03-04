package com.resume.dashboard.dto.analytics;

public class AnalyticsSummaryResponse {

    private long totalViews;
    private long projectClicks;
    private long contactClicks;
    private long downloads;
    private long ctaClicks;

    public long getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(long totalViews) {
        this.totalViews = totalViews;
    }

    public long getProjectClicks() {
        return projectClicks;
    }

    public void setProjectClicks(long projectClicks) {
        this.projectClicks = projectClicks;
    }

    public long getContactClicks() {
        return contactClicks;
    }

    public void setContactClicks(long contactClicks) {
        this.contactClicks = contactClicks;
    }

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public long getCtaClicks() {
        return ctaClicks;
    }

    public void setCtaClicks(long ctaClicks) {
        this.ctaClicks = ctaClicks;
    }
}