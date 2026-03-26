package com.resume.dashboard.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.resume.dashboard.exception.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private static final long MAX_FILE_SIZE = 15L * 1024 * 1024;

    public static final String FOLDER_THEMES = "resume-builder/previews/themes";
    public static final String FOLDER_LAYOUTS = "resume-builder/previews/layouts";
    public static final String FOLDER_TEMPLATES = "resume-builder/previews/templates";
    public static final String FOLDER_TEXTURES = "resume-builder/textures";
    public static final String FOLDER_AVATARS = "resume-builder/avatars";
    public static final String FOLDER_BLOGS = "resume-builder/blogs";

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file, String folder) {
        return uploadOptimizedImage(file, folder, "img_" + shortId()).secureUrl();
    }

    public String uploadVideo(MultipartFile file, String folder) {
        validateFile(file, false, false);

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", folder,
                    "public_id", "vid_" + shortId(),
                    "resource_type", "video",
                    "quality", "auto"
                )
            );

            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new FileUploadException("Video upload failed: " + e.getMessage(), e);
        }
    }

    public String uploadDocument(MultipartFile file, String folder) {
        validateFile(file, false, true);

        try {
            String contentType = file.getContentType();
            if ("application/pdf".equalsIgnoreCase(contentType)) {
                Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                        "folder", folder,
                        "public_id", "doc_" + shortId(),
                        "resource_type", "raw"
                    )
                );
                return result.get("secure_url").toString();
            }

            return uploadOptimizedImage(file, folder, "doc_" + shortId()).secureUrl();
        } catch (FileUploadException e) {
            throw e;
        } catch (Exception e) {
            throw new FileUploadException("Document upload failed: " + e.getMessage(), e);
        }
    }

    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.err.println("[CloudinaryService] delete() failed for publicId="
                + publicId + " - " + e.getMessage());
        }
    }

    public CloudinaryUploadResult uploadThemePreview(MultipartFile file) {
        return uploadOptimizedImage(file, FOLDER_THEMES, "theme_" + shortId());
    }

    public CloudinaryUploadResult uploadLayoutPreview(MultipartFile file) {
        return uploadOptimizedImage(file, FOLDER_LAYOUTS, "layout_" + shortId());
    }

    public CloudinaryUploadResult uploadTemplatePreview(MultipartFile file) {
        return uploadOptimizedImage(file, FOLDER_TEMPLATES, "template_" + shortId());
    }

    public CloudinaryUploadResult uploadTexture(MultipartFile file) {
        return uploadOptimizedImage(file, FOLDER_TEXTURES, "texture_" + shortId());
    }

    public CloudinaryUploadResult uploadAvatar(MultipartFile file) {
        return uploadOptimizedImage(file, FOLDER_AVATARS, "avatar_" + shortId());
    }

    @SuppressWarnings("unchecked")
    private CloudinaryUploadResult uploadOptimizedImage(MultipartFile file, String folder, String publicId) {
        validateFile(file, true, false);

        try {
            Map<String, Object> params = ObjectUtils.asMap(
                "folder", folder,
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image",
                "format", "webp",
                "quality", "auto:good",
                "width", 1920,
                "height", 1080,
                "crop", "limit"
            );

            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
            Object bytesRaw = result.get("bytes");
            Long bytes = bytesRaw instanceof Long l ? l
                : bytesRaw instanceof Integer i ? i.longValue()
                : null;

            return new CloudinaryUploadResult(
                (String) result.get("secure_url"),
                (String) result.get("public_id"),
                (Integer) result.get("width"),
                (Integer) result.get("height"),
                bytes
            );
        } catch (IOException e) {
            throw new FileUploadException("Upload to Cloudinary failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new FileUploadException("Upload to Cloudinary failed: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file, boolean imageOnly, boolean allowPdf) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("Upload file cannot be empty");
        }

        String contentType = file.getContentType();
        if (imageOnly) {
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new FileUploadException("Only image files are accepted. Got: " + contentType);
            }
        } else if (allowPdf) {
            boolean valid = contentType != null
                && (contentType.startsWith("image/") || "application/pdf".equalsIgnoreCase(contentType));
            if (!valid) {
                throw new FileUploadException("Only image or PDF files are accepted. Got: " + contentType);
            }
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException(
                "File exceeds the 15MB limit. Size received: " + (file.getSize() / 1024 / 1024) + "MB"
            );
        }
    }

    private String shortId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public record CloudinaryUploadResult(
        String secureUrl,
        String publicId,
        Integer width,
        Integer height,
        Long bytes
    ) {}
}
