package com.resume.dashboard.dto.customization;

import com.resume.dashboard.entity.ThemeBackground;
import com.resume.dashboard.entity.ThemeColorPalette;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.ThemeTypography;
import com.resume.dashboard.entity.VisualMood;


/**
 * FULLY MERGED theme — base theme values overridden by user customizations.
 * This is the ONLY object the frontend renderer needs to paint the resume.
 * No nulls — every field is resolved to a concrete value.
 */
public class ResolvedThemeResponse {

    // Which base theme this was built from (for editor UI awareness)
    private String baseThemeId;
    private String baseThemeName;

    // Fully resolved — no nulls below this line in a valid response
    private ThemeColorPalette colorPalette;
    private ThemeBackground background;
    private ThemeTypography typography;
    private ThemeEffects effects;
    private VisualMood mood;

    // True if user has any active overrides on top of base theme
    private boolean hasCustomizations;

	public String getBaseThemeId() {
		return baseThemeId;
	}

	public void setBaseThemeId(String baseThemeId) {
		this.baseThemeId = baseThemeId;
	}

	public String getBaseThemeName() {
		return baseThemeName;
	}

	public void setBaseThemeName(String baseThemeName) {
		this.baseThemeName = baseThemeName;
	}

	public ThemeColorPalette getColorPalette() {
		return colorPalette;
	}

	public void setColorPalette(ThemeColorPalette colorPalette) {
		this.colorPalette = colorPalette;
	}

	public ThemeBackground getBackground() {
		return background;
	}

	public void setBackground(ThemeBackground background) {
		this.background = background;
	}

	public ThemeTypography getTypography() {
		return typography;
	}

	public void setTypography(ThemeTypography typography) {
		this.typography = typography;
	}

	public ThemeEffects getEffects() {
		return effects;
	}

	public void setEffects(ThemeEffects effects) {
		this.effects = effects;
	}

	public VisualMood getMood() {
		return mood;
	}

	public void setMood(VisualMood mood) {
		this.mood = mood;
	}

	public boolean isHasCustomizations() {
		return hasCustomizations;
	}

	public void setHasCustomizations(boolean hasCustomizations) {
		this.hasCustomizations = hasCustomizations;
	}
}