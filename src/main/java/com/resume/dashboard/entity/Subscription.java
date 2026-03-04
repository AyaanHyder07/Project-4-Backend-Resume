package com.resume.dashboard.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;

    @Indexed
    private String userId;

    private PlanType plan; // FREE, BASIC, PRO, PREMIUM

    private Instant startDate;
    private Instant endDate;

    private boolean active;

    // LIMITS
    private int resumeLimit;
    private int publicLinkLimit;
    private boolean versioningEnabled;

    private Instant createdAt;
    private Instant updatedAt;



    // Getters & Setters


	public Instant getUpdatedAt() {
		return updatedAt;
	}



	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}



	public String getId() {
		return id;
	}



	public void setPlan(PlanType plan) {
		this.plan = plan;
	}



	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

	public boolean isVersioningEnabled() {
	    return versioningEnabled;
	}

	public void setVersioningEnabled(boolean versioningEnabled) {
	    this.versioningEnabled = versioningEnabled;
	}

	public int getResumeLimit() {
		return resumeLimit;
	}

	public void setResumeLimit(int resumeLimit) {
		this.resumeLimit = resumeLimit;
	}

	public int getPublicLinkLimit() {
		return publicLinkLimit;
	}

	public void setPublicLinkLimit(int publicLinkLimit) {
		this.publicLinkLimit = publicLinkLimit;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
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


	public PlanType getPlan() {
		return plan;
	}



	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}



	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
    
    
}