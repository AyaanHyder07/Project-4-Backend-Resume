package com.resume.dashboard.entity;

public class ThemeEffects {

    // Cards / sections
    private String cardBorderRadius;     // "0px", "8px", "24px", "50%"
    private String cardShadow;           // CSS box-shadow string
    private String cardBorderStyle;      // "none", "solid 1px #ccc", "dashed"
    private String cardBackdropFilter;   // "blur(10px)" for glassmorphism

    // Animations (hints for frontend, backend just stores config)
    private Boolean enableScrollReveal;
    private Boolean enableHoverLift;
    private Boolean enableParallax;
    private String transitionSpeed;      // "fast", "medium", "slow"

    // Special visual effects
    private Boolean enableGlassmorphism;
    private Boolean enableNeumorphism;
    private Boolean enableGrain;         // global grain overlay
    private Integer globalGrainIntensity;// 0–100

    // Divider style between sections
    private String sectionDividerStyle;  // "none", "wave", "diagonal", "zigzag", "brush"

	public String getCardBorderRadius() {
		return cardBorderRadius;
	}

	public void setCardBorderRadius(String cardBorderRadius) {
		this.cardBorderRadius = cardBorderRadius;
	}

	public String getCardShadow() {
		return cardShadow;
	}

	public void setCardShadow(String cardShadow) {
		this.cardShadow = cardShadow;
	}

	public String getCardBorderStyle() {
		return cardBorderStyle;
	}

	public void setCardBorderStyle(String cardBorderStyle) {
		this.cardBorderStyle = cardBorderStyle;
	}

	public String getCardBackdropFilter() {
		return cardBackdropFilter;
	}

	public void setCardBackdropFilter(String cardBackdropFilter) {
		this.cardBackdropFilter = cardBackdropFilter;
	}

	public Boolean getEnableScrollReveal() {
		return enableScrollReveal;
	}

	public void setEnableScrollReveal(Boolean enableScrollReveal) {
		this.enableScrollReveal = enableScrollReveal;
	}

	public Boolean getEnableHoverLift() {
		return enableHoverLift;
	}

	public void setEnableHoverLift(Boolean enableHoverLift) {
		this.enableHoverLift = enableHoverLift;
	}

	public Boolean getEnableParallax() {
		return enableParallax;
	}

	public void setEnableParallax(Boolean enableParallax) {
		this.enableParallax = enableParallax;
	}

	public String getTransitionSpeed() {
		return transitionSpeed;
	}

	public void setTransitionSpeed(String transitionSpeed) {
		this.transitionSpeed = transitionSpeed;
	}

	public Boolean getEnableGlassmorphism() {
		return enableGlassmorphism;
	}

	public void setEnableGlassmorphism(Boolean enableGlassmorphism) {
		this.enableGlassmorphism = enableGlassmorphism;
	}

	public Boolean getEnableNeumorphism() {
		return enableNeumorphism;
	}

	public void setEnableNeumorphism(Boolean enableNeumorphism) {
		this.enableNeumorphism = enableNeumorphism;
	}

	public Boolean getEnableGrain() {
		return enableGrain;
	}

	public void setEnableGrain(Boolean enableGrain) {
		this.enableGrain = enableGrain;
	}

	public Integer getGlobalGrainIntensity() {
		return globalGrainIntensity;
	}

	public void setGlobalGrainIntensity(Integer globalGrainIntensity) {
		this.globalGrainIntensity = globalGrainIntensity;
	}

	public String getSectionDividerStyle() {
		return sectionDividerStyle;
	}

	public void setSectionDividerStyle(String sectionDividerStyle) {
		this.sectionDividerStyle = sectionDividerStyle;
	}
    
    
    
}