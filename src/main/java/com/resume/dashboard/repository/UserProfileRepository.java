package com.resume.dashboard.repository;

import com.resume.dashboard.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findByResumeId(String resumeId);

    boolean existsByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);
}