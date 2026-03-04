package com.resume.dashboard.dto.certification;

import jakarta.validation.constraints.NotBlank;

public class UpdateCertificationRequest {

    @NotBlank
    private String title;

    private String certificateUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCertificateUrl() {
		return certificateUrl;
	}

	public void setCertificateUrl(String certificateUrl) {
		this.certificateUrl = certificateUrl;
	}

    // getters & setters
    
}