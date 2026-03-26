package com.resume.dashboard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.resume.dashboard.dto.resume.CreateResumeRequest;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.service.ResumeService;

@RestController
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/api/resumes")
    public ResponseEntity<Resume> create(@AuthenticationPrincipal String userId, @RequestBody CreateResumeRequest request) {
        return ResponseEntity.ok(resumeService.createResume(userId, request));
    }

    @GetMapping("/api/resumes/{resumeId}")
    public ResponseEntity<Resume> getById(@AuthenticationPrincipal String userId, @PathVariable String resumeId) {
        return ResponseEntity.ok(resumeService.getByIdForOwner(userId, resumeId));
    }

    @PatchMapping("/api/resumes/{resumeId}/meta")
    public ResponseEntity<Resume> updateMeta(@AuthenticationPrincipal String userId, @PathVariable String resumeId,
                                             @RequestBody UpdateMetaRequest request) {
        return ResponseEntity.ok(resumeService.updateMeta(userId, resumeId, request.getTitle(), request.getProfessionType()));
    }

    @PatchMapping("/api/resumes/{resumeId}/theme")
    public ResponseEntity<Resume> changeTheme(@AuthenticationPrincipal String userId, @PathVariable String resumeId,
                                              @RequestBody ChangeThemeRequest request) {
        return ResponseEntity.ok(resumeService.changeTheme(userId, resumeId, request.getThemeId()));
    }

    @PatchMapping("/api/resumes/{resumeId}/template")
    public ResponseEntity<Resume> changeTemplate(@AuthenticationPrincipal String userId, @PathVariable String resumeId,
                                                 @RequestBody ChangeTemplateRequest request) {
        return ResponseEntity.ok(resumeService.changeTemplate(userId, resumeId, request.getTemplateId()));
    }

    @PostMapping("/api/resumes/{resumeId}/submit")
    public ResponseEntity<Resume> submit(@AuthenticationPrincipal String userId, @PathVariable String resumeId) {
        return ResponseEntity.ok(resumeService.submitForApproval(userId, resumeId));
    }

    @PostMapping("/api/resumes/{resumeId}/publish")
    public ResponseEntity<Resume> publish(@AuthenticationPrincipal String userId, @PathVariable String resumeId) {
        return ResponseEntity.ok(resumeService.publishResume(userId, resumeId));
    }

    @PostMapping("/api/resumes/{resumeId}/unpublish")
    public ResponseEntity<Resume> unpublish(@AuthenticationPrincipal String userId, @PathVariable String resumeId) {
        return ResponseEntity.ok(resumeService.unpublishResume(userId, resumeId));
    }

    @DeleteMapping("/api/resumes/{resumeId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal String userId, @PathVariable String resumeId) {
        resumeService.deleteResume(userId, resumeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/public/resumes/{slug}")
    public ResponseEntity<Resume> getPublicBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(resumeService.getPublicBySlug(slug));
    }

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

    public static class ChangeTemplateRequest {
        private String templateId;
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
    }


    @GetMapping("/api/resumes")
    public ResponseEntity<List<Resume>> getAllByUser(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(resumeService.getAllByUser(userId));
    }
}


