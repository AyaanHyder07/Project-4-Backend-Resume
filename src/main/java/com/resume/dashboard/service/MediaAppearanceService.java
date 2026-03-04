package com.resume.dashboard.service;

import com.resume.dashboard.dto.media.*;
import com.resume.dashboard.entity.MediaAppearance;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.MediaAppearanceRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MediaAppearanceService {

    private final MediaAppearanceRepository repo;
    private final ResumeRepository resumeRepo;
    

    public MediaAppearanceService(MediaAppearanceRepository repo,
                                   ResumeRepository resumeRepo) {
        this.repo = repo;
        this.resumeRepo = resumeRepo;
    }

    /*
     * CREATE
     */
    public MediaAppearanceResponse create(String userId,
                                          CreateMediaAppearanceRequest request) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int nextOrder = 1;
        List<MediaAppearance> existing =
                repo.findByResumeIdOrderByDisplayOrderAsc(request.getResumeId());

        if (!existing.isEmpty()) {
            nextOrder = existing.get(existing.size() - 1).getDisplayOrder() + 1;
        }

        MediaAppearance entity = new MediaAppearance();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(request.getResumeId());
        entity.setPlatformName(request.getPlatformName());
        entity.setEpisodeTitle(request.getEpisodeTitle());
        entity.setUrl(request.getUrl());
        entity.setDescription(request.getDescription());
        entity.setAppearanceDate(request.getAppearanceDate());
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * UPDATE
     */
    public MediaAppearanceResponse update(String userId,
                                          String id,
                                          UpdateMediaAppearanceRequest request) {

        MediaAppearance entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media appearance not found"));

        Resume resume = resumeRepo.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        entity.setPlatformName(request.getPlatformName());
        entity.setEpisodeTitle(request.getEpisodeTitle());
        entity.setUrl(request.getUrl());
        entity.setDescription(request.getDescription());
        entity.setAppearanceDate(request.getAppearanceDate());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * DELETE
     */
    public void delete(String userId, String id) {

        MediaAppearance entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media appearance not found"));

        Resume resume = resumeRepo.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        repo.delete(entity);
    }

    /*
     * PRIVATE LIST
     */
    public List<MediaAppearanceResponse> getByResume(String userId,
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
     * PUBLIC LIST
     */
    public List<PublicMediaAppearanceResponse> getPublic(String resumeId) {

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::mapPublic)
                .collect(Collectors.toList());
    }

    /*
     * REORDER
     */
    public void reorder(String userId,
                        String resumeId,
                        List<String> orderedIds) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int order = 1;

        for (String id : orderedIds) {
            MediaAppearance entity = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Media appearance not found"));

            entity.setDisplayOrder(order++);
            entity.setUpdatedAt(Instant.now());
            repo.save(entity);
        }
    }

    private void validateOwnership(Resume resume, String userId) {
        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    private MediaAppearanceResponse map(MediaAppearance entity) {

        MediaAppearanceResponse res = new MediaAppearanceResponse();
        res.setId(entity.getId());
        res.setResumeId(entity.getResumeId());
        res.setPlatformName(entity.getPlatformName());
        res.setEpisodeTitle(entity.getEpisodeTitle());
        res.setUrl(entity.getUrl());
        res.setDescription(entity.getDescription());
        res.setAppearanceDate(entity.getAppearanceDate());
        res.setDisplayOrder(entity.getDisplayOrder());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());

        return res;
    }

    private PublicMediaAppearanceResponse mapPublic(MediaAppearance entity) {

        PublicMediaAppearanceResponse res = new PublicMediaAppearanceResponse();
        res.setPlatformName(entity.getPlatformName());
        res.setEpisodeTitle(entity.getEpisodeTitle());
        res.setUrl(entity.getUrl());
        res.setDescription(entity.getDescription());
        res.setAppearanceDate(entity.getAppearanceDate());

        return res;
    }
}