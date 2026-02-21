package com.resume.dashboard.dto.auth;

import jakarta.validation.constraints.Email;

public class UpdateProfileRequest {

    @Email(message = "Invalid email format")
    private String email;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
