package com.resume.dashboard.controller;

import com.resume.dashboard.dto.media.*;
import com.resume.dashboard.service.MediaAppearanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media-appearances")
public class MediaAppearanceController {

    private final MediaAppearanceService service;

    public MediaAppearanceController(MediaAppearanceService service) {
        this.service = service;
    }

    /* ================= CREATE ================= */

    @PostMapping
    public ResponseEntity<MediaAppearanceResponse> create(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreateMediaAppearanceRequest request) {

        return ResponseEntity.ok(
                service.create(userId, request)
        );
    }

    /* ================= UPDATE ================= */

    @PutMapping("/{id}")
    public ResponseEntity<MediaAppearanceResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @Valid @RequestBody UpdateMediaAppearanceRequest request) {

        return ResponseEntity.ok(
                service.update(userId, id, request)
        );
    }

    /* ================= DELETE ================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {

        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    /* ================= PRIVATE LIST ================= */

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<MediaAppearanceResponse>> getPrivate(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                service.getByResume(userId, resumeId)
        );
    }

    /* ================= PUBLIC LIST ================= */

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<List<PublicMediaAppearanceResponse>> getPublic(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                service.getPublic(resumeId)
        );
    }

    /* ================= REORDER ================= */

    @PutMapping("/resume/{resumeId}/reorder")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {

        service.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}