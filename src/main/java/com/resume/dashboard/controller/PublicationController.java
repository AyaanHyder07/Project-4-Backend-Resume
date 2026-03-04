package com.resume.dashboard.controller;

import com.resume.dashboard.dto.publication.*;
import com.resume.dashboard.service.PublicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationService service;

    public PublicationController(PublicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PublicationResponse> create(
    		@AuthenticationPrincipal String userId,
            @RequestBody CreatePublicationRequest request) {

        return ResponseEntity.ok(service.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicationResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @RequestBody UpdatePublicationRequest request) {

        return ResponseEntity.ok(service.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {

        service.delete(userId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<PublicationResponse>> getByResume(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getByResume(userId, resumeId));
    }

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<List<PublicPublicationResponse>> getPublic(
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