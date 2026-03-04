package com.resume.dashboard.controller;

import com.resume.dashboard.dto.auth.*;
import com.resume.dashboard.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/signup
     * Register a new user with email or phone number
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody RegisterRequest request) {
        log.info("Signup endpoint called");
        try {
            AuthResponse response = authService.signup(request);
            log.info("Signup successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Signup failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * POST /api/auth/login
     * Login with email or phone number
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login endpoint called");
        try {
            AuthResponse response = authService.login(request);
            log.info("Login successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * POST /api/auth/logout
     * Logout user (invalidate token on client side)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        log.info("Logout endpoint called");
        // Token invalidation is typically handled on the client side
        // Server can maintain a blacklist in a real application
        return ResponseEntity.ok().build();
    }
}

