package com.resume.dashboard.dto.service;

import com.resume.dashboard.entity.*;

import java.time.Instant;
import java.util.List;

public class ServiceOfferingResponse {

    private String id;
    private String resumeId;
    private String serviceTitle;
    private String serviceCategory;
    private String description;
    private PricingModel pricingModel;
    private double basePrice;
    private CurrencyType currency;
    private String duration;
    private List<String> deliverables;
    private String targetAudience;
    private boolean featured;
    private VisibilityType visibility;
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
	public String getServiceTitle() {
		return serviceTitle;
	}
	public void setServiceTitle(String serviceTitle) {
		this.serviceTitle = serviceTitle;
	}
	public String getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public PricingModel getPricingModel() {
		return pricingModel;
	}
	public void setPricingModel(PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	public CurrencyType getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyType currency) {
		this.currency = currency;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public List<String> getDeliverables() {
		return deliverables;
	}
	public void setDeliverables(List<String> deliverables) {
		this.deliverables = deliverables;
	}
	public String getTargetAudience() {
		return targetAudience;
	}
	public void setTargetAudience(String targetAudience) {
		this.targetAudience = targetAudience;
	}
	public boolean isFeatured() {
		return featured;
	}
	public void setFeatured(boolean featured) {
		this.featured = featured;
	}
	public VisibilityType getVisibility() {
		return visibility;
	}
	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
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

    // getters & setters
}