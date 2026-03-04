package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Project;
import com.resume.dashboard.entity.VisibilityType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findByResumeId(String resumeId);

    List<Project> findByResumeIdAndVisibility(
            String resumeId,
            VisibilityType visibility
    );

    List<Project> findByResumeIdAndFeaturedTrue(String resumeId);

    List<Project> findByResumeIdAndVisibilityAndFeaturedTrue(
            String resumeId,
            VisibilityType visibility
    );

    List<Project> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    Optional<Project> findByResumeIdAndSlug(String resumeId, String slug);

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);
}