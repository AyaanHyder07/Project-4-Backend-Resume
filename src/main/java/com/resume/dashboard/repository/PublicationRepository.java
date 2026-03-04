package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Publication;
import com.resume.dashboard.entity.PublicationType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends MongoRepository<Publication, String> {

    List<Publication> findByResumeId(String resumeId);

    List<Publication> findByResumeIdAndType(String resumeId, PublicationType type);

    List<Publication> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    Optional<Publication> findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);
}