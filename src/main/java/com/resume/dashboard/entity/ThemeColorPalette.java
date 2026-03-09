package com.resume.dashboard.entity;

public class ThemeColorPalette {

    // Core semantic colors
    private String primary;             // Main brand color
    private String secondary;           // Supporting color
    private String accent;              // CTA / highlight color

    // Surface & background
    private String surfaceBackground;   // Card/section background
    private String pageBackground;      // Full page bg (can be overridden by ThemeBackground)

    // Text
    private String textPrimary;
    private String textSecondary;
    private String textMuted;

    // Borders & dividers
    private String borderColor;
    private String dividerColor;

    // Special effects
    private String glowColor;           // For dark/neon themes
    private String shadowColor;

    // Tags & badges
    private String tagBackground;
    private String tagText;
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}
	public String getSecondary() {
		return secondary;
	}
	public void setSecondary(String secondary) {
		this.secondary = secondary;
	}
	public String getAccent() {
		return accent;
	}
	public void setAccent(String accent) {
		this.accent = accent;
	}
	public String getSurfaceBackground() {
		return surfaceBackground;
	}
	public void setSurfaceBackground(String surfaceBackground) {
		this.surfaceBackground = surfaceBackground;
	}
	public String getPageBackground() {
		return pageBackground;
	}
	public void setPageBackground(String pageBackground) {
		this.pageBackground = pageBackground;
	}
	public String getTextPrimary() {
		return textPrimary;
	}
	public void setTextPrimary(String textPrimary) {
		this.textPrimary = textPrimary;
	}
	public String getTextSecondary() {
		return textSecondary;
	}
	public void setTextSecondary(String textSecondary) {
		this.textSecondary = textSecondary;
	}
	public String getTextMuted() {
		return textMuted;
	}
	public void setTextMuted(String textMuted) {
		this.textMuted = textMuted;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public String getDividerColor() {
		return dividerColor;
	}
	public void setDividerColor(String dividerColor) {
		this.dividerColor = dividerColor;
	}
	public String getGlowColor() {
		return glowColor;
	}
	public void setGlowColor(String glowColor) {
		this.glowColor = glowColor;
	}
	public String getShadowColor() {
		return shadowColor;
	}
	public void setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
	}
	public String getTagBackground() {
		return tagBackground;
	}
	public void setTagBackground(String tagBackground) {
		this.tagBackground = tagBackground;
	}
	public String getTagText() {
		return tagText;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}
    
    	
}