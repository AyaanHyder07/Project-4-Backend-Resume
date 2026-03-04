package com.resume.dashboard.dto.publicview;

import java.util.Map;

public class ThemeDTO {

    private String name;
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
}