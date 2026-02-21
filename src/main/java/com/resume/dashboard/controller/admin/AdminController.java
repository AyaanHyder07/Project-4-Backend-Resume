package com.resume.dashboard.controller.admin;

import com.resume.dashboard.dto.admin.AdminDashboardResponse;
import com.resume.dashboard.dto.admin.RejectRequest;
import com.resume.dashboard.dto.resume.ResumeResponse;
import com.resume.dashboard.entity.AuditLog;
import com.resume.dashboard.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        AdminDashboardResponse res = adminService.getDashboard();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/resumes")
    public ResponseEntity<Page<ResumeResponse>> getResumes(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String userId,
            @PageableDefault(size = 20) Pageable pageable) {
        String sortBy = pageable.getSort().stream().findFirst().map(o -> o.getProperty()).orElse("updatedAt");
        String sortDir = pageable.getSort().stream().findFirst().map(o -> o.getDirection().name().toLowerCase()).orElse("desc");
        Page<ResumeResponse> page = adminService.getResumes(state, userId, pageable.getPageNumber(), pageable.getPageSize(), sortBy, sortDir);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/resumes/{id}/approve")
    public ResponseEntity<ResumeResponse> approve(@PathVariable String id) {
        ResumeResponse res = adminService.approve(id);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/resumes/{id}/publish")
    public ResponseEntity<ResumeResponse> publish(@PathVariable String id) {
        ResumeResponse res = adminService.publish(id);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/resumes/{id}/disable")
    public ResponseEntity<ResumeResponse> disable(@PathVariable String id) {
        ResumeResponse res = adminService.disable(id);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/resumes/{id}/reject")
    public ResponseEntity<ResumeResponse> reject(@PathVariable String id, @RequestBody(required = false) RejectRequest body) {
        RejectRequest req = body != null ? body : new RejectRequest();
        ResumeResponse res = adminService.reject(id, req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<com.resume.dashboard.dto.admin.UserResponse>> getUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(adminService.getUsers(pageable.getPageNumber(), pageable.getPageSize()));
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable String id) {
        adminService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable String id) {
        adminService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{id}/resumes")
    public ResponseEntity<List<ResumeResponse>> getUserResumes(@PathVariable String id) {
        List<ResumeResponse> list = adminService.getUserResumes(id);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/analytics")
    public ResponseEntity<Page<com.resume.dashboard.dto.admin.AnalyticsResponse.AnalyticsItemDto>> getAnalytics(
            @RequestParam(required = false) String resumeId,
            @RequestParam(required = false) String action,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<com.resume.dashboard.dto.admin.AnalyticsResponse.AnalyticsItemDto> page = adminService.getAnalytics(
                resumeId, action, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/audit")
    public ResponseEntity<Page<AuditLog>> getAudit(
            @RequestParam(required = false) String actorId,
            @RequestParam(required = false) String action,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AuditLog> page = adminService.getAudit(actorId, action, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(page);
    }
}
