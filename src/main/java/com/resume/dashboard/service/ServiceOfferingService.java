package com.resume.dashboard.service;

import com.resume.dashboard.dto.service.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.ServiceOfferingRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceOfferingService {

    private final ServiceOfferingRepository repo;
    private final ResumeRepository resumeRepository;

    public ServiceOfferingService(ServiceOfferingRepository repo,
                                   ResumeRepository resumeRepository) {
        this.repo = repo;
        this.resumeRepository = resumeRepository;
    }

    public ServiceOfferingResponse create(String userId,
                                          CreateServiceOfferingRequest request) {

        Resume resume = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        int nextOrder = repo.findTopByResumeIdOrderByDisplayOrderDesc(resume.getId())
                .map(s -> s.getDisplayOrder() + 1)
                .orElse(1);

        ServiceOffering entity = new ServiceOffering();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(resume.getId());
        entity.setServiceTitle(request.getServiceTitle());
        entity.setServiceCategory(request.getServiceCategory());
        entity.setDescription(request.getDescription());
        entity.setPricingModel(request.getPricingModel());
        entity.setBasePrice(request.getBasePrice());
        entity.setCurrency(request.getCurrency());
        entity.setDuration(request.getDuration());
        entity.setDeliverables(request.getDeliverables());
        entity.setTargetAudience(request.getTargetAudience());
        entity.setFeatured(request.isFeatured());
        entity.setVisibility(
                request.getVisibility() != null ? request.getVisibility() : VisibilityType.PUBLIC
        );
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    public ServiceOfferingResponse update(String userId,
                                          String id,
                                          UpdateServiceOfferingRequest request) {

        ServiceOffering entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        Resume resume = resumeRepository.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        if (request.getServiceTitle() != null)
            entity.setServiceTitle(request.getServiceTitle());

        if (request.getServiceCategory() != null)
            entity.setServiceCategory(request.getServiceCategory());

        if (request.getDescription() != null)
            entity.setDescription(request.getDescription());

        if (request.getPricingModel() != null)
            entity.setPricingModel(request.getPricingModel());

        if (request.getBasePrice() != null)
            entity.setBasePrice(request.getBasePrice());

        if (request.getCurrency() != null)
            entity.setCurrency(request.getCurrency());

        if (request.getDuration() != null)
            entity.setDuration(request.getDuration());

        if (request.getDeliverables() != null)
            entity.setDeliverables(request.getDeliverables());

        if (request.getTargetAudience() != null)
            entity.setTargetAudience(request.getTargetAudience());

        if (request.getFeatured() != null)
            entity.setFeatured(request.getFeatured());

        if (request.getVisibility() != null)
            entity.setVisibility(request.getVisibility());

        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    public void delete(String userId, String id) {

        ServiceOffering entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        Resume resume = resumeRepository.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        repo.delete(entity);
    }

    public List<ServiceOfferingResponse> getByResume(String userId, String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<PublicServiceOfferingResponse> getPublic(String resumeId) {

        return repo.findByResumeIdAndVisibility(resumeId, VisibilityType.PUBLIC)
                .stream()
                .map(this::mapPublic)
                .collect(Collectors.toList());
    }

    private void validateOwnership(Resume resume, String userId) {
        if (!resume.getUserId().equals(userId)) {
            throw new AccessDeniedException("Unauthorized access");
        }
    }

    private ServiceOfferingResponse map(ServiceOffering s) {

        ServiceOfferingResponse r = new ServiceOfferingResponse();
        r.setId(s.getId());
        r.setResumeId(s.getResumeId());
        r.setServiceTitle(s.getServiceTitle());
        r.setServiceCategory(s.getServiceCategory());
        r.setDescription(s.getDescription());
        r.setPricingModel(s.getPricingModel());
        r.setBasePrice(s.getBasePrice());
        r.setCurrency(s.getCurrency());
        r.setDuration(s.getDuration());
        r.setDeliverables(s.getDeliverables());
        r.setTargetAudience(s.getTargetAudience());
        r.setFeatured(s.isFeatured());
        r.setVisibility(s.getVisibility());
        r.setDisplayOrder(s.getDisplayOrder());
        r.setCreatedAt(s.getCreatedAt());
        r.setUpdatedAt(s.getUpdatedAt());
        return r;
    }

    private PublicServiceOfferingResponse mapPublic(ServiceOffering s) {

        PublicServiceOfferingResponse r = new PublicServiceOfferingResponse();
        r.setServiceTitle(s.getServiceTitle());
        r.setServiceCategory(s.getServiceCategory());
        r.setDescription(s.getDescription());
        r.setPricingModel(s.getPricingModel());
        r.setBasePrice(s.getBasePrice());
        r.setCurrency(s.getCurrency());
        r.setDuration(s.getDuration());
        r.setDeliverables(s.getDeliverables());
        r.setTargetAudience(s.getTargetAudience());
        return r;
    }
}