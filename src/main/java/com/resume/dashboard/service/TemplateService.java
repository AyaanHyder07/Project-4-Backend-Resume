package com.resume.dashboard.service;

import com.resume.dashboard.dto.template.CreateTemplateRequest;
import com.resume.dashboard.dto.template.TemplateResponse;
import com.resume.dashboard.dto.template.UpdateTemplateRequest;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.TemplateDefaultTheme;
import com.resume.dashboard.entity.Theme;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.VisualMood;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.LayoutRepository;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final LayoutRepository layoutRepository;
    private final ThemeRepository themeRepository;

    public TemplateService(TemplateRepository templateRepository,
                           LayoutRepository layoutRepository,
                           ThemeRepository themeRepository) {
        this.templateRepository = templateRepository;
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
    }

    public TemplateResponse create(CreateTemplateRequest request) {
        if (templateRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Template with this name already exists");
        }

        String templateKey = normalizeTemplateKey(request.getTemplateKey(), request.getName());
        if (templateRepository.existsByTemplateKeyIgnoreCase(templateKey)) {
            throw new IllegalStateException("Template with this templateKey already exists");
        }

        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));
        Theme theme = themeRepository.findById(request.getDefaultThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        Template template = new Template();
        template.setId(UUID.randomUUID().toString());
        template.setName(request.getName());
        template.setTemplateKey(templateKey);
        template.setRenderFamily(resolveRenderFamily(request.getRenderFamily(), templateKey));
        template.setVariantKey(resolveVariantKey(request.getVariantKey(), request.getName()));
        template.setProfession(resolveProfession(request.getProfession(), request.getProfessionTypes(), request.getProfessionCategories()));
        template.setDescription(request.getDescription());
        template.setTagline(request.getTagline());
        template.setPreviewImageUrl(request.getPreviewImageUrl());
        template.setPreviewVideoUrl(request.getPreviewVideoUrl());
        template.setPlanLevel(request.getPlanLevel());
        template.setLayoutId(request.getLayoutId());
        template.setDefaultThemeId(request.getDefaultThemeId());
        template.setTargetAudiences(request.getTargetAudiences());
        template.setProfessionTags(request.getProfessionTags());
        template.setProfessionCategories(request.getProfessionCategories());
        template.setProfessionTypes(request.getProfessionTypes());
        template.setDefaultTheme(request.getDefaultTheme() != null ? request.getDefaultTheme() : buildDefaultTheme(theme, request.getDefaultMotionPreset()));
        template.setPrimaryMood(request.getPrimaryMood());
        template.setSupportedContentModes(request.getSupportedContentModes());
        template.setSupportedSections(request.getSupportedSections());
        template.setEnabledSections(request.getEnabledSections() != null ? request.getEnabledSections() : request.getSupportedSections());
        template.setSectionOrder(request.getSectionOrder() != null ? request.getSectionOrder() : request.getSupportedSections());
        template.setRequiredSections(request.getRequiredSections());
        template.setSupportedBlockTypes(request.getSupportedBlockTypes());
        template.setRecommendedBlockTypes(request.getRecommendedBlockTypes());
        template.setAllowedMotionPresets(request.getAllowedMotionPresets());
        template.setDefaultMotionPreset(request.getDefaultMotionPreset());
        template.setNavStyle(request.getNavStyle());
        template.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
        template.setNew(Boolean.TRUE.equals(request.getIsNew()));
        template.setPremiumRank(request.getPremiumRank() != null ? request.getPremiumRank() : 0);
        template.setGloballySelectable(request.getGloballySelectable() == null || request.getGloballySelectable());
        template.setSystemTemplate(Boolean.TRUE.equals(request.getSystemTemplate()));
        template.setEditableByAdmin(request.getEditableByAdmin() == null || request.getEditableByAdmin());
        template.setSupportsPremiumCustomization(request.getSupportsPremiumCustomization() == null || request.getSupportsPremiumCustomization());
        template.setPopularityScore(0);
        template.setActive(true);
        template.setVersion(1);
        template.setCreatedAt(Instant.now());
        template.setUpdatedAt(Instant.now());

        return map(templateRepository.save(template));
    }

    public TemplateResponse update(String id, UpdateTemplateRequest request) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        boolean structuralChange = false;
        if (request.getName() != null) template.setName(request.getName());
        if (request.getTemplateKey() != null) {
            String nextTemplateKey = normalizeTemplateKey(request.getTemplateKey(), template.getName());
            if (!nextTemplateKey.equalsIgnoreCase(template.getTemplateKey())
                    && templateRepository.existsByTemplateKeyIgnoreCase(nextTemplateKey)) {
                throw new IllegalStateException("Template with this templateKey already exists");
            }
            template.setTemplateKey(nextTemplateKey);
            if (template.getRenderFamily() == null || template.getRenderFamily().isBlank()) {
                template.setRenderFamily(resolveRenderFamily(null, nextTemplateKey));
            }
        }
        if (request.getRenderFamily() != null) template.setRenderFamily(resolveRenderFamily(request.getRenderFamily(), template.getTemplateKey()));
        if (request.getVariantKey() != null) template.setVariantKey(resolveVariantKey(request.getVariantKey(), template.getName()));
        if (request.getProfession() != null) template.setProfession(request.getProfession().trim().toUpperCase(Locale.ROOT));
        if (request.getDescription() != null) template.setDescription(request.getDescription());
        if (request.getTagline() != null) template.setTagline(request.getTagline());
        if (request.getPreviewImageUrl() != null) template.setPreviewImageUrl(request.getPreviewImageUrl());
        if (request.getPreviewVideoUrl() != null) template.setPreviewVideoUrl(request.getPreviewVideoUrl());
        if (request.getPlanLevel() != null) template.setPlanLevel(request.getPlanLevel());
        if (request.getLayoutId() != null) {
            layoutRepository.findById(request.getLayoutId())
                    .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));
            template.setLayoutId(request.getLayoutId());
            structuralChange = true;
        }
        if (request.getDefaultThemeId() != null) {
            Theme theme = themeRepository.findById(request.getDefaultThemeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));
            template.setDefaultThemeId(request.getDefaultThemeId());
            if (request.getDefaultTheme() == null) {
                template.setDefaultTheme(buildDefaultTheme(theme, request.getDefaultMotionPreset() != null ? request.getDefaultMotionPreset() : template.getDefaultMotionPreset()));
            }
            structuralChange = true;
        }
        if (request.getTargetAudiences() != null) template.setTargetAudiences(request.getTargetAudiences());
        if (request.getProfessionTags() != null) template.setProfessionTags(request.getProfessionTags());
        if (request.getProfessionCategories() != null) template.setProfessionCategories(request.getProfessionCategories());
        if (request.getProfessionTypes() != null) template.setProfessionTypes(request.getProfessionTypes());
        if (request.getDefaultTheme() != null) template.setDefaultTheme(request.getDefaultTheme());
        if (request.getPrimaryMood() != null) template.setPrimaryMood(request.getPrimaryMood());
        if (request.getSupportedContentModes() != null) template.setSupportedContentModes(request.getSupportedContentModes());
        if (request.getSupportedSections() != null) {
            template.setSupportedSections(request.getSupportedSections());
            structuralChange = true;
        }
        if (request.getEnabledSections() != null) template.setEnabledSections(request.getEnabledSections());
        if (request.getSectionOrder() != null) template.setSectionOrder(request.getSectionOrder());
        if (request.getRequiredSections() != null) template.setRequiredSections(request.getRequiredSections());
        if (request.getSupportedBlockTypes() != null) template.setSupportedBlockTypes(request.getSupportedBlockTypes());
        if (request.getRecommendedBlockTypes() != null) template.setRecommendedBlockTypes(request.getRecommendedBlockTypes());
        if (request.getAllowedMotionPresets() != null) template.setAllowedMotionPresets(request.getAllowedMotionPresets());
        if (request.getDefaultMotionPreset() != null) {
            template.setDefaultMotionPreset(request.getDefaultMotionPreset());
            TemplateDefaultTheme defaultTheme = template.getDefaultTheme();
            if (defaultTheme != null) {
                defaultTheme.setMotionLevel(mapMotionLevel(request.getDefaultMotionPreset()));
            }
        }
        if (request.getNavStyle() != null) template.setNavStyle(request.getNavStyle());
        if (request.getPremiumRank() != null) template.setPremiumRank(request.getPremiumRank());
        if (request.getGloballySelectable() != null) template.setGloballySelectable(request.getGloballySelectable());
        if (request.getSystemTemplate() != null) template.setSystemTemplate(request.getSystemTemplate());
        if (request.getEditableByAdmin() != null) template.setEditableByAdmin(request.getEditableByAdmin());
        if (request.getSupportsPremiumCustomization() != null) template.setSupportsPremiumCustomization(request.getSupportsPremiumCustomization());
        if (request.getActive() != null) template.setActive(request.getActive());
        if (request.getFeatured() != null) template.setFeatured(request.getFeatured());
        if (request.getIsNew() != null) template.setNew(request.getIsNew());
        if (structuralChange) template.setVersion(template.getVersion() + 1);
        template.setUpdatedAt(Instant.now());
        return map(templateRepository.save(template));
    }

    public List<TemplateResponse> getAvailableTemplates(PlanType userPlan) {
        return templateRepository.findByActiveTrue().stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<TemplateResponse> getByProfession(String profession, PlanType userPlan) {
        return templateRepository.findByProfessionTagsContainingAndActiveTrue(profession).stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<TemplateResponse> getByMood(VisualMood mood, PlanType userPlan) {
        return templateRepository.findByPrimaryMoodAndActiveTrue(mood).stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<TemplateResponse> getByProfessionCategory(ProfessionCategory category, PlanType userPlan) {
        return templateRepository.findByActiveTrue().stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .filter(t -> t.getProfessionCategories() != null && t.getProfessionCategories().contains(category))
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<TemplateResponse> getRecommendations(ProfessionType professionType, PlanType userPlan, VisualMood mood) {
        ProfessionCategory category = professionType.getCategory();
        return templateRepository.findByActiveTrue().stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .filter(t -> matchesProfession(t, professionType, category))
                .filter(t -> mood == null || t.getPrimaryMood() == mood)
                .sorted(Comparator
                        .comparing((Template t) -> isExactProfessionMatch(t, professionType)).reversed()
                        .thenComparing(t -> isCategoryMatch(t, category), Comparator.reverseOrder())
                        .thenComparing(Template::isFeatured, Comparator.reverseOrder())
                        .thenComparing(t -> t.getPremiumRank() == null ? 0 : t.getPremiumRank(), Comparator.reverseOrder()))
                .map(this::map)
                .collect(Collectors.toList());
    }

    public void deactivate(String id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        template.setActive(false);
        template.setUpdatedAt(Instant.now());
        templateRepository.save(template);
    }

    public TemplateResponse getActiveById(String id) {
        return map(templateRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + id)));
    }

    private boolean matchesProfession(Template template, ProfessionType professionType, ProfessionCategory category) {
        return isExactProfessionMatch(template, professionType)
                || isCategoryMatch(template, category)
                || template.isGloballySelectable();
    }

    private boolean isExactProfessionMatch(Template template, ProfessionType professionType) {
        return template.getProfessionTypes() != null && template.getProfessionTypes().contains(professionType);
    }

    private boolean isCategoryMatch(Template template, ProfessionCategory category) {
        return template.getProfessionCategories() != null && template.getProfessionCategories().contains(category);
    }

    private TemplateResponse map(Template t) {
        TemplateResponse r = new TemplateResponse();
        r.setId(t.getId());
        r.setName(t.getName());
        r.setTemplateKey(t.getTemplateKey());
        r.setRenderFamily(t.getRenderFamily());
        r.setVariantKey(t.getVariantKey());
        r.setProfession(t.getProfession());
        r.setDescription(t.getDescription());
        r.setTagline(t.getTagline());
        r.setPreviewImageUrl(t.getPreviewImageUrl());
        r.setPreviewVideoUrl(t.getPreviewVideoUrl());
        r.setPlanLevel(t.getPlanLevel());
        r.setLayoutId(t.getLayoutId());
        r.setDefaultThemeId(t.getDefaultThemeId());
        r.setTargetAudiences(t.getTargetAudiences());
        r.setProfessionTags(t.getProfessionTags());
        r.setProfessionCategories(t.getProfessionCategories());
        r.setProfessionTypes(t.getProfessionTypes());
        r.setDefaultTheme(t.getDefaultTheme());
        r.setPrimaryMood(t.getPrimaryMood());
        r.setSupportedContentModes(t.getSupportedContentModes());
        r.setSupportedSections(t.getSupportedSections());
        r.setEnabledSections(t.getEnabledSections());
        r.setSectionOrder(t.getSectionOrder());
        r.setRequiredSections(t.getRequiredSections());
        r.setSupportedBlockTypes(t.getSupportedBlockTypes());
        r.setRecommendedBlockTypes(t.getRecommendedBlockTypes());
        r.setAllowedMotionPresets(t.getAllowedMotionPresets());
        r.setDefaultMotionPreset(t.getDefaultMotionPreset());
        r.setNavStyle(t.getNavStyle());
        r.setActive(t.isActive());
        r.setFeatured(t.isFeatured());
        r.setNew(t.isNew());
        r.setPopularityScore(t.getPopularityScore());
        r.setPremiumRank(t.getPremiumRank());
        r.setGloballySelectable(t.isGloballySelectable());
        r.setSystemTemplate(t.isSystemTemplate());
        r.setEditableByAdmin(t.isEditableByAdmin());
        r.setSupportsPremiumCustomization(t.isSupportsPremiumCustomization());
        r.setVersion(t.getVersion());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());

        try {
            layoutRepository.findById(t.getLayoutId()).ifPresent(layout -> {
                com.resume.dashboard.dto.publicview.LayoutDTO l = new com.resume.dashboard.dto.publicview.LayoutDTO();
                l.setLayoutType(layout.getLayoutType().name());
                l.setLayoutVersion(layout.getVersion());
                l.setStructureConfig(layout.getStructureConfig());
                l.setTargetAudiences(layout.getTargetAudiences());
                l.setCompatibleMoods(layout.getCompatibleMoods());
                l.setProfessionTags(layout.getProfessionTags());
                l.setSupportedProfessionCategories(layout.getSupportedProfessionCategories());
                l.setSupportedProfessionTypes(layout.getSupportedProfessionTypes());
                l.setSupportedContentModes(layout.getSupportedContentModes());
                l.setSupportedMotionPresets(layout.getSupportedMotionPresets());
                l.setRecommendedBlockTypes(layout.getRecommendedBlockTypes());
                l.setDefaultMotionPreset(layout.getDefaultMotionPreset());
                r.setLayout(l);
            });

            themeRepository.findById(t.getDefaultThemeId()).ifPresent(theme -> {
                com.resume.dashboard.dto.publicview.ResolvedThemeDTO th = new com.resume.dashboard.dto.publicview.ResolvedThemeDTO();
                th.setThemeId(theme.getId());
                th.setThemeName(theme.getName());
                th.setColorPalette(theme.getColorPalette());
                th.setBackground(theme.getBackground());
                th.setTypography(theme.getTypography());
                th.setEffects(theme.getEffects());
                r.setTheme(th);
            });
        } catch (Exception ignored) {
        }

        return r;
    }

    private String normalizeTemplateKey(String requestedKey, String fallbackName) {
        String source = requestedKey != null && !requestedKey.isBlank() ? requestedKey : fallbackName;
        return normalizeKey(source);
    }

    private String resolveRenderFamily(String requestedRenderFamily, String templateKey) {
        String source = requestedRenderFamily != null && !requestedRenderFamily.isBlank() ? requestedRenderFamily : templateKey;
        return normalizeKey(source);
    }

    private String resolveVariantKey(String requestedVariantKey, String fallbackName) {
        String source = requestedVariantKey != null && !requestedVariantKey.isBlank() ? requestedVariantKey : fallbackName;
        return source == null ? null : source.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }

    private String normalizeKey(String source) {
        return source == null ? null : source.trim().toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }

    private String resolveProfession(String requestedProfession,
                                     List<ProfessionType> professionTypes,
                                     List<ProfessionCategory> professionCategories) {
        if (requestedProfession != null && !requestedProfession.isBlank()) {
            return requestedProfession.trim().toUpperCase(Locale.ROOT);
        }
        if (professionTypes != null && !professionTypes.isEmpty()) {
            return professionTypes.get(0).name();
        }
        if (professionCategories != null && !professionCategories.isEmpty()) {
            return professionCategories.get(0).name();
        }
        return "GENERAL";
    }

    private TemplateDefaultTheme buildDefaultTheme(Theme theme, MotionPreset defaultMotionPreset) {
        TemplateDefaultTheme value = new TemplateDefaultTheme();
        value.setPrimaryColor(theme.getColorPalette() != null ? theme.getColorPalette().getPrimary() : null);
        value.setAccentColor(theme.getColorPalette() != null ? theme.getColorPalette().getAccent() : null);
        value.setBackgroundColor(theme.getColorPalette() != null ? theme.getColorPalette().getPageBackground() : null);
        value.setTextColor(theme.getColorPalette() != null ? theme.getColorPalette().getTextPrimary() : null);
        value.setFontFamily(theme.getTypography() != null
                ? (theme.getTypography().getBodyFont() != null ? theme.getTypography().getBodyFont() : theme.getTypography().getHeadingFont())
                : null);
        value.setBorderRadius(mapBorderRadius(theme.getEffects()));
        value.setMotionLevel(mapMotionLevel(defaultMotionPreset));
        return value;
    }

    private String mapBorderRadius(ThemeEffects effects) {
        String radius = effects != null ? effects.getCardBorderRadius() : null;
        if (radius == null || radius.isBlank()) return "sm";
        if (radius.contains("0")) return "none";
        if (radius.contains("24") || radius.contains("22") || radius.contains("20")) return "lg";
        if (radius.contains("18") || radius.contains("16") || radius.contains("14")) return "md";
        return "sm";
    }

    private String mapMotionLevel(MotionPreset motionPreset) {
        if (motionPreset == null) return "subtle";
        return switch (motionPreset) {
            case NONE -> "none";
            case SUBTLE, EDITORIAL -> "subtle";
            case PLAYFUL, PARALLAX, SLIDESHOW -> "moderate";
            case CINEMATIC, IMMERSIVE -> "rich";
        };
    }
}
