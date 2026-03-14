package com.resume.dashboard.repository;

import com.resume.dashboard.entity.ApprovalStatus;
import com.resume.dashboard.entity.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends MongoRepository<Resume, String> {

    long countByUserId(String userId);

    boolean existsBySlug(String slug);

    Optional<Resume> findBySlugAndPublishedTrue(String slug);

    List<Resume> findByUserId(String userId);

    Optional<Resume> findByIdAndUserId(String id, String userId);
    List<Resume> findByUserIdOrderByCreatedAtDesc(String userId);
    


    Optional<Resume> findByUserIdAndPublishedTrue(String userId);


    boolean existsByUserId(String userId);
    
    long countByUserIdAndPublishedTrue(String userId);
    List<Resume> findByApprovalStatus(ApprovalStatus status);

}