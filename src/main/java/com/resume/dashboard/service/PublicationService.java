package com.resume.dashboard.service;

import com.resume.dashboard.dto.publication.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.PublicationRepository;
import com.resume.dashboard.repository.ResumeRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublicationService {

    private final PublicationRepository repo;
    private final ResumeRepository resumeRepository;

    public PublicationService(PublicationRepository repo,
                              ResumeRepository resumeRepository) {
        this.repo = repo;
        this.resumeRepository = resumeRepository;
    }

    /*
     * CREATE
     */
    public PublicationResponse create(String userId,
                                      CreatePublicationRequest request) {

        Resume resume = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int nextOrder = repo.findTopByResumeIdOrderByDisplayOrderDesc(resume.getId())
                .map(p -> p.getDisplayOrder() + 1)
                .orElse(1);

        Publication entity = new Publication();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(resume.getId());
        entity.setTitle(request.getTitle());
        entity.setType(request.getType());
        entity.setPublisher(request.getPublisher());
        entity.setPublicationDate(request.getPublicationDate());
        entity.setAbstractText(request.getAbstractText());
        entity.setContentUrl(request.getContentUrl());
        entity.setKeywords(request.getKeywords());
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * UPDATE
     */
    public PublicationResponse update(String userId,
                                      String publicationId,
                                      UpdatePublicationRequest request) {

        Publication entity = repo.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        Resume resume = resumeRepository.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        if (request.getTitle() != null)
            entity.setTitle(request.getTitle());

        if (request.getType() != null)
            entity.setType(request.getType());

        if (request.getPublisher() != null)
            entity.setPublisher(request.getPublisher());

        if (request.getPublicationDate() != null)
            entity.setPublicationDate(request.getPublicationDate());

        if (request.getAbstractText() != null)
            entity.setAbstractText(request.getAbstractText());

        if (request.getContentUrl() != null)
            entity.setContentUrl(request.getContentUrl());

        if (request.getKeywords() != null)
            entity.setKeywords(request.getKeywords());

        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * DELETE
     */
    public void delete(String userId, String publicationId) {

        Publication entity = repo.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        Resume resume = resumeRepository.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        repo.delete(entity);
    }

    /*
     * PRIVATE LIST
     */
    public List<PublicationResponse> getByResume(String userId, String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
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
    public List<PublicPublicationResponse> getPublic(String resumeId) {

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::mapPublic)
                .collect(Collectors.toList());
    }

    /*
     * REORDER
     */
    @Transactional
    public void reorder(String userId,
                        String resumeId,
                        List<String> orderedIds) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int order = 1;

        for (String id : orderedIds) {

            Publication entity = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

            if (!entity.getResumeId().equals(resumeId)) {
                throw new AccessDeniedException("Publication mismatch");
            }

            entity.setDisplayOrder(order++);
            entity.setUpdatedAt(Instant.now());
            repo.save(entity);
        }
    }

    private void validateOwnership(Resume resume, String userId) {
        if (!resume.getUserId().equals(userId)) {
            throw new AccessDeniedException("Unauthorized access");
        }
    }

    private PublicationResponse map(Publication p) {

        PublicationResponse r = new PublicationResponse();
        r.setId(p.getId());
        r.setResumeId(p.getResumeId());
        r.setTitle(p.getTitle());
        r.setType(p.getType());
        r.setPublisher(p.getPublisher());
        r.setPublicationDate(p.getPublicationDate());
        r.setAbstractText(p.getAbstractText());
        r.setContentUrl(p.getContentUrl());
        r.setKeywords(p.getKeywords());
        r.setDisplayOrder(p.getDisplayOrder());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }

    private PublicPublicationResponse mapPublic(Publication p) {

        PublicPublicationResponse r = new PublicPublicationResponse();
        r.setTitle(p.getTitle());
        r.setType(p.getType());
        r.setPublisher(p.getPublisher());
        r.setPublicationDate(p.getPublicationDate());
        r.setAbstractText(p.getAbstractText());
        r.setContentUrl(p.getContentUrl());
        r.setKeywords(p.getKeywords());
        return r;
    }
}