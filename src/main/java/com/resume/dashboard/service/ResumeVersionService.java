package com.resume.dashboard.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resume.dashboard.dto.resumeversion.ResumeVersionResponse;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.ResumeVersion;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.ResumeVersionRepository;

@Service
public class ResumeVersionService {

    private final ResumeVersionRepository versionRepository;
    private final ResumeRepository resumeRepository;
    private final SubscriptionService subscriptionService;

    public ResumeVersionService(
            ResumeVersionRepository versionRepository,
            ResumeRepository resumeRepository,
            SubscriptionService subscriptionService) {

        this.versionRepository = versionRepository;
        this.resumeRepository = resumeRepository;
        this.subscriptionService = subscriptionService;
    }

    /* =====================================================
       INITIAL VERSION (called internally only on resume creation)
       No plan check — every user gets their initial snapshot.
    ===================================================== */
    @Transactional
    public void createInitialVersion(Resume resume) {

        ResumeVersion version = buildSnapshot(resume, "Initial version");
        version.setCurrent(true);
        versionRepository.save(version);
    }

    /* =====================================================
       CREATE VERSION — USER FACING (Plan Restricted)
       Only PRO and PREMIUM users can manually trigger this.
       FIX: Renamed from createVersion to make intent explicit.
    ===================================================== */
    @Transactional
    public ResumeVersion createVersion(String userId,
                                       String resumeId,
                                       String changeNote) {

        // 🔥 Plan enforcement — only PRO and PREMIUM pass
        subscriptionService.validateVersioning(userId);

        return saveNewVersion(userId, resumeId, changeNote);
    }

    /* =====================================================
       CREATE VERSION — INTERNAL (System Only, No Plan Check)
       Used by ResumeService.publishResume for auto-snapshots.
       Must NOT be called from user-facing endpoints directly.

       FIX: Previously publishResume called createVersion which
            called validateVersioning — this would block FREE/BASIC
            users from publishing, which is wrong. Auto-snapshots
            on publish are a system action, not a user versioning action.
    ===================================================== */
    @Transactional
    public ResumeVersion createVersionInternal(String resumeId,
                                               String userId,
                                               String changeNote) {
        // No plan check — system-initiated snapshot only
        return saveNewVersion(userId, resumeId, changeNote);
    }

    /* =====================================================
       SHARED SAVE LOGIC
    ===================================================== */
    private ResumeVersion saveNewVersion(String userId,
                                         String resumeId,
                                         String changeNote) {

        Resume resume = getOwnedResume(userId, resumeId);

        List<ResumeVersion> versions =
                versionRepository.findByResumeIdOrderByCreatedAtDesc(resumeId);

        // Keep max 2 versions — drop oldest when at cap
        if (versions.size() >= 2) {
            versionRepository.delete(versions.get(versions.size() - 1));
        }

        // Mark current version as non-current
        if (!versions.isEmpty()) {
            ResumeVersion current = versions.get(0);
            current.setCurrent(false);
            versionRepository.save(current);
        }

        ResumeVersion newVersion = buildSnapshot(resume, changeNote);
        newVersion.setCurrent(true);

        return versionRepository.save(newVersion);
    }

    /* =====================================================
       GET VERSIONS — USER FACING (Plan Restricted)
       Only PRO and PREMIUM can view version history.
    ===================================================== */
    public List<ResumeVersionResponse> getVersions(String userId,
                                                   String resumeId) {

        getOwnedResume(userId, resumeId); // ownership check

        // 🔥 Only allow if versioning is enabled on plan
        subscriptionService.validateVersioning(userId);

        return versionRepository
                .findByResumeIdOrderByCreatedAtDesc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /* =====================================================
       REVERT TO PREVIOUS (Plan Restricted)
       Only PRO and PREMIUM can revert.
    ===================================================== */
    @Transactional
    public Resume revertToPrevious(String userId,
                                   String resumeId) {

        // 🔥 Plan enforcement
        subscriptionService.validateVersioning(userId);

        Resume resume = getOwnedResume(userId, resumeId);

        List<ResumeVersion> versions =
                versionRepository.findByResumeIdOrderByCreatedAtDesc(resumeId);

        if (versions.size() < 2) {
            throw new IllegalStateException("No previous version available to revert to.");
        }

        ResumeVersion previous = versions.stream()
                .filter(v -> !v.isCurrent())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Previous version not found"));

        resume.setContent(previous.getSnapshotContent());
        resume.setTemplateId(previous.getTemplateId());
        resume.setTemplateVersion(previous.getTemplateVersion());
        resume.setLayoutId(previous.getLayoutId());
        resume.setLayoutVersion(previous.getLayoutVersion());
        resume.setThemeId(previous.getThemeId());
        resume.setThemeVersion(previous.getThemeVersion());
        resume.setUpdatedAt(Instant.now());

        resumeRepository.save(resume);

        return resume;
    }

    /* =====================================================
       INTERNAL HELPERS
    ===================================================== */

    private Resume getOwnedResume(String userId, String resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return resume;
    }

    private ResumeVersion buildSnapshot(Resume resume, String changeNote) {

        ResumeVersion version = new ResumeVersion();

        version.setResumeId(resume.getId());
        version.setUserId(resume.getUserId());

        version.setSnapshotContent(resume.getContent());

        version.setTemplateId(resume.getTemplateId());
        version.setTemplateVersion(resume.getTemplateVersion());

        version.setLayoutId(resume.getLayoutId());
        version.setLayoutVersion(resume.getLayoutVersion());

        version.setThemeId(resume.getThemeId());
        version.setThemeVersion(resume.getThemeVersion());

        version.setChangeNote(changeNote);
        version.setCreatedAt(Instant.now());

        return version;
    }

    private ResumeVersionResponse map(ResumeVersion v) {

        ResumeVersionResponse r = new ResumeVersionResponse();

        r.setId(v.getId());
        r.setResumeId(v.getResumeId());
        r.setCurrent(v.isCurrent());
        r.setTemplateId(v.getTemplateId());
        r.setTemplateVersion(v.getTemplateVersion());
        r.setLayoutId(v.getLayoutId());
        r.setLayoutVersion(v.getLayoutVersion());
        r.setThemeId(v.getThemeId());
        r.setThemeVersion(v.getThemeVersion());
        r.setChangeNote(v.getChangeNote());
        r.setCreatedAt(v.getCreatedAt());

        return r;
    }
}