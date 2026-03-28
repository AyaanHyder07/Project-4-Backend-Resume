package com.resume.dashboard.service.publicview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resume.dashboard.dto.publicview.LayoutDTO;
import com.resume.dashboard.dto.publicview.PublicPortfolioBlockDTO;
import com.resume.dashboard.dto.publicview.PublicPortfolioResponse;
import com.resume.dashboard.dto.publicview.PublicThemeDataDTO;
import com.resume.dashboard.dto.publicview.ResolvedThemeDTO;
import com.resume.dashboard.dto.publicview.TemplateMetaDTO;
import com.resume.dashboard.dto.userprofile.PublicUserProfileResponse;
import com.resume.dashboard.entity.AnalyticsEventType;
import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.PortfolioBlock;
import com.resume.dashboard.entity.PortfolioSectionConfig;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.ResolvedTheme;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.TemplateDefaultTheme;
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
import com.resume.dashboard.service.SubscriptionService;
import com.resume.dashboard.service.UserProfileService;
import com.resume.dashboard.service.publicview.section.SectionHandler;
import com.resume.dashboard.service.publicview.section.SectionHandlerRegistry;
import com.resume.dashboard.service.user.AnalyticsService;

@Service
public class PublicPortfolioAggregatorService {

    private static final String FALLBACK_TEMPLATE_KEY = "CLASSICPRO";

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

        Template template = resolveTemplate(resume);
        Layout layout = resolveLayout(resume, template);
        Theme baseTheme = resolveBaseTheme(resume, template);
        PublicUserProfileResponse profile = safePublicProfile(resume.getId());
        ResolvedThemeDTO resolvedTheme = resolveTheme(resume, baseTheme);
        PublicThemeDataDTO themeData = resolveThemeData(resume, template, baseTheme);
        UserThemeCustomization customization = customizationRepository
                .findByUserIdAndResumeId(resume.getUserId(), resume.getId())
                .orElse(null);

        List<PortfolioSectionConfig> enabledSections = sectionConfigRepository
                .findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .filter(PortfolioSectionConfig::isEnabled)
                .collect(Collectors.toList());

        Map<String, Object> normalizedSections = new LinkedHashMap<>();
        Map<String, String> sectionTitles = new LinkedHashMap<>();
        List<String> sectionOrder = new ArrayList<>();

        for (PortfolioSectionConfig config : enabledSections) {
            String normalizedKey = normalizeSectionKey(config.getSectionName());
            if (normalizedKey == null) {
                continue;
            }
            if (config.getCustomTitle() != null && !config.getCustomTitle().isBlank()) {
                sectionTitles.put(normalizedKey, config.getCustomTitle().trim());
            }

            Object data = buildSectionData(config.getSectionName(), resume, profile);
            if (data == null) {
                continue;
            }

            normalizedSections.put(normalizedKey, data);
            sectionOrder.add(normalizedKey);
        }

        boolean templateSupportsContact = supportsContact(template);
        if (!normalizedSections.containsKey("contact") && templateSupportsContact) {
            normalizedSections.put("contact", buildContactSection(resume, profile));
            sectionOrder.add("contact");
        }

