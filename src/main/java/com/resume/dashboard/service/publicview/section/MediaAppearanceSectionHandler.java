package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicMediaAppearanceDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.repository.MediaAppearanceRepository;

@Component
public class MediaAppearanceSectionHandler implements SectionHandler {

    private final MediaAppearanceRepository repository;

    public MediaAppearanceSectionHandler(MediaAppearanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.MEDIA_APPEARANCES;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(m -> {
                    PublicMediaAppearanceDTO dto = new PublicMediaAppearanceDTO();
                    dto.setPlatformName(m.getPlatformName());
                    dto.setEpisodeTitle(m.getEpisodeTitle());
                    dto.setUrl(m.getUrl());
                    dto.setDescription(m.getDescription());
                    dto.setAppearanceDate(m.getAppearanceDate());
                    return dto;
                }).toList();
    }
}
