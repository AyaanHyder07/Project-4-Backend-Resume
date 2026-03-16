package com.resume.dashboard.service;

import com.resume.dashboard.dto.template.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.*;

import org.springframework.stereotype.Service;

import java.time.Instant;
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

    // ─── CREATE ─────────────────────────────────────────────────────
    public TemplateResponse create(CreateTemplateRequest request) {

        if (templateRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Template with this name already exists");
        }

        // Validate referenced layout exists
        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        // Validate referenced theme exists
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
        template.setPrimaryMood(request.getPrimaryMood());
        template.setSupportedSections(request.getSupportedSections());
        template.setRequiredSections(request.getRequiredSections());
        template.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
        template.setNew(Boolean.TRUE.equals(request.getIsNew()));
        template.setPopularityScore(0);
        template.setActive(true);
        template.setVersion(1);
        template.setCreatedAt(Instant.now());
        template.setUpdatedAt(Instant.now());

        return map(templateRepository.save(template));
    }

    // ─── UPDATE ─────────────────────────────────────────────────────
    public TemplateResponse update(String id, UpdateTemplateRequest request) {

        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        boolean structuralChange = false;

        if (request.getName() != null)
            template.setName(request.getName());

        if (request.getDescription() != null)
            template.setDescription(request.getDescription());

        if (request.getTagline() != null)
            template.setTagline(request.getTagline());

        if (request.getPreviewImageUrl() != null)
            template.setPreviewImageUrl(request.getPreviewImageUrl());

        if (request.getPreviewVideoUrl() != null)
            template.setPreviewVideoUrl(request.getPreviewVideoUrl());

        if (request.getPlanLevel() != null)
            template.setPlanLevel(request.getPlanLevel());

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

        if (request.getTargetAudiences() != null)
            template.setTargetAudiences(request.getTargetAudiences());

        if (request.getProfessionTags() != null)
            template.setProfessionTags(request.getProfessionTags());

        if (request.getPrimaryMood() != null)
            template.setPrimaryMood(request.getPrimaryMood());

        if (request.getSupportedSections() != null) {
            template.setSupportedSections(request.getSupportedSections());
            structuralChange = true;
        }

        if (request.getRequiredSections() != null)
            template.setRequiredSections(request.getRequiredSections());

        if (request.getActive() != null)
            template.setActive(request.getActive());

        if (request.getFeatured() != null)
            template.setFeatured(request.getFeatured());

        if (request.getIsNew() != null)
            template.setNew(request.getIsNew());

        if (structuralChange)
            template.setVersion(template.getVersion() + 1);

        template.setUpdatedAt(Instant.now());
        return map(templateRepository.save(template));
    }

    // ─── GET AVAILABLE FOR PLAN ──────────────────────────────────────
    public List<TemplateResponse> getAvailableTemplates(PlanType userPlan) {
        return templateRepository.findByActiveTrue()
                .stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ─── GET BY PROFESSION TAG ───────────────────────────────────────
    public List<TemplateResponse> getByProfession(String profession, PlanType userPlan) {
        return templateRepository
                .findByProfessionTagsContainingAndActiveTrue(profession)
                .stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ─── GET BY AUDIENCE ─────────────────────────────────────────────
    public List<TemplateResponse> getByAudience(LayoutAudience audience, PlanType userPlan) {
        return templateRepository
                .findByTargetAudiencesContainingAndActiveTrue(audience)
                .stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ─── GET BY MOOD ─────────────────────────────────────────────────
    public List<TemplateResponse> getByMood(VisualMood mood, PlanType userPlan) {
        return templateRepository
                .findByPrimaryMoodAndActiveTrue(mood)
                .stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ─── SOFT DELETE ─────────────────────────────────────────────────
    public void deactivate(String id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        template.setActive(false);
        template.setUpdatedAt(Instant.now());
        templateRepository.save(template);
    }

    // ─── MAPPER ──────────────────────────────────────────────────────
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
        r.setPrimaryMood(t.getPrimaryMood());
        r.setSupportedSections(t.getSupportedSections());
        r.setRequiredSections(t.getRequiredSections());
        r.setActive(t.isActive());
        r.setFeatured(t.isFeatured());
        r.setNew(t.isNew());
        r.setPopularityScore(t.getPopularityScore());
        r.setVersion(t.getVersion());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());

        // Enrich with Layout and Theme summaries for visual previews
        try {
            layoutRepository.findById(t.getLayoutId()).ifPresent(layout -> {
                com.resume.dashboard.dto.publicview.LayoutDTO l = new com.resume.dashboard.dto.publicview.LayoutDTO();
                l.setLayoutType(layout.getLayoutType().name());
                l.setLayoutVersion(layout.getVersion());
                l.setStructureConfig(layout.getStructureConfig());
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
        } catch (Exception e) {
            // Ignore DB fetch errors during mapping
        }

        return r;
    }
    // ─── GET ACTIVE BY ID ────────────────────────────────────────────
    /**
     * Admin-safe single template lookup by ID.
     * Used by admin GET /api/admin/templates/{id} and user GET /api/templates/{id}.
     * No plan filter — plan check is done in the controller for user endpoint.
     */
    public TemplateResponse getActiveById(String id) {
        return map(templateRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + id)));
    }
}