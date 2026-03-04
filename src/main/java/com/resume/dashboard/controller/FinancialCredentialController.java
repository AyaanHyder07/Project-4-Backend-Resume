package com.resume.dashboard.controller;

import com.resume.dashboard.dto.financial.*;
import com.resume.dashboard.service.FinancialCredentialService;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/financial-credentials")
public class FinancialCredentialController {

    private final FinancialCredentialService service;

    public FinancialCredentialController(FinancialCredentialService service) {
        this.service = service;
    }

    /* ================= CREATE ================= */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FinancialCredentialResponse> create(
            @AuthenticationPrincipal String userId,
            @RequestPart("data") @Valid CreateFinancialCredentialRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.ok(
        		service.create(userId, request, file)
        );
    }

    /* ================= UPDATE ================= */

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FinancialCredentialResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @RequestPart("data") @Valid UpdateFinancialCredentialRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.ok(
        		service.update(userId, id, request, file)
        );
    }
    /* ================= DELETE ================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {

        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    /* ================= PRIVATE GET ================= */

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<FinancialCredentialResponse>> getPrivate(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                service.getByResume(userId, resumeId)
        );
    }

    /* ================= PUBLIC GET ================= */

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<List<PublicFinancialCredentialResponse>> getPublic(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                service.getPublic(resumeId)
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