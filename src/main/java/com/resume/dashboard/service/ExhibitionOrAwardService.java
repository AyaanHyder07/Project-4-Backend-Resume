package com.resume.dashboard.service;

import com.resume.dashboard.dto.exhibition.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ExhibitionOrAwardRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExhibitionOrAwardService {

    private final ExhibitionOrAwardRepository repo;
    private final ResumeRepository resumeRepo;

    public ExhibitionOrAwardService(ExhibitionOrAwardRepository repo,
                                    ResumeRepository resumeRepo) {
        this.repo = repo;
        this.resumeRepo = resumeRepo;
    }

    /*
     * CREATE
     */
    public ExhibitionResponse create(String userId,
                                     CreateExhibitionRequest request) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        validateYear(request.getYear());

        AwardType type = AwardType.valueOf(request.getAwardType().toUpperCase());

        int nextOrder = 1;
        ExhibitionOrAward last =
                repo.findTopByResumeIdOrderByDisplayOrderDesc(request.getResumeId());

        if (last != null) {
            nextOrder = last.getDisplayOrder() + 1;
        }

        ExhibitionOrAward entity = new ExhibitionOrAward();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(request.getResumeId());
        entity.setTitle(request.getTitle());
        entity.setEventName(request.getEventName());
        entity.setLocation(request.getLocation());
        entity.setYear(request.getYear());
        entity.setDescription(request.getDescription());
        entity.setAwardType(type);
        entity.setVisibility(request.getVisibility() != null ? request.getVisibility() : VisibilityType.PUBLIC);
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * UPDATE
     */
    public ExhibitionResponse update(String userId,
                                     String id,
                                     UpdateExhibitionRequest request) {

        ExhibitionOrAward entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));

        Resume resume = resumeRepo.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        validateYear(request.getYear());

        entity.setTitle(request.getTitle());
        entity.setEventName(request.getEventName());
        entity.setLocation(request.getLocation());
        entity.setYear(request.getYear());
        entity.setDescription(request.getDescription());
        entity.setAwardType(
                AwardType.valueOf(request.getAwardType().toUpperCase())
        );
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * DELETE
     */
    public void delete(String userId, String id) {

        ExhibitionOrAward entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));

        Resume resume = resumeRepo.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        repo.delete(entity);
    }

    /*
     * GET ALL
     */
    public List<ExhibitionResponse> getAll(String userId,
                                           String resumeId) {

        validateOwnership(userId, resumeId);

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * FILTER BY TYPE
     */
    public List<ExhibitionResponse> getByType(String userId,
                                              String resumeId,
                                              AwardType type) {

        validateOwnership(userId, resumeId);

        return repo
                .findByResumeIdAndAwardTypeOrderByDisplayOrderAsc(resumeId, type)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * REORDER
     */
    public void reorder(String userId,
                        String resumeId,
                        List<String> orderedIds) {

        validateOwnership(userId, resumeId);

        int order = 1;

        for (String id : orderedIds) {
            ExhibitionOrAward entity = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));

            entity.setDisplayOrder(order++);
            entity.setUpdatedAt(Instant.now());
            repo.save(entity);
        }
    }

    private void validateOwnership(String userId, String resumeId) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    private void validateYear(int year) {

        int currentYear = Year.now().getValue();

        if (year > currentYear) {
            throw new IllegalArgumentException("Year cannot be in future");
        }
    }

    private ExhibitionResponse map(ExhibitionOrAward entity) {

        ExhibitionResponse res = new ExhibitionResponse();
        res.setId(entity.getId());
        res.setResumeId(entity.getResumeId());
        res.setTitle(entity.getTitle());
        res.setEventName(entity.getEventName());
        res.setLocation(entity.getLocation());
        res.setYear(entity.getYear());
        res.setDescription(entity.getDescription());
        res.setAwardType(entity.getAwardType().name());
        res.setDisplayOrder(entity.getDisplayOrder());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());

        return res;
    }
}