package com.resume.dashboard.service;

import com.resume.dashboard.dto.template.CreateTemplateRequest;
import com.resume.dashboard.dto.template.TemplateResponse;
import com.resume.dashboard.dto.template.UpdateTemplateRequest;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.VisualMood;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.LayoutRepository;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.ThemeRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
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

        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));
        themeRepository.findById(request.getDefaultThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        Template template = new Template();
        template.setId(UUID.randomUUID().toString());
        template.setName(request.getName());
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
        template.setPrimaryMood(request.getPrimaryMood());
        template.setSupportedContentModes(request.getSupportedContentModes());
        template.setSupportedSections(request.getSupportedSections());
        template.setRequiredSections(request.getRequiredSections());
        template.setSupportedBlockTypes(request.getSupportedBlockTypes());
        template.setRecommendedBlockTypes(request.getRecommendedBlockTypes());
        template.setAllowedMotionPresets(request.getAllowedMotionPresets());
        template.setDefaultMotionPreset(request.getDefaultMotionPreset());
        template.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
        template.setNew(Boolean.TRUE.equals(request.getIsNew()));
        template.setPremiumRank(request.getPremiumRank() != null ? request.getPremiumRank() : 0);
        template.setGloballySelectable(request.getGloballySelectable() == null || request.getGloballySelectable());
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
            themeRepository.findById(request.getDefaultThemeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));
            template.setDefaultThemeId(request.getDefaultThemeId());
            structuralChange = true;
        }
        if (request.getTargetAudiences() != null) template.setTargetAudiences(request.getTargetAudiences());
        if (request.getProfessionTags() != null) template.setProfessionTags(request.getProfessionTags());
        if (request.getProfessionCategories() != null) template.setProfessionCategories(request.getProfessionCategories());
        if (request.getProfessionTypes() != null) template.setProfessionTypes(request.getProfessionTypes());
        if (request.getPrimaryMood() != null) template.setPrimaryMood(request.getPrimaryMood());
        if (request.getSupportedContentModes() != null) template.setSupportedContentModes(request.getSupportedContentModes());
        if (request.getSupportedSections() != null) {
            template.setSupportedSections(request.getSupportedSections());
            structuralChange = true;
        }
        if (request.getRequiredSections() != null) template.setRequiredSections(request.getRequiredSections());
        if (request.getSupportedBlockTypes() != null) template.setSupportedBlockTypes(request.getSupportedBlockTypes());
        if (request.getRecommendedBlockTypes() != null) template.setRecommendedBlockTypes(request.getRecommendedBlockTypes());
        if (request.getAllowedMotionPresets() != null) template.setAllowedMotionPresets(request.getAllowedMotionPresets());
        if (request.getDefaultMotionPreset() != null) template.setDefaultMotionPreset(request.getDefaultMotionPreset());
        if (request.getPremiumRank() != null) template.setPremiumRank(request.getPremiumRank());
        if (request.getGloballySelectable() != null) template.setGloballySelectable(request.getGloballySelectable());
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
        r.setPrimaryMood(t.getPrimaryMood());
        r.setSupportedContentModes(t.getSupportedContentModes());
        r.setSupportedSections(t.getSupportedSections());
        r.setRequiredSections(t.getRequiredSections());
        r.setSupportedBlockTypes(t.getSupportedBlockTypes());
        r.setRecommendedBlockTypes(t.getRecommendedBlockTypes());
        r.setAllowedMotionPresets(t.getAllowedMotionPresets());
        r.setDefaultMotionPreset(t.getDefaultMotionPreset());
        r.setActive(t.isActive());
        r.setFeatured(t.isFeatured());
        r.setNew(t.isNew());
        r.setPopularityScore(t.getPopularityScore());
        r.setPremiumRank(t.getPremiumRank());
        r.setGloballySelectable(t.isGloballySelectable());
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
}
