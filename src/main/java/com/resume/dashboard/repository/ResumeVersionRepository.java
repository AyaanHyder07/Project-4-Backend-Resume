package com.resume.dashboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.resume.dashboard.entity.ResumeVersion;

public interface ResumeVersionRepository extends MongoRepository<ResumeVersion, String> {



    Optional<ResumeVersion> findByIdAndUserId(String id, String userId);
    List<ResumeVersion> findByResumeIdOrderByCreatedAtDesc(String resumeId);
}