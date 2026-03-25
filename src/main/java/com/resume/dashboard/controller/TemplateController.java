package com.resume.dashboard.controller;

import com.resume.dashboard.dto.template.CreateTemplateRequest;
import com.resume.dashboard.dto.template.TemplateResponse;
import com.resume.dashboard.dto.template.UpdateTemplateRequest;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.VisualMood;
import com.resume.dashboard.service.SubscriptionService;
import com.resume.dashboard.service.TemplateService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TemplateController {

    private final TemplateService templateService;
    private final SubscriptionService subscriptionService;

    public TemplateController(TemplateService templateService, SubscriptionService subscriptionService) {
        this.templateService = templateService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/api/templates")
    public ResponseEntity<List<TemplateResponse>> getAvailable(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) ProfessionCategory professionCategory,
            @RequestParam(required = false) LayoutAudience audience,
            @RequestParam(required = false) VisualMood mood) {

        PlanType userPlan = (userId != null) ? subscriptionService.getCurrentPlan(userId) : PlanType.FREE;

        if (profession != null && !profession.isBlank()) {
            return ResponseEntity.ok(templateService.getByProfession(profession, userPlan));
        }
        if (professionCategory != null) {
            return ResponseEntity.ok(templateService.getByProfessionCategory(professionCategory, userPlan));
        }
        if (mood != null) {
            return ResponseEntity.ok(templateService.getByMood(mood, userPlan));
        }
        if (audience != null) {
            return ResponseEntity.ok(
                    templateService.getAvailableTemplates(userPlan).stream()
                            .filter(t -> t.getTargetAudiences() != null && t.getTargetAudiences().contains(audience))
                            .toList());
        }
        return ResponseEntity.ok(templateService.getAvailableTemplates(userPlan));
    }

    @GetMapping("/api/templates/recommendations")
    public ResponseEntity<List<TemplateResponse>> getRecommendations(
            @AuthenticationPrincipal String userId,
            @RequestParam ProfessionType professionType,
            @RequestParam(required = false) VisualMood mood) {
        PlanType userPlan = (userId != null) ? subscriptionService.getCurrentPlan(userId) : PlanType.FREE;
        return ResponseEntity.ok(templateService.getRecommendations(professionType, userPlan, mood));
    }

    @GetMapping("/api/templates/{id}")
    public ResponseEntity<TemplateResponse> getById(@PathVariable String id, @AuthenticationPrincipal String userId) {
        PlanType userPlan = (userId != null) ? subscriptionService.getCurrentPlan(userId) : PlanType.FREE;
        TemplateResponse template = templateService.getActiveById(id);
        if (userPlan.ordinal() < template.getPlanLevel().ordinal()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(template);
    }

    @PostMapping("/api/admin/templates")
    public ResponseEntity<TemplateResponse> create(@Valid @RequestBody CreateTemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.create(request));
    }

    @PatchMapping("/api/admin/templates/{id}")
    public ResponseEntity<TemplateResponse> update(@PathVariable String id, @Valid @RequestBody UpdateTemplateRequest request) {
        return ResponseEntity.ok(templateService.update(id, request));
    }

    @GetMapping("/api/admin/templates")
    public ResponseEntity<List<TemplateResponse>> adminGetAll() {
        return ResponseEntity.ok(templateService.getAvailableTemplates(PlanType.PREMIUM));
    }

    @GetMapping("/api/admin/templates/{id}")
    public ResponseEntity<TemplateResponse> adminGetById(@PathVariable String id) {
        return ResponseEntity.ok(templateService.getActiveById(id));
    }

    @DeleteMapping("/api/admin/templates/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        templateService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
