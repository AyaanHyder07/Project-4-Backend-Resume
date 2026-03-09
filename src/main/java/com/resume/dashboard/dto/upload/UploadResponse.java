package com.resume.dashboard.dto.upload;


/**
 * Returned to React after any image upload.
 * React stores secureUrl and sends it back inside the
 * CreateThemeRequest / CreateLayoutRequest / CreateTemplateRequest body.
 */
public class UploadResponse {

    private String secureUrl;    // ← React uses this as previewImageUrl
    private String publicId;     // ← Optionally store for future deletion
    private Integer width;
    private Integer height;
    private Long bytes;
    private String message;

    public static UploadResponse of(
            com.resume.dashboard.service.CloudinaryService.CloudinaryUploadResult result) {
        return new UploadResponse(
            result.secureUrl(),
            result.publicId(),
            result.width(),
            result.height(),
            result.bytes(),
            "Upload successful"
        );
    }

	public String getSecureUrl() {
		return secureUrl;
	}

	public void setSecureUrl(String secureUrl) {
		this.secureUrl = secureUrl;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getBytes() {
		return bytes;
	}

	public void setBytes(Long bytes) {
		this.bytes = bytes;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UploadResponse(String secureUrl, String publicId, Integer width, Integer height, Long bytes,
			String message) {
		super();
		this.secureUrl = secureUrl;
		this.publicId = publicId;
		this.width = width;
		this.height = height;
		this.bytes = bytes;
		this.message = message;
	}

	public UploadResponse() {
		super();
	}
    
    
}