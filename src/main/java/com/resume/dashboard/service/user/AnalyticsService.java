package com.resume.dashboard.service.user;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.resume.dashboard.dto.analytics.AnalyticsSummaryResponse;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.repository.AnalyticsEventRepository;

@Service
public class AnalyticsService {

    private final AnalyticsEventRepository analyticsRepository;

    public AnalyticsService(AnalyticsEventRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    /* ==========================================
       RECORD EVENT
    ========================================== */

    public void recordEvent(String resumeId,
                            AnalyticsEventType type,
                            Map<String, Object> metadata) {

        AnalyticsEvent event = new AnalyticsEvent();
        event.setResumeId(resumeId);
        event.setEventType(type);
        event.setMetadata(metadata);
        event.setCreatedAt(Instant.now());

        analyticsRepository.save(event);
    }

    /* ==========================================
       GET SUMMARY
    ========================================== */

    public AnalyticsSummaryResponse getSummary(String resumeId) {

        AnalyticsSummaryResponse response = new AnalyticsSummaryResponse();

        response.setTotalViews(
                analyticsRepository.countByResumeIdAndEventType(
                        resumeId, AnalyticsEventType.VIEW)
        );

        response.setProjectClicks(
                analyticsRepository.countByResumeIdAndEventType(
                        resumeId, AnalyticsEventType.CLICK_PROJECT)
        );

        response.setContactClicks(
                analyticsRepository.countByResumeIdAndEventType(
                        resumeId, AnalyticsEventType.CLICK_CONTACT)
        );

        response.setDownloads(
                analyticsRepository.countByResumeIdAndEventType(
                        resumeId, AnalyticsEventType.DOWNLOAD_RESUME)
        );

        return response;
    }
}