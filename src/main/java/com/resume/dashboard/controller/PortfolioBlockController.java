package com.resume.dashboard.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resume.dashboard.dto.portfolioBlock.CreatePortfolioBlockRequest;
import com.resume.dashboard.dto.portfolioBlock.PortfolioBlockResponse;
import com.resume.dashboard.dto.portfolioBlock.UpdatePortfolioBlockRequest;
import com.resume.dashboard.service.PortfolioBlockService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/blocks")
public class PortfolioBlockController {

    private final PortfolioBlockService portfolioBlockService;

    public PortfolioBlockController(PortfolioBlockService portfolioBlockService) {
        this.portfolioBlockService = portfolioBlockService;
    }

    @PostMapping
    public ResponseEntity<PortfolioBlockResponse> create(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreatePortfolioBlockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioBlockService.create(userId, request));
    }

    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<PortfolioBlockResponse>> getByResume(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {
        return ResponseEntity.ok(portfolioBlockService.getByResume(userId, resumeId));
    }

    @PutMapping("/{blockId}")
    public ResponseEntity<PortfolioBlockResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String blockId,
            @RequestBody UpdatePortfolioBlockRequest request) {
        return ResponseEntity.ok(portfolioBlockService.update(userId, blockId, request));
    }

    @PutMapping("/resume/{resumeId}/reorder")
    public ResponseEntity<Void> reorder(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId,
            @RequestBody List<String> orderedIds) {
        portfolioBlockService.reorder(userId, resumeId, orderedIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{blockId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String blockId) {
        portfolioBlockService.delete(userId, blockId);
        return ResponseEntity.noContent().build();
    }
}
