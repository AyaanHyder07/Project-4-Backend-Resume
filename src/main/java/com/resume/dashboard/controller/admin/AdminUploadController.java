package com.resume.dashboard.controller.admin;

import com.resume.dashboard.dto.upload.UploadResponse;
import com.resume.dashboard.service.CloudinaryService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Single upload endpoint for all preview types.
 * React calls this FIRST before creating a theme/layout/template.
 *
 * Usage flow:
 *   1. Admin designs theme in React live editor
 *   2. React takes screenshot (html2canvas) → sends to POST /api/admin/upload/preview?type=theme
 *   3. Backend uploads to Cloudinary → returns { secureUrl, publicId }
 *   4. React includes secureUrl in CreateThemeRequest.previewImageUrl
 *   5. React POSTs to /api/admin/themes with full request body
 */
@RestController
@RequestMapping("/api/admin/upload")
public class AdminUploadController {

    private final CloudinaryService cloudinaryService;

    public AdminUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    // ─── PREVIEW UPLOAD ──────────────────────────────────────────────
    /**
     * POST /api/admin/upload/preview?type=theme|layout|template
     * Content-Type: multipart/form-data
     * Body: file=<image>
     */
    @PostMapping(value = "/preview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadPreview(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {

        CloudinaryService.CloudinaryUploadResult result = switch (type.toLowerCase()) {
            case "theme"    -> cloudinaryService.uploadThemePreview(file);
            case "layout"   -> cloudinaryService.uploadLayoutPreview(file);
            case "template" -> cloudinaryService.uploadTemplatePreview(file);
            default -> throw new IllegalArgumentException(
                "Invalid type: '" + type + "'. Must be: theme, layout, or template"
            );
        };

        return ResponseEntity.ok(UploadResponse.of(result));
    }

    // ─── TEXTURE UPLOAD ──────────────────────────────────────────────
    /**
     * POST /api/admin/upload/texture
     * Used when admin uploads a custom texture image
     * (paper scan, fabric photo, etc.) for ThemeBackground.TextureOverlay
     */
    @PostMapping(value = "/texture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadTexture(
            @RequestParam("file") MultipartFile file) {

        CloudinaryService.CloudinaryUploadResult result =
                cloudinaryService.uploadTexture(file);

        return ResponseEntity.ok(UploadResponse.of(result));
    }

    // ─── DELETE ASSET ────────────────────────────────────────────────
    /**
     * DELETE /api/admin/upload/{publicId}
     * Called when admin replaces a preview image — cleans up old Cloudinary asset.
     * publicId must be URL-encoded if it contains slashes.
     */
    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String publicId) {
        cloudinaryService.delete(publicId);
        return ResponseEntity.noContent().build();
    }
}