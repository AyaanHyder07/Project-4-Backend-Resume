package com.resume.dashboard.service;

import com.resume.dashboard.dto.customization.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.*;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserThemeCustomizationService {

    private final UserThemeCustomizationRepository customizationRepository;
    private final ThemeRepository themeRepository;
    private final SubscriptionService subscriptionService;  // ✅ injected for plan enforcement

    public UserThemeCustomizationService(
            UserThemeCustomizationRepository customizationRepository,
            ThemeRepository themeRepository,
            SubscriptionService subscriptionService) {
        this.customizationRepository = customizationRepository;
        this.themeRepository = themeRepository;
        this.subscriptionService = subscriptionService;
    }

    // ─── SAVE / UPSERT ───────────────────────────────────────────────
    /**
     * PRO and PREMIUM ONLY.
     * Upserts the user's theme delta for a specific resume.
     * Only fields present in the request are applied — null = keep base default.
     *
     * FIX: Replaced ad-hoc `if (userPlan == PlanType.FREE)` check with
     *      subscriptionService.validateThemeCustomization(userId) so that
     *      BASIC is also correctly blocked, and plan logic stays in one place.
     */
    public UserThemeCustomizationResponse saveCustomization(
            String userId,
            String resumeId,
            SaveCustomizationRequest request) {

        // 🔥 Plan enforcement — only PRO and PREMIUM pass
        subscriptionService.validateThemeCustomization(userId);

        themeRepository.findById(request.getBaseThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Base theme not found"));

        // Upsert — one record per (userId, resumeId)
        UserThemeCustomization custom = customizationRepository
                .findByUserIdAndResumeId(userId, resumeId)
                .orElse(new UserThemeCustomization());

        if (custom.getId() == null) {
            custom.setId(UUID.randomUUID().toString());
            custom.setUserId(userId);
            custom.setResumeId(resumeId);
            custom.setCreatedAt(Instant.now());
        }

        custom.setBaseThemeId(request.getBaseThemeId());

        if (request.getPrimaryColor() != null)
            custom.setPrimaryColor(request.getPrimaryColor());

        if (request.getSecondaryColor() != null)
            custom.setSecondaryColor(request.getSecondaryColor());

        if (request.getAccentColor() != null)
            custom.setAccentColor(request.getAccentColor());

        if (request.getTextPrimaryColor() != null)
            custom.setTextPrimaryColor(request.getTextPrimaryColor());

        if (request.getTextSecondaryColor() != null)
            custom.setTextSecondaryColor(request.getTextSecondaryColor());

        if (request.getPageBackgroundColor() != null)
            custom.setPageBackgroundColor(request.getPageBackgroundColor());

        if (request.getCustomGradient() != null)
            custom.setCustomGradient(request.getCustomGradient());

        if (request.getHeadingFont() != null)
            custom.setHeadingFont(request.getHeadingFont());

        if (request.getBodyFont() != null)
            custom.setBodyFont(request.getBodyFont());

        if (request.getBaseFontSize() != null)
            custom.setBaseFontSize(request.getBaseFontSize());

        if (request.getHeadingWeight() != null)
            custom.setHeadingWeight(request.getHeadingWeight());

        if (request.getEnableGrain() != null)
            custom.setEnableGrain(request.getEnableGrain());

        if (request.getGrainIntensity() != null)
            custom.setGrainIntensity(request.getGrainIntensity());

        if (request.getCardBorderRadius() != null)
            custom.setCardBorderRadius(request.getCardBorderRadius());

        if (request.getTemplateOptions() != null)
            custom.setTemplateOptions(request.getTemplateOptions());

        if (request.getTemplateLabels() != null)
            custom.setTemplateLabels(request.getTemplateLabels());

        custom.setUpdatedAt(Instant.now());

        return mapToCustomizationResponse(customizationRepository.save(custom));
    }

    // ─── RESOLVE THEME ───────────────────────────────────────────────
    /**
     * Merges base theme + user overrides into one flat render-ready object.
     * Frontend renderer calls this — no need to know about the delta system.
     *
     * Works for all plans:
     *  - FREE/BASIC: returns pure base theme, hasCustomizations=false
     *  - PRO/PREMIUM: returns merged result, hasCustomizations=true if delta exists
     *
     * No plan check needed here — reading the resolved theme is safe for all plans.
     * Customization records simply won't exist for FREE/BASIC users.
     */
    public ResolvedThemeResponse resolveTheme(String userId, String resumeId, String baseThemeId) {

        Theme base = themeRepository.findById(baseThemeId)
                .orElseThrow(() -> new ResourceNotFoundException("Base theme not found"));

        UserThemeCustomization custom = customizationRepository
                .findByUserIdAndResumeId(userId, resumeId)
                .orElse(null);

        ResolvedThemeResponse resolved = new ResolvedThemeResponse();
        resolved.setBaseThemeId(base.getId());
        resolved.setBaseThemeName(base.getName());
        resolved.setMood(base.getMood());

        ThemeColorPalette palette = copyPalette(base.getColorPalette());
        ThemeBackground background = base.getBackground();
        ThemeTypography typography = copyTypography(base.getTypography());
        ThemeEffects effects = copyEffects(base.getEffects());

        boolean hasCustomizations = false;

        if (custom != null && custom.getBaseThemeId().equals(baseThemeId)) {
            hasCustomizations = true;

            if (custom.getPrimaryColor() != null)       palette.setPrimary(custom.getPrimaryColor());
            if (custom.getSecondaryColor() != null)     palette.setSecondary(custom.getSecondaryColor());
            if (custom.getAccentColor() != null)        palette.setAccent(custom.getAccentColor());
            if (custom.getTextPrimaryColor() != null)   palette.setTextPrimary(custom.getTextPrimaryColor());
            if (custom.getTextSecondaryColor() != null) palette.setTextSecondary(custom.getTextSecondaryColor());
            if (custom.getPageBackgroundColor() != null) palette.setPageBackground(custom.getPageBackgroundColor());

            if (custom.getCustomGradient() != null) {
                ThemeBackground overrideBackground = new ThemeBackground();
                overrideBackground.setType(ThemeBackground.BackgroundType.GRADIENT);
                overrideBackground.setGradient(custom.getCustomGradient());
                overrideBackground.setTextureOverlay(base.getBackground().getTextureOverlay());
                background = overrideBackground;
            }

            if (custom.getHeadingFont() != null)    typography.setHeadingFont(custom.getHeadingFont());
            if (custom.getBodyFont() != null)        typography.setBodyFont(custom.getBodyFont());
            if (custom.getBaseFontSize() != null)    typography.setBaseSize(custom.getBaseFontSize());
            if (custom.getHeadingWeight() != null)   typography.setHeadingWeight(custom.getHeadingWeight());

            if (custom.getEnableGrain() != null)      effects.setEnableGrain(custom.getEnableGrain());
            if (custom.getGrainIntensity() != null)   effects.setGlobalGrainIntensity(custom.getGrainIntensity());
            if (custom.getCardBorderRadius() != null) effects.setCardBorderRadius(custom.getCardBorderRadius());
        }

        resolved.setColorPalette(palette);
        resolved.setBackground(background);
        resolved.setTypography(typography);
        resolved.setEffects(effects);
        resolved.setHasCustomizations(hasCustomizations);

        return resolved;
    }

    // ─── GET RAW CUSTOMIZATION ───────────────────────────────────────
    /**
     * Returns the raw delta record for the theme editor UI.
     * Plan check enforced — only PRO/PREMIUM should access the editor.
     */
    public UserThemeCustomizationResponse getCustomization(String userId, String resumeId) {

        // 🔥 Plan enforcement
        subscriptionService.validateThemeCustomization(userId);

        return mapToCustomizationResponse(
            customizationRepository
                .findByUserIdAndResumeId(userId, resumeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "No customization found for this resume"
                ))
        );
    }

    // ─── RESET ───────────────────────────────────────────────────────
    /**
     * Wipes all overrides — resume reverts to pure base theme.
     * Plan check enforced — only PRO/PREMIUM users have customizations to reset.
     */
    public void resetCustomization(String userId, String resumeId) {

        // 🔥 Plan enforcement
        subscriptionService.validateThemeCustomization(userId);

        customizationRepository
                .findByUserIdAndResumeId(userId, resumeId)
                .ifPresent(customizationRepository::delete);
    }

    // ─── COPY HELPERS (prevent entity mutation) ──────────────────────

    private ThemeColorPalette copyPalette(ThemeColorPalette src) {
        ThemeColorPalette copy = new ThemeColorPalette();
        copy.setPrimary(src.getPrimary());
        copy.setSecondary(src.getSecondary());
        copy.setAccent(src.getAccent());
        copy.setSurfaceBackground(src.getSurfaceBackground());
        copy.setPageBackground(src.getPageBackground());
        copy.setTextPrimary(src.getTextPrimary());
        copy.setTextSecondary(src.getTextSecondary());
        copy.setTextMuted(src.getTextMuted());
        copy.setBorderColor(src.getBorderColor());
        copy.setDividerColor(src.getDividerColor());
        copy.setGlowColor(src.getGlowColor());
        copy.setShadowColor(src.getShadowColor());
        copy.setTagBackground(src.getTagBackground());
        copy.setTagText(src.getTagText());
        return copy;
    }

    private ThemeTypography copyTypography(ThemeTypography src) {
        ThemeTypography copy = new ThemeTypography();
        copy.setHeadingFont(src.getHeadingFont());
        copy.setBodyFont(src.getBodyFont());
        copy.setAccentFont(src.getAccentFont());
        copy.setHeadingFontUrl(src.getHeadingFontUrl());
        copy.setBodyFontUrl(src.getBodyFontUrl());
        copy.setBaseSize(src.getBaseSize());
        copy.setHeadingScale(src.getHeadingScale());
        copy.setSubheadingScale(src.getSubheadingScale());
        copy.setLabelScale(src.getLabelScale());
        copy.setHeadingWeight(src.getHeadingWeight());
        copy.setBodyWeight(src.getBodyWeight());
        copy.setHeadingTransform(src.getHeadingTransform());
        copy.setHeadingStyle(src.getHeadingStyle());
        copy.setHeadingLetterSpacing(src.getHeadingLetterSpacing());
        copy.setBodyLineHeight(src.getBodyLineHeight());
        copy.setHeadingLineHeight(src.getHeadingLineHeight());
        return copy;
    }

    private ThemeEffects copyEffects(ThemeEffects src) {
        if (src == null) return new ThemeEffects();
        ThemeEffects copy = new ThemeEffects();
        copy.setCardBorderRadius(src.getCardBorderRadius());
        copy.setCardShadow(src.getCardShadow());
        copy.setCardBorderStyle(src.getCardBorderStyle());
        copy.setCardBackdropFilter(src.getCardBackdropFilter());
        copy.setEnableScrollReveal(src.getEnableScrollReveal());
        copy.setEnableHoverLift(src.getEnableHoverLift());
        copy.setEnableParallax(src.getEnableParallax());
        copy.setTransitionSpeed(src.getTransitionSpeed());
        copy.setEnableGlassmorphism(src.getEnableGlassmorphism());
        copy.setEnableNeumorphism(src.getEnableNeumorphism());
        copy.setEnableGrain(src.getEnableGrain());
        copy.setGlobalGrainIntensity(src.getGlobalGrainIntensity());
        copy.setSectionDividerStyle(src.getSectionDividerStyle());
        return copy;
    }

    // ─── MAPPER ──────────────────────────────────────────────────────
    private UserThemeCustomizationResponse mapToCustomizationResponse(UserThemeCustomization c) {
        UserThemeCustomizationResponse r = new UserThemeCustomizationResponse();
        r.setId(c.getId());
        r.setUserId(c.getUserId());
        r.setResumeId(c.getResumeId());
        r.setBaseThemeId(c.getBaseThemeId());
        r.setPrimaryColor(c.getPrimaryColor());
        r.setSecondaryColor(c.getSecondaryColor());
        r.setAccentColor(c.getAccentColor());
        r.setTextPrimaryColor(c.getTextPrimaryColor());
        r.setTextSecondaryColor(c.getTextSecondaryColor());
        r.setPageBackgroundColor(c.getPageBackgroundColor());
        r.setCustomGradient(c.getCustomGradient());
        r.setHeadingFont(c.getHeadingFont());
        r.setBodyFont(c.getBodyFont());
        r.setBaseFontSize(c.getBaseFontSize());
        r.setHeadingWeight(c.getHeadingWeight());
        r.setEnableGrain(c.getEnableGrain());
        r.setGrainIntensity(c.getGrainIntensity());
        r.setCardBorderRadius(c.getCardBorderRadius());
        r.setTemplateOptions(c.getTemplateOptions());
        r.setTemplateLabels(c.getTemplateLabels());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }
}
