package com.resume.dashboard.controller;

import com.resume.dashboard.dto.resumeversion.ResumeVersionResponse;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.service.ResumeVersionService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RESUME VERSION CONTROLLER
 *
 * ALL endpoints here are PRO and PREMIUM only.
 * The plan check is enforced inside ResumeVersionService — if a FREE or BASIC
 * user somehow hits these endpoints, the service throws and returns 500/400.
 * You should also add a plan-check middleware/guard at the controller level
 * for a cleaner 403 response.
 *
 *   POST /api/resumes/{resumeId}/versions              → manually save a version snapshot
 *   GET  /api/resumes/{resumeId}/versions              → list all saved versions
 *   POST /api/resumes/{resumeId}/versions/revert       → revert resume to previous version
 */
@RestController
public class ResumeVersionController {

    private final ResumeVersionService resumeVersionService;

    public ResumeVersionController(ResumeVersionService resumeVersionService) {
        this.resumeVersionService = resumeVersionService;
    }

    /* ================================================================
       SAVE VERSION SNAPSHOT (PRO / PREMIUM only)
       Body: { "changeNote": "Updated work experience section" }
    ================================================================ */
    @PostMapping("/api/resumes/{resumeId}/versions")
    public ResponseEntity<ResumeVersionResponse> saveVersion(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody(required = false) SaveVersionRequest request) {

        String note = (request != null && request.getChangeNote() != null)
                ? request.getChangeNote()
                : "Manual snapshot";

        // createVersion enforces plan check internally
        var version = resumeVersionService.createVersion(userId, resumeId, note);

        // Map to response DTO
        ResumeVersionResponse r = new ResumeVersionResponse();
        r.setId(version.getId());
        r.setResumeId(version.getResumeId());
        r.setCurrent(version.isCurrent());
        r.setTemplateId(version.getTemplateId());
        r.setTemplateVersion(version.getTemplateVersion());
        r.setLayoutId(version.getLayoutId());
        r.setLayoutVersion(version.getLayoutVersion());
        r.setThemeId(version.getThemeId());
        r.setThemeVersion(version.getThemeVersion());
        r.setChangeNote(version.getChangeNote());
        r.setCreatedAt(version.getCreatedAt());

        return ResponseEntity.ok(r);
    }

    /* ================================================================
       GET VERSION HISTORY (PRO / PREMIUM only)
    ================================================================ */
    @GetMapping("/api/resumes/{resumeId}/versions")
    public ResponseEntity<List<ResumeVersionResponse>> getVersions(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                resumeVersionService.getVersions(userId, resumeId));
    }

    /* ================================================================
       REVERT TO PREVIOUS VERSION (PRO / PREMIUM only)
    ================================================================ */
    @PostMapping("/api/resumes/{resumeId}/versions/revert")
    public ResponseEntity<Resume> revert(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                resumeVersionService.revertToPrevious(userId, resumeId));
    }

    /* ================================================================
       INLINE REQUEST DTO
    ================================================================ */

    public static class SaveVersionRequest {
        private String changeNote;
        public String getChangeNote() { return changeNote; }
        public void setChangeNote(String changeNote) { this.changeNote = changeNote; }
    }
}