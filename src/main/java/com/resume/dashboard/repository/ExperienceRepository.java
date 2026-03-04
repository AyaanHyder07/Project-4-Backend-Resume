package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Experience;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExperienceRepository extends MongoRepository<Experience, String> {

    List<Experience> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);

    Experience findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);
}