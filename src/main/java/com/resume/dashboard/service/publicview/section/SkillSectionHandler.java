package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicSkillDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Skill;
import com.resume.dashboard.repository.SkillRepository;

@Component
public class SkillSectionHandler implements SectionHandler {

    private final SkillRepository repository;

    public SkillSectionHandler(SkillRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.SKILLS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    private PublicSkillDTO map(Skill skill) {
        PublicSkillDTO dto = new PublicSkillDTO();
        dto.setSkillName(skill.getSkillName());
        dto.setCategory(skill.getCategory().name());
        dto.setYearsOfExperience(skill.getYearsOfExperience());
        return dto;
    }
}