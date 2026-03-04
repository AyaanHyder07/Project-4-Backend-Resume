package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.repository.EducationRepository;

@Component
public class EducationSectionHandler implements SectionHandler {

    private final EducationRepository repository;

    public EducationSectionHandler(EducationRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.EDUCATION;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId());
    }
}
