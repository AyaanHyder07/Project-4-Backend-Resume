package com.resume.dashboard.repository;

import com.resume.dashboard.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    Page<AuditLog> findByActorId(String actorId, Pageable pageable);
    Page<AuditLog> findByAction(String action, Pageable pageable);
}
