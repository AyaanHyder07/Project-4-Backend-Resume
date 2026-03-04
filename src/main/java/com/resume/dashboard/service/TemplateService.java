package com.resume.dashboard.service;

import com.resume.dashboard.dto.template.*;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.LayoutRepository;
import com.resume.dashboard.repository.ThemeRepository;

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

    /*
     * CREATE TEMPLATE
     */
    public TemplateResponse create(CreateTemplateRequest request) {

        if (templateRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Template with this name already exists");
        }

        // Validate layout
        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        // Validate theme
        themeRepository.findById(request.getDefaultThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        Template template = new Template();
        template.setId(UUID.randomUUID().toString());
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setPreviewImageUrl(request.getPreviewImageUrl());
        template.setPlanLevel(request.getPlanLevel());
        template.setActive(true);
        template.setLayoutId(request.getLayoutId());
        template.setDefaultThemeId(request.getDefaultThemeId());
        template.setProfessionTags(request.getProfessionTags());
        template.setSupportedSections(request.getSupportedSections());
        template.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
        template.setVersion(1);
        template.setCreatedAt(Instant.now());
        template.setUpdatedAt(Instant.now());

        return map(templateRepository.save(template));
    }

    /*
     * UPDATE TEMPLATE
     */
    public TemplateResponse update(String id, UpdateTemplateRequest request) {

        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        boolean structuralChange = false;

        if (request.getName() != null)
            template.setName(request.getName());

        if (request.getDescription() != null)
            template.setDescription(request.getDescription());

        if (request.getPreviewImageUrl() != null)
            template.setPreviewImageUrl(request.getPreviewImageUrl());

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

        if (request.getProfessionTags() != null)
            template.setProfessionTags(request.getProfessionTags());

        if (request.getSupportedSections() != null) {
            template.setSupportedSections(request.getSupportedSections());
            structuralChange = true;
        }

        if (request.getActive() != null)
            template.setActive(request.getActive());

        if (request.getFeatured() != null)
            template.setFeatured(request.getFeatured());

        if (structuralChange) {
            template.setVersion(template.getVersion() + 1);
        }

        template.setUpdatedAt(Instant.now());

        return map(templateRepository.save(template));
    }

    /*
     * GET AVAILABLE TEMPLATES FOR USER PLAN
     */
    public List<TemplateResponse> getAvailableTemplates(PlanType userPlan) {

        return templateRepository.findByActiveTrue()
                .stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * GET BY PROFESSION
     */
    public List<TemplateResponse> getByProfession(String profession, PlanType userPlan) {

        return templateRepository
                .findByProfessionTagsContainingAndActiveTrue(profession)
                .stream()
                .filter(t -> userPlan.ordinal() >= t.getPlanLevel().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * MAPPER
     */
    private TemplateResponse map(Template t) {

        TemplateResponse r = new TemplateResponse();
        r.setId(t.getId());
        r.setName(t.getName());
        r.setDescription(t.getDescription());
        r.setPreviewImageUrl(t.getPreviewImageUrl());
        r.setPlanLevel(t.getPlanLevel());
        r.setActive(t.isActive());
        r.setProfessionTags(t.getProfessionTags());
        r.setLayoutId(t.getLayoutId());
        r.setDefaultThemeId(t.getDefaultThemeId());
        r.setSupportedSections(t.getSupportedSections());
        r.setVersion(t.getVersion());
        r.setFeatured(t.isFeatured());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());
        return r;
    }
}