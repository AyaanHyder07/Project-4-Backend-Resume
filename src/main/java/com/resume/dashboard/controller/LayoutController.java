package com.resume.dashboard.controller;

import com.resume.dashboard.dto.layout.CreateLayoutRequest;
import com.resume.dashboard.dto.layout.LayoutResponse;
import com.resume.dashboard.dto.layout.UpdateLayoutRequest;
import com.resume.dashboard.entity.ContentMode;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.VisualMood;
import com.resume.dashboard.service.LayoutService;
import com.resume.dashboard.service.SubscriptionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LayoutController {

    private final LayoutService layoutService;
    private final SubscriptionService subscriptionService;

    public LayoutController(LayoutService layoutService, SubscriptionService subscriptionService) {
        this.layoutService = layoutService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/api/layouts")
    public ResponseEntity<List<LayoutResponse>> getAvailable(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) LayoutType type,
            @RequestParam(required = false) LayoutAudience audience,
            @RequestParam(required = false) VisualMood mood,
            @RequestParam(required = false) ProfessionCategory professionCategory,
            @RequestParam(required = false) MotionPreset motionPreset,
            @RequestParam(required = false) ContentMode contentMode) {

        PlanType userPlan = (userId != null) ? subscriptionService.getCurrentPlan(userId) : PlanType.FREE;
        List<LayoutResponse> results;

        if (type != null) results = layoutService.getByType(type);
        else if (audience != null) results = layoutService.getByAudience(audience);
        else if (mood != null) results = layoutService.getByMood(mood);
        else if (professionCategory != null) results = layoutService.getByProfessionCategory(professionCategory);
        else if (motionPreset != null) results = layoutService.getByMotionPreset(motionPreset);
        else if (contentMode != null) results = layoutService.getByContentMode(contentMode);
        else results = layoutService.getAvailableForPlan(userPlan);

        return ResponseEntity.ok(results.stream()
                .filter(l -> userPlan.ordinal() >= l.getRequiredPlan().ordinal())
                .toList());
    }

    @GetMapping("/api/layouts/{id}")
    public ResponseEntity<LayoutResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(layoutService.getActiveById(id));
    }

    @PostMapping("/api/admin/layouts")
    public ResponseEntity<LayoutResponse> create(@Valid @RequestBody CreateLayoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(layoutService.create(request));
    }

    @PatchMapping("/api/admin/layouts/{id}")
    public ResponseEntity<LayoutResponse> update(@PathVariable String id, @Valid @RequestBody UpdateLayoutRequest request) {
        return ResponseEntity.ok(layoutService.update(id, request));
    }

    @GetMapping("/api/admin/layouts")
    public ResponseEntity<List<LayoutResponse>> adminGetAll() {
        return ResponseEntity.ok(layoutService.getAllActive());
    }

    @GetMapping("/api/admin/layouts/{id}")
    public ResponseEntity<LayoutResponse> adminGetById(@PathVariable String id) {
        return ResponseEntity.ok(layoutService.getActiveById(id));
    }

    @DeleteMapping("/api/admin/layouts/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        layoutService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
