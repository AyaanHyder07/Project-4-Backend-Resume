package com.resume.dashboard.service;

import com.resume.dashboard.dto.experience.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ExperienceRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepo;
    private final ResumeRepository resumeRepo;

    public ExperienceService(ExperienceRepository experienceRepo,
                             ResumeRepository resumeRepo) {
        this.experienceRepo = experienceRepo;
        this.resumeRepo = resumeRepo;
    }

    /*
     * CREATE
     */
    public ExperienceResponse create(String userId,
                                     CreateExperienceRequest request) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);
        validateDates(request.getStartDate(),
                      request.getEndDate(),
                      request.getCurrentlyWorking());

        EmploymentType type =
                EmploymentType.valueOf(request.getEmploymentType().toUpperCase());

        int nextOrder = 1;
        Experience last =
                experienceRepo.findTopByResumeIdOrderByDisplayOrderDesc(
                        request.getResumeId());

        if (last != null) {
            nextOrder = last.getDisplayOrder() + 1;
        }

        Experience exp = new Experience();
        exp.setId(UUID.randomUUID().toString());
        exp.setResumeId(request.getResumeId());
        exp.setOrganizationName(request.getOrganizationName());
        exp.setEmploymentType(type);
        exp.setRoleTitle(request.getRoleTitle());
        exp.setLocation(request.getLocation());
        exp.setStartDate(request.getStartDate());
        exp.setEndDate(request.getCurrentlyWorking() ? null : request.getEndDate());
        exp.setCurrentlyWorking(Boolean.TRUE.equals(request.getCurrentlyWorking()));
        exp.setRoleDescription(request.getRoleDescription());
        exp.setKeyAchievements(request.getKeyAchievements());
        exp.setSkillsUsed(request.getSkillsUsed());
        exp.setDisplayOrder(nextOrder);
        exp.setCreatedAt(Instant.now());
        exp.setUpdatedAt(Instant.now());

        return map(experienceRepo.save(exp));
    }

    /*
     * UPDATE
     */
    public ExperienceResponse update(String userId,
                                     String experienceId,
                                     UpdateExperienceRequest request) {

        Experience exp = experienceRepo.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));

        Resume resume = resumeRepo.findById(exp.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);
        validateDates(request.getStartDate(),
                      request.getEndDate(),
                      request.getCurrentlyWorking());

        exp.setOrganizationName(request.getOrganizationName());
        exp.setEmploymentType(
                EmploymentType.valueOf(request.getEmploymentType().toUpperCase())
        );
        exp.setRoleTitle(request.getRoleTitle());
        exp.setLocation(request.getLocation());
        exp.setStartDate(request.getStartDate());
        exp.setEndDate(request.getCurrentlyWorking() ? null : request.getEndDate());
        exp.setCurrentlyWorking(Boolean.TRUE.equals(request.getCurrentlyWorking()));
        exp.setRoleDescription(request.getRoleDescription());
        exp.setKeyAchievements(request.getKeyAchievements());
        exp.setSkillsUsed(request.getSkillsUsed());
        exp.setUpdatedAt(Instant.now());

        return map(experienceRepo.save(exp));
    }

    /*
     * DELETE
     */
    public void delete(String userId, String experienceId) {

        Experience exp = experienceRepo.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));

        Resume resume = resumeRepo.findById(exp.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        experienceRepo.delete(exp);
    }

    /*
     * GET ALL
     */
    public List<ExperienceResponse> getByResume(String userId,
                                                String resumeId) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        return experienceRepo
                .findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * REORDER
     */
    public void reorder(String userId,
                        String resumeId,
                        List<String> orderedIds) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int order = 1;

        for (String id : orderedIds) {
            Experience exp = experienceRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));

            exp.setDisplayOrder(order++);
            exp.setUpdatedAt(Instant.now());
            experienceRepo.save(exp);
        }
    }

    /*
     * VALIDATIONS
     */
    private void validateDates(LocalDate start,
                               LocalDate end,
                               Boolean currentlyWorking) {

        if (start == null) {
            throw new IllegalArgumentException("Start date required");
        }

        if (Boolean.TRUE.equals(currentlyWorking)) {
            return;
        }

        if (end == null) {
            throw new IllegalArgumentException("End date required");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    private void validateOwnership(Resume resume, String userId) {

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    private ExperienceResponse map(Experience exp) {

        ExperienceResponse res = new ExperienceResponse();
        res.setId(exp.getId());
        res.setResumeId(exp.getResumeId());
        res.setOrganizationName(exp.getOrganizationName());
        res.setEmploymentType(exp.getEmploymentType().name());
        res.setRoleTitle(exp.getRoleTitle());
        res.setLocation(exp.getLocation());
        res.setStartDate(exp.getStartDate());
        res.setEndDate(exp.getEndDate());
        res.setCurrentlyWorking(exp.isCurrentlyWorking());
        res.setRoleDescription(exp.getRoleDescription());
        res.setKeyAchievements(exp.getKeyAchievements());
        res.setSkillsUsed(exp.getSkillsUsed());
        res.setDisplayOrder(exp.getDisplayOrder());
        res.setCreatedAt(exp.getCreatedAt());
        res.setUpdatedAt(exp.getUpdatedAt());

        return res;
    }
}