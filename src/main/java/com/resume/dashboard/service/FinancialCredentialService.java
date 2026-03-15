package com.resume.dashboard.service;

import com.resume.dashboard.dto.financial.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.FinancialCredentialRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FinancialCredentialService {

    private final FinancialCredentialRepository repo;
    private final ResumeRepository resumeRepo;
    private final CloudinaryService cloudinaryService;

    public FinancialCredentialService(FinancialCredentialRepository repo,
            ResumeRepository resumeRepo,
            CloudinaryService cloudinaryService) {
this.repo = repo;
this.resumeRepo = resumeRepo;
this.cloudinaryService = cloudinaryService;
}

    /*
     * CREATE
     */
    public FinancialCredentialResponse create(String userId,
            CreateFinancialCredentialRequest request,
            MultipartFile proofFile){

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);
        validateDates(request.getIssueDate(), request.getValidTill());

        FinancialCredentialType type =
                FinancialCredentialType.valueOf(request.getCredentialType().toUpperCase());

        int nextOrder = 1;
        FinancialCredential last =
                repo.findTopByResumeIdOrderByDisplayOrderDesc(request.getResumeId());

        if (last != null) {
            nextOrder = last.getDisplayOrder() + 1;
        }

        FinancialCredential entity = new FinancialCredential();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(request.getResumeId());
        entity.setCredentialType(type);
        entity.setCertificationName(request.getCertificationName());
        entity.setLicenseNumber(request.getLicenseNumber());
        entity.setIssuingAuthority(request.getIssuingAuthority());
        entity.setIssueDate(request.getIssueDate());
        entity.setValidTill(request.getValidTill());
        entity.setStatus(calculateStatus(request.getValidTill()));
        entity.setRegion(request.getRegion());
        entity.setVisibility(request.getVisibility() != null ? request.getVisibility() : VisibilityType.PUBLIC);
        String proofUrl = request.getVerificationUrl(); // fallback if frontend sends external URL

        if (proofFile != null && !proofFile.isEmpty()) {

            proofUrl = cloudinaryService.uploadImage(
                    proofFile,
                    "resume/financial/" + resume.getId()
            );
        }

        entity.setVerificationUrl(proofUrl);
        entity.setVerified(false);
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * UPDATE
     */
    public FinancialCredentialResponse update(String userId,
            String id,
            UpdateFinancialCredentialRequest request,
            MultipartFile proofFile) {

        FinancialCredential entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        Resume resume = resumeRepo.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);
        validateDates(request.getIssueDate(), request.getValidTill());

        entity.setCredentialType(
                FinancialCredentialType.valueOf(request.getCredentialType().toUpperCase())
        );
        entity.setCertificationName(request.getCertificationName());
        entity.setLicenseNumber(request.getLicenseNumber());
        entity.setIssuingAuthority(request.getIssuingAuthority());
        entity.setIssueDate(request.getIssueDate());
        entity.setValidTill(request.getValidTill());

        if (request.getStatus() != null) {
            entity.setStatus(
                    CredentialStatus.valueOf(request.getStatus().toUpperCase())
            );
        } else {
            entity.setStatus(calculateStatus(request.getValidTill()));
        }

        entity.setRegion(request.getRegion());
        entity.setVisibility(request.getVisibility() != null ? request.getVisibility() : VisibilityType.PUBLIC);
        if (proofFile != null && !proofFile.isEmpty()) {

            String newUrl = cloudinaryService.uploadImage(
                    proofFile,
                    "resume/financial/" + resume.getId()
            );

            entity.setVerificationUrl(newUrl);
        } else if (request.getVerificationUrl() != null) {

            entity.setVerificationUrl(request.getVerificationUrl());
        }
        entity.setUpdatedAt(Instant.now());

        return map(repo.save(entity));
    }

    /*
     * DELETE
     */
    public void delete(String userId, String id) {

        FinancialCredential entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        Resume resume = resumeRepo.findById(entity.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        validateOwnership(resume, userId);

        repo.delete(entity);
    }

    /*
     * GET ALL (PRIVATE)
     */
    public List<FinancialCredentialResponse> getByResume(String userId,
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
     * GET PUBLIC (Only ACTIVE)
     */
    public List<PublicFinancialCredentialResponse> getPublic(String resumeId) {

        return repo.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .filter(c -> c.getStatus() == CredentialStatus.ACTIVE)
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
            FinancialCredential entity = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

            entity.setDisplayOrder(order++);
            entity.setUpdatedAt(Instant.now());
            repo.save(entity);
        }
    }

    /*
     * STATUS AUTO-CALCULATION
     */
    private CredentialStatus calculateStatus(LocalDate validTill) {

        if (validTill == null) {
            return CredentialStatus.ACTIVE;
        }

        if (validTill.isBefore(LocalDate.now())) {
            return CredentialStatus.EXPIRED;
        }

        return CredentialStatus.ACTIVE;
    }

    private void validateDates(LocalDate issueDate,
                               LocalDate validTill) {

        if (issueDate != null && validTill != null &&
                validTill.isBefore(issueDate)) {
            throw new IllegalArgumentException("Valid till cannot be before issue date");
        }
    }

    private void validateOwnership(Resume resume, String userId) {

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }
    }

    private FinancialCredentialResponse map(FinancialCredential entity) {

        FinancialCredentialResponse res = new FinancialCredentialResponse();
        res.setId(entity.getId());
        res.setResumeId(entity.getResumeId());
        res.setCredentialType(entity.getCredentialType().name());
        res.setCertificationName(entity.getCertificationName());
        res.setLicenseNumber(entity.getLicenseNumber());
        res.setIssuingAuthority(entity.getIssuingAuthority());
        res.setIssueDate(entity.getIssueDate());
        res.setValidTill(entity.getValidTill());
        res.setStatus(entity.getStatus().name());
        res.setRegion(entity.getRegion());
        res.setVerificationUrl(entity.getVerificationUrl());
        res.setVerified(entity.isVerified());
        res.setDisplayOrder(entity.getDisplayOrder());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());

        return res;
    }

    private PublicFinancialCredentialResponse mapPublic(FinancialCredential entity) {

        PublicFinancialCredentialResponse res =
                new PublicFinancialCredentialResponse();

        res.setCredentialType(entity.getCredentialType().name());
        res.setCertificationName(entity.getCertificationName());
        res.setIssuingAuthority(entity.getIssuingAuthority());
        res.setValidTill(entity.getValidTill());
        res.setRegion(entity.getRegion());
        res.setVerificationUrl(entity.getVerificationUrl());
        res.setVerified(entity.isVerified());

        return res;
    }
}