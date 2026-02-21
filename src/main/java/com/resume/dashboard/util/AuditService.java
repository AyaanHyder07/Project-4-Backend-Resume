package com.resume.dashboard.util;

import com.resume.dashboard.entity.AuditLog;
import com.resume.dashboard.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String entityType, String entityId, Map<String, Object> metadata) {
        String actorId = SecurityUtils.getCurrentUserEmail();
        if (actorId == null) {
            actorId = "anonymous";
        }
        AuditLog entry = new AuditLog();
        entry.setId(null);
        entry.setActorId(actorId);
        entry.setAction(action);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setTimestamp(Instant.now());
        entry.setMetadata(metadata);
        auditLogRepository.save(entry);
        log.debug("Audit: {} {} {} by {}", action, entityType, entityId, actorId);
    }
}
