package com.resume.dashboard.dto.dashboard;

import java.time.Instant;
import java.util.List;

public class DashboardResponse {

    private String currentPlan;

    private int resumeLimit;
    private int totalResumes;
    private int remainingResumes;

    private int publicLinkLimit;
    private long publishedCount;
    private int remainingPublicSlots;

    private long totalViews;
    private int draftCount;
    private int pendingCount;
    private int approvedCount;

    private Instant subscriptionEndDate;
    public int getDraftCount() {
		return draftCount;
	}

	public void setDraftCount(int draftCount) {
		this.draftCount = draftCount;
	}

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	public int getApprovedCount() {
		return approvedCount;
	}

	public void setApprovedCount(int approvedCount) {
		this.approvedCount = approvedCount;
	}

	public Instant getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(Instant subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public long getDaysRemaining() {
		return daysRemaining;
	}

	public void setDaysRemaining(long daysRemaining) {
		this.daysRemaining = daysRemaining;
	}

	public int getProfileCompletionPercentage() {
		return profileCompletionPercentage;
	}

	public void setProfileCompletionPercentage(int profileCompletionPercentage) {
		this.profileCompletionPercentage = profileCompletionPercentage;
	}

	private long daysRemaining;

    private int profileCompletionPercentage;
    private List<ResumeSummaryDTO> resumes;

	public String getCurrentPlan() {
		return currentPlan;
	}

	public void setCurrentPlan(String currentPlan) {
		this.currentPlan = currentPlan;
	}

	public int getResumeLimit() {
		return resumeLimit;
	}

	public void setResumeLimit(int resumeLimit) {
		this.resumeLimit = resumeLimit;
	}

	public int getTotalResumes() {
		return totalResumes;
	}

	public void setTotalResumes(int totalResumes) {
		this.totalResumes = totalResumes;
	}

	public int getRemainingResumes() {
		return remainingResumes;
	}

	public void setRemainingResumes(int remainingResumes) {
		this.remainingResumes = remainingResumes;
	}

	public int getPublicLinkLimit() {
		return publicLinkLimit;
	}

	public void setPublicLinkLimit(int publicLinkLimit) {
		this.publicLinkLimit = publicLinkLimit;
	}

	public long getPublishedCount() {
		return publishedCount;
	}

	public void setPublishedCount(long publishedCount) {
		this.publishedCount = publishedCount;
	}

	public int getRemainingPublicSlots() {
		return remainingPublicSlots;
	}

	public void setRemainingPublicSlots(int remainingPublicSlots) {
		this.remainingPublicSlots = remainingPublicSlots;
	}

	public long getTotalViews() {
		return totalViews;
	}

	public void setTotalViews(long totalViews) {
		this.totalViews = totalViews;
	}

	public List<ResumeSummaryDTO> getResumes() {
		return resumes;
	}

	public void setResumes(List<ResumeSummaryDTO> resumes) {
		this.resumes = resumes;
	}

    // getters setters
}