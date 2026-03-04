package com.resume.dashboard.controller;

import com.resume.dashboard.dto.theme.*;
import com.resume.dashboard.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService service;

    public ThemeController(ThemeService service) {
        this.service = service;
    }

    /*
     * CREATE THEME
     */
    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody CreateThemeRequest request) {

        return ResponseEntity.ok(service.create(request));
    }

    /*
     * UPDATE THEME
     */
    @PutMapping("/{id}")
    public ResponseEntity<ThemeResponse> update(
            @PathVariable String id,
            @RequestBody UpdateThemeRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    /*
     * GET ACTIVE THEME BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> getActiveById(
            @PathVariable String id) {

        return ResponseEntity.ok(service.getActiveById(id));
    }

    /*
     * GET ALL ACTIVE THEMES
     */
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllActive() {

        return ResponseEntity.ok(service.getAllActive());
    }

    /*
     * DEACTIVATE
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(
            @PathVariable String id) {

        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}