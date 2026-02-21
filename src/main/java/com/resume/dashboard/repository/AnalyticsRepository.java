package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Analytics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnalyticsRepository extends MongoRepository<Analytics, String> {
    Page<Analytics> findByResumeId(String resumeId, Pageable pageable);
    Page<Analytics> findByAction(Analytics.AnalyticsAction action, Pageable pageable);
    Page<Analytics> findByResumeIdAndAction(String resumeId, Analytics.AnalyticsAction action, Pageable pageable);
}
