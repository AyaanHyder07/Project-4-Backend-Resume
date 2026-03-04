package com.resume.dashboard.dto.testimonial;

import java.time.Instant;
import java.time.LocalDate;

public class TestimonialResponse {

    private String id;
    private String resumeId;
    private String clientName;
    private String clientCompany;
    private int rating;
    private LocalDate dateGiven;
    private boolean verified;
    private String testimonialText;
    private int displayOrder;
    private Instant createdAt;
    private Instant updatedAt;
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
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientCompany() {
		return clientCompany;
	}
	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public LocalDate getDateGiven() {
		return dateGiven;
	}
	public void setDateGiven(LocalDate dateGiven) {
		this.dateGiven = dateGiven;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getTestimonialText() {
		return testimonialText;
	}
	public void setTestimonialText(String testimonialText) {
		this.testimonialText = testimonialText;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
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