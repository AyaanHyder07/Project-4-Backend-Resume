package com.resume.dashboard.dto.layout;

import com.resume.dashboard.entity.LayoutType;

import java.time.Instant;

public class LayoutResponse {

    private String id;
    private String name;
    private LayoutType layoutType;
    private String layoutConfigJson;
    private boolean active;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

    // getters & setters
}