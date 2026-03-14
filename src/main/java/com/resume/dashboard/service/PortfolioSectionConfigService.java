package com.resume.dashboard.service;

import com.resume.dashboard.dto.section.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.PortfolioSectionConfigRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortfolioSectionConfigService {

    private final PortfolioSectionConfigRepository repo;
    private final ResumeRepository resumeRepo;

    public PortfolioSectionConfigService(
            PortfolioSectionConfigRepository repo,
            ResumeRepository resumeRepo) {
        this.repo = repo;
        this.resumeRepo = resumeRepo;
    }

    /*
     * INITIALIZE DEFAULT SECTIONS ON RESUME CREATE
     */
    public void initializeDefaultSections(String resumeId) {

        int order = 1;

        for (PortfolioSectionType type : PortfolioSectionType.values()) {

            PortfolioSectionConfig config = new PortfolioSectionConfig();
            config.setId(UUID.randomUUID().toString());
            config.setResumeId(resumeId);
            config.setSectionName(type);
            config.setEnabled(true);
            config.setDisplayOrder(order++);
            config.setCreatedAt(Instant.now());
            config.setUpdatedAt(Instant.now());

            repo.save(config);
        }
    }

    /*
     * UPDATE SECTION SETTINGS
     */
    public PortfolioSectionResponse update(
            String userId,
            String configId,
            UpdatePortfolioSectionRequest request) {

        PortfolioSectionConfig config = repo.findById(configId)
                .orElseThrow(() -> new ResourceNotFoundException("Section config not found"));

        Resume resume = resumeRepo.findById(config.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        if (request.getEnabled() != null) {
            config.setEnabled(request.getEnabled());
        }

        if (request.getCustomTitle() != null) {
            config.setCustomTitle(request.getCustomTitle());
        }

        if (request.getDisplayOrder() != null) {
            config.setDisplayOrder(request.getDisplayOrder());
        }

        config.setUpdatedAt(Instant.now());

        return map(repo.save(config));
    }

    /*
     * GET SECTIONS FOR RESUME
     */
    public List<PortfolioSectionResponse> getSections(
            String userId,
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
     * REORDER
     */
    public void reorder(
            String userId,
            String resumeId,
            List<String> orderedIds) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int order = 1;

        for (String id : orderedIds) {

            PortfolioSectionConfig config = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

            config.setDisplayOrder(order++);
            config.setUpdatedAt(Instant.now());

            repo.save(config);
        }
    }

    private void validateOwnership(Resume resume, String userId) {
        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }
    }

    private PortfolioSectionResponse map(PortfolioSectionConfig config) {

        PortfolioSectionResponse res = new PortfolioSectionResponse();
        res.setId(config.getId());
        res.setResumeId(config.getResumeId());
        res.setSectionName(config.getSectionName());
        res.setEnabled(config.isEnabled());
        res.setDisplayOrder(config.getDisplayOrder());
        res.setCustomTitle(config.getCustomTitle());
        res.setCreatedAt(config.getCreatedAt());
        res.setUpdatedAt(config.getUpdatedAt());

        return res;
    }


}