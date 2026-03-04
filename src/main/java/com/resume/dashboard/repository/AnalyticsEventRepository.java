package com.resume.dashboard.repository;

import com.resume.dashboard.entity.AnalyticsEvent;
import com.resume.dashboard.entity.AnalyticsEventType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnalyticsEventRepository
        extends MongoRepository<AnalyticsEvent, String> {

    List<AnalyticsEvent> findByResumeId(String resumeId);

    long countByResumeIdAndEventType(String resumeId, AnalyticsEventType eventType);
}