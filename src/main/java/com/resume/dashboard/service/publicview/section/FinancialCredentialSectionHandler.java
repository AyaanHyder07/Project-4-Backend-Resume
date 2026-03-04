package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicFinancialCredentialDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.repository.FinancialCredentialRepository;

@Component
public class FinancialCredentialSectionHandler implements SectionHandler {

    private final FinancialCredentialRepository repository;

    public FinancialCredentialSectionHandler(FinancialCredentialRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.FINANCIAL_CREDENTIALS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(f -> {
                    PublicFinancialCredentialDTO dto = new PublicFinancialCredentialDTO();
                    dto.setCertificationName(f.getCertificationName());
                    dto.setIssuingAuthority(f.getIssuingAuthority());
                    dto.setIssueDate(f.getIssueDate());
                    dto.setValidTill(f.getValidTill());
                    dto.setRegion(f.getRegion());
                    dto.setVerificationUrl(f.getVerificationUrl());
                    dto.setVerified(f.isVerified());
                    return dto;
                }).toList();
    }
}
