package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Skill;
import com.resume.dashboard.entity.SkillCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends MongoRepository<Skill, String> {

    List<Skill> findByResumeId(String resumeId);

    List<Skill> findByResumeIdAndCategory(String resumeId, SkillCategory category);

    List<Skill> findByResumeIdAndFeaturedTrue(String resumeId);

    List<Skill> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    Optional<Skill> findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);
}