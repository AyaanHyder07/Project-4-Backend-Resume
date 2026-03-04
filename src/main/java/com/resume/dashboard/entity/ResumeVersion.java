package com.resume.dashboard.entity;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resume_versions")
public class ResumeVersion {

    @Id
    private String id;

    @Indexed
    private String resumeId;

    @Indexed
    private String userId;

    private boolean current;   // 🔥 ADD THIS

    private Map<String, Object> snapshotContent;

    private String templateId;
    private int templateVersion;

    private String layoutId;
    private int layoutVersion;

    private String themeId;
    private int themeVersion;

    private String changeNote;

    private Instant createdAt;

    // getters + setters


	public String getId() {
		return id;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResumeId() {
		return resumeId;
	}

	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	public Map<String, Object> getSnapshotContent() {
		return snapshotContent;
	}

	public void setSnapshotContent(Map<String, Object> snapshotContent) {
		this.snapshotContent = snapshotContent;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getTemplateVersion() {
		return templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public int getLayoutVersion() {
		return layoutVersion;
	}

	public void setLayoutVersion(int layoutVersion) {
		this.layoutVersion = layoutVersion;
	}

	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public int getThemeVersion() {
		return themeVersion;
	}

	public void setThemeVersion(int themeVersion) {
		this.themeVersion = themeVersion;
	}

	public String getChangeNote() {
		return changeNote;
	}

	public void setChangeNote(String changeNote) {
		this.changeNote = changeNote;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}