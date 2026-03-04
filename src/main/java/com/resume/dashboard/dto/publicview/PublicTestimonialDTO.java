package com.resume.dashboard.dto.publicview;

import java.time.LocalDate;

public class PublicTestimonialDTO {

    private String clientName;
    private String clientCompany;
    private int rating;
    private LocalDate dateGiven;
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
	public String getTestimonialText() {
		return testimonialText;
	}
	public void setTestimonialText(String testimonialText) {
		this.testimonialText = testimonialText;
	}

    // getters setters
}