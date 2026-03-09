package com.resume.dashboard.service;

import com.resume.dashboard.dto.layout.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.LayoutRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LayoutService {

    private final LayoutRepository layoutRepository;

    public LayoutService(LayoutRepository layoutRepository) {
        this.layoutRepository = layoutRepository;
    }

    // ─── CREATE ─────────────────────────────────────────────────────
    public LayoutResponse create(CreateLayoutRequest request) {

        Layout layout = new Layout();
        layout.setId(UUID.randomUUID().toString());
        layout.setName(request.getName());
        layout.setDescription(request.getDescription());
        layout.setLayoutType(request.getLayoutType());
        layout.setTargetAudiences(request.getTargetAudiences());
        layout.setCompatibleMoods(request.getCompatibleMoods());
        layout.setProfessionTags(request.getProfessionTags());
        layout.setStructureConfig(request.getStructureConfig());
        layout.setRequiredPlan(
            request.getRequiredPlan() != null ? request.getRequiredPlan() : PlanType.FREE
        );
        layout.setPreviewImageUrl(request.getPreviewImageUrl());
        layout.setActive(true);
        layout.setVersion(1);
        layout.setCreatedAt(Instant.now());
        layout.setUpdatedAt(Instant.now());

        return map(layoutRepository.save(layout));
    }

    // ─── UPDATE ─────────────────────────────────────────────────────
    public LayoutResponse update(String id, UpdateLayoutRequest request) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        boolean structuralChange = false;

        if (request.getName() != null)
            layout.setName(request.getName());

        if (request.getDescription() != null)
            layout.setDescription(request.getDescription());

        if (request.getLayoutType() != null)
            layout.setLayoutType(request.getLayoutType());

        if (request.getTargetAudiences() != null)
            layout.setTargetAudiences(request.getTargetAudiences());

        if (request.getCompatibleMoods() != null)
            layout.setCompatibleMoods(request.getCompatibleMoods());

        if (request.getProfessionTags() != null)
            layout.setProfessionTags(request.getProfessionTags());

        if (request.getStructureConfig() != null) {
            layout.setStructureConfig(request.getStructureConfig());
            structuralChange = true;
        }

        if (request.getRequiredPlan() != null)
            layout.setRequiredPlan(request.getRequiredPlan());

        if (request.getPreviewImageUrl() != null)
            layout.setPreviewImageUrl(request.getPreviewImageUrl());

        if (request.getActive() != null)
            layout.setActive(request.getActive());

        if (structuralChange)
            layout.setVersion(layout.getVersion() + 1);

        layout.setUpdatedAt(Instant.now());
        return map(layoutRepository.save(layout));
    }

    // ─── GET ACTIVE BY ID ────────────────────────────────────────────
    public LayoutResponse getActiveById(String id) {
        return map(layoutRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active layout not found")));
    }

    // ─── GET ALL ACTIVE ──────────────────────────────────────────────
    public List<LayoutResponse> getAllActive() {
        return layoutRepository.findByActiveTrue()
                .stream().map(this::map).collect(Collectors.toList());
    }

    // ─── GET BY TYPE ─────────────────────────────────────────────────
    public List<LayoutResponse> getByType(LayoutType type) {
        return layoutRepository.findByLayoutTypeAndActiveTrue(type)
                .stream().map(this::map).collect(Collectors.toList());
    }

    // ─── GET BY AUDIENCE ─────────────────────────────────────────────
    public List<LayoutResponse> getByAudience(LayoutAudience audience) {
        return layoutRepository.findByTargetAudiencesContainingAndActiveTrue(audience)
                .stream().map(this::map).collect(Collectors.toList());
    }

    // ─── GET BY MOOD ─────────────────────────────────────────────────
    public List<LayoutResponse> getByMood(VisualMood mood) {
        return layoutRepository.findByCompatibleMoodsContainingAndActiveTrue(mood)
                .stream().map(this::map).collect(Collectors.toList());
    }

    // ─── GET AVAILABLE FOR PLAN ──────────────────────────────────────
    public List<LayoutResponse> getAvailableForPlan(PlanType userPlan) {
        return layoutRepository.findByActiveTrue()
                .stream()
                .filter(l -> userPlan.ordinal() >= l.getRequiredPlan().ordinal())
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ─── SOFT DELETE ─────────────────────────────────────────────────
    public void deactivate(String id) {
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));
        layout.setActive(false);
        layout.setUpdatedAt(Instant.now());
        layoutRepository.save(layout);
    }

    // ─── MAPPER ──────────────────────────────────────────────────────
    private LayoutResponse map(Layout l) {
        LayoutResponse r = new LayoutResponse();
        r.setId(l.getId());
        r.setName(l.getName());
        r.setDescription(l.getDescription());
        r.setLayoutType(l.getLayoutType());
        r.setTargetAudiences(l.getTargetAudiences());
        r.setCompatibleMoods(l.getCompatibleMoods());
        r.setProfessionTags(l.getProfessionTags());
        r.setStructureConfig(l.getStructureConfig());
        r.setRequiredPlan(l.getRequiredPlan());
        r.setPreviewImageUrl(l.getPreviewImageUrl());
        r.setActive(l.isActive());
        r.setVersion(l.getVersion());
        r.setCreatedAt(l.getCreatedAt());
        r.setUpdatedAt(l.getUpdatedAt());
        return r;
    }
}