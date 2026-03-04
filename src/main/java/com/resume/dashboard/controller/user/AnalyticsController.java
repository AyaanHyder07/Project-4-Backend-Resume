package com.resume.dashboard.controller.user;

import com.resume.dashboard.dto.analytics.AnalyticsSummaryResponse;
import com.resume.dashboard.service.user.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    /*
     * GET ANALYTICS SUMMARY FOR RESUME
     */
    @GetMapping("/{resumeId}")
    public ResponseEntity<AnalyticsSummaryResponse> getSummary(
    		@AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        // 🔥 Optional: add ownership validation here later
        return ResponseEntity.ok(service.getSummary(resumeId));
    }
}