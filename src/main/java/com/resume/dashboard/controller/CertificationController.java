package com.resume.dashboard.controller;

import com.resume.dashboard.dto.certification.*;
import com.resume.dashboard.service.CertificationService;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    /* =========================================================
       CREATE CERTIFICATION (OWNER)
    ========================================================= */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificationResponse> create(
            @AuthenticationPrincipal String userId,
            @RequestPart("data") CreateCertificationRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.ok(
                certificationService.create(userId, request, file)
        );
    }

    /* =========================================================
       UPDATE CERTIFICATION (OWNER)
    ========================================================= */
    @PutMapping(
            value = "/{certificationId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CertificationResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String certificationId,
            @RequestPart("data") @Valid UpdateCertificationRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.ok(
                certificationService.update(userId, certificationId, request, file)
        );
    }
    /* =========================================================
       DELETE CERTIFICATION (OWNER)
    ========================================================= */
    @DeleteMapping("/{certificationId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String certificationId) {

        certificationService.delete(userId, certificationId);
        return ResponseEntity.noContent().build();
    }

    /* =========================================================
       GET ALL CERTIFICATIONS (OWNER)
    ========================================================= */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<CertificationResponse>> getByResume(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                certificationService.getByResume(userId, resumeId)
        );
    }

    /* =========================================================
       REORDER CERTIFICATIONS (OWNER)
    ========================================================= */
    @PutMapping("/resume/{resumeId}/reorder")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {

        certificationService.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }
}