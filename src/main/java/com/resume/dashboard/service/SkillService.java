package com.resume.dashboard.service;

import com.resume.dashboard.dto.skill.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.SkillRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository repo;
    private final ResumeRepository resumeRepository;

    public SkillService(SkillRepository repo,
                        ResumeRepository resumeRepository) {
        this.repo = repo;
        this.resumeRepository = resumeRepository;
    }

    public SkillResponse create(String userId,
                                CreateSkillRequest request) {

        Resume resume = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int nextOrder = repo.findTopByResumeIdOrderByDisplayOrderDesc(resume.getId())
                .map(s -> s.getDisplayOrder() + 1)
                .orElse(1);

        Skill entity = new Skill();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(resume.getId());
        entity.setSkillName(request.getSkillName());
        entity.setCategory(request.getCategory());
        entity.setProficiency(request.getProficiency());
        entity.setYearsOfExperience(request.getYearsOfExperience());
        entity.setFeatured(request.isFeatured());
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    public SkillResponse update(String userId,
                                String skillId,
                                UpdateSkillRequest request) {

        Skill entity = repo.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        Resume resume = resumeRepository.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        if (request.getSkillName() != null)
            entity.setSkillName(request.getSkillName());

        if (request.getCategory() != null)
            entity.setCategory(request.getCategory());

        if (request.getProficiency() != null)
            entity.setProficiency(request.getProficiency());

        if (request.getYearsOfExperience() != null)
            entity.setYearsOfExperience(request.getYearsOfExperience());

        if (request.getFeatured() != null)
            entity.setFeatured(request.getFeatured());

        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    public void delete(String userId, String skillId) {

        Skill entity = repo.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        Resume resume = resumeRepository.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        repo.delete(entity);
    }

    public List<SkillResponse> getByResume(String userId, String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<PublicSkillResponse> getPublic(String resumeId) {

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::mapPublic)
                .collect(Collectors.toList());
    }

    @Transactional
    public void reorder(String userId,
                        String resumeId,
                        List<String> orderedIds) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int order = 1;

        for (String id : orderedIds) {

            Skill entity = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

            if (!entity.getResumeId().equals(resumeId)) {
                throw new AccessDeniedException("Skill mismatch");
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

    private SkillResponse map(Skill s) {

        SkillResponse r = new SkillResponse();
        r.setId(s.getId());
        r.setResumeId(s.getResumeId());
        r.setSkillName(s.getSkillName());
        r.setCategory(s.getCategory());
        r.setProficiency(s.getProficiency());
        r.setYearsOfExperience(s.getYearsOfExperience());
        r.setFeatured(s.isFeatured());
        r.setDisplayOrder(s.getDisplayOrder());
        r.setCreatedAt(s.getCreatedAt());
        r.setUpdatedAt(s.getUpdatedAt());
        return r;
    }

    private PublicSkillResponse mapPublic(Skill s) {

        PublicSkillResponse r = new PublicSkillResponse();
        r.setSkillName(s.getSkillName());
        r.setCategory(s.getCategory());
        r.setProficiency(s.getProficiency());
        r.setYearsOfExperience(s.getYearsOfExperience());
        return r;
    }
}