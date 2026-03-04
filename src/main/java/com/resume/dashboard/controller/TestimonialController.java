package com.resume.dashboard.controller;

import com.resume.dashboard.dto.testimonial.*;
import com.resume.dashboard.service.TestimonialService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {

    private final TestimonialService service;

    public TestimonialController(TestimonialService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TestimonialResponse> create(
            @AuthenticationPrincipal String userId,
            @RequestBody CreateTestimonialRequest request) {

        return ResponseEntity.ok(service.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestimonialResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @RequestBody UpdateTestimonialRequest request) {

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
    public ResponseEntity<List<TestimonialResponse>> getPrivate(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getByResume(userId, resumeId));
    }

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<List<PublicTestimonialResponse>> getPublic(
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