package com.resume.dashboard.entity;

import java.util.List;

/**
 * Structured background definition.
 * Supports: solid color, gradient, texture overlay, pattern, image.
 * These can be LAYERED — e.g. grain texture ON TOP of a gradient.
 */

public class ThemeBackground {

    private BackgroundType type; // PRIMARY layer type

    // ─── SOLID ──────────────────────────────────────────────────────
    private String solidColor;           // e.g. "#1A1A2E"

    // ─── GRADIENT ───────────────────────────────────────────────────
    private GradientConfig gradient;

    // ─── TEXTURE / GRAIN / PATTERN ──────────────────────────────────
    private TextureOverlay textureOverlay; // layered ON TOP of bg

    // ─── IMAGE ──────────────────────────────────────────────────────
    private String imageUrl;
    private String imageBlendMode;       // e.g. "multiply", "overlay"
    private Integer imageOpacity;    
    
    // 0–100


    // ════════════════════════════════════════════════════════════════
    //  NESTED: Gradient
    // ════════════════════════════════════════════════════════════════

    public static class GradientConfig {
        private GradientType gradientType;   // LINEAR, RADIAL, CONIC, MESH
        private String angle;                // e.g. "135deg" (for LINEAR)
        private List<GradientStop> stops;    // color stops
        private Boolean grainy;              // true = grain noise over gradient
        private Integer grainIntensity;      // 0–100
		public GradientType getGradientType() {
			return gradientType;
		}
		public void setGradientType(GradientType gradientType) {
			this.gradientType = gradientType;
		}
		public String getAngle() {
			return angle;
		}
		public void setAngle(String angle) {
			this.angle = angle;
		}
		public List<GradientStop> getStops() {
			return stops;
		}
		public void setStops(List<GradientStop> stops) {
			this.stops = stops;
		}
		public Boolean getGrainy() {
			return grainy;
		}
		public void setGrainy(Boolean grainy) {
			this.grainy = grainy;
		}
		public Integer getGrainIntensity() {
			return grainIntensity;
		}
		public void setGrainIntensity(Integer grainIntensity) {
			this.grainIntensity = grainIntensity;
		}
        
        
        
    }


    public static class GradientStop {
        private String color;       // hex or rgba
        private String position;    // "0%", "50%", "100%"
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
        
        
    }

    public enum GradientType {
        LINEAR, RADIAL, CONIC, MESH
    }


    // ════════════════════════════════════════════════════════════════
    //  NESTED: Texture Overlay
    // ════════════════════════════════════════════════════════════════

    public static class TextureOverlay {
        private TextureType textureType;
        private Integer opacity;             // 0–100
        private String blendMode;            // CSS blend mode string
        private String customPatternUrl;     // if CUSTOM_IMAGE
        private String patternColor;         // for SVG-based patterns
        private Integer patternScale;        // 1–10
		public TextureType getTextureType() {
			return textureType;
		}
		public void setTextureType(TextureType textureType) {
			this.textureType = textureType;
		}
		public Integer getOpacity() {
			return opacity;
		}
		public void setOpacity(Integer opacity) {
			this.opacity = opacity;
		}
		public String getBlendMode() {
			return blendMode;
		}
		public void setBlendMode(String blendMode) {
			this.blendMode = blendMode;
		}
		public String getCustomPatternUrl() {
			return customPatternUrl;
		}
		public void setCustomPatternUrl(String customPatternUrl) {
			this.customPatternUrl = customPatternUrl;
		}
		public String getPatternColor() {
			return patternColor;
		}
		public void setPatternColor(String patternColor) {
			this.patternColor = patternColor;
		}
		public Integer getPatternScale() {
			return patternScale;
		}
		public void setPatternScale(Integer patternScale) {
			this.patternScale = patternScale;
		}
        
        
    }

    public enum TextureType {
        GRAIN,              // Film grain / noise
        PAPER,              // Aged paper texture
        CANVAS,             // Painterly canvas weave
        LINEN,              // Fine linen fabric
        CONCRETE,           // Raw concrete / brutalist
        MARBLE,             // Marble veining
        WATERCOLOR_WASH,    // Soft watercolor bleeds
        HALFTONE,           // Dot pattern print style
        CROSSHATCH,         // Ink crosshatch lines
        TOPOGRAPHIC,        // Map contour lines
        NOISE_STATIC,       // Digital static/TV noise
        GLASS_FROSTED,      // Frosted glass blur shimmer
        CUSTOM_IMAGE        // User-uploaded or admin-set URL
    }

    public enum BackgroundType {
        SOLID, GRADIENT, IMAGE, MESH_GRADIENT
    }

	public BackgroundType getType() {
		return type;
	}

	public void setType(BackgroundType type) {
		this.type = type;
	}

	public String getSolidColor() {
		return solidColor;
	}

	public void setSolidColor(String solidColor) {
		this.solidColor = solidColor;
	}

	public GradientConfig getGradient() {
		return gradient;
	}

	public void setGradient(GradientConfig gradient) {
		this.gradient = gradient;
	}

	public TextureOverlay getTextureOverlay() {
		return textureOverlay;
	}

	public void setTextureOverlay(TextureOverlay textureOverlay) {
		this.textureOverlay = textureOverlay;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageBlendMode() {
		return imageBlendMode;
	}

	public void setImageBlendMode(String imageBlendMode) {
		this.imageBlendMode = imageBlendMode;
	}

	public Integer getImageOpacity() {
		return imageOpacity;
	}

	public void setImageOpacity(Integer imageOpacity) {
		this.imageOpacity = imageOpacity;
	}
    
    
}