package com.resume.dashboard.controller.admin;

import com.resume.dashboard.entity.ApprovalStatus;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.service.admin.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/resumes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /* =========================================================
       GET ALL
    ========================================================= */
    @GetMapping
    public ResponseEntity<List<Resume>> getAll() {
        return ResponseEntity.ok(adminService.getAll());
    }

    /* =========================================================
       GET BY STATUS
    ========================================================= */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Resume>> getByStatus(
            @PathVariable ApprovalStatus status) {

        return ResponseEntity.ok(adminService.getByStatus(status));
    }

    /* =========================================================
       GET PENDING
    ========================================================= */
    @GetMapping("/pending")
    public ResponseEntity<List<Resume>> getPending() {
        return ResponseEntity.ok(adminService.getPending());
    }

    /* =========================================================
       APPROVE (AUTO PUBLISH INSIDE SERVICE)
    ========================================================= */
    @PutMapping("/{resumeId}/approve")
    public ResponseEntity<Resume> approve(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(adminService.approve(resumeId));
    }

    /* =========================================================
       REJECT
    ========================================================= */
    @PutMapping("/{resumeId}/reject")
    public ResponseEntity<Resume> reject(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(adminService.reject(resumeId));
    }

    /* =========================================================
       FORCE UNPUBLISH
    ========================================================= */
    @PutMapping("/{resumeId}/unpublish")
    public ResponseEntity<Resume> forceUnpublish(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(adminService.forceUnpublish(resumeId));
    }

    /* =========================================================
       DELETE
    ========================================================= */
    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> delete(
            @PathVariable String resumeId) {

        adminService.delete(resumeId);
        return ResponseEntity.noContent().build();
    }
}