package com.resume.dashboard.repository;

import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.Theme;
import com.resume.dashboard.entity.VisualMood;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends MongoRepository<Theme, String> {

    List<Theme> findByActiveTrue();

    Optional<Theme> findByIdAndActiveTrue(String id);

    boolean existsByNameIgnoreCase(String name);
    
    List<Theme> findByTargetAudiencesContainingAndActiveTrue(LayoutAudience audience);
    List<Theme> findByMoodAndActiveTrue(VisualMood mood);
}