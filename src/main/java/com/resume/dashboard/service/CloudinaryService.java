package com.resume.dashboard.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.resume.dashboard.exception.FileUploadException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * CloudinaryService — FINAL MERGED VERSION
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * BACKWARD-COMPATIBLE: All existing callers keep working with ZERO changes:
 *
 *   BlogPostService       → cloudinaryService.uploadImage(file, "resume/blogs/" + id)   ✅
 *   FinancialCre...       → cloudinaryService.uploadImage(file, folder)                  ✅
 *   Any video caller      → cloudinaryService.uploadVideo(file, folder)                  ✅
 *   Any delete caller     → cloudinaryService.delete(publicId)                           ✅
 *
 * NEW TYPED METHODS (for theme/layout/template preview upload flow):
 *
 *   AdminUploadController → cloudinaryService.uploadThemePreview(file)                   ✅
 *   AdminUploadController → cloudinaryService.uploadLayoutPreview(file)                  ✅
 *   AdminUploadController → cloudinaryService.uploadTemplatePreview(file)                ✅
 *   AdminUploadController → cloudinaryService.uploadTexture(file)                        ✅
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // ─── Cloudinary folder constants ─────────────────────────────────
    // Use these wherever you pass folder strings to avoid typos
    public static final String FOLDER_THEMES     = "resume-builder/previews/themes";
    public static final String FOLDER_LAYOUTS    = "resume-builder/previews/layouts";
    public static final String FOLDER_TEMPLATES  = "resume-builder/previews/templates";
    public static final String FOLDER_TEXTURES   = "resume-builder/textures";
    public static final String FOLDER_AVATARS    = "resume-builder/avatars";
    public static final String FOLDER_BLOGS      = "resume-builder/blogs";

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // ═════════════════════════════════════════════════════════════════
    // ── BACKWARD-COMPATIBLE METHODS ──────────────────────────────────
    // These are the ORIGINAL method signatures.
    // Every existing caller (BlogPostService, FinancialCre..., etc.)
    // continues to call these with ZERO code changes.
    // ═════════════════════════════════════════════════════════════════

    /**
     * Original method — kept exactly as before.
     * All existing callers use: cloudinaryService.uploadImage(file, folder)
     *
     * Internally now routes through the optimized upload pipeline
     * (WebP, quality auto, 1600px cap) instead of bare upload.
     *
     * Return type: String (secure URL) — unchanged.
     */
    public String uploadImage(MultipartFile file, String folder) {
        return uploadOptimized(file, folder, "img_" + shortId()).secureUrl();
    }

    /**
     * Original method — kept exactly as before.
     * All existing video callers use: cloudinaryService.uploadVideo(file, folder)
     *
     * Return type: String (secure URL) — unchanged.
     */
    public String uploadVideo(MultipartFile file, String folder) {
        validateFile(file, false); // false = don't restrict to image types
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder",        folder,
                    "public_id",     "vid_" + shortId(),
                    "resource_type", "video",
                    "quality",       "auto"
                )
            );
            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new FileUploadException("Video upload failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new FileUploadException("Video upload failed: " + e.getMessage(), e);
        }
    }

    /**
     * Original method — kept exactly as before.
     * Callers use: cloudinaryService.delete(publicId)
     *
     * Non-throwing — logs on failure but does not crash the calling flow.
     * Previously this threw RuntimeException on failure which could break
     * update flows if Cloudinary was temporarily unavailable.
     */
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            // Log but never throw — deletion failure must not break business logic
            System.err.println("[CloudinaryService] delete() failed for publicId="
                + publicId + " — " + e.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════
    // ── NEW TYPED METHODS ────────────────────────────────────────────
    // Used by AdminUploadController for theme/layout/template preview flow.
    // Return CloudinaryUploadResult (includes publicId for future deletion).
    // ═════════════════════════════════════════════════════════════════

    /**
     * React takes a screenshot of the theme preview panel → sends to backend →
     * this uploads it to Cloudinary → returns URL + publicId.
     * URL is then included in CreateThemeRequest.previewImageUrl.
     */
    public CloudinaryUploadResult uploadThemePreview(MultipartFile file) {
        return uploadOptimized(file, FOLDER_THEMES, "theme_" + shortId());
    }

    /**
     * Same flow for layout previews.
     */
    public CloudinaryUploadResult uploadLayoutPreview(MultipartFile file) {
        return uploadOptimized(file, FOLDER_LAYOUTS, "layout_" + shortId());
    }

    /**
     * Same flow for template previews.
     */
    public CloudinaryUploadResult uploadTemplatePreview(MultipartFile file) {
        return uploadOptimized(file, FOLDER_TEMPLATES, "template_" + shortId());
    }

    /**
     * Admin uploads a custom texture/pattern image (paper scan, fabric photo, etc.).
     * The returned URL is stored in ThemeBackground.TextureOverlay.customPatternUrl.
     */
    public CloudinaryUploadResult uploadTexture(MultipartFile file) {
        return uploadOptimized(file, FOLDER_TEXTURES, "texture_" + shortId());
    }

    /**
     * User avatar / profile photo upload.
     */
    public CloudinaryUploadResult uploadAvatar(MultipartFile file) {
        return uploadOptimized(file, FOLDER_AVATARS, "avatar_" + shortId());
    }

    // ═════════════════════════════════════════════════════════════════
    // ── CORE OPTIMIZED UPLOAD (private — all image uploads go here) ──
    // ═════════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    private CloudinaryUploadResult uploadOptimized(
            MultipartFile file,
            String folder,
            String publicId) {

        validateFile(file, true); // true = images only

        try {
            Map<String, Object> params = ObjectUtils.asMap(
                "folder",         folder,
                "public_id",      publicId,
                "overwrite",      true,
                "resource_type",  "image",
                "fetch_format",   "auto",       // serve WebP to browsers that support it
                "quality",        "auto:good",  // Cloudinary auto quality optimization
                "transformation", "w_1600,c_limit,q_auto" // cap max width at 1600px
            );

            Map<String, Object> result = cloudinary.uploader()
                    .upload(file.getBytes(), params);

            // bytes comes back as Integer for small files, Long for large — handle both
            Object bytesRaw = result.get("bytes");
            Long bytes = bytesRaw instanceof Long    l ? l
                       : bytesRaw instanceof Integer i ? i.longValue()
                       : null;

            return new CloudinaryUploadResult(
                (String)  result.get("secure_url"),
                (String)  result.get("public_id"),
                (Integer) result.get("width"),
                (Integer) result.get("height"),
                bytes
            );

        } catch (IOException e) {
            throw new FileUploadException(
                "Upload to Cloudinary failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new FileUploadException(
                "Upload to Cloudinary failed: " + e.getMessage(), e);
        }
    }

    // ═════════════════════════════════════════════════════════════════
    // ── VALIDATION ───────────────────────────────────────────────────
    // ═════════════════════════════════════════════════════════════════

    private void validateFile(MultipartFile file, boolean imageOnly) {

        if (file == null || file.isEmpty()) {
            throw new FileUploadException("Upload file cannot be empty");
        }

        if (imageOnly) {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new FileUploadException(
                    "Only image files are accepted. Got: " + contentType);
            }
        }

        long maxBytes = 10L * 1024 * 1024; // 10 MB hard limit
        if (file.getSize() > maxBytes) {
            throw new FileUploadException(
                "File exceeds the 10MB limit. Size received: "
                + (file.getSize() / 1024 / 1024) + "MB");
        }
    }

    private String shortId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // ═════════════════════════════════════════════════════════════════
    // ── RESULT RECORD ────────────────────────────────────────────────
    // Returned by all NEW typed upload methods.
    // secureUrl → store in Theme/Layout/Template.previewImageUrl
    // publicId  → store if you want to call delete() later
    // ═════════════════════════════════════════════════════════════════

    public record CloudinaryUploadResult(
        String  secureUrl,
        String  publicId,
        Integer width,
        Integer height,
        Long    bytes
    ) {}
}