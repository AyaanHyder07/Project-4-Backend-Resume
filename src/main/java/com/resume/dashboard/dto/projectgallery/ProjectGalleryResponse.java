package com.resume.dashboard.dto.projectgallery;

import java.time.Instant;

import com.resume.dashboard.entity.ProjectMediaType;

public class ProjectGalleryResponse {

    private String id;
    private String projectId;
    private ProjectMediaType mediaType;
    private String mediaUrl;
    private String thumbnailUrl;
    private String caption;
    private String resolutionInfo;
    private int displayOrder;
    private Instant createdAt;
    private Instant updatedAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public ProjectMediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(ProjectMediaType mediaType) {
		this.mediaType = mediaType;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getResolutionInfo() {
		return resolutionInfo;
	}
	public void setResolutionInfo(String resolutionInfo) {
		this.resolutionInfo = resolutionInfo;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
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