package com.resume.dashboard.dto.media;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateMediaAppearanceRequest {

    @NotBlank
    private String resumeId;

    @NotBlank
    private String platformName;

    @NotBlank
    private String episodeTitle;

    @NotBlank
    private String url;

    private String description;

    @NotNull
    private LocalDate appearanceDate;

	public String getResumeId() {
		return resumeId;
	}

	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getEpisodeTitle() {
		return episodeTitle;
	}

	public void setEpisodeTitle(String episodeTitle) {
		this.episodeTitle = episodeTitle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getAppearanceDate() {
		return appearanceDate;
	}

	public void setAppearanceDate(LocalDate appearanceDate) {
		this.appearanceDate = appearanceDate;
	}

    // getters & setters
}