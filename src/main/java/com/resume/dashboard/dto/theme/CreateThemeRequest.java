package com.resume.dashboard.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class CreateThemeRequest {

    @NotBlank
    private String name;

    @NotNull
    private Map<String, Object> themeConfig;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getThemeConfig() {
		return themeConfig;
	}

	public void setThemeConfig(Map<String, Object> themeConfig) {
		this.themeConfig = themeConfig;
	}

    // getters and setters
}