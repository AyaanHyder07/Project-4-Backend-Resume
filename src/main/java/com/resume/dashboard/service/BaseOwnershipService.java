package com.resume.dashboard.service;

import org.springframework.stereotype.Service;

import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ResumeRepository;

@Service
public class BaseOwnershipService {

    private final ResumeRepository resumeRepository;

    public BaseOwnershipService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume validateResumeOwnership(String resumeId, String userId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return resume;
    }
}