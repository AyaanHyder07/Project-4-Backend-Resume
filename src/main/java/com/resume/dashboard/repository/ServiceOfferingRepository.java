package com.resume.dashboard.repository;

import com.resume.dashboard.entity.ServiceOffering;
import com.resume.dashboard.entity.VisibilityType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceOfferingRepository extends MongoRepository<ServiceOffering, String> {

    List<ServiceOffering> findByResumeId(String resumeId);

    List<ServiceOffering> findByResumeIdAndFeaturedTrue(String resumeId);

    List<ServiceOffering> findByResumeIdAndVisibility(String resumeId, VisibilityType visibility);

    List<ServiceOffering> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    Optional<ServiceOffering> findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);
}