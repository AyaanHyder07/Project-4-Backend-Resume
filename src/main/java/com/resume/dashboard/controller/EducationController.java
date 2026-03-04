package com.resume.dashboard.controller;

import com.resume.dashboard.dto.education.*;
import com.resume.dashboard.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educations")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    /* =========================================================
       CREATE
    ========================================================= */
    @PostMapping
    public ResponseEntity<EducationResponse> create(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreateEducationRequest request) {

        return ResponseEntity.ok(
                educationService.create(userId, request)
        );
    }

    /* =========================================================
       UPDATE
    ========================================================= */
    @PutMapping("/{educationId}")
    public ResponseEntity<EducationResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String educationId,
            @Valid @RequestBody UpdateEducationRequest request) {

        return ResponseEntity.ok(
                educationService.update(userId, educationId, request)
        );
    }

    /* =========================================================
       DELETE
    ========================================================= */
    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String educationId) {

        educationService.delete(userId, educationId);
        return ResponseEntity.noContent().build();
    }

    /* =========================================================
       GET ALL
    ========================================================= */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<EducationResponse>> getAll(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                educationService.getByResume(userId, resumeId)
        );
    }

    /* =========================================================
       REORDER
    ========================================================= */
    @PutMapping("/resume/{resumeId}/reorder")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {

        educationService.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}