package com.resume.dashboard.repository;

import com.resume.dashboard.entity.MediaAppearance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MediaAppearanceRepository extends MongoRepository<MediaAppearance, String> {
    List<MediaAppearance> findByResumeId(String resumeId);
    List<MediaAppearance> findByResumeIdOrderByDisplayOrderAsc(String resumeId);
    void deleteByResumeId(String resumeId);
    long countByResumeId(String resumeId);
}
