package com.resume.dashboard.service;

import com.resume.dashboard.dto.project.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ProjectRepository;
import com.resume.dashboard.repository.ResumeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository repo;
    private final ResumeRepository resumeRepo;

    public ProjectService(ProjectRepository repo,
                          ResumeRepository resumeRepo) {
        this.repo = repo;
        this.resumeRepo = resumeRepo;
    }



    /*
     * CREATE PROJECT
     */
    public ProjectResponse create(String userId,
                                  CreateProjectRequest request) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);
        validateDates(request.getStartDate(), request.getEndDate());

        String slug = generateSlug(request.getTitle(), resume.getUserId());

        // ensure slug uniqueness
        repo.findByResumeIdAndSlug(resume.getId(), slug)
                .ifPresent(p -> {
                    throw new IllegalStateException("Project slug already exists");
                });

        int nextOrder = repo.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .size() + 1;

        Project entity = new Project();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(resume.getId());
        entity.setTitle(request.getTitle());
        entity.setProjectType(request.getProjectType());
        entity.setNdaRestricted(request.isNdaRestricted());
        entity.setSlug(slug);
        entity.setDescription(request.getDescription());
        entity.setKeyFeatures(request.getKeyFeatures());
        entity.setRoleInProject(request.getRoleInProject());
        entity.setClientName(request.getClientName());
        entity.setIndustry(request.getIndustry());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setProjectStatus(request.getProjectStatus());
        entity.setTechnologiesUsed(request.getTechnologiesUsed());
        entity.setToolsUsed(request.getToolsUsed());
        entity.setLiveUrl(request.getLiveUrl());
        entity.setSourceCodeUrl(request.getSourceCodeUrl());
        entity.setCaseStudyUrl(request.getCaseStudyUrl());
        entity.setFeatured(request.isFeatured());
        entity.setVisibility(request.getVisibility());
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * UPDATE PROJECT
     */
    public ProjectResponse update(String userId,
                                  String projectId,
                                  UpdateProjectRequest request) {

        Project project = repo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Resume resume = resumeRepo.findById(project.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);
        validateDates(request.getStartDate(), request.getEndDate());

        // Regenerate slug if title changed
        if (!project.getTitle().equals(request.getTitle())) {

            String newSlug = generateSlug(request.getTitle(), resume.getUserId());

            repo.findByResumeIdAndSlug(resume.getId(), newSlug)
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(project.getId())) {
                            throw new IllegalStateException("Project slug already exists");
                        }
                    });

            project.setSlug(newSlug);
            project.setTitle(request.getTitle());
        }

        project.setProjectType(request.getProjectType());
        project.setNdaRestricted(request.isNdaRestricted());
        project.setDescription(request.getDescription());
        project.setKeyFeatures(request.getKeyFeatures());
        project.setRoleInProject(request.getRoleInProject());
        project.setClientName(request.getClientName());
        project.setIndustry(request.getIndustry());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setProjectStatus(request.getProjectStatus());
        project.setTechnologiesUsed(request.getTechnologiesUsed());
        project.setToolsUsed(request.getToolsUsed());
        project.setLiveUrl(request.getLiveUrl());
        project.setSourceCodeUrl(request.getSourceCodeUrl());
        project.setCaseStudyUrl(request.getCaseStudyUrl());
        project.setFeatured(request.isFeatured());
        project.setVisibility(request.getVisibility());
        project.setUpdatedAt(Instant.now());

        return map(repo.save(project));
    }

    /*
     * DELETE PROJECT
     */
    public void delete(String userId, String projectId) {

        Project project = repo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Resume resume = resumeRepo.findById(project.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        repo.delete(project);
    }

    /*
     * GET PRIVATE PROJECTS (Dashboard)
     */
    public List<ProjectResponse> getByResume(String userId,
                                             String resumeId) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * GET PUBLIC PROJECTS
     */
    public List<PublicProjectResponse> getPublic(String resumeId) {

        return repo.findByResumeIdAndVisibility(resumeId, VisibilityType.PUBLIC)
                .stream()
                .filter(p -> !p.isNdaRestricted())
                .map(this::mapPublic)
                .collect(Collectors.toList());
    }

    /*
     * GET FEATURED PUBLIC PROJECTS
     */
    public List<PublicProjectResponse> getFeatured(String resumeId) {

        return repo.findByResumeIdAndFeaturedTrue(resumeId)
                .stream()
                .filter(p -> p.getVisibility() == VisibilityType.PUBLIC)
                .filter(p -> !p.isNdaRestricted())
                .map(this::mapPublic)
                .collect(Collectors.toList());
    }

    /*
     * REORDER PROJECTS
     */
    public void reorder(String userId,
                        String resumeId,
                        List<String> orderedIds) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int order = 1;

        for (String id : orderedIds) {

            Project project = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

            if (!project.getResumeId().equals(resumeId)) {
                throw new IllegalStateException("Project does not belong to resume");
            }

            project.setDisplayOrder(order++);
            project.setUpdatedAt(Instant.now());
            repo.save(project);
        }
    }

    /*
     * UTILITIES
     */

    private void validateOwnership(Resume resume, String userId) {
        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    private String generateSlug(String title, String userId) {

        String base = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");

        return base + "-" + userId.substring(0, 6);
    }

    private ProjectResponse map(Project p) {

        ProjectResponse r = new ProjectResponse();
        r.setId(p.getId());
        r.setResumeId(p.getResumeId());
        r.setTitle(p.getTitle());
        r.setProjectType(p.getProjectType());
        r.setNdaRestricted(p.isNdaRestricted());
        r.setSlug(p.getSlug());
        r.setDescription(p.getDescription());
        r.setKeyFeatures(p.getKeyFeatures());
        r.setRoleInProject(p.getRoleInProject());
        r.setClientName(p.getClientName());
        r.setIndustry(p.getIndustry());
        r.setStartDate(p.getStartDate());
        r.setEndDate(p.getEndDate());
        r.setProjectStatus(p.getProjectStatus());
        r.setTechnologiesUsed(p.getTechnologiesUsed());
        r.setToolsUsed(p.getToolsUsed());
        r.setLiveUrl(p.getLiveUrl());
        r.setSourceCodeUrl(p.getSourceCodeUrl());
        r.setCaseStudyUrl(p.getCaseStudyUrl());
        r.setFeatured(p.isFeatured());
        r.setVisibility(p.getVisibility());
        r.setDisplayOrder(p.getDisplayOrder());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }

    private PublicProjectResponse mapPublic(Project p) {

        PublicProjectResponse r = new PublicProjectResponse();

        r.setTitle(p.getTitle());
        r.setSlug(p.getSlug());
        r.setProjectType(p.getProjectType());

        if (!p.isNdaRestricted()) {
            r.setDescription(p.getDescription());
            r.setKeyFeatures(p.getKeyFeatures());
            r.setRoleInProject(p.getRoleInProject());
            r.setIndustry(p.getIndustry());
            r.setTechnologiesUsed(p.getTechnologiesUsed());
        } else {
            r.setDescription("Confidential project under NDA");
        }

        r.setStartDate(p.getStartDate());
        r.setEndDate(p.getEndDate());
        r.setProjectStatus(p.getProjectStatus());
        r.setLiveUrl(p.getLiveUrl());
        r.setCaseStudyUrl(p.getCaseStudyUrl());
        r.setFeatured(p.isFeatured());

        return r;
    }
}