        LayoutDTO layoutDTO = layout != null ? buildLayout(layout) : null;
        TemplateMetaDTO templateMeta = buildTemplateMeta(template, resume);
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
        response.setTemplateKey(resolveTemplateKey(resume, template));
        response.setRenderFamily(template != null && template.getRenderFamily() != null ? template.getRenderFamily() : response.getTemplateKey());
        response.setThemeData(themeData);
        response.setSectionOrder(sectionOrder);
        response.setOpenToWork(isOpenToWork(profile));
        response.setTemplateOptions(customization != null ? customization.getTemplateOptions() : null);
        response.setTemplateLabels(customization != null ? customization.getTemplateLabels() : null);
        response.setTemplateMeta(templateMeta);
        response.setLayout(layoutDTO);
        response.setTheme(resolvedTheme);
        response.setSections(normalizedSections);
        response.setSectionTitles(sectionTitles);
        response.setCustomBlocks(customBlocks);
        response.setProfile(profile);
        return response;
    }

    private Object buildSectionData(PortfolioSectionType sectionType, Resume resume, PublicUserProfileResponse profile) {
        if (sectionType == PortfolioSectionType.CONTACT) {
            return buildContactSection(resume, profile);
        }
        SectionHandler handler = registry.getHandler(sectionType);
        if (handler == null) {
            return null;
        }
        return handler.getSectionData(resume);
    }

    private Map<String, Object> buildContactSection(Resume resume, PublicUserProfileResponse profile) {
        Map<String, Object> contactMeta = new LinkedHashMap<>();
        contactMeta.put("email", profile != null ? profile.getEmail() : null);
        contactMeta.put("phone", profile != null ? profile.getPhone() : null);
        contactMeta.put("whatsapp", profile != null ? profile.getWhatsapp() : null);
        contactMeta.put("showContactForm", true);
        contactMeta.put("resumeId", resume.getId());
        return contactMeta;
    }

    private PublicUserProfileResponse safePublicProfile(String resumeId) {
        try {
            return userProfileService.getPublic(resumeId);
        } catch (Exception e) {
            return null;
        }
    }

    private Template resolveTemplate(Resume resume) {
        if (resume.getTemplateId() != null) {
            Template template = templateRepository.findById(resume.getTemplateId()).orElse(null);
            if (template != null && template.isActive()) {
                return template;
            }
        }
        if (resume.getTemplateKey() != null && !resume.getTemplateKey().isBlank()) {
            return templateRepository.findByTemplateKeyIgnoreCaseAndActiveTrue(resume.getTemplateKey()).orElse(null);
        }
        return null;
    }

    private Layout resolveLayout(Resume resume, Template template) {
        String layoutId = resume.getLayoutId() != null ? resume.getLayoutId() : template != null ? template.getLayoutId() : null;
        if (layoutId == null) return null;
        return layoutRepository.findById(layoutId).orElse(null);
    }

    private Theme resolveBaseTheme(Resume resume, Template template) {
        String themeId = resume.getThemeId() != null ? resume.getThemeId() : template != null ? template.getDefaultThemeId() : null;
        if (themeId == null) return null;
        return themeRepository.findById(themeId).orElse(null);
    }

    private TemplateMetaDTO buildTemplateMeta(Template template, Resume resume) {
        if (template == null) return null;
        TemplateMetaDTO templateMeta = new TemplateMetaDTO();
        templateMeta.setTemplateId(template.getId());
        templateMeta.setTemplateName(template.getName());
        templateMeta.setTemplateVersion(resume.getTemplateVersion());
        templateMeta.setPlanLevel(template.getPlanLevel() != null ? template.getPlanLevel().name() : null);
        templateMeta.setPrimaryMood(template.getPrimaryMood() != null ? template.getPrimaryMood().name() : null);
        return templateMeta;
    }

    private LayoutDTO buildLayout(Layout layout) {
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
        return layoutDTO;
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
        ThemeColorPalette palette = base != null ? copyPalette(base.getColorPalette()) : new ThemeColorPalette();
        ThemeBackground background = base != null ? base.getBackground() : null;
        ThemeTypography typography = base != null ? copyTypography(base.getTypography()) : new ThemeTypography();
        ThemeEffects effects = base != null ? copyEffects(base.getEffects()) : new ThemeEffects();

        UserThemeCustomization custom = customizationRepository
                .findByUserIdAndResumeId(resume.getUserId(), resume.getId())
                .orElse(null);

        boolean hasCustomizations = false;
        if (custom != null) {
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
                if (background != null) {
                    overrideBg.setTextureOverlay(background.getTextureOverlay());
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
        dto.setThemeId(base != null ? base.getId() : null);
        dto.setThemeName(base != null ? base.getName() : null);
        dto.setThemeVersion(resume.getThemeVersion());
        dto.setMood(base != null && base.getMood() != null ? base.getMood().name() : null);
        dto.setColorPalette(palette);
        dto.setBackground(background);
        dto.setTypography(typography);
        dto.setEffects(effects);
        dto.setHasCustomizations(hasCustomizations);
        return dto;
    }

    private PublicThemeDataDTO resolveThemeData(Resume resume, Template template, Theme baseTheme) {
        PublicThemeDataDTO data = fromCustomization(baseTheme, resume);
        if (data != null) {
            return withFallbacks(data);
        }

        if (resume.getResolvedTheme() != null) {
            return withFallbacks(fromResolvedTheme(resume.getResolvedTheme()));
        }

        if (template != null && template.getDefaultTheme() != null) {
            return withFallbacks(fromTemplateTheme(template.getDefaultTheme()));
        }

        if (baseTheme != null) {
            return withFallbacks(fromBaseTheme(baseTheme, resume));
        }

        return fallbackTheme();
    }

    private PublicThemeDataDTO fromCustomization(Theme baseTheme, Resume resume) {
        UserThemeCustomization custom = customizationRepository
                .findByUserIdAndResumeId(resume.getUserId(), resume.getId())
                .orElse(null);
        if (custom == null) return null;

        PublicThemeDataDTO data = baseTheme != null ? fromBaseTheme(baseTheme, resume) : fallbackTheme();
        if (custom.getPrimaryColor() != null) data.setPrimaryColor(custom.getPrimaryColor());
        if (custom.getAccentColor() != null) data.setAccentColor(custom.getAccentColor());
        if (custom.getPageBackgroundColor() != null) data.setBackgroundColor(custom.getPageBackgroundColor());
        if (custom.getTextPrimaryColor() != null) data.setTextColor(custom.getTextPrimaryColor());
        if (custom.getBodyFont() != null) data.setFontFamily(custom.getBodyFont());
        return data;
    }

    private PublicThemeDataDTO fromResolvedTheme(ResolvedTheme source) {
        PublicThemeDataDTO data = new PublicThemeDataDTO();
        data.setPrimaryColor(source.getPrimaryColor());
        data.setAccentColor(source.getAccentColor());
        data.setBackgroundColor(source.getBackgroundColor());
        data.setTextColor(source.getTextColor());
        data.setFontFamily(source.getFontFamily());
        data.setMotionLevel(source.getMotionLevel());
        return data;
    }

    private PublicThemeDataDTO fromTemplateTheme(TemplateDefaultTheme source) {
        PublicThemeDataDTO data = new PublicThemeDataDTO();
        data.setPrimaryColor(source.getPrimaryColor());
        data.setAccentColor(source.getAccentColor());
        data.setBackgroundColor(source.getBackgroundColor());
        data.setTextColor(source.getTextColor());
        data.setFontFamily(source.getFontFamily());
        data.setMotionLevel(source.getMotionLevel());
        return data;
    }

    private PublicThemeDataDTO fromBaseTheme(Theme baseTheme, Resume resume) {
        PublicThemeDataDTO data = new PublicThemeDataDTO();
        data.setPrimaryColor(baseTheme.getColorPalette() != null ? baseTheme.getColorPalette().getPrimary() : null);
        data.setAccentColor(baseTheme.getColorPalette() != null ? baseTheme.getColorPalette().getAccent() : null);
        data.setBackgroundColor(baseTheme.getColorPalette() != null ? baseTheme.getColorPalette().getPageBackground() : null);
        data.setTextColor(baseTheme.getColorPalette() != null ? baseTheme.getColorPalette().getTextPrimary() : null);
        data.setFontFamily(baseTheme.getTypography() != null
                ? (baseTheme.getTypography().getBodyFont() != null ? baseTheme.getTypography().getBodyFont() : baseTheme.getTypography().getHeadingFont())
                : null);
        data.setMotionLevel(mapMotionLevel(resume.getMotionPreset()));
        return data;
    }

    private PublicThemeDataDTO withFallbacks(PublicThemeDataDTO data) {
        if (data.getPrimaryColor() == null) data.setPrimaryColor("#0A0A0A");
        if (data.getAccentColor() == null) data.setAccentColor("#00FF88");
        if (data.getBackgroundColor() == null) data.setBackgroundColor("#0A0A0A");
        if (data.getTextColor() == null) data.setTextColor("#E0E0E0");
        if (data.getFontFamily() == null) data.setFontFamily("Inter");
        if (data.getMotionLevel() == null) data.setMotionLevel("subtle");
        return data;
    }

    private PublicThemeDataDTO fallbackTheme() {
        PublicThemeDataDTO data = new PublicThemeDataDTO();
        data.setPrimaryColor("#0A0A0A");
        data.setAccentColor("#00FF88");
        data.setBackgroundColor("#0A0A0A");
        data.setTextColor("#E0E0E0");
        data.setFontFamily("Inter");
        data.setMotionLevel("subtle");
        return data;
    }

    private String resolveTemplateKey(Resume resume, Template template) {
        if (resume.getTemplateKey() != null && !resume.getTemplateKey().isBlank()) {
            return resume.getTemplateKey();
        }
        if (template != null && template.getTemplateKey() != null && !template.getTemplateKey().isBlank()) {
            return template.getTemplateKey();
        }
        return FALLBACK_TEMPLATE_KEY;
    }

    private boolean supportsContact(Template template) {
        if (template == null) return true;
        return (template.getEnabledSections() != null && template.getEnabledSections().contains(PortfolioSectionType.CONTACT.name()))
                || (template.getSupportedSections() != null && template.getSupportedSections().contains(PortfolioSectionType.CONTACT.name()))
                || (template.getRequiredSections() != null && template.getRequiredSections().contains(PortfolioSectionType.CONTACT.name()));
    }

    private boolean isOpenToWork(PublicUserProfileResponse profile) {
        if (profile == null || profile.getAvailabilityStatus() == null) return false;
        String normalized = profile.getAvailabilityStatus().toUpperCase(Locale.ROOT);
        return normalized.contains("OPEN") || normalized.contains("AVAILABLE");
    }

    private String normalizeSectionKey(PortfolioSectionType type) {
        return switch (type) {
            case EXPERIENCE -> "experience";
            case EDUCATION -> "education";
            case SKILLS -> "skills";
            case PROJECTS -> "projects";
            case CERTIFICATIONS -> "certifications";
            case PUBLICATIONS -> "publications";
            case TESTIMONIALS -> "testimonials";
            case SERVICE_OFFERINGS -> "services";
            case BLOG_POSTS -> "blogPosts";
            case EXHIBITIONS_AWARDS -> "exhibitions";
            case CONTACT -> "contact";
            case FINANCIAL_CREDENTIALS -> "financialCredentials";
            case MEDIA_APPEARANCES -> "mediaAppearances";
            case PROJECT_GALLERY, PROFILE, ANALYTICS -> null;
        };
    }

    private String mapMotionLevel(com.resume.dashboard.entity.MotionPreset motionPreset) {
        if (motionPreset == null) return "subtle";
        return switch (motionPreset) {
            case NONE -> "none";
            case SUBTLE, EDITORIAL -> "subtle";
            case PLAYFUL, PARALLAX, SLIDESHOW -> "moderate";
            case CINEMATIC, IMMERSIVE -> "rich";
        };
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



