package com.resume.dashboard.repository;

import com.resume.dashboard.entity.CredentialStatus;
import com.resume.dashboard.entity.FinancialCredential;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FinancialCredentialRepository extends MongoRepository<FinancialCredential, String> {

    List<FinancialCredential> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    List<FinancialCredential> findByResumeIdAndStatusOrderByDisplayOrderAsc(
            String resumeId,
            CredentialStatus status
    );

    long countByResumeId(String resumeId);

    void deleteByResumeId(String resumeId);

    FinancialCredential findTopByResumeIdOrderByDisplayOrderDesc(String resumeId);
}
