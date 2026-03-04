package com.resume.dashboard.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

	private String identifier; 

    @NotBlank(message = "Password is required")
    private String password;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
