package com.resume.dashboard.controller;

import com.resume.dashboard.dto.project.*;
import com.resume.dashboard.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(
    		@AuthenticationPrincipal String userId,
            @RequestBody CreateProjectRequest request) {

        return ResponseEntity.ok(service.create(userId, request));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> update(
    		@AuthenticationPrincipal String userId,
            @PathVariable String projectId,
            @RequestBody UpdateProjectRequest request) {

        return ResponseEntity.ok(service.update(userId, projectId, request));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(
    		@AuthenticationPrincipal String userId,
            @PathVariable String projectId) {

        service.delete(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<ProjectResponse>> getByResume(
    		@AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getByResume(userId, resumeId));
    }

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<List<PublicProjectResponse>> getPublic(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getPublic(resumeId));
    }

    @PostMapping("/reorder/{resumeId}")
    public ResponseEntity<Void> reorder(
    		@AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {

        service.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}