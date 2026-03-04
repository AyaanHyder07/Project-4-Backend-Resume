package com.resume.dashboard.dto.publicview;

import java.time.Instant;
import java.util.List;

public class PublicBlogPostDTO {

    private String title;
    private String slug;
    private String coverImage;
    private List<String> tags;
    private Instant publishedAt;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public Instant getPublishedAt() {
		return publishedAt;
	}
	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

    // getters setters
}