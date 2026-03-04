package com.resume.dashboard.dto.layout;

import com.resume.dashboard.entity.LayoutType;

public class UpdateLayoutRequest {

    private String name;
    private LayoutType layoutType;
    private String layoutConfigJson;
    private Boolean active;
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

    // getters & setters
}