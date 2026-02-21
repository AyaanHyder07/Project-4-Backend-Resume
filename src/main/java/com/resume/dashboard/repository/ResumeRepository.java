package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResumeRepository extends MongoRepository<Resume, String> {
    List<Resume> findByUserIdOrderByUpdatedAtDesc(String userId);
    Page<Resume> findByState(Resume.ResumeState state, Pageable pageable);
    Page<Resume> findByStateAndUserId(Resume.ResumeState state, String userId, Pageable pageable);
    Page<Resume> findByUserId(String userId, Pageable pageable);
    long countByState(Resume.ResumeState state);
}
