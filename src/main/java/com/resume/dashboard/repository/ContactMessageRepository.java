package com.resume.dashboard.repository;

import com.resume.dashboard.entity.ContactMessage;
import com.resume.dashboard.entity.ContactStatus;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactMessageRepository extends MongoRepository<ContactMessage, String> {

    List<ContactMessage> findByResumeIdOrderByReceivedAtDesc(String resumeId);

    List<ContactMessage> findByResumeIdAndStatusOrderByReceivedAtDesc(
            String resumeId,
            ContactStatus status
    );

    long countByResumeIdAndStatus(String resumeId, ContactStatus status);

    void deleteByResumeId(String resumeId);
}