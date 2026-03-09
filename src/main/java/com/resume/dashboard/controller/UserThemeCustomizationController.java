package com.resume.dashboard.controller;

import com.resume.dashboard.dto.customization.*;
import com.resume.dashboard.service.UserThemeCustomizationService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Theme customization for PRO/PREMIUM users.
 * Plan enforcement is handled INSIDE UserThemeCustomizationService
 * via subscriptionService.validateThemeCustomization(userId).
 *
 * FIX (Image 2): Removed the @RequestHeader X-User-Plan parameter
 * and the userPlan argument from saveCustomization() call.
 * The service now resolves the plan itself from SubscriptionService —
 * never trust the client to pass their own plan level.
 *
 * URL: /api/users/{userId}/resumes/{resumeId}/theme/...
 */
@RestController
@RequestMapping("/api/users/{userId}/resumes/{resumeId}/theme")
public class UserThemeCustomizationController {

    private final UserThemeCustomizationService customizationService;

    public UserThemeCustomizationController(
            UserThemeCustomizationService customizationService) {
        this.customizationService = customizationService;
    }

    // ─── SAVE / UPDATE CUSTOMIZATIONS ────────────────────────────────
    /**
     * PUT /api/users/{userId}/resumes/{resumeId}/theme/customize
     * PRO/PREMIUM only — enforced inside the service.
     *
     * FIX: No longer accepts @RequestHeader X-User-Plan.
     * Service calls subscriptionService.validateThemeCustomization(userId) internally.
     */
    @PutMapping("/customize")
    public ResponseEntity<UserThemeCustomizationResponse> saveCustomization(
            @PathVariable String userId,
            @PathVariable String resumeId,
            @Valid @RequestBody SaveCustomizationRequest request) {

        return ResponseEntity.ok(
            customizationService.saveCustomization(userId, resumeId, request)
        );
    }

    // ─── RESOLVE FINAL THEME (for the resume renderer) ───────────────
    /**
     * GET /api/users/{userId}/resumes/{resumeId}/theme/resolved?baseThemeId=xxx
     *
     * Called by the frontend resume renderer.
     * Returns a fully merged, null-free theme object:
     *   base theme values + user's customization overrides applied on top.
     *
     * Works for ALL plans:
     *   FREE/BASIC → returns pure base theme (no overrides stored)
     *   PRO/PREMIUM → returns merged result with user's overrides
     */
    @GetMapping("/resolved")
    public ResponseEntity<ResolvedThemeResponse> resolveTheme(
            @PathVariable String userId,
            @PathVariable String resumeId,
            @RequestParam String baseThemeId) {

        return ResponseEntity.ok(
            customizationService.resolveTheme(userId, resumeId, baseThemeId)
        );
    }

    // ─── GET RAW CUSTOMIZATION DELTA (for the theme editor UI) ───────
    /**
     * GET /api/users/{userId}/resumes/{resumeId}/theme/customization
     *
     * Returns only the fields the user has overridden (null = using base theme default).
     * Used by the theme editor panel to pre-populate inputs with user's saved values.
     * PRO/PREMIUM only — enforced inside the service.
     */
    @GetMapping("/customization")
    public ResponseEntity<UserThemeCustomizationResponse> getCustomization(
            @PathVariable String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
            customizationService.getCustomization(userId, resumeId)
        );
    }

    // ─── RESET TO BASE THEME ─────────────────────────────────────────
    /**
     * DELETE /api/users/{userId}/resumes/{resumeId}/theme/customization
     *
     * Wipes all saved overrides — resume falls back to pure base theme defaults.
     * PRO/PREMIUM only — enforced inside the service.
     */
    @DeleteMapping("/customization")
    public ResponseEntity<Void> resetCustomization(
            @PathVariable String userId,
            @PathVariable String resumeId) {

        customizationService.resetCustomization(userId, resumeId);
        return ResponseEntity.noContent().build();
    }
}