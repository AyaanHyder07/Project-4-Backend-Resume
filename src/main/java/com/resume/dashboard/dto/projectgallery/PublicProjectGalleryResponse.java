package com.resume.dashboard.dto.projectgallery;

import com.resume.dashboard.entity.ProjectMediaType;

public class PublicProjectGalleryResponse {

    private ProjectMediaType mediaType;
    private String mediaUrl;
    private String thumbnailUrl;
    private String caption;
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

    // getters & setters
    
    
}