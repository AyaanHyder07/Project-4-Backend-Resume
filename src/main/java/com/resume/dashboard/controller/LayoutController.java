package com.resume.dashboard.controller;

import com.resume.dashboard.dto.layout.*;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.service.LayoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {

    private final LayoutService layoutService;

    public LayoutController(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<LayoutResponse> create(
            @Valid @RequestBody CreateLayoutRequest request) {

        return ResponseEntity.ok(
                layoutService.create(request)
        );
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<LayoutResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateLayoutRequest request) {

        return ResponseEntity.ok(
                layoutService.update(id, request)
        );
    }

    /* ================= GET ACTIVE BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<LayoutResponse> getActive(
            @PathVariable String id) {

        return ResponseEntity.ok(
                layoutService.getActiveById(id)
        );
    }

    /* ================= GET ALL ACTIVE ================= */
    @GetMapping
    public ResponseEntity<List<LayoutResponse>> getAllActive() {

        return ResponseEntity.ok(
                layoutService.getAllActive()
        );
    }

    /* ================= GET BY TYPE ================= */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<LayoutResponse>> getByType(
            @PathVariable LayoutType type) {

        return ResponseEntity.ok(
                layoutService.getByType(type)
        );
    }

    /* ================= DEACTIVATE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(
            @PathVariable String id) {

        layoutService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}