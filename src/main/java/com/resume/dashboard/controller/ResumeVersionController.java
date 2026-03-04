package com.resume.dashboard.controller;

import com.resume.dashboard.dto.resumeversion.ResumeVersionResponse;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.ResumeVersion;
import com.resume.dashboard.service.ResumeVersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume-versions")
public class ResumeVersionController {

    private final ResumeVersionService service;

    public ResumeVersionController(ResumeVersionService service) {
        this.service = service;
    }

    @PostMapping("/{resumeId}")
    public ResponseEntity<ResumeVersion> createVersion(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestParam String note) {

        return ResponseEntity.ok(
                service.createVersion(userId, resumeId, note));
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<List<ResumeVersionResponse>> getVersions(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                service.getVersions(userId, resumeId));
    }

    @PostMapping("/revert/{resumeId}")
    public ResponseEntity<Resume> revert(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                service.revertToPrevious(userId, resumeId));
    }
}