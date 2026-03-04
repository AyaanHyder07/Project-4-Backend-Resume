package com.resume.dashboard.dto.certification;

import jakarta.validation.constraints.NotBlank;

public class CreateCertificationRequest {

    @NotBlank
    private String resumeId;

    @NotBlank
    private String title;

    private String certificateUrl;

	public String getResumeId() {
		return resumeId;
	}

	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}

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