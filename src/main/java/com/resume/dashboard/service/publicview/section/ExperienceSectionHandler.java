package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicExperienceDTO;
import com.resume.dashboard.entity.Experience;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.repository.ExperienceRepository;

@Component
public class ExperienceSectionHandler implements SectionHandler {

    private final ExperienceRepository repository;

    public ExperienceSectionHandler(ExperienceRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.EXPERIENCE;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    private PublicExperienceDTO map(Experience e) {
        PublicExperienceDTO dto = new PublicExperienceDTO();
        dto.setOrganizationName(e.getOrganizationName());
        dto.setRoleTitle(e.getRoleTitle());
        dto.setLocation(e.getLocation());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        dto.setCurrentlyWorking(e.isCurrentlyWorking());
        dto.setRoleDescription(e.getRoleDescription());
        dto.setSkillsUsed(e.getSkillsUsed());
        return dto;
    }
}