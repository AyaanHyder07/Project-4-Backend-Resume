package com.resume.dashboard.entity;

public class ThemeTypography {

    // Font families
    private String headingFont;          // e.g. "Playfair Display"
    private String bodyFont;             // e.g. "Inter"
    private String accentFont;           // optional third font for labels/tags

    // Font sources
    private String headingFontUrl;       // Google Fonts URL or CDN
    private String bodyFontUrl;

    // Sizing scale (in rem)
    private Double baseSize;             // e.g. 1.0 (= 16px)
    private Double headingScale;         // multiplier: 2.5, 3.0, etc.
    private Double subheadingScale;
    private Double labelScale;           // 0.75 = small caps label

    // Weight
    private Integer headingWeight;       // 400, 600, 700, 900
    private Integer bodyWeight;

    // Style
    private String headingTransform;     // "uppercase", "none", "capitalize"
    private String headingStyle;         // "italic", "normal"
    private Double headingLetterSpacing; // em value e.g. 0.05

    // Line height
    private Double bodyLineHeight;       // e.g. 1.6
    private Double headingLineHeight;    // e.g. 1.1
	public String getHeadingFont() {
		return headingFont;
	}
	public void setHeadingFont(String headingFont) {
		this.headingFont = headingFont;
	}
	public String getBodyFont() {
		return bodyFont;
	}
	public void setBodyFont(String bodyFont) {
		this.bodyFont = bodyFont;
	}
	public String getAccentFont() {
		return accentFont;
	}
	public void setAccentFont(String accentFont) {
		this.accentFont = accentFont;
	}
	public String getHeadingFontUrl() {
		return headingFontUrl;
	}
	public void setHeadingFontUrl(String headingFontUrl) {
		this.headingFontUrl = headingFontUrl;
	}
	public String getBodyFontUrl() {
		return bodyFontUrl;
	}
	public void setBodyFontUrl(String bodyFontUrl) {
		this.bodyFontUrl = bodyFontUrl;
	}
	public Double getBaseSize() {
		return baseSize;
	}
	public void setBaseSize(Double baseSize) {
		this.baseSize = baseSize;
	}
	public Double getHeadingScale() {
		return headingScale;
	}
	public void setHeadingScale(Double headingScale) {
		this.headingScale = headingScale;
	}
	public Double getSubheadingScale() {
		return subheadingScale;
	}
	public void setSubheadingScale(Double subheadingScale) {
		this.subheadingScale = subheadingScale;
	}
	public Double getLabelScale() {
		return labelScale;
	}
	public void setLabelScale(Double labelScale) {
		this.labelScale = labelScale;
	}
	public Integer getHeadingWeight() {
		return headingWeight;
	}
	public void setHeadingWeight(Integer headingWeight) {
		this.headingWeight = headingWeight;
	}
	public Integer getBodyWeight() {
		return bodyWeight;
	}
	public void setBodyWeight(Integer bodyWeight) {
		this.bodyWeight = bodyWeight;
	}
	public String getHeadingTransform() {
		return headingTransform;
	}
	public void setHeadingTransform(String headingTransform) {
		this.headingTransform = headingTransform;
	}
	public String getHeadingStyle() {
		return headingStyle;
	}
	public void setHeadingStyle(String headingStyle) {
		this.headingStyle = headingStyle;
	}
	public Double getHeadingLetterSpacing() {
		return headingLetterSpacing;
	}
	public void setHeadingLetterSpacing(Double headingLetterSpacing) {
		this.headingLetterSpacing = headingLetterSpacing;
	}
	public Double getBodyLineHeight() {
		return bodyLineHeight;
	}
	public void setBodyLineHeight(Double bodyLineHeight) {
		this.bodyLineHeight = bodyLineHeight;
	}
	public Double getHeadingLineHeight() {
		return headingLineHeight;
	}
	public void setHeadingLineHeight(Double headingLineHeight) {
		this.headingLineHeight = headingLineHeight;
	}
    
    
    
}