package com.resume.dashboard.controller.user;

import com.resume.dashboard.dto.dashboard.DashboardResponse;
import com.resume.dashboard.service.user.UserDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class UserDashboardController {

    private final UserDashboardService service;

    public UserDashboardController(UserDashboardService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
    		@AuthenticationPrincipal String userId) {

        return ResponseEntity.ok(service.getDashboard(userId));
    }
}