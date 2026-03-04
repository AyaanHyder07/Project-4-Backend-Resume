package com.resume.dashboard.dto.layout;

import com.resume.dashboard.entity.LayoutType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateLayoutRequest {

    @NotBlank
    private String name;

    @NotNull
    private LayoutType layoutType;

    @NotBlank
    private String layoutConfigJson;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LayoutType getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(LayoutType layoutType) {
		this.layoutType = layoutType;
	}

	public String getLayoutConfigJson() {
		return layoutConfigJson;
	}

	public void setLayoutConfigJson(String layoutConfigJson) {
		this.layoutConfigJson = layoutConfigJson;
	}

    // getters & setters
}