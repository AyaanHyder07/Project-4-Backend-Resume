package com.resume.dashboard.dto.testimonial;

import java.time.LocalDate;

public class UpdateTestimonialRequest {

    private String clientName;
    private String clientCompany;
    private Integer rating;
    private LocalDate dateGiven;
    private Boolean verified;
    private String testimonialText;
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
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public LocalDate getDateGiven() {
		return dateGiven;
	}
	public void setDateGiven(LocalDate dateGiven) {
		this.dateGiven = dateGiven;
	}
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	public String getTestimonialText() {
		return testimonialText;
	}
	public void setTestimonialText(String testimonialText) {
		this.testimonialText = testimonialText;
	}
}