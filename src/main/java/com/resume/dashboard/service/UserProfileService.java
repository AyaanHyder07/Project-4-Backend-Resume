package com.resume.dashboard.service;

import com.resume.dashboard.dto.userprofile.*;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.UserProfile;
import com.resume.dashboard.exception.FileUploadException;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.UserProfileRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;
    private final ResumeRepository resumeRepository;
    private final CloudinaryService cloudinaryService;
    public UserProfileService(UserProfileRepository repository,
            ResumeRepository resumeRepository,
            CloudinaryService cloudinaryService) {
this.repository = repository;
this.resumeRepository = resumeRepository;
this.cloudinaryService = cloudinaryService;
}
    /*
     * CREATE PROFILE (only once per resume)
     */
    public UserProfileResponse create(String userId,
            CreateUserProfileRequest request,
            MultipartFile profilePhoto) {

        Resume resume = validateOwnership(request.getResumeId(), userId);

        if (repository.existsByResumeId(resume.getId())) {
            throw new IllegalStateException("Profile already exists for this resume");
        }

        UserProfile profile = new UserProfile();
        profile.setId(UUID.randomUUID().toString());
        profile.setResumeId(resume.getId());

        applyFields(profile, request);
        if (profilePhoto != null && !profilePhoto.isEmpty()) {

            if (!profilePhoto.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files allowed");
            }

            if (profilePhoto.getSize() > 5_000_000) {
                throw new IllegalArgumentException("File too large (max 5MB)");
            }

            String imageUrl = cloudinaryService.uploadImage(
                    profilePhoto,
                    "resume/profile/" + resume.getId()
            );

            profile.setProfilePhotoUrl(imageUrl);
        }
        profile.setCreatedAt(Instant.now());
        profile.setUpdatedAt(Instant.now());

        return map(repository.save(profile));
    }

    /*
     * CREATE EMPTY PROFILE (auto-generated when resume is created)
     */
    public void createEmptyProfile(String resumeId) {
        if (repository.existsByResumeId(resumeId)) {
            return;
        }

        UserProfile profile = new UserProfile();
        profile.setId(UUID.randomUUID().toString());
        profile.setResumeId(resumeId);
        profile.setCreatedAt(Instant.now());
        profile.setUpdatedAt(Instant.now());

        repository.save(profile);
    }

    /*
     * UPDATE PROFILE
     */
    public UserProfileResponse update(String userId,
            String resumeId,
            UpdateUserProfileRequest request,
            MultipartFile profilePhoto){

        validateOwnership(resumeId, userId);

        UserProfile profile = repository.findByResumeId(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        applyUpdate(profile, request);
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(
                        profilePhoto,
                        "resume/profile/" + resumeId
                );
                profile.setProfilePhotoUrl(imageUrl);
            } catch (FileUploadException e) {
                // Log the error but don't fail the update
                System.err.println("Image upload failed: " + e.getMessage());
            }
        }
        profile.setUpdatedAt(Instant.now());

        return map(repository.save(profile));
    }

    /*
     * PRIVATE FETCH
     */
    public UserProfileResponse getPrivate(String userId, String resumeId) {

        validateOwnership(resumeId, userId);

        UserProfile profile = repository.findByResumeId(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return map(profile);
    }

    /*
     * PUBLIC FETCH
     */
    public PublicUserProfileResponse getPublic(String resumeId) {

        UserProfile profile = repository.findByResumeId(resumeId)
                .orElse(null);

        if (profile == null) return null;

        return mapPublic(profile);
    }

    /*
     * DELETE
     */
    public void delete(String userId, String resumeId) {

        validateOwnership(resumeId, userId);

        repository.deleteByResumeId(resumeId);
    }

    /*
     * OWNERSHIP CHECK
     */
    private Resume validateOwnership(String resumeId, String userId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return resume;
    }

    /*
     * FIELD APPLY
     */
    private void applyFields(UserProfile profile,
                             CreateUserProfileRequest request) {
    	profile.setEmail(request.getEmail());
        profile.setFullName(request.getFullName());
        profile.setDisplayName(request.getDisplayName());
        profile.setProfessionalTitle(request.getProfessionalTitle());
        profile.setBio(request.getBio());
        profile.setDetailedBio(request.getDetailedBio());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setNationality(request.getNationality());
        profile.setLocation(request.getLocation());
        profile.setAvailabilityStatus(request.getAvailabilityStatus());
        profile.setPhone(request.getPhone());
        profile.setWhatsapp(request.getWhatsapp());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setHobbies(request.getHobbies());
        profile.setInterests(request.getInterests());
        profile.setAchievementsSummary(request.getAchievementsSummary());
    }

    private void applyUpdate(UserProfile profile,
                             UpdateUserProfileRequest request) {
    	if (request.getEmail() != null)
    	    profile.setEmail(request.getEmail());

        if (request.getFullName() != null)
            profile.setFullName(request.getFullName());

        if (request.getDisplayName() != null)
            profile.setDisplayName(request.getDisplayName());

        if (request.getProfessionalTitle() != null)
            profile.setProfessionalTitle(request.getProfessionalTitle());


        if (request.getBio() != null)
            profile.setBio(request.getBio());

        if (request.getDetailedBio() != null)
            profile.setDetailedBio(request.getDetailedBio());

        if (request.getDateOfBirth() != null)
            profile.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));

        if (request.getGender() != null)
            profile.setGender(request.getGender());

        if (request.getNationality() != null)
            profile.setNationality(request.getNationality());

        if (request.getLocation() != null)
            profile.setLocation(request.getLocation());

        if (request.getAvailabilityStatus() != null)
            profile.setAvailabilityStatus(request.getAvailabilityStatus());

        if (request.getPhone() != null)
            profile.setPhone(request.getPhone());

        if (request.getWhatsapp() != null)
            profile.setWhatsapp(request.getWhatsapp());

        if (request.getLinkedinUrl() != null)
            profile.setLinkedinUrl(request.getLinkedinUrl());

        if (request.getGithubUrl() != null)
            profile.setGithubUrl(request.getGithubUrl());

        if (request.getHobbies() != null)
            profile.setHobbies(request.getHobbies());

        if (request.getInterests() != null)
            profile.setInterests(request.getInterests());

        if (request.getAchievementsSummary() != null)
            profile.setAchievementsSummary(request.getAchievementsSummary());
    }

    /*
     * MAPPERS
     */
    private UserProfileResponse map(UserProfile p) {

        UserProfileResponse r = new UserProfileResponse();
        r.setId(p.getId());
        
        r.setResumeId(p.getResumeId());
        r.setEmail(p.getEmail());
        r.setFullName(p.getFullName());
        r.setDisplayName(p.getDisplayName());
        r.setProfessionalTitle(p.getProfessionalTitle());
        r.setProfilePhotoUrl(p.getProfilePhotoUrl());
        r.setBio(p.getBio());
        r.setDetailedBio(p.getDetailedBio());
        r.setDateOfBirth(p.getDateOfBirth());
        r.setGender(p.getGender());
        r.setNationality(p.getNationality());
        r.setLocation(p.getLocation());
        r.setAvailabilityStatus(p.getAvailabilityStatus());
        r.setPhone(p.getPhone());
        r.setWhatsapp(p.getWhatsapp());
        r.setLinkedinUrl(p.getLinkedinUrl());
        r.setGithubUrl(p.getGithubUrl());
        r.setHobbies(p.getHobbies());
        r.setInterests(p.getInterests());
        r.setAchievementsSummary(p.getAchievementsSummary());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }

    private PublicUserProfileResponse mapPublic(UserProfile p) {

        PublicUserProfileResponse r = new PublicUserProfileResponse();
        r.setFullName(p.getFullName());
        r.setDisplayName(p.getDisplayName());
        r.setProfessionalTitle(p.getProfessionalTitle());
        r.setProfilePhotoUrl(p.getProfilePhotoUrl());
        r.setBio(p.getBio());
        r.setDetailedBio(p.getDetailedBio());
        r.setNationality(p.getNationality());
        r.setLocation(p.getLocation());
        r.setAvailabilityStatus(p.getAvailabilityStatus());
        r.setLinkedinUrl(p.getLinkedinUrl());
        r.setGithubUrl(p.getGithubUrl());
        r.setHobbies(p.getHobbies());
        r.setInterests(p.getInterests());
        r.setAchievementsSummary(p.getAchievementsSummary());
        return r;
    }
}