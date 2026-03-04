package com.resume.dashboard.repository;

import com.resume.dashboard.entity.ProjectGallery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectGalleryRepository extends MongoRepository<ProjectGallery, String> {

    List<ProjectGallery> findByProjectId(String projectId);

    List<ProjectGallery> findByProjectIdOrderByDisplayOrderAsc(String projectId);

    Optional<ProjectGallery> findTopByProjectIdOrderByDisplayOrderDesc(String projectId);

    long countByProjectId(String projectId);

    void deleteByProjectId(String projectId);
}