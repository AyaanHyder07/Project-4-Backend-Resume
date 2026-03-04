package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Certification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CertificationRepository extends MongoRepository<Certification, String> {

    List<Certification> findByResumeId(String resumeId);

    List<Certification> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);

    Certification findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);
    
}