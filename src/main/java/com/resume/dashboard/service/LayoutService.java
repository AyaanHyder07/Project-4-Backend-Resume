package com.resume.dashboard.service;

import com.resume.dashboard.dto.layout.*;
import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.LayoutType;
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

    /*
     * CREATE
     */
    public LayoutResponse create(CreateLayoutRequest request) {

        Layout layout = new Layout();
        layout.setId(UUID.randomUUID().toString());
        layout.setName(request.getName());
        layout.setLayoutType(request.getLayoutType());
        layout.setLayoutConfigJson(request.getLayoutConfigJson());
        layout.setActive(true);
        layout.setVersion(1);
        layout.setCreatedAt(Instant.now());
        layout.setUpdatedAt(Instant.now());

        return map(layoutRepository.save(layout));
    }

    /*
     * UPDATE
     */
    public LayoutResponse update(String id, UpdateLayoutRequest request) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        boolean configChanged = false;

        if (request.getName() != null)
            layout.setName(request.getName());

        if (request.getLayoutType() != null)
            layout.setLayoutType(request.getLayoutType());

        if (request.getLayoutConfigJson() != null) {
            layout.setLayoutConfigJson(request.getLayoutConfigJson());
            configChanged = true;
        }

        if (request.getActive() != null)
            layout.setActive(request.getActive());

        if (configChanged) {
            layout.setVersion(layout.getVersion() + 1);
        }

        layout.setUpdatedAt(Instant.now());

        return map(layoutRepository.save(layout));
    }

    /*
     * GET BY ID (only active allowed for public use)
     */
    public LayoutResponse getActiveById(String id) {

        Layout layout = layoutRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active layout not found"));

        return map(layout);
    }

    /*
     * GET ALL ACTIVE
     */
    public List<LayoutResponse> getAllActive() {

        return layoutRepository.findByActiveTrue()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * GET BY TYPE
     */
    public List<LayoutResponse> getByType(LayoutType type) {

        return layoutRepository.findByLayoutTypeAndActiveTrue(type)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * SOFT DELETE (Deactivate)
     */
    public void deactivate(String id) {

        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        layout.setActive(false);
        layout.setUpdatedAt(Instant.now());

        layoutRepository.save(layout);
    }

    /*
     * MAPPER
     */
    private LayoutResponse map(Layout layout) {

        LayoutResponse response = new LayoutResponse();
        response.setId(layout.getId());
        response.setName(layout.getName());
        response.setLayoutType(layout.getLayoutType());
        response.setLayoutConfigJson(layout.getLayoutConfigJson());
        response.setActive(layout.isActive());
        response.setVersion(layout.getVersion());
        response.setCreatedAt(layout.getCreatedAt());
        response.setUpdatedAt(layout.getUpdatedAt());

        return response;
    }
}