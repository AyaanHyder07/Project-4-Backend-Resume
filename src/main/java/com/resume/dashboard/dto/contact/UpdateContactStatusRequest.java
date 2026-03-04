package com.resume.dashboard.dto.contact;

import jakarta.validation.constraints.NotNull;

public class UpdateContactStatusRequest {

    @NotNull
    private String status; // convert to enum later

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
    // getters & setters
}