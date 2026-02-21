package com.resume.dashboard.service;

import com.resume.dashboard.dto.resume.ResumeResponse;
import com.resume.dashboard.entity.Analytics;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.AnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PublicResumeService {

    private static final Logger log = LoggerFactory.getLogger(PublicResumeService.class);

    private final ResumeService resumeService;
    private final AnalyticsRepository analyticsRepository;

    @Autowired
    public PublicResumeService(ResumeService resumeService, AnalyticsRepository analyticsRepository) {
        this.resumeService = resumeService;
        this.analyticsRepository = analyticsRepository;
    }

    public ResumeResponse getPublishedResume(String id, String viewerIp) {
        Resume resume = resumeService.getResumeEntity(id);
        if (resume.getState() != Resume.ResumeState.PUBLISHED) {
            throw new ResourceNotFoundException("Resume not found or not published");
        }
        logView(id, viewerIp);
        var v = resumeService.getActiveVersion(id);
        return resumeService.toResponse(resume, v.getContent());
    }

    public void logDownload(String id, String viewerIp) {
        Resume resume = resumeService.getResumeEntity(id);
        if (resume.getState() != Resume.ResumeState.PUBLISHED) {
            throw new ResourceNotFoundException("Resume not found or not published");
        }
        Analytics a = new Analytics();
        a.setId(UUID.randomUUID().toString());
        a.setResumeId(id);
        a.setAction(Analytics.AnalyticsAction.DOWNLOAD);
        a.setViewerIp(viewerIp != null ? viewerIp : "unknown");
        a.setTimestamp(Instant.now());
        analyticsRepository.save(a);
    }

    public void logView(String id, String viewerIp) {
        Analytics a = new Analytics();
        a.setId(UUID.randomUUID().toString());
        a.setResumeId(id);
        a.setAction(Analytics.AnalyticsAction.VIEW);
        a.setViewerIp(viewerIp != null ? viewerIp : "unknown");
        a.setTimestamp(Instant.now());
        analyticsRepository.save(a);
    }
}
