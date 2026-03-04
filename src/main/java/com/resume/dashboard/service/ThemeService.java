package com.resume.dashboard.service;

import com.resume.dashboard.dto.theme.*;
import com.resume.dashboard.entity.Theme;
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

    /*
     * CREATE
     */
    public ThemeResponse create(CreateThemeRequest request) {

        if (themeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Theme with this name already exists");
        }

        Theme theme = new Theme();
        theme.setId(UUID.randomUUID().toString());
        theme.setName(request.getName());
        theme.setThemeConfig(request.getThemeConfig());
        theme.setActive(true);
        theme.setVersion(1);
        theme.setCreatedAt(Instant.now());
        theme.setUpdatedAt(Instant.now());

        return map(themeRepository.save(theme));
    }

    /*
     * UPDATE
     */
    public ThemeResponse update(String id, UpdateThemeRequest request) {

        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        boolean configChanged = false;

        if (request.getName() != null) {
            theme.setName(request.getName());
        }

        if (request.getThemeConfig() != null) {
            theme.setThemeConfig(request.getThemeConfig());
            configChanged = true;
        }

        if (request.getActive() != null) {
            theme.setActive(request.getActive());
        }

        if (configChanged) {
            theme.setVersion(theme.getVersion() + 1);
        }

        theme.setUpdatedAt(Instant.now());

        return map(themeRepository.save(theme));
    }

    /*
     * GET ACTIVE BY ID
     */
    public ThemeResponse getActiveById(String id) {

        Theme theme = themeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active theme not found"));

        return map(theme);
    }

    /*
     * GET ALL ACTIVE
     */
    public List<ThemeResponse> getAllActive() {

        return themeRepository.findByActiveTrue()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * SOFT DELETE (Deactivate)
     */
    public void deactivate(String id) {

        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        theme.setActive(false);
        theme.setUpdatedAt(Instant.now());

        themeRepository.save(theme);
    }

    /*
     * MAPPER
     */
    private ThemeResponse map(Theme theme) {

        ThemeResponse response = new ThemeResponse();
        response.setId(theme.getId());
        response.setName(theme.getName());
        response.setThemeConfig(theme.getThemeConfig());
        response.setActive(theme.isActive());
        response.setVersion(theme.getVersion());
        response.setCreatedAt(theme.getCreatedAt());
        response.setUpdatedAt(theme.getUpdatedAt());

        return response;
    }
}