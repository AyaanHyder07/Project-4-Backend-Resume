package com.resume.dashboard.service.publicview;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resume.dashboard.dto.publicview.LayoutDTO;
import com.resume.dashboard.dto.publicview.PublicPortfolioBlockDTO;
import com.resume.dashboard.dto.publicview.PublicPortfolioResponse;
import com.resume.dashboard.dto.publicview.ResolvedThemeDTO;
import com.resume.dashboard.dto.publicview.TemplateMetaDTO;
import com.resume.dashboard.entity.AnalyticsEventType;
import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.PortfolioBlock;
import com.resume.dashboard.entity.PortfolioSectionConfig;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.Theme;
import com.resume.dashboard.entity.ThemeBackground;
import com.resume.dashboard.entity.ThemeColorPalette;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.ThemeTypography;
import com.resume.dashboard.entity.UserThemeCustomization;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.LayoutRepository;
import com.resume.dashboard.repository.PortfolioBlockRepository;
import com.resume.dashboard.repository.PortfolioSectionConfigRepository;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.ThemeRepository;
import com.resume.dashboard.repository.UserThemeCustomizationRepository;
import com.resume.dashboard.service.UserProfileService;
import com.resume.dashboard.service.UserThemeCustomizationService;
import com.resume.dashboard.service.publicview.section.SectionHandler;
import com.resume.dashboard.service.publicview.section.SectionHandlerRegistry;
import com.resume.dashboard.service.user.AnalyticsService;

@Service
public class PublicPortfolioAggregatorService {

    private final ResumeRepository resumeRepository;
    private final LayoutRepository layoutRepository;
    private final ThemeRepository themeRepository;
    private final TemplateRepository templateRepository;
    private final PortfolioSectionConfigRepository sectionConfigRepository;
    private final UserThemeCustomizationRepository customizationRepository;
    private final PortfolioBlockRepository portfolioBlockRepository;
    private final SectionHandlerRegistry registry;
    private final AnalyticsService analyticsService;
    private final UserProfileService userProfileService;

    public PublicPortfolioAggregatorService(
            ResumeRepository resumeRepository,
            LayoutRepository layoutRepository,
            ThemeRepository themeRepository,
            TemplateRepository templateRepository,
            PortfolioSectionConfigRepository sectionConfigRepository,
            UserThemeCustomizationRepository customizationRepository,
            PortfolioBlockRepository portfolioBlockRepository,
            SectionHandlerRegistry registry,
            AnalyticsService analyticsService,
            UserProfileService userProfileService) {
        this.resumeRepository = resumeRepository;
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
        this.templateRepository = templateRepository;
        this.sectionConfigRepository = sectionConfigRepository;
        this.customizationRepository = customizationRepository;
        this.portfolioBlockRepository = portfolioBlockRepository;
        this.registry = registry;
        this.analyticsService = analyticsService;
        this.userProfileService = userProfileService;
    }

    public PublicPortfolioResponse getPublicPortfolio(String slug) {
        Resume resume = resumeRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        analyticsService.recordEvent(resume.getId(), AnalyticsEventType.VIEW, null);

        Template template = templateRepository.findByIdAndActiveTrue(resume.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        Layout layout = layoutRepository.findById(resume.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));
        Theme theme = themeRepository.findById(resume.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        ResolvedThemeDTO resolvedTheme = resolveTheme(resume, theme);

        List<PortfolioSectionConfig> enabledSections = sectionConfigRepository
                .findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .filter(PortfolioSectionConfig::isEnabled)
                .collect(Collectors.toList());

        Map<String, Object> sections = new LinkedHashMap<>();
        Map<String, String> sectionTitles = new LinkedHashMap<>();

        for (PortfolioSectionConfig config : enabledSections) {
            String sectionKey = config.getSectionName().name();
            if (config.getCustomTitle() != null && !config.getCustomTitle().isBlank()) {
                sectionTitles.put(sectionKey, config.getCustomTitle().trim());
            }

            if (config.getSectionName() == PortfolioSectionType.CONTACT) {
                Map<String, Object> contactMeta = new LinkedHashMap<>();
                contactMeta.put("resumeId", resume.getId());
                sections.put(sectionKey, contactMeta);
                continue;
            }

            SectionHandler handler = registry.getHandler(config.getSectionName());
            if (handler != null) {
                Object data = handler.getSectionData(resume);
                if (data != null) {
                    sections.put(sectionKey, data);
                }
            }
        }

        boolean contactAlreadyPresent = sections.containsKey(PortfolioSectionType.CONTACT.name());
        boolean templateSupportsContact = (template.getSupportedSections() != null && template.getSupportedSections().contains(PortfolioSectionType.CONTACT.name()))
                || (template.getRequiredSections() != null && template.getRequiredSections().contains(PortfolioSectionType.CONTACT.name()));
        if (!contactAlreadyPresent && templateSupportsContact) {
            Map<String, Object> contactMeta = new LinkedHashMap<>();
            contactMeta.put("resumeId", resume.getId());
            sections.put(PortfolioSectionType.CONTACT.name(), contactMeta);
        }

        LayoutDTO layoutDTO = new LayoutDTO();
        layoutDTO.setLayoutType(layout.getLayoutType().name());
        layoutDTO.setLayoutVersion(layout.getVersion());
        layoutDTO.setTargetAudiences(layout.getTargetAudiences());
        layoutDTO.setCompatibleMoods(layout.getCompatibleMoods());
        layoutDTO.setProfessionTags(layout.getProfessionTags());
        layoutDTO.setSupportedProfessionCategories(layout.getSupportedProfessionCategories());
        layoutDTO.setSupportedProfessionTypes(layout.getSupportedProfessionTypes());
        layoutDTO.setSupportedContentModes(layout.getSupportedContentModes());
        layoutDTO.setSupportedMotionPresets(layout.getSupportedMotionPresets());
        layoutDTO.setRecommendedBlockTypes(layout.getRecommendedBlockTypes());
        layoutDTO.setDefaultMotionPreset(layout.getDefaultMotionPreset());
        layoutDTO.setStructureConfig(layout.getStructureConfig());

        TemplateMetaDTO templateMeta = new TemplateMetaDTO();
        templateMeta.setTemplateId(template.getId());
        templateMeta.setTemplateName(template.getName());
        templateMeta.setTemplateVersion(resume.getTemplateVersion());
        templateMeta.setPlanLevel(template.getPlanLevel().name());
        templateMeta.setPrimaryMood(template.getPrimaryMood() != null ? template.getPrimaryMood().name() : null);

        List<PublicPortfolioBlockDTO> customBlocks = portfolioBlockRepository
                .findByResumeIdAndEnabledTrueOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(this::mapBlock)
                .collect(Collectors.toList());

        PublicPortfolioResponse response = new PublicPortfolioResponse();
        response.setResumeId(resume.getId());
        response.setTitle(resume.getTitle());
        response.setProfessionType(resume.getProfessionType());
        response.setProfessionCategory(resume.getProfessionCategory());
        response.setSlug(resume.getSlug());
        response.setViewCount(resume.getViewCount());
        response.setTemplateMeta(templateMeta);
        response.setLayout(layoutDTO);
        response.setTheme(resolvedTheme);
        response.setSections(sections);
        response.setSectionTitles(sectionTitles);
        response.setCustomBlocks(customBlocks);

        try {
            response.setProfile(userProfileService.getPublic(resume.getId()));
        } catch (Exception e) {
            response.setProfile(null);
        }

        return response;
    }

    private PublicPortfolioBlockDTO mapBlock(PortfolioBlock block) {
        PublicPortfolioBlockDTO dto = new PublicPortfolioBlockDTO();
        dto.setId(block.getId());
        dto.setBlockType(block.getBlockType() != null ? block.getBlockType().name() : null);
        dto.setTitle(block.getTitle());
        dto.setDisplayOrder(block.getDisplayOrder());
        dto.setStyleVariant(block.getStyleVariant());
        dto.setParentSection(block.getParentSection());
        dto.setPayload(block.getPayload());
        return dto;
    }

    private ResolvedThemeDTO resolveTheme(Resume resume, Theme base) {
        UserThemeCustomization custom = customizationRepository
                .findByUserIdAndResumeId(resume.getUserId(), resume.getId())
                .orElse(null);

        ThemeColorPalette palette = copyPalette(base.getColorPalette());
        ThemeBackground background = base.getBackground();
        ThemeTypography typography = copyTypography(base.getTypography());
        ThemeEffects effects = copyEffects(base.getEffects());

        boolean hasCustomizations = false;
        if (custom != null && base.getId().equals(custom.getBaseThemeId())) {
            hasCustomizations = true;
            if (custom.getPrimaryColor() != null) palette.setPrimary(custom.getPrimaryColor());
            if (custom.getSecondaryColor() != null) palette.setSecondary(custom.getSecondaryColor());
            if (custom.getAccentColor() != null) palette.setAccent(custom.getAccentColor());
            if (custom.getTextPrimaryColor() != null) palette.setTextPrimary(custom.getTextPrimaryColor());
            if (custom.getTextSecondaryColor() != null) palette.setTextSecondary(custom.getTextSecondaryColor());
            if (custom.getPageBackgroundColor() != null) palette.setPageBackground(custom.getPageBackgroundColor());
            if (custom.getCustomGradient() != null) {
                ThemeBackground overrideBg = new ThemeBackground();
                overrideBg.setType(ThemeBackground.BackgroundType.GRADIENT);
                overrideBg.setGradient(custom.getCustomGradient());
                if (base.getBackground() != null) {
                    overrideBg.setTextureOverlay(base.getBackground().getTextureOverlay());
                }
                background = overrideBg;
            }
            if (custom.getHeadingFont() != null) typography.setHeadingFont(custom.getHeadingFont());
            if (custom.getBodyFont() != null) typography.setBodyFont(custom.getBodyFont());
            if (custom.getBaseFontSize() != null) typography.setBaseSize(custom.getBaseFontSize());
            if (custom.getHeadingWeight() != null) typography.setHeadingWeight(custom.getHeadingWeight());
            if (custom.getEnableGrain() != null) effects.setEnableGrain(custom.getEnableGrain());
            if (custom.getGrainIntensity() != null) effects.setGlobalGrainIntensity(custom.getGrainIntensity());
            if (custom.getCardBorderRadius() != null) effects.setCardBorderRadius(custom.getCardBorderRadius());
        }

        ResolvedThemeDTO dto = new ResolvedThemeDTO();
        dto.setThemeId(base.getId());
        dto.setThemeName(base.getName());
        dto.setThemeVersion(resume.getThemeVersion());
        dto.setMood(base.getMood() != null ? base.getMood().name() : null);
        dto.setColorPalette(palette);
        dto.setBackground(background);
        dto.setTypography(typography);
        dto.setEffects(effects);
        dto.setHasCustomizations(hasCustomizations);
        return dto;
    }

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
