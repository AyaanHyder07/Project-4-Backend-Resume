package com.resume.dashboard.dto.projectgallery;

import com.resume.dashboard.entity.ProjectMediaType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateProjectGalleryRequest {

    @NotBlank
    private String projectId;

    @NotNull
    private ProjectMediaType mediaType;

    @NotBlank
    private String mediaUrl;

    private String thumbnailUrl;

    private String caption;

    private String resolutionInfo;

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

    // getters & setters
}