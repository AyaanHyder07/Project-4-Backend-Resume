package com.resume.dashboard.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.resume.dashboard.entity.UserThemeCustomization;

@Repository
public interface UserThemeCustomizationRepository
        extends MongoRepository<UserThemeCustomization, String> {
    Optional<UserThemeCustomization> findByUserIdAndResumeId(String userId, String resumeId);
}