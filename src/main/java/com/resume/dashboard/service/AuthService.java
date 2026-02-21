package com.resume.dashboard.service;

import com.resume.dashboard.dto.auth.*;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.exception.DuplicateEmailException;
import com.resume.dashboard.exception.UserBlockedException;
import com.resume.dashboard.repository.UserRepository;
import com.resume.dashboard.security.JwtUtil;
import com.resume.dashboard.util.AuditService;
import com.resume.dashboard.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.auditService = auditService;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException("Email already registered");
        }
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole("ROLE_USER");
        user.setStatus(User.UserStatus.ACTIVE);
        user.setCreatedAt(Instant.now());
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        auditService.log("REGISTER", "USER", user.getId(), new HashMap<>());
        log.info("User registered: {}", user.getEmail());
        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setRole(user.getRole());
        res.setEmail(user.getEmail());
        res.setUserId(user.getId());
        return res;
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (user.getStatus() == User.UserStatus.BLOCKED) {
            throw new UserBlockedException("Account is blocked");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        auditService.log("LOGIN", "USER", user.getId(), new HashMap<>());
        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setRole(user.getRole());
        res.setEmail(user.getEmail());
        res.setUserId(user.getId());
        return res;
    }

    public void updateProfile(UpdateProfileRequest req) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.getEmail())) {
                throw new DuplicateEmailException("Email already in use");
            }
            user.setEmail(req.getEmail());
            userRepository.save(user);
            auditService.log("UPDATE_PROFILE", "USER", user.getId(), new HashMap<>());
        }
    }

    public void changePassword(ChangePasswordRequest req) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        auditService.log("CHANGE_PASSWORD", "USER", user.getId(), new HashMap<>());
    }
}
