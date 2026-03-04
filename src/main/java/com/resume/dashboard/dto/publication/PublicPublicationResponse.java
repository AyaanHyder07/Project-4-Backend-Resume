package com.resume.dashboard.dto.publication;

import com.resume.dashboard.entity.PublicationType;

import java.time.LocalDate;
import java.util.List;

public class PublicPublicationResponse {

    private String title;
    private PublicationType type;
    private String publisher;
    private LocalDate publicationDate;
    private String abstractText;
    private String contentUrl;
    private List<String> keywords;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public PublicationType getType() {
		return type;
	}
	public void setType(PublicationType type) {
		this.type = type;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public LocalDate getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(LocalDate publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getAbstractText() {
		return abstractText;
	}
	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

    // getters & setters
}