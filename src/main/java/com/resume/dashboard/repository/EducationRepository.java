package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Education;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EducationRepository extends MongoRepository<Education, String> {

    List<Education> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);

    Education findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);
}
