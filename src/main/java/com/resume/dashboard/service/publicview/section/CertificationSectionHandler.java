package com.resume.dashboard.service.publicview.section;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicCertificationDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.repository.CertificationRepository;

@Component
public class CertificationSectionHandler implements SectionHandler {

    private final CertificationRepository repository;

    public CertificationSectionHandler(CertificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.CERTIFICATIONS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(c -> {
                    PublicCertificationDTO dto = new PublicCertificationDTO();
                    dto.setTitle(c.getTitle());
                    dto.setCertificateUrl(c.getCertificateUrl());
                    dto.setCreatedAt(c.getCreatedAt());
                    return dto;
                }).collect(Collectors.toList());
    }
}
