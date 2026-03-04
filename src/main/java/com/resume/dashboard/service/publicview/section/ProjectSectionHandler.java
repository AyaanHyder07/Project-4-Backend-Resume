package com.resume.dashboard.service.publicview.section;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicProjectDTO;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.repository.ProjectRepository;

@Component
public class ProjectSectionHandler implements SectionHandler {

    private final ProjectRepository projectRepository;

    public ProjectSectionHandler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.PROJECTS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return projectRepository
                .findByResumeIdAndVisibility(resume.getId(), VisibilityType.PUBLIC)
                .stream()
                .filter(p -> !p.isNdaRestricted())
                .map(this::map)
                .collect(Collectors.toList());
    }

    private PublicProjectDTO map(Project p) {
        PublicProjectDTO dto = new PublicProjectDTO();
        dto.setTitle(p.getTitle());
        dto.setSlug(p.getSlug());
        dto.setDescription(p.getDescription());
        dto.setRoleInProject(p.getRoleInProject());
        dto.setIndustry(p.getIndustry());
        dto.setStartDate(p.getStartDate());
        dto.setEndDate(p.getEndDate());
        dto.setTechnologiesUsed(p.getTechnologiesUsed());
        dto.setLiveUrl(p.getLiveUrl());
        dto.setFeatured(p.isFeatured());
        return dto;
    }
}