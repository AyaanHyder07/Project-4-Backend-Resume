package com.resume.dashboard.service;

import com.resume.dashboard.dto.education.*;
import com.resume.dashboard.entity.Education;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.EducationRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EducationService {

    private final EducationRepository educationRepo;
    private final ResumeRepository resumeRepo;

    public EducationService(EducationRepository educationRepo,
                            ResumeRepository resumeRepo) {
        this.educationRepo = educationRepo;
        this.resumeRepo = resumeRepo;
    }

    /*
     * CREATE
     */
    public EducationResponse create(String userId,
                                    CreateEducationRequest request) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        validateYears(request.getStartYear(), request.getEndYear());

        int nextOrder = 1;
        Education last = educationRepo
                .findTopByResumeIdOrderByDisplayOrderDesc(request.getResumeId());

        if (last != null) {
            nextOrder = last.getDisplayOrder() + 1;
        }

        Education edu = new Education();
        edu.setId(UUID.randomUUID().toString());
        edu.setResumeId(request.getResumeId());
        edu.setInstitutionName(request.getInstitutionName());
        edu.setDegree(request.getDegree());
        edu.setSpecialization(request.getSpecialization());
        edu.setGrade(request.getGrade());
        edu.setStartYear(request.getStartYear());
        edu.setEndYear(request.getEndYear());
        edu.setDescription(request.getDescription());
        edu.setDisplayOrder(nextOrder);
        edu.setCreatedAt(Instant.now());
        edu.setUpdatedAt(Instant.now());

        return map(educationRepo.save(edu));
    }

    /*
     * UPDATE
     */
    public EducationResponse update(String userId,
                                    String educationId,
                                    UpdateEducationRequest request) {

        Education edu = educationRepo.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));

        Resume resume = resumeRepo.findById(edu.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        validateYears(request.getStartYear(), request.getEndYear());

        edu.setInstitutionName(request.getInstitutionName());
        edu.setDegree(request.getDegree());
        edu.setSpecialization(request.getSpecialization());
        edu.setGrade(request.getGrade());
        edu.setStartYear(request.getStartYear());
        edu.setEndYear(request.getEndYear());
        edu.setDescription(request.getDescription());
        edu.setUpdatedAt(Instant.now());

        return map(educationRepo.save(edu));
    }

    /*
     * DELETE
     */
    public void delete(String userId, String educationId) {

        Education edu = educationRepo.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));

        Resume resume = resumeRepo.findById(edu.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        educationRepo.delete(edu);
    }

    /*
     * GET ALL
     */
    public List<EducationResponse> getByResume(String userId,
                                               String resumeId) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        return educationRepo
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

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        int order = 1;

        for (String id : orderedIds) {
            Education edu = educationRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Education not found"));

            edu.setDisplayOrder(order++);
            edu.setUpdatedAt(Instant.now());
            educationRepo.save(edu);
        }
    }

    /*
     * YEAR VALIDATION
     */
    private void validateYears(int startYear, Integer endYear) {

        int currentYear = Year.now().getValue();

        if (startYear > currentYear) {
            throw new IllegalArgumentException("Start year cannot be in the future");
        }

        if (endYear != null && endYear < startYear) {
            throw new IllegalArgumentException("End year cannot be before start year");
        }
    }

    private EducationResponse map(Education edu) {

        EducationResponse res = new EducationResponse();
        res.setId(edu.getId());
        res.setResumeId(edu.getResumeId());
        res.setInstitutionName(edu.getInstitutionName());
        res.setDegree(edu.getDegree());
        res.setSpecialization(edu.getSpecialization());
        res.setGrade(edu.getGrade());
        res.setStartYear(edu.getStartYear());
        res.setEndYear(edu.getEndYear());
        res.setDescription(edu.getDescription());
        res.setDisplayOrder(edu.getDisplayOrder());
        res.setCreatedAt(edu.getCreatedAt());
        res.setUpdatedAt(edu.getUpdatedAt());

        return res;
    }
}