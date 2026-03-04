package com.resume.dashboard.repository;

import com.resume.dashboard.entity.PortfolioSectionConfig;
import com.resume.dashboard.entity.PortfolioSectionType;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioSectionConfigRepository extends MongoRepository<PortfolioSectionConfig, String> {
    List<PortfolioSectionConfig> findByResumeId(String resumeId);
    Optional<PortfolioSectionConfig> findByResumeIdAndSectionName(
            String resumeId,
            PortfolioSectionType sectionName
    );
    List<PortfolioSectionConfig> findByResumeIdOrderByDisplayOrderAsc(String resumeId);
    
    
}
