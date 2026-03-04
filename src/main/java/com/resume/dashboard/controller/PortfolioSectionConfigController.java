package com.resume.dashboard.controller;

import com.resume.dashboard.dto.section.*;
import com.resume.dashboard.service.PortfolioSectionConfigService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class PortfolioSectionConfigController {

    private final PortfolioSectionConfigService service;

    public PortfolioSectionConfigController(PortfolioSectionConfigService service) {
        this.service = service;
    }

    /* ================= UPDATE SECTION ================= */

    @PutMapping("/{configId}")
    public ResponseEntity<PortfolioSectionResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String configId,
            @Valid @RequestBody UpdatePortfolioSectionRequest request) {

        return ResponseEntity.ok(
                service.update(userId, configId, request)
        );
    }

    /* ================= GET SECTIONS ================= */

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<PortfolioSectionResponse>> getSections(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                service.getSections(userId, resumeId)
        );
    }

    /* ================= REORDER ================= */

    @PutMapping("/resume/{resumeId}/reorder")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {

        service.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}