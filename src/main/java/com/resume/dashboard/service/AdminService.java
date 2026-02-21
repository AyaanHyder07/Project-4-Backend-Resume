package com.resume.dashboard.service;

import com.resume.dashboard.dto.admin.*;
import com.resume.dashboard.dto.resume.ResumeResponse;
import com.resume.dashboard.entity.Analytics;
import com.resume.dashboard.entity.AuditLog;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.exception.InvalidStateTransitionException;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.*;
import com.resume.dashboard.util.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final ResumeRepository resumeRepository;
    private final ResumeVersionRepository versionRepository;
    private final UserRepository userRepository;
    private final AnalyticsRepository analyticsRepository;
    private final AuditLogRepository auditLogRepository;
    private final ResumeService resumeService;
    private final AuditService auditService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AdminService(ResumeRepository resumeRepository, ResumeVersionRepository versionRepository,
                        UserRepository userRepository, AnalyticsRepository analyticsRepository,
                        AuditLogRepository auditLogRepository, ResumeService resumeService,
                        AuditService auditService, MongoTemplate mongoTemplate) {
        this.resumeRepository = resumeRepository;
        this.versionRepository = versionRepository;
        this.userRepository = userRepository;
        this.analyticsRepository = analyticsRepository;
        this.auditLogRepository = auditLogRepository;
        this.resumeService = resumeService;
        this.auditService = auditService;
        this.mongoTemplate = mongoTemplate;
    }

    public AdminDashboardResponse getDashboard() {
        AdminDashboardResponse d = new AdminDashboardResponse();
        d.setTotalUsers(userRepository.count());
        d.setTotalResumes(resumeRepository.count());
        d.setSubmittedCount(resumeRepository.countByState(Resume.ResumeState.SUBMITTED));
        d.setApprovedCount(resumeRepository.countByState(Resume.ResumeState.APPROVED));
        d.setPublishedCount(resumeRepository.countByState(Resume.ResumeState.PUBLISHED));

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("action").is("VIEW")),
                Aggregation.group("resumeId").count().as("viewCount"),
                Aggregation.sort(Sort.Direction.DESC, "viewCount"),
                Aggregation.limit(5)
        );
        // Simpler: get top 5 by VIEW count - MongoDB aggregation returns documents with resumeId and viewCount
        // We need to join with resumes to get title. Simpler: just group by resumeId and get counts, then fetch resumes.
        AggregationResults<Map> results = mongoTemplate.aggregate(agg, "analytics", Map.class);
        List<Map> maps = results.getMappedResults();
        List<TopResumeDto> top = maps.stream().map(m -> {
            String rid = (String) m.get("_id");
            Number cnt = (Number) m.get("viewCount");
            Resume r = resumeRepository.findById(rid).orElse(null);
            TopResumeDto dto = new TopResumeDto();
            dto.setResumeId(rid);
            dto.setTitle(r != null ? r.getTitle() : "Unknown");
            dto.setViewCount(cnt != null ? cnt.longValue() : 0);
            return dto;
        }).collect(Collectors.toList());
        d.setTopResumes(top);
        return d;
    }

    public Page<ResumeResponse> getResumes(String state, String userId, int page, int size, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy != null ? sortBy : "updatedAt").descending()
                : Sort.by(sortBy != null ? sortBy : "updatedAt").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Resume> resumes;
        if (state != null && !state.isEmpty() && userId != null && !userId.isEmpty()) {
            resumes = resumeRepository.findByStateAndUserId(Resume.ResumeState.valueOf(state), userId, pageable);
        } else if (state != null && !state.isEmpty()) {
            resumes = resumeRepository.findByState(Resume.ResumeState.valueOf(state), pageable);
        } else if (userId != null && !userId.isEmpty()) {
            resumes = resumeRepository.findByUserId(userId, pageable);
        } else {
            resumes = resumeRepository.findAll(pageable);
        }

        return resumes.map(r -> {
            var v = versionRepository.findByResumeIdAndVersion(r.getId(), r.getActiveVersion());
            return resumeService.toResponse(r, v.map(rv -> rv.getContent()).orElse(null));
        });
    }

    public ResumeResponse approve(String id) {
        Resume r = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (r.getState() != Resume.ResumeState.SUBMITTED) {
            throw new InvalidStateTransitionException("Can only approve SUBMITTED resumes");
        }
        r.setState(Resume.ResumeState.APPROVED);
        r.setUpdatedAt(Instant.now());
        resumeRepository.save(r);
        Map<String, Object> meta = new HashMap<>();
        meta.put("from", "SUBMITTED");
        meta.put("to", "APPROVED");
        auditService.log("APPROVE_RESUME", "RESUME", id, meta);
        var v = versionRepository.findByResumeIdAndVersion(id, r.getActiveVersion()).orElseThrow();
        return resumeService.toResponse(r, v.getContent());
    }

    public ResumeResponse publish(String id) {
        Resume r = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (r.getState() != Resume.ResumeState.APPROVED) {
            throw new InvalidStateTransitionException("Can only publish APPROVED resumes");
        }
        r.setState(Resume.ResumeState.PUBLISHED);
        r.setUpdatedAt(Instant.now());
        resumeRepository.save(r);
        Map<String, Object> meta = new HashMap<>();
        meta.put("from", "APPROVED");
        meta.put("to", "PUBLISHED");
        auditService.log("PUBLISH_RESUME", "RESUME", id, meta);
        var v = versionRepository.findByResumeIdAndVersion(id, r.getActiveVersion()).orElseThrow();
        return resumeService.toResponse(r, v.getContent());
    }

    public ResumeResponse disable(String id) {
        Resume r = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (r.getState() != Resume.ResumeState.PUBLISHED) {
            throw new InvalidStateTransitionException("Can only disable PUBLISHED resumes");
        }
        r.setState(Resume.ResumeState.DISABLED);
        r.setUpdatedAt(Instant.now());
        resumeRepository.save(r);
        Map<String, Object> meta = new HashMap<>();
        meta.put("from", "PUBLISHED");
        meta.put("to", "DISABLED");
        auditService.log("DISABLE_RESUME", "RESUME", id, meta);
        var v = versionRepository.findByResumeIdAndVersion(id, r.getActiveVersion()).orElseThrow();
        return resumeService.toResponse(r, v.getContent());
    }

    public ResumeResponse reject(String id, RejectRequest req) {
        Resume r = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (r.getState() != Resume.ResumeState.SUBMITTED) {
            throw new InvalidStateTransitionException("Can only reject SUBMITTED resumes");
        }
        r.setState(Resume.ResumeState.DRAFT);
        r.setUpdatedAt(Instant.now());
        resumeRepository.save(r);
        Map<String, Object> meta = new HashMap<>();
        meta.put("reason", req != null && req.getReason() != null ? req.getReason() : "");
        meta.put("from", "SUBMITTED");
        meta.put("to", "DRAFT");
        auditService.log("REJECT_RESUME", "RESUME", id, meta);
        var v = versionRepository.findByResumeIdAndVersion(id, r.getActiveVersion()).orElseThrow();
        return resumeService.toResponse(r, v.getContent());
    }

    public Page<UserResponse> getUsers(int page, int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAll(p).map(u -> {
            UserResponse ur = new UserResponse();
            ur.setId(u.getId());
            ur.setEmail(u.getEmail());
            ur.setRole(u.getRole());
            ur.setStatus(u.getStatus().name());
            ur.setCreatedAt(u.getCreatedAt());
            return ur;
        });
    }

    public void blockUser(String id) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        u.setStatus(User.UserStatus.BLOCKED);
        userRepository.save(u);
        auditService.log("BLOCK_USER", "USER", id, new HashMap<>());
    }

    public void unblockUser(String id) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        u.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(u);
        auditService.log("UNBLOCK_USER", "USER", id, new HashMap<>());
    }

    public List<ResumeResponse> getUserResumes(String userId) {
        List<Resume> list = resumeRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        return list.stream().map(r -> {
            var v = versionRepository.findByResumeIdAndVersion(r.getId(), r.getActiveVersion());
            return resumeService.toResponse(r, v.map(rv -> rv.getContent()).orElse(null));
        }).collect(Collectors.toList());
    }

    public Page<AnalyticsResponse.AnalyticsItemDto> getAnalytics(String resumeId, String action, int page, int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Analytics> pageResult;
        if (resumeId != null && !resumeId.isEmpty() && action != null && !action.isEmpty()) {
            pageResult = analyticsRepository.findByResumeIdAndAction(resumeId, Analytics.AnalyticsAction.valueOf(action), p);
        } else if (resumeId != null && !resumeId.isEmpty()) {
            pageResult = analyticsRepository.findByResumeId(resumeId, p);
        } else if (action != null && !action.isEmpty()) {
            pageResult = analyticsRepository.findByAction(Analytics.AnalyticsAction.valueOf(action), p);
        } else {
            pageResult = analyticsRepository.findAll(p);
        }
        return pageResult.map(a -> {
            AnalyticsResponse.AnalyticsItemDto d = new AnalyticsResponse.AnalyticsItemDto();
            d.setId(a.getId());
            d.setResumeId(a.getResumeId());
            d.setAction(a.getAction().name());
            d.setViewerIp(a.getViewerIp());
            d.setTimestamp(a.getTimestamp());
            return d;
        });
    }

    public Page<AuditLog> getAudit(String actorId, String action, int page, int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("timestamp").descending());
        if (actorId != null && !actorId.isEmpty() && action != null && !action.isEmpty()) {
            Query q = new Query();
            q.addCriteria(Criteria.where("actorId").is(actorId).and("action").is(action));
            long total = mongoTemplate.count(q, AuditLog.class);
            q.with(p);
            List<AuditLog> list = mongoTemplate.find(q, AuditLog.class);
            return PageableExecutionUtils.getPage(list, p, () -> total);
        } else if (actorId != null && !actorId.isEmpty()) {
            return auditLogRepository.findByActorId(actorId, p);
        } else if (action != null && !action.isEmpty()) {
            return auditLogRepository.findByAction(action, p);
        } else {
            return auditLogRepository.findAll(p);
        }
    }
}
