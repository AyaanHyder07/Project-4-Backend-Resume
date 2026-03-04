package com.resume.dashboard.controller;

import com.resume.dashboard.dto.service.*;
import com.resume.dashboard.service.ServiceOfferingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceOfferingController {

    private final ServiceOfferingService service;

    public ServiceOfferingController(ServiceOfferingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServiceOfferingResponse> create(
            @AuthenticationPrincipal String userId,
            @RequestBody CreateServiceOfferingRequest request) {

        return ResponseEntity.ok(service.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOfferingResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @RequestBody UpdateServiceOfferingRequest request) {

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
    public ResponseEntity<List<ServiceOfferingResponse>> getByResume(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getByResume(userId, resumeId));
    }

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<List<PublicServiceOfferingResponse>> getPublic(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getPublic(resumeId));
    }
}