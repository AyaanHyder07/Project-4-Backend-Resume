package com.resume.dashboard.controller;

import com.resume.dashboard.dto.experience.*;
import com.resume.dashboard.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    /* ================= CREATE ================= */

    @PostMapping
    public ResponseEntity<ExperienceResponse> create(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreateExperienceRequest request) {

        return ResponseEntity.ok(
                experienceService.create(userId, request)
        );
    }

    /* ================= UPDATE ================= */

    @PutMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String experienceId,
            @Valid @RequestBody UpdateExperienceRequest request) {

        return ResponseEntity.ok(
                experienceService.update(userId, experienceId, request)
        );
    }

    /* ================= DELETE ================= */

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String experienceId) {

        experienceService.delete(userId, experienceId);
        return ResponseEntity.noContent().build();
    }

    /* ================= GET ALL ================= */

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<ExperienceResponse>> getAll(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                experienceService.getByResume(userId, resumeId)
        );
    }

    /* ================= REORDER ================= */

    @PutMapping("/resume/{resumeId}/reorder")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {

        experienceService.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}