package com.resume.dashboard.dto.admin;

import java.util.List;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalResumes;
    private long submittedCount;
    private long approvedCount;
    private long publishedCount;
    private List<TopResumeDto> topResumes;

    public AdminDashboardResponse() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalResumes() {
        return totalResumes;
    }

    public void setTotalResumes(long totalResumes) {
        this.totalResumes = totalResumes;
    }

    public long getSubmittedCount() {
        return submittedCount;
    }

    public void setSubmittedCount(long submittedCount) {
        this.submittedCount = submittedCount;
    }

    public long getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public long getPublishedCount() {
        return publishedCount;
    }

    public void setPublishedCount(long publishedCount) {
        this.publishedCount = publishedCount;
    }

    public List<TopResumeDto> getTopResumes() {
        return topResumes;
    }

    public void setTopResumes(List<TopResumeDto> topResumes) {
        this.topResumes = topResumes;
    }
}
