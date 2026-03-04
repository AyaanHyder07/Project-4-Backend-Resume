package com.resume.dashboard.dto.testimonial;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateTestimonialRequest {

    @NotNull
    private String resumeId;

    @NotBlank
    private String clientName;

    private String clientCompany;

    @Min(1)
    @Max(5)
    private int rating;

    private LocalDate dateGiven;

    @NotBlank
    private String testimonialText;

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

	public String getTestimonialText() {
		return testimonialText;
	}

	public void setTestimonialText(String testimonialText) {
		this.testimonialText = testimonialText;
	}

    // getters & setters
}