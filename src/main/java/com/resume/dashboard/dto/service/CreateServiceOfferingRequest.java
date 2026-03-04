package com.resume.dashboard.dto.service;

import com.resume.dashboard.entity.PricingModel;
import com.resume.dashboard.entity.CurrencyType;
import com.resume.dashboard.entity.VisibilityType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateServiceOfferingRequest {

    @NotBlank
    private String resumeId;

    @NotBlank
    private String serviceTitle;

    private String serviceCategory;

    private String description;

    @NotNull
    private PricingModel pricingModel;

    private double basePrice;

    private CurrencyType currency;

    private String duration;

    private List<String> deliverables;

    private String targetAudience;

    private boolean featured;

    private VisibilityType visibility;

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

    // getters & setters
}