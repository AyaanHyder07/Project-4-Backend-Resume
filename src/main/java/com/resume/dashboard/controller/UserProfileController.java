package com.resume.dashboard.controller;

import com.resume.dashboard.dto.userprofile.*;
import com.resume.dashboard.service.UserProfileService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @PostMapping(
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	public ResponseEntity<UserProfileResponse> create(
    	        @AuthenticationPrincipal String userId,
    	        @RequestPart("data") CreateUserProfileRequest request,
    	        @RequestPart(value = "profilePhoto", required = false)
    	        MultipartFile profilePhoto) {

    	    return ResponseEntity.ok(
    	            service.create(userId, request, profilePhoto)
    	    );
    	}

    @PutMapping(
    	    value = "/{resumeId}",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	public ResponseEntity<UserProfileResponse> update(
    	        @AuthenticationPrincipal String userId,
    	        @PathVariable String resumeId,
    	        @RequestPart("data") UpdateUserProfileRequest request,
    	        @RequestPart(value = "profilePhoto", required = false)
    	        MultipartFile profilePhoto) {

    	    return ResponseEntity.ok(
    	            service.update(userId, resumeId, request, profilePhoto)
    	    );
    	}
    @GetMapping("/{resumeId}")
    public ResponseEntity<UserProfileResponse> getPrivate(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getPrivate(userId, resumeId));
    }

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<PublicUserProfileResponse> getPublic(
            @PathVariable String resumeId) {

        return ResponseEntity.ok(service.getPublic(resumeId));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        service.delete(userId, resumeId);
        return ResponseEntity.noContent().build();
    }
}