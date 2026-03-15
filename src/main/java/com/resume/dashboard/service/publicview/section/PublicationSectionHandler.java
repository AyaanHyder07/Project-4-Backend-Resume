package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicPublicationDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Publication;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.repository.PublicationRepository;

@Component
public class PublicationSectionHandler implements SectionHandler {

    private final PublicationRepository repository;

    public PublicationSectionHandler(PublicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.PUBLICATIONS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    private PublicPublicationDTO map(Publication p) {
        PublicPublicationDTO dto = new PublicPublicationDTO();
        dto.setTitle(p.getTitle());
        dto.setType(p.getType() != null ? p.getType().name() : null);
        dto.setPublisher(p.getPublisher());
        dto.setPublicationDate(p.getPublicationDate());
        dto.setContentUrl(p.getContentUrl());
        dto.setKeywords(p.getKeywords());
        return dto;
    }
}
