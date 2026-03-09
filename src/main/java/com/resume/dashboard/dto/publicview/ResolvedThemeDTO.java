package com.resume.dashboard.dto.publicview;

import com.resume.dashboard.entity.ThemeBackground;
import com.resume.dashboard.entity.ThemeColorPalette;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.ThemeTypography;

/**
 * Fully resolved theme sent to the frontend renderer.
 * This is already merged — base theme + any PRO/PREMIUM user overrides.
 * The renderer just reads this directly, no extra merging needed client-side.
 *
 * hasCustomizations = true means a PRO/PREMIUM user has overridden some fields.
 * The renderer can use this to show "Reset to default" button in edit mode.
 */
public class ResolvedThemeDTO {

    private String themeId;
    private String themeName;
    private int themeVersion;        // snapshot version stored on resume
    private String mood;             // e.g. "DARK", "MINIMAL"
    private boolean hasCustomizations; // true = PRO/PREMIUM user has overrides applied

    private ThemeColorPalette colorPalette;
    private ThemeBackground background;
    private ThemeTypography typography;
    private ThemeEffects effects;

    public String getThemeId() { return themeId; }
    public void setThemeId(String themeId) { this.themeId = themeId; }

    public String getThemeName() { return themeName; }
    public void setThemeName(String themeName) { this.themeName = themeName; }

    public int getThemeVersion() { return themeVersion; }
    public void setThemeVersion(int themeVersion) { this.themeVersion = themeVersion; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public boolean isHasCustomizations() { return hasCustomizations; }
    public void setHasCustomizations(boolean hasCustomizations) { this.hasCustomizations = hasCustomizations; }

    public ThemeColorPalette getColorPalette() { return colorPalette; }
    public void setColorPalette(ThemeColorPalette colorPalette) { this.colorPalette = colorPalette; }

    public ThemeBackground getBackground() { return background; }
    public void setBackground(ThemeBackground background) { this.background = background; }

    public ThemeTypography getTypography() { return typography; }
    public void setTypography(ThemeTypography typography) { this.typography = typography; }

    public ThemeEffects getEffects() { return effects; }
    public void setEffects(ThemeEffects effects) { this.effects = effects; }
}