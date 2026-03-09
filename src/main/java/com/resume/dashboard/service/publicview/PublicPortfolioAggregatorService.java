package com.resume.dashboard.service.publicview;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resume.dashboard.dto.publicview.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.*;
import com.resume.dashboard.service.publicview.section.SectionHandler;
import com.resume.dashboard.service.publicview.section.SectionHandlerRegistry;
import com.resume.dashboard.service.UserThemeCustomizationService;
import com.resume.dashboard.service.user.AnalyticsService;

@Service
public class PublicPortfolioAggregatorService {

    private final ResumeRepository resumeRepository;
    private final LayoutRepository layoutRepository;
    private final ThemeRepository themeRepository;
    private final TemplateRepository templateRepository;
    private final PortfolioSectionConfigRepository sectionConfigRepository;
    private final UserThemeCustomizationRepository customizationRepository;
    private final SectionHandlerRegistry registry;
    private final AnalyticsService analyticsService;

    public PublicPortfolioAggregatorService(
            ResumeRepository resumeRepository,
            LayoutRepository layoutRepository,
            ThemeRepository themeRepository,
            TemplateRepository templateRepository,
            PortfolioSectionConfigRepository sectionConfigRepository,
            UserThemeCustomizationRepository customizationRepository,
            SectionHandlerRegistry registry,
            AnalyticsService analyticsService) {

        this.resumeRepository = resumeRepository;
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
        this.templateRepository = templateRepository;
        this.sectionConfigRepository = sectionConfigRepository;
        this.customizationRepository = customizationRepository;
        this.registry = registry;
        this.analyticsService = analyticsService;
    }

    /* =============================================================
       MAIN ENTRY POINT — called by public controller via slug
    ============================================================= */
    public PublicPortfolioResponse getPublicPortfolio(String slug) {

        // ── 1. Load resume — must be published ──────────────────────
        Resume resume = resumeRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        // ── 2. Record analytics view event ──────────────────────────
        analyticsService.recordEvent(
                resume.getId(),
                AnalyticsEventType.VIEW,
                null
        );

        // ── 3. Load template (for plan level + section config) ──────
        Template template = templateRepository
                .findByIdAndActiveTrue(resume.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        // ── 4. Load layout (structureConfig drives the renderer) ────
        Layout layout = layoutRepository
                .findById(resume.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        // ── 5. Load base theme ───────────────────────────────────────
        Theme theme = themeRepository
                .findById(resume.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        // ── 6. Merge theme with user customization if PRO/PREMIUM ───
        // Customization records only exist for PRO/PREMIUM users.
        // For FREE/BASIC this returns the pure base theme unchanged.
        ResolvedThemeDTO resolvedTheme = resolveTheme(resume, theme);

        // ── 7. Load enabled sections in display order ────────────────
        List<PortfolioSectionConfig> enabledSections =
                sectionConfigRepository
                        .findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                        .stream()
                        .filter(PortfolioSectionConfig::isEnabled)
                        .collect(Collectors.toList());

        // ── 8. Dispatch each section to its handler ──────────────────
        // Use LinkedHashMap to preserve section display order
        Map<String, Object> sections = new LinkedHashMap<>();

        for (PortfolioSectionConfig config : enabledSections) {
            SectionHandler handler = registry.getHandler(config.getSectionName());
            if (handler != null) {
                Object data = handler.getSectionData(resume);
                if (data != null) {
                    sections.put(config.getSectionName().name(), data);
                }
            }
        }

        // ── 9. Build layout DTO ──────────────────────────────────────
        LayoutDTO layoutDTO = new LayoutDTO();
        layoutDTO.setLayoutType(layout.getLayoutType().name());
        layoutDTO.setLayoutVersion(layout.getVersion());
        layoutDTO.setTargetAudiences(layout.getTargetAudiences());
        layoutDTO.setCompatibleMoods(layout.getCompatibleMoods());
        layoutDTO.setStructureConfig(layout.getStructureConfig()); // JSON config for frontend renderer

        // ── 10. Build template meta DTO ──────────────────────────────
        TemplateMetaDTO templateMeta = new TemplateMetaDTO();
        templateMeta.setTemplateId(template.getId());
        templateMeta.setTemplateName(template.getName());
        templateMeta.setTemplateVersion(resume.getTemplateVersion()); // version snapshot on resume
        templateMeta.setPlanLevel(template.getPlanLevel().name());
        templateMeta.setPrimaryMood(template.getPrimaryMood() != null
                ? template.getPrimaryMood().name() : null);

        // ── 11. Assemble final response ──────────────────────────────
        PublicPortfolioResponse response = new PublicPortfolioResponse();
        response.setResumeId(resume.getId());
        response.setTitle(resume.getTitle());
        response.setProfessionType(resume.getProfessionType());
        response.setSlug(resume.getSlug());
        response.setViewCount(resume.getViewCount());
        response.setTemplateMeta(templateMeta);
        response.setLayout(layoutDTO);
        response.setTheme(resolvedTheme);
        response.setSections(sections);

        return response;
    }

    /* =============================================================
       THEME RESOLUTION
       Merges base theme + user customization overrides.
       If no customization exists (FREE/BASIC), returns pure base theme.
       If customization exists (PRO/PREMIUM), overlays the delta fields.
    ============================================================= */
    private ResolvedThemeDTO resolveTheme(Resume resume, Theme base) {

        // Try to load user customization for this resume
        UserThemeCustomization custom = customizationRepository
                .findByUserIdAndResumeId(resume.getUserId(), resume.getId())
                .orElse(null);

        // Deep-copy all theme objects — never mutate the stored entity
        ThemeColorPalette palette   = copyPalette(base.getColorPalette());
        ThemeBackground background  = base.getBackground();   // replaced wholesale if gradient override
        ThemeTypography typography  = copyTypography(base.getTypography());
        ThemeEffects effects        = copyEffects(base.getEffects());

        boolean hasCustomizations = false;

        // Only apply overrides if customization exists AND matches this theme
        // (user may have customized a different base theme previously)
        if (custom != null && base.getId().equals(custom.getBaseThemeId())) {
            hasCustomizations = true;

            // Color overrides
            if (custom.getPrimaryColor() != null)        palette.setPrimary(custom.getPrimaryColor());
            if (custom.getSecondaryColor() != null)      palette.setSecondary(custom.getSecondaryColor());
            if (custom.getAccentColor() != null)         palette.setAccent(custom.getAccentColor());
            if (custom.getTextPrimaryColor() != null)    palette.setTextPrimary(custom.getTextPrimaryColor());
            if (custom.getTextSecondaryColor() != null)  palette.setTextSecondary(custom.getTextSecondaryColor());
            if (custom.getPageBackgroundColor() != null) palette.setPageBackground(custom.getPageBackgroundColor());

            // Gradient override — replaces entire background but preserves texture overlay
            if (custom.getCustomGradient() != null) {
                ThemeBackground overrideBg = new ThemeBackground();
                overrideBg.setType(ThemeBackground.BackgroundType.GRADIENT);
                overrideBg.setGradient(custom.getCustomGradient());
                if (base.getBackground() != null) {
                    overrideBg.setTextureOverlay(base.getBackground().getTextureOverlay());
                }
                background = overrideBg;
            }

            // Typography overrides
            if (custom.getHeadingFont() != null)   typography.setHeadingFont(custom.getHeadingFont());
            if (custom.getBodyFont() != null)       typography.setBodyFont(custom.getBodyFont());
            if (custom.getBaseFontSize() != null)   typography.setBaseSize(custom.getBaseFontSize());
            if (custom.getHeadingWeight() != null)  typography.setHeadingWeight(custom.getHeadingWeight());

            // Effects overrides
            if (custom.getEnableGrain() != null)       effects.setEnableGrain(custom.getEnableGrain());
            if (custom.getGrainIntensity() != null)    effects.setGlobalGrainIntensity(custom.getGrainIntensity());
            if (custom.getCardBorderRadius() != null)  effects.setCardBorderRadius(custom.getCardBorderRadius());
        }

        ResolvedThemeDTO dto = new ResolvedThemeDTO();
        dto.setThemeId(base.getId());
        dto.setThemeName(base.getName());
        dto.setThemeVersion(resume.getThemeVersion()); // version snapshot stored on resume
        dto.setMood(base.getMood() != null ? base.getMood().name() : null);
        dto.setColorPalette(palette);
        dto.setBackground(background);
        dto.setTypography(typography);
        dto.setEffects(effects);
        dto.setHasCustomizations(hasCustomizations);

        return dto;
    }

    /* =============================================================
       COPY HELPERS — prevent entity mutation
    ============================================================= */
    private ThemeColorPalette copyPalette(ThemeColorPalette src) {
        if (src == null) return new ThemeColorPalette();
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
        if (src == null) return new ThemeTypography();
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
}