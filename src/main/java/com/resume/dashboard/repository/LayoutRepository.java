package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.entity.VisualMood;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LayoutRepository extends MongoRepository<Layout, String> {

    List<Layout> findByActiveTrue();

    List<Layout> findByLayoutTypeAndActiveTrue(LayoutType layoutType);

    Optional<Layout> findByIdAndActiveTrue(String id);

    long countByLayoutType(LayoutType layoutType);
    
    List<Layout> findByTargetAudiencesContainingAndActiveTrue(LayoutAudience audience);
    List<Layout> findByCompatibleMoodsContainingAndActiveTrue(VisualMood mood);
}