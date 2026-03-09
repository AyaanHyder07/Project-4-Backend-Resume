package com.resume.dashboard.controller;

import com.resume.dashboard.dto.theme.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.service.SubscriptionService;
import com.resume.dashboard.service.ThemeService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ThemeController {

    private final ThemeService themeService;
    private final SubscriptionService subscriptionService;

    public ThemeController(ThemeService themeService,
                           SubscriptionService subscriptionService) {
        this.themeService = themeService;
        this.subscriptionService = subscriptionService;
    }

    // ════════════════════════════════════════════════════════════════
    // USER ENDPOINTS
    // ════════════════════════════════════════════════════════════════

    /**
     * GET /api/themes
     *
     * FIX: changed @RequestAttribute("userId") to required=false.
     *
     * WHY IT WAS 400:
     * @RequestAttribute throws ServletRequestBindingException (→ 400)
     * when the attribute is absent. This happens when:
     *   a) The JWT token is missing or invalid so the filter never sets userId
     *   b) The request reaches the controller before the filter sets the attribute
     * With required=false, absent userId falls back to FREE plan instead of crashing.
     */
    @GetMapping("/api/themes")
    public ResponseEntity<List<ThemeResponse>> getAvailable(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) LayoutAudience audience,
            @RequestParam(required = false) VisualMood mood) {

        // Resolve real plan for authenticated users, FREE fallback for others
        PlanType userPlan = (userId != null)
                ? subscriptionService.getCurrentPlan(userId)
                : PlanType.FREE;

        if (audience != null) {
            return ResponseEntity.ok(
                themeService.getByAudience(audience)
                    .stream()
                    .filter(t -> {
                        PlanType themePlan = t.getRequiredPlan() != null ? t.getRequiredPlan() : PlanType.FREE;
                        return userPlan.ordinal() >= themePlan.ordinal();
                    })
                    .toList()
            );
        }
        if (mood != null) {
            return ResponseEntity.ok(
                themeService.getByMood(mood)
                    .stream()
                    .filter(t -> {
                        PlanType themePlan = t.getRequiredPlan() != null ? t.getRequiredPlan() : PlanType.FREE;
                        return userPlan.ordinal() >= themePlan.ordinal();
                    })
                    .toList()
            );
        }
        return ResponseEntity.ok(themeService.getAvailableForPlan(userPlan));
    }

    /**
     * GET /api/themes/{id}
     */
    @GetMapping("/api/themes/{id}")
    public ResponseEntity<ThemeResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(themeService.getActiveById(id));
    }

    // ════════════════════════════════════════════════════════════════
    // ADMIN ENDPOINTS
    // ════════════════════════════════════════════════════════════════

    /**
     * POST /api/admin/themes
     * FIX: Frontend was calling POST /api/themes → 405.
     * Correct URL is POST /api/admin/themes.
     */
    @PostMapping("/api/admin/themes")
    public ResponseEntity<ThemeResponse> create(
            @Valid @RequestBody CreateThemeRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(themeService.create(request));
    }

    /**
     * PATCH /api/admin/themes/{id}
     */
    @PatchMapping("/api/admin/themes/{id}")
    public ResponseEntity<ThemeResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateThemeRequest request) {
        return ResponseEntity.ok(themeService.update(id, request));
    }

    /**
     * GET /api/admin/themes — all themes, no plan filter
     */
    @GetMapping("/api/admin/themes")
    public ResponseEntity<List<ThemeResponse>> adminGetAll() {
        return ResponseEntity.ok(themeService.getAllActive());
    }

    /**
     * GET /api/admin/themes/{id}
     */
    @GetMapping("/api/admin/themes/{id}")
    public ResponseEntity<ThemeResponse> adminGetById(@PathVariable String id) {
        return ResponseEntity.ok(themeService.getActiveById(id));
    }

    /**
     * DELETE /api/admin/themes/{id} — soft delete only
     */
    @DeleteMapping("/api/admin/themes/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        themeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}