package com.resume.dashboard.controller;

import com.resume.dashboard.dto.skill.*;
import com.resume.dashboard.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService service;

    public SkillController(SkillService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> create(
            @AuthenticationPrincipal String userId,
            @RequestBody CreateSkillRequest request) {

        return ResponseEntity.ok(service.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @RequestBody UpdateSkillRequest request) {

        return ResponseEntity.ok(service.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {

        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<SkillResponse>> getByResume(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getByResume(userId, resumeId));
    }

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<List<PublicSkillResponse>> getPublic(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getPublic(resumeId));
    }

    @PutMapping("/reorder/{resumeId}")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {

        service.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}