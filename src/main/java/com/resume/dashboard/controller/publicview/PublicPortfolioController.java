package com.resume.dashboard.controller.publicview;

import com.resume.dashboard.dto.publicview.PublicPortfolioResponse;
import com.resume.dashboard.service.publicview.PublicPortfolioAggregatorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PUBLIC PORTFOLIO CONTROLLER
 *
 * No authentication required — this is the publicly accessible
 * portfolio viewer endpoint.
 *
 * GET /api/public/portfolios/{slug}
 *   → Returns the fully assembled portfolio response:
 *       - Resume metadata (title, profession, viewCount)
 *       - Template meta (plan level, mood, version)
 *       - Layout (type, structureConfig, audiences, moods)
 *       - Theme (fully resolved — base + PRO/PREMIUM overrides merged)
 *       - Sections (ordered map of section name → section data)
 *
 * The frontend renderer consumes this single response to draw
 * the entire public portfolio page. No further API calls needed.
 */
@RestController
@RequestMapping("/api/public/portfolios")
public class PublicPortfolioController {

    private final PublicPortfolioAggregatorService aggregatorService;

    public PublicPortfolioController(PublicPortfolioAggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PublicPortfolioResponse> getPortfolio(
            @PathVariable String slug) {

        return ResponseEntity.ok(aggregatorService.getPublicPortfolio(slug));
    }
}