package com.resume.dashboard.repository;

import com.resume.dashboard.entity.ResumeVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeVersionRepository extends MongoRepository<ResumeVersion, String> {
    List<ResumeVersion> findByResumeIdOrderByVersionDesc(String resumeId);
    Optional<ResumeVersion> findByResumeIdAndVersion(String resumeId, int version);
}
