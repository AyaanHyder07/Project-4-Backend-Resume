package com.resume.dashboard.controller;

import com.resume.dashboard.dto.projectgallery.*;
import com.resume.dashboard.service.ProjectGalleryService;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/project-gallery")
public class ProjectGalleryController {

    private final ProjectGalleryService service;

    public ProjectGalleryController(ProjectGalleryService service) {
        this.service = service;
    }

    /* ================= CREATE ================= */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectGalleryResponse> create(
            @AuthenticationPrincipal String userId,
            @RequestPart("data") CreateProjectGalleryRequest request,
            @RequestPart(value = "mediaFile", required = false) MultipartFile mediaFile,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {

        return ResponseEntity.ok(
                service.create(userId, request, mediaFile, thumbnailFile)
        );
    }

    /* ================= UPDATE ================= */
    @PutMapping(
            value = "/{galleryId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProjectGalleryResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String galleryId,
            @RequestPart("data") UpdateProjectGalleryRequest request,
            @RequestPart(value = "mediaFile", required = false) MultipartFile mediaFile,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {

        return ResponseEntity.ok(
                service.update(userId, galleryId, request, mediaFile, thumbnailFile)
        );
    }
    /* ================= DELETE ================= */

    @DeleteMapping("/{galleryId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String galleryId) {

        service.delete(userId, galleryId);
        return ResponseEntity.noContent().build();
    }

    /* ================= PRIVATE LIST ================= */

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectGalleryResponse>> getPrivate(
            @AuthenticationPrincipal String userId,
            @PathVariable String projectId) {

        return ResponseEntity.ok(
                service.getByProject(userId, projectId)
        );
    }

    /* ================= PUBLIC LIST ================= */

    @GetMapping("/public/{projectId}")
    public ResponseEntity<List<PublicProjectGalleryResponse>> getPublic(
            @PathVariable String projectId) {

        return ResponseEntity.ok(
                service.getPublic(projectId)
        );
    }

    /* ================= REORDER ================= */

    @PutMapping("/project/{projectId}/reorder")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String projectId,
            @RequestBody List<String> orderedIds) {

        service.reorder(userId, projectId, orderedIds);
        return ResponseEntity.ok().build();
    }
}