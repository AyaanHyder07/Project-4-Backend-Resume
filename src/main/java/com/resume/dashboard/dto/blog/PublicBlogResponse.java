package com.resume.dashboard.dto.blog;

import java.time.Instant;
import java.util.List;

public class PublicBlogResponse {

    private String title;
    private String slug;
    private String coverImage;
    private String content;
    private List<String> tags;
    private long viewCount;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public long getViewCount() {
		return viewCount;
	}
	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}
	public Instant getPublishedAt() {
		return publishedAt;
	}
	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

    // getters & setters
    
    
}