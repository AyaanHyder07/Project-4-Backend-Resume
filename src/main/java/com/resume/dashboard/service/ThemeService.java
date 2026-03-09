package com.resume.dashboard.service;

import com.resume.dashboard.dto.theme.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ThemeRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    // ─── CREATE ─────────────────────────────────────────────────────
    public ThemeResponse create(CreateThemeRequest request) {

        if (themeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Theme with this name already exists");
        }

        Theme theme = new Theme();
        theme.setId(UUID.randomUUID().toString());
        theme.setName(request.getName());
        theme.setDescription(request.getDescription());
        theme.setColorPalette(request.getColorPalette());
        theme.setBackground(request.getBackground());
        theme.setTypography(request.getTypography());
        theme.setEffects(request.getEffects());
        theme.setMood(request.getMood());
        theme.setTargetAudiences(request.getTargetAudiences());
        theme.setRequiredPlan(
            request.getRequiredPlan() != null ? request.getRequiredPlan() : PlanType.FREE
        );
        theme.setPreviewImageUrl(request.getPreviewImageUrl());
        theme.setPreviewVideoUrl(request.getPreviewVideoUrl());
        theme.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
        theme.setActive(true);
        theme.setVersion(1);
        theme.setCreatedAt(Instant.now());
        theme.setUpdatedAt(Instant.now());

        return map(themeRepository.save(theme));
    }

    // ─── UPDATE ─────────────────────────────────────────────────────
    public ThemeResponse update(String id, UpdateThemeRequest request) {

        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        boolean structuralChange = false;

        if (request.getName() != null)
            theme.setName(request.getName());

        if (request.getDescription() != null)
            theme.setDescription(request.getDescription());

        if (request.getColorPalette() != null) {
            theme.setColorPalette(request.getColorPalette());
            structuralChange = true;
        }

        if (request.getBackground() != null) {
            theme.setBackground(request.getBackground());
            structuralChange = true;
        }

        if (request.getTypography() != null) {
            theme.setTypography(request.getTypography());
            structuralChange = true;
        }

        if (request.getEffects() != null) {
            theme.setEffects(request.getEffects());
            structuralChange = true;
        }

        if (request.getMood() != null)
            theme.setMood(request.getMood());

        if (request.getTargetAudiences() != null)
            theme.setTargetAudiences(request.getTargetAudiences());

        if (request.getRequiredPlan() != null)
            theme.setRequiredPlan(request.getRequiredPlan());

        if (request.getPreviewImageUrl() != null)
            theme.setPreviewImageUrl(request.getPreviewImageUrl());

        if (request.getPreviewVideoUrl() != null)
            theme.setPreviewVideoUrl(request.getPreviewVideoUrl());

        if (request.getActive() != null)
            theme.setActive(request.getActive());

        if (request.getFeatured() != null)
            theme.setFeatured(request.getFeatured());

        if (structuralChange)
            theme.setVersion(theme.getVersion() + 1);

        theme.setUpdatedAt(Instant.now());
        return map(themeRepository.save(theme));
    }

    // ─── GET ACTIVE BY ID ────────────────────────────────────────────
    public ThemeResponse getActiveById(String id) {
        return map(themeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active theme not found")));
    }

    // ─── GET ALL ACTIVE ──────────────────────────────────────────────
    public List<ThemeResponse> getAllActive() {
        return themeRepository.findByActiveTrue()
                .stream().map(this::map).collect(Collectors.toList());
    }

    // ─── GET BY AUDIENCE ─────────────────────────────────────────────
    public List<ThemeResponse> getByAudience(LayoutAudience audience) {
        return themeRepository.findByTargetAudiencesContainingAndActiveTrue(audience)
                .stream().map(this::map).collect(Collectors.toList());
    }

    // ─── GET BY MOOD ─────────────────────────────────────────────────
    public List<ThemeResponse> getByMood(VisualMood mood) {
        return themeRepository.findByMoodAndActiveTrue(mood)
                .stream().map(this::map).collect(Collectors.toList());
    }

    // ─── GET AVAILABLE FOR PLAN ──────────────────────────────────────
    public List<ThemeResponse> getAvailableForPlan(PlanType userPlan) {
        return themeRepository.findByActiveTrue()
                .stream()
                .filter(t -> {
                    PlanType themePlan = t.getRequiredPlan() != null ? t.getRequiredPlan() : PlanType.FREE;
                    return userPlan.ordinal() >= themePlan.ordinal();
                })
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ─── SOFT DELETE ─────────────────────────────────────────────────
    public void deactivate(String id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));
        theme.setActive(false);
        theme.setUpdatedAt(Instant.now());
        themeRepository.save(theme);
    }

    // ─── MAPPER ──────────────────────────────────────────────────────
    private ThemeResponse map(Theme t) {
        ThemeResponse r = new ThemeResponse();
        r.setId(t.getId());
        r.setName(t.getName());
        r.setDescription(t.getDescription());
        r.setColorPalette(t.getColorPalette());
        r.setBackground(t.getBackground());
        r.setTypography(t.getTypography());
        r.setEffects(t.getEffects());
        r.setMood(t.getMood());
        r.setTargetAudiences(t.getTargetAudiences());
        r.setRequiredPlan(t.getRequiredPlan());
        r.setPreviewImageUrl(t.getPreviewImageUrl());
        r.setPreviewVideoUrl(t.getPreviewVideoUrl());
        r.setActive(t.isActive());
        r.setFeatured(t.isFeatured());
        r.setVersion(t.getVersion());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());
        return r;
    }
}