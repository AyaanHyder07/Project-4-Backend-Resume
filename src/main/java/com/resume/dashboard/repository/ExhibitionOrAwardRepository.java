package com.resume.dashboard.repository;

import com.resume.dashboard.entity.AwardType;
import com.resume.dashboard.entity.ExhibitionOrAward;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExhibitionOrAwardRepository extends MongoRepository<ExhibitionOrAward, String> {

    List<ExhibitionOrAward> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    List<ExhibitionOrAward> findByResumeIdAndAwardTypeOrderByDisplayOrderAsc(
            String resumeId,
            AwardType awardType
    );

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);

    ExhibitionOrAward findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);
}