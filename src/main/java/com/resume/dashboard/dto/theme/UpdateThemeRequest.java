package com.resume.dashboard.dto.theme;

import java.util.Map;

public class UpdateThemeRequest {

    private String name;
    private Map<String, Object> themeConfig;
    private Boolean active;
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

    // getters and setters
}