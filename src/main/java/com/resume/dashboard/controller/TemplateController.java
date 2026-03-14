package com.resume.dashboard.controller;

import com.resume.dashboard.dto.template.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.service.SubscriptionService;
import com.resume.dashboard.service.TemplateService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TEMPLATE CONTROLLER — user browsing + admin CRUD in one file.
 *
 * ── USER ENDPOINTS ─────────────────────────────────────────────────
 *   GET /api/templates                    → templates for my plan
 *   GET /api/templates?profession=        → filter by profession tag (plan applied)
 *   GET /api/templates?audience=          → filter by audience       (plan applied)
 *   GET /api/templates?mood=              → filter by visual mood    (plan applied)
 *   GET /api/templates/{id}               → single template (plan checked)
 *
 * ── ADMIN ENDPOINTS ────────────────────────────────────────────────
 *   POST   /api/admin/templates           → create
 *   PATCH  /api/admin/templates/{id}      → partial update
 *   GET    /api/admin/templates           → all active templates
 *   GET    /api/admin/templates/{id}      → single by id  ← FIX: was the bad hack
 *   DELETE /api/admin/templates/{id}      → soft-delete
 */
@RestController
public class TemplateController {

    private final TemplateService templateService;
    private final SubscriptionService subscriptionService;

    public TemplateController(TemplateService templateService,
                              SubscriptionService subscriptionService) {
        this.templateService = templateService;
        this.subscriptionService = subscriptionService;
    }

    // ════════════════════════════════════════════════════════════════
    // USER ENDPOINTS
    // ════════════════════════════════════════════════════════════════

    /**
     * GET /api/templates
     * Returns templates accessible for the user's real subscription plan.
     * Plan is ALWAYS resolved from SubscriptionService — never from a request param.
     */
    @GetMapping("/api/templates")
    public ResponseEntity<List<TemplateResponse>> getAvailable(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) LayoutAudience audience,
            @RequestParam(required = false) VisualMood mood) {

        PlanType userPlan = (userId != null) ? subscriptionService.getCurrentPlan(userId) : PlanType.FREE;

        if (profession != null) {
            return ResponseEntity.ok(templateService.getByProfession(profession, PlanType.PREMIUM));
        }
        if (audience != null) {
            return ResponseEntity.ok(templateService.getByAudience(audience, PlanType.PREMIUM));
        }
        if (mood != null) {
            return ResponseEntity.ok(templateService.getByMood(mood, PlanType.PREMIUM));
        }
        return ResponseEntity.ok(templateService.getAvailableTemplates(PlanType.PREMIUM));
    }

    /**
     * GET /api/templates/{id}
     * Returns a single template only if it's accessible on the user's plan.
     */
    @GetMapping("/api/templates/{id}")
    public ResponseEntity<TemplateResponse> getById(
            @PathVariable String id,
            @AuthenticationPrincipal String userId) {

        PlanType userPlan = (userId != null) ? subscriptionService.getCurrentPlan(userId) : PlanType.FREE;

        // Use the proper service method — no stream hacks
        TemplateResponse template = templateService.getActiveById(id);

        // Enforce plan gate — user cannot access templates above their plan
        if (userPlan.ordinal() < template.getPlanLevel().ordinal()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(template);
    }

    // ════════════════════════════════════════════════════════════════
    // ADMIN ENDPOINTS
    // ════════════════════════════════════════════════════════════════

    /**
     * POST /api/admin/templates
     * previewImageUrl = Cloudinary URL from POST /api/admin/upload/preview?type=template
     */
    @PostMapping("/api/admin/templates")
    public ResponseEntity<TemplateResponse> create(
            @Valid @RequestBody CreateTemplateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(templateService.create(request));
    }

    /**
     * PATCH /api/admin/templates/{id}
     * Partial update. layoutId, defaultThemeId, supportedSections changes bump version.
     */
    @PatchMapping("/api/admin/templates/{id}")
    public ResponseEntity<TemplateResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateTemplateRequest request) {
        return ResponseEntity.ok(templateService.update(id, request));
    }

    /**
     * GET /api/admin/templates
     * Admin sees ALL active templates — no plan filter.
     */
    @GetMapping("/api/admin/templates")
    public ResponseEntity<List<TemplateResponse>> adminGetAll() {
        // PREMIUM = highest plan (ordinal 3) — returns everything
        return ResponseEntity.ok(templateService.getAvailableTemplates(PlanType.PREMIUM));
    }

    /**
     * GET /api/admin/templates/{id}
     *
     * FIX: The old version was doing a terrible stream filter hack:
     *   templateService.getAvailableTemplates(PlanType.ENTERPRISE).stream().filter(id)
     *
     * TemplateService now has a proper getActiveById(id) method.
     * No stream scanning, no ENTERPRISE (which doesn't exist), clean O(1) lookup.
     */
    @GetMapping("/api/admin/templates/{id}")
    public ResponseEntity<TemplateResponse> adminGetById(@PathVariable String id) {
        return ResponseEntity.ok(templateService.getActiveById(id));
    }

    /**
     * DELETE /api/admin/templates/{id}
     * Soft-delete only — sets active=false.
     */
    @DeleteMapping("/api/admin/templates/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        templateService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}