package com.resume.dashboard.service;

import com.resume.dashboard.dto.auth.AuthResponse;
import com.resume.dashboard.dto.auth.LoginRequest;
import com.resume.dashboard.dto.auth.RegisterRequest;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.exception.UserBlockedException;
import com.resume.dashboard.repository.UserRepository;
import com.resume.dashboard.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SubscriptionService subscriptionService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       SubscriptionService subscriptionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.subscriptionService = subscriptionService;
    }

    public AuthResponse signup(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username required");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Phone number required");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        String username = request.getUsername().trim().toLowerCase();
        String phone = request.getPhoneNumber().trim();

        if (!username.matches("^[a-z0-9]{3,20}$")) {
            throw new IllegalArgumentException("Username must be 3-20 lowercase letters or numbers");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already taken");
        }
        if (userRepository.existsByPhoneNumber(phone)) {
            throw new IllegalStateException("Phone already registered");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPhoneNumber(phone);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        user.setStatus(User.UserStatus.ACTIVE);
        user.setFreePlanConsumed(false);
        user.setFreePlanConsumedAt(null);
        user.setCreatedAt(Instant.now());
        user.setLastLogin(Instant.now());

        User saved = userRepository.save(user);
        subscriptionService.createSubscription(saved.getId(), PlanType.FREE, null);
        log.info("User registered: {}", saved.getId());

        String token = jwtUtil.generateToken(saved.getUsername(), saved.getId());
        return new AuthResponse(token, saved.getRole(), saved.getUsername(), saved.getPhoneNumber(), saved.getId());
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getIdentifier() == null || request.getIdentifier().isBlank()) {
            throw new IllegalArgumentException("Username or phone required");
        }

        String identifier = request.getIdentifier().trim().toLowerCase();
        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByPhoneNumber(identifier))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (user.getStatus() == User.UserStatus.BLOCKED) {
            throw new UserBlockedException("Account blocked");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        user.setLastLogin(Instant.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        log.info("User logged in: {}", user.getId());

        return new AuthResponse(token, user.getRole(), user.getUsername(), user.getPhoneNumber(), user.getId());
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void updateLastLogin(String userId) {
        User user = getUserById(userId);
        user.setLastLogin(Instant.now());
        userRepository.save(user);
    }
}
