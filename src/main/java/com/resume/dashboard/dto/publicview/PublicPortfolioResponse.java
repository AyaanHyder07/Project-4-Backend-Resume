package com.resume.dashboard.dto.publicview;

import java.util.Map;

public class PublicPortfolioResponse {
	
	private String resumeId;
    private String title;
    private String professionType;
    private String slug;

    private LayoutDTO layout;
    private ThemeDTO theme;

    private Map<String, Object> sections;

    public String getResumeId() {
		return resumeId;
	}

	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfessionType() {
        return professionType;
    }

    public void setProfessionType(String professionType) {
        this.professionType = professionType;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LayoutDTO getLayout() {
        return layout;
    }

    public void setLayout(LayoutDTO layout) {
        this.layout = layout;
    }

    public ThemeDTO getTheme() {
        return theme;
    }

    public void setTheme(ThemeDTO theme) {
        this.theme = theme;
    }

    public Map<String, Object> getSections() {
        return sections;
    }

    public void setSections(Map<String, Object> sections) {
        this.sections = sections;
    }
}