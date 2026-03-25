package com.resume.dashboard.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.resume.dashboard.entity.PortfolioBlock;

public interface PortfolioBlockRepository extends MongoRepository<PortfolioBlock, String> {
    List<PortfolioBlock> findByResumeIdOrderByDisplayOrderAsc(String resumeId);
    List<PortfolioBlock> findByResumeIdAndEnabledTrueOrderByDisplayOrderAsc(String resumeId);
    long countByResumeId(String resumeId);
}
