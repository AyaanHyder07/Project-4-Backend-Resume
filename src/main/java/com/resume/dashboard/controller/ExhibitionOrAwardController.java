package com.resume.dashboard.controller;

import com.resume.dashboard.dto.exhibition.*;
import com.resume.dashboard.entity.AwardType;
import com.resume.dashboard.service.ExhibitionOrAwardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionOrAwardController {

    private final ExhibitionOrAwardService exhibitionService;

    public ExhibitionOrAwardController(ExhibitionOrAwardService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    /* =========================================================
       CREATE
    ========================================================= */
    @PostMapping
    public ResponseEntity<ExhibitionResponse> create(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreateExhibitionRequest request) {

        return ResponseEntity.ok(
                exhibitionService.create(userId, request)
        );
    }

    /* =========================================================
       UPDATE
    ========================================================= */
    @PutMapping("/{id}")
    public ResponseEntity<ExhibitionResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @Valid @RequestBody UpdateExhibitionRequest request) {

        return ResponseEntity.ok(
                exhibitionService.update(userId, id, request)
        );
    }

    /* =========================================================
       DELETE
    ========================================================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {

        exhibitionService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    /* =========================================================
       GET ALL
    ========================================================= */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<ExhibitionResponse>> getAll(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                exhibitionService.getAll(userId, resumeId)
        );
    }

    /* =========================================================
       FILTER BY TYPE
    ========================================================= */
    @GetMapping("/resume/{resumeId}/type/{type}")
    public ResponseEntity<List<ExhibitionResponse>> getByType(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @PathVariable AwardType type) {

        return ResponseEntity.ok(
                exhibitionService.getByType(userId, resumeId, type)
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

        exhibitionService.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}