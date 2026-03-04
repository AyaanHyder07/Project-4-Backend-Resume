package com.resume.dashboard.dto.publicview;

import java.util.List;

public class PublicServiceOfferingDTO {

    private String serviceTitle;
    private String serviceCategory;
    private String description;

    private String pricingModel;
    private double basePrice;
    private String currency;

    private List<String> deliverables;

    private boolean featured;

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

	public String getPricingModel() {
		return pricingModel;
	}

	public void setPricingModel(String pricingModel) {
		this.pricingModel = pricingModel;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<String> getDeliverables() {
		return deliverables;
	}

	public void setDeliverables(List<String> deliverables) {
		this.deliverables = deliverables;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

    // getters setters
}