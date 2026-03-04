package com.resume.dashboard.controller.publicview;

import com.resume.dashboard.dto.publicview.PublicPortfolioResponse;
import com.resume.dashboard.service.publicview.PublicPortfolioAggregatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicPortfolioController {

    private final PublicPortfolioAggregatorService service;

    public PublicPortfolioController(PublicPortfolioAggregatorService service) {
        this.service = service;
    }

    /*
     * GET PUBLIC PORTFOLIO BY SLUG
     * Example:
     * GET /api/public/{username}
     */
    @GetMapping("/{slug}")
    public ResponseEntity<PublicPortfolioResponse> getPublicPortfolio(
            @PathVariable String slug) {

        return ResponseEntity.ok(service.getPublicPortfolio(slug));
    }
}