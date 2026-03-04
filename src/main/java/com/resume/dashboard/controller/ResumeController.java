package com.resume.dashboard.controller;

import com.resume.dashboard.dto.resume.CreateResumeRequest;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    /* =========================================================
       CREATE RESUME (DRAFT)
    ========================================================= */
    @PostMapping
    public ResponseEntity<Resume> createResume(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreateResumeRequest request) {

        return ResponseEntity.ok(
                resumeService.createResume(userId, request)
        );
    }

    /* =========================================================
       UPDATE BASIC META
    ========================================================= */
    @PutMapping("/{resumeId}/meta")
    public ResponseEntity<Resume> updateMeta(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestParam String title,
            @RequestParam String profession) {

        return ResponseEntity.ok(
                resumeService.updateMeta(userId, resumeId, title, profession)
        );
    }

    /* =========================================================
       CHANGE THEME
    ========================================================= */
    @PutMapping("/{resumeId}/theme")
    public ResponseEntity<Resume> changeTheme(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestParam String themeId) {

        return ResponseEntity.ok(
                resumeService.changeTheme(userId, resumeId, themeId)
        );
    }

    /* =========================================================
       SUBMIT FOR APPROVAL
    ========================================================= */
    @PutMapping("/{resumeId}/submit")
    public ResponseEntity<Resume> submit(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                resumeService.submitForApproval(userId, resumeId)
        );
    }

    /* =========================================================
       PUBLISH (Slug assigned here)
    ========================================================= */
    @PutMapping("/{resumeId}/publish")
    public ResponseEntity<Resume> publish(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                resumeService.publishResume(userId, resumeId)
        );
    }

    /* =========================================================
       UNPUBLISH
    ========================================================= */
    @PutMapping("/{resumeId}/unpublish")
    public ResponseEntity<Resume> unpublish(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                resumeService.unpublishResume(userId, resumeId)
        );
    }

    /* =========================================================
       DELETE
    ========================================================= */
    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        resumeService.deleteResume(userId, resumeId);
        return ResponseEntity.noContent().build();
    }

    /* =========================================================
       GET OWNER RESUME
    ========================================================= */
    @GetMapping("/{resumeId}")
    public ResponseEntity<Resume> getOwnerResume(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                resumeService.getByIdForOwner(userId, resumeId)
        );
    }

    /* =========================================================
       PUBLIC FETCH (NO AUTH)
    ========================================================= */
    @GetMapping("/public/{slug}")
    public ResponseEntity<Resume> getPublic(@PathVariable String slug) {

        return ResponseEntity.ok(
                resumeService.getPublicBySlug(slug)
        );
    }
}