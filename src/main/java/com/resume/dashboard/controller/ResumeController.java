package com.resume.dashboard.controller;

import com.resume.dashboard.dto.resume.CreateResumeRequest;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.service.ResumeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * RESUME CONTROLLER
 *
 * Owner endpoints (auth required, userId injected from JWT filter):
 *   POST   /api/resumes                          → create resume (plan-gated: limit + template)
 *   GET    /api/resumes/{resumeId}               → get own resume by id
 *   PATCH  /api/resumes/{resumeId}/meta          → update title / profession
 *   PATCH  /api/resumes/{resumeId}/theme         → change theme
 *   POST   /api/resumes/{resumeId}/submit        → submit for admin approval
 *   POST   /api/resumes/{resumeId}/publish       → publish (plan-gated: public link limit)
 *   POST   /api/resumes/{resumeId}/unpublish     → unpublish
 *   DELETE /api/resumes/{resumeId}               → delete
 *
 * Public endpoint (no auth):
 *   GET    /api/public/resumes/{slug}            → view public published resume by slug
 *
 * Admin endpoint:
 *   POST   /api/admin/resumes/{resumeId}/approve → approve a submitted resume
 */
@RestController
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    /* ================================================================
       CREATE RESUME
       Plan checks inside service: resume limit + template gating
    ================================================================ */
    @PostMapping("/api/resumes")
    public ResponseEntity<Resume> create(
            @AuthenticationPrincipal String userId,
            @RequestBody CreateResumeRequest request) {

        return ResponseEntity.ok(resumeService.createResume(userId, request));
    }

    /* ================================================================
       GET OWN RESUME
    ================================================================ */
    @GetMapping("/api/resumes/{resumeId}")
    public ResponseEntity<Resume> getById(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(resumeService.getByIdForOwner(userId, resumeId));
    }

    /* ================================================================
       UPDATE TITLE / PROFESSION
    ================================================================ */
    @PatchMapping("/api/resumes/{resumeId}/meta")
    public ResponseEntity<Resume> updateMeta(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody UpdateMetaRequest request) {

        return ResponseEntity.ok(
                resumeService.updateMeta(userId, resumeId,
                        request.getTitle(), request.getProfessionType()));
    }

    /* ================================================================
       CHANGE THEME
       No plan check needed here — changing to a different base theme
       is allowed on all plans. Theme customization (colors/fonts)
       is gated separately in UserThemeCustomizationController.
    ================================================================ */
    @PatchMapping("/api/resumes/{resumeId}/theme")
    public ResponseEntity<Resume> changeTheme(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody ChangeThemeRequest request) {

        return ResponseEntity.ok(
                resumeService.changeTheme(userId, resumeId, request.getThemeId()));
    }

    /* ================================================================
       SUBMIT FOR APPROVAL
    ================================================================ */
    @PostMapping("/api/resumes/{resumeId}/submit")
    public ResponseEntity<Resume> submitForApproval(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(resumeService.submitForApproval(userId, resumeId));
    }

    /* ================================================================
       PUBLISH RESUME
       Plan checks inside service: subscription active + public link limit
    ================================================================ */
    @PostMapping("/api/resumes/{resumeId}/publish")
    public ResponseEntity<Resume> publish(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(resumeService.publishResume(userId, resumeId));
    }

    /* ================================================================
       UNPUBLISH RESUME
    ================================================================ */
    @PostMapping("/api/resumes/{resumeId}/unpublish")
    public ResponseEntity<Resume> unpublish(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(resumeService.unpublishResume(userId, resumeId));
    }

    /* ================================================================
       DELETE RESUME
    ================================================================ */
    @DeleteMapping("/api/resumes/{resumeId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        resumeService.deleteResume(userId, resumeId);
        return ResponseEntity.noContent().build();
    }

    /* ================================================================
       PUBLIC — VIEW BY SLUG (no auth)
    ================================================================ */
    @GetMapping("/api/public/resumes/{slug}")
    public ResponseEntity<Resume> getPublicBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(resumeService.getPublicBySlug(slug));
    }

    /* ================================================================
       ADMIN — APPROVE RESUME
       Approval is an admin-only action, handled in AdminResumeController.
       Kept here as a stub comment for clarity — implement in admin layer.
    ================================================================ */

    /* ================================================================
       INLINE REQUEST DTOs
    ================================================================ */

    public static class UpdateMetaRequest {
        private String title;
        private String professionType;
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getProfessionType() { return professionType; }
        public void setProfessionType(String professionType) { this.professionType = professionType; }
    }

    public static class ChangeThemeRequest {
        private String themeId;
        public String getThemeId() { return themeId; }
        public void setThemeId(String themeId) { this.themeId = themeId; }
    }
}