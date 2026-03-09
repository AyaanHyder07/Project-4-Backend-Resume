package com.resume.dashboard.controller;

import com.resume.dashboard.dto.layout.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.service.LayoutService;
import com.resume.dashboard.service.SubscriptionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LAYOUT CONTROLLER — user browsing + admin CRUD in one file.
 *
 * ── USER ENDPOINTS ─────────────────────────────────────────────────
 *   GET /api/layouts               → layouts for my plan
 *   GET /api/layouts?type=         → filter by layout type  (plan applied)
 *   GET /api/layouts?audience=     → filter by audience     (plan applied)
 *   GET /api/layouts?mood=         → filter by mood         (plan applied)
 *   GET /api/layouts/{id}          → single active layout
 *
 * ── ADMIN ENDPOINTS ────────────────────────────────────────────────
 *   POST   /api/admin/layouts          → create
 *   PATCH  /api/admin/layouts/{id}     → partial update
 *   GET    /api/admin/layouts          → all active layouts
 *   GET    /api/admin/layouts/{id}     → single layout by id
 *   DELETE /api/admin/layouts/{id}     → soft-delete
 */
@RestController
public class LayoutController {

    private final LayoutService layoutService;
    private final SubscriptionService subscriptionService;

    public LayoutController(LayoutService layoutService,
                            SubscriptionService subscriptionService) {
        this.layoutService = layoutService;
        this.subscriptionService = subscriptionService;
    }

    // ════════════════════════════════════════════════════════════════
    // USER ENDPOINTS
    // ════════════════════════════════════════════════════════════════

    /**
     * GET /api/layouts
     * Returns layouts the user's plan can access, with optional filters.
     * All filters also apply the plan gate.
     */
    @GetMapping("/api/layouts")
    public ResponseEntity<List<LayoutResponse>> getAvailable(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) LayoutType type,
            @RequestParam(required = false) LayoutAudience audience,
            @RequestParam(required = false) VisualMood mood) {

        PlanType userPlan = subscriptionService.getCurrentPlan(userId);

        if (type != null) {
            return ResponseEntity.ok(
                layoutService.getByType(type)
                    .stream()
                    .filter(l -> userPlan.ordinal() >= l.getRequiredPlan().ordinal())
                    .toList()
            );
        }
        if (audience != null) {
            return ResponseEntity.ok(
                layoutService.getByAudience(audience)
                    .stream()
                    .filter(l -> userPlan.ordinal() >= l.getRequiredPlan().ordinal())
                    .toList()
            );
        }
        if (mood != null) {
            return ResponseEntity.ok(
                layoutService.getByMood(mood)
                    .stream()
                    .filter(l -> userPlan.ordinal() >= l.getRequiredPlan().ordinal())
                    .toList()
            );
        }
        return ResponseEntity.ok(layoutService.getAvailableForPlan(userPlan));
    }

    /**
     * GET /api/layouts/{id}
     */
    @GetMapping("/api/layouts/{id}")
    public ResponseEntity<LayoutResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(layoutService.getActiveById(id));
    }

    // ════════════════════════════════════════════════════════════════
    // ADMIN ENDPOINTS
    // ════════════════════════════════════════════════════════════════

    /**
     * POST /api/admin/layouts
     * previewImageUrl = Cloudinary URL from POST /api/admin/upload/preview?type=layout
     */
    @PostMapping("/api/admin/layouts")
    public ResponseEntity<LayoutResponse> create(
            @Valid @RequestBody CreateLayoutRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(layoutService.create(request));
    }

    /**
     * PATCH /api/admin/layouts/{id}
     * Partial update — structureConfig changes bump version.
     */
    @PatchMapping("/api/admin/layouts/{id}")
    public ResponseEntity<LayoutResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateLayoutRequest request) {
        return ResponseEntity.ok(layoutService.update(id, request));
    }

    /**
     * GET /api/admin/layouts
     * Admin sees ALL active layouts — no plan filter.
     */
    @GetMapping("/api/admin/layouts")
    public ResponseEntity<List<LayoutResponse>> adminGetAll() {
        return ResponseEntity.ok(layoutService.getAllActive());
    }

    /**
     * GET /api/admin/layouts/{id}
     */
    @GetMapping("/api/admin/layouts/{id}")
    public ResponseEntity<LayoutResponse> adminGetById(@PathVariable String id) {
        return ResponseEntity.ok(layoutService.getActiveById(id));
    }

    /**
     * DELETE /api/admin/layouts/{id}
     * Soft-delete only.
     */
    @DeleteMapping("/api/admin/layouts/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        layoutService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}