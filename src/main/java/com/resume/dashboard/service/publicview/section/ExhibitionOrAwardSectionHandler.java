package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicExhibitionOrAwardDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.repository.ExhibitionOrAwardRepository;

@Component
public class ExhibitionOrAwardSectionHandler implements SectionHandler {

    private final ExhibitionOrAwardRepository repository;

    public ExhibitionOrAwardSectionHandler(ExhibitionOrAwardRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.EXHIBITIONS_AWARDS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(e -> {
                    PublicExhibitionOrAwardDTO dto = new PublicExhibitionOrAwardDTO();
                    dto.setTitle(e.getTitle());
                    dto.setEventName(e.getEventName());
                    dto.setLocation(e.getLocation());
                    dto.setYear(e.getYear());
                    dto.setDescription(e.getDescription());
                    dto.setAwardType(e.getAwardType() != null ? e.getAwardType().name() : null);
                    return dto;
                }).toList();
    }
}
