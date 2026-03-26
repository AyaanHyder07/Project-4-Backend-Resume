package com.resume.dashboard.service;

import com.resume.dashboard.dto.certification.*;
import com.resume.dashboard.entity.Certification;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.CertificationRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepo;
    private final ResumeRepository resumeRepo;
    private final CloudinaryService cloudinaryService;

    public CertificationService(CertificationRepository certificationRepo,
            ResumeRepository resumeRepo,
            CloudinaryService cloudinaryService) {
        this.certificationRepo = certificationRepo;
        this.resumeRepo = resumeRepo;
        this.cloudinaryService = cloudinaryService;
    }

    public CertificationResponse create(String userId,
            CreateCertificationRequest request,
            MultipartFile certificateFile) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        int nextOrder = 1;
        Certification last = certificationRepo
                .findTopByResumeIdOrderByDisplayOrderDesc(request.getResumeId());

        if (last != null) {
            nextOrder = last.getDisplayOrder() + 1;
        }

        Certification cert = new Certification();
        cert.setId(UUID.randomUUID().toString());
        cert.setResumeId(request.getResumeId());
        cert.setTitle(request.getTitle());

        if (certificateFile != null && !certificateFile.isEmpty()) {
            cert.setCertificateUrl(
                cloudinaryService.uploadDocument(certificateFile, "resume/certifications/" + resume.getId())
            );
        }

        cert.setDisplayOrder(nextOrder);
        cert.setCreatedAt(Instant.now());
        cert.setUpdatedAt(Instant.now());

        return map(certificationRepo.save(cert));
    }

    public CertificationResponse update(String userId,
            String certificationId,
            UpdateCertificationRequest request,
            MultipartFile certificateFile) {

        Certification cert = certificationRepo.findById(certificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));

        Resume resume = resumeRepo.findById(cert.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        cert.setTitle(request.getTitle());
        if (certificateFile != null && !certificateFile.isEmpty()) {
            cert.setCertificateUrl(
                cloudinaryService.uploadDocument(certificateFile, "resume/certifications/" + resume.getId())
            );
        }
        cert.setUpdatedAt(Instant.now());

        return map(certificationRepo.save(cert));
    }

    public void delete(String userId, String certificationId) {

        Certification cert = certificationRepo.findById(certificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));

        Resume resume = resumeRepo.findById(cert.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        certificationRepo.delete(cert);
    }

    public List<CertificationResponse> getByResume(String userId, String resumeId) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        return certificationRepo
                .findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public void reorder(String userId, String resumeId, List<String> orderedIds) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        int order = 1;

        for (String id : orderedIds) {
            Certification cert = certificationRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));

            cert.setDisplayOrder(order++);
            cert.setUpdatedAt(Instant.now());
            certificationRepo.save(cert);
        }
    }

    private CertificationResponse map(Certification cert) {
        CertificationResponse res = new CertificationResponse();
        res.setId(cert.getId());
        res.setResumeId(cert.getResumeId());
        res.setTitle(cert.getTitle());
        res.setCertificateUrl(cert.getCertificateUrl());
        res.setDisplayOrder(cert.getDisplayOrder());
        res.setCreatedAt(cert.getCreatedAt());
        res.setUpdatedAt(cert.getUpdatedAt());
        return res;
    }
}
