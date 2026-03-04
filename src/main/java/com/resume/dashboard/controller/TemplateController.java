package com.resume.dashboard.controller;

import com.resume.dashboard.dto.template.*;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService service;

    public TemplateController(TemplateService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TemplateResponse> create(
            @RequestBody CreateTemplateRequest request) {

        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateResponse> update(
            @PathVariable String id,
            @RequestBody UpdateTemplateRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/available")
    public ResponseEntity<List<TemplateResponse>> getAvailable(
            @RequestParam PlanType plan) {

        return ResponseEntity.ok(service.getAvailableTemplates(plan));
    }

    @GetMapping("/profession")
    public ResponseEntity<List<TemplateResponse>> getByProfession(
            @RequestParam String profession,
            @RequestParam PlanType plan) {

        return ResponseEntity.ok(service.getByProfession(profession, plan));
    }
}