package com.resume.dashboard.service;

import com.resume.dashboard.dto.resume.ResumeRequest;
import com.resume.dashboard.dto.resume.ResumeResponse;
import com.resume.dashboard.dto.resume.ResumeVersionResponse;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.ResumeVersion;
import com.resume.dashboard.exception.InvalidStateTransitionException;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.exception.UnauthorizedActionException;
import com.resume.dashboard.exception.UserBlockedException;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.ResumeVersionRepository;
import com.resume.dashboard.repository.UserRepository;
import com.resume.dashboard.util.AuditService;
import com.resume.dashboard.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final ResumeRepository resumeRepository;
    private final ResumeVersionRepository versionRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @Autowired
    public ResumeService(ResumeRepository resumeRepository, ResumeVersionRepository versionRepository,
                         UserRepository userRepository, AuditService auditService) {
        this.resumeRepository = resumeRepository;
        this.versionRepository = versionRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    private void ensureUserNotBlocked(String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            if (u.getStatus() == com.resume.dashboard.entity.User.UserStatus.BLOCKED) {
                throw new UserBlockedException("Account is blocked");
            }
        });
    }

    public ResumeResponse create(ResumeRequest req) {
        String email = SecurityUtils.getCurrentUserEmail();
        ensureUserNotBlocked(email);
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = new Resume();
        resume.setId(UUID.randomUUID().toString());
        resume.setUserId(user.getId());
        resume.setTitle(req.getTitle());
        resume.setState(Resume.ResumeState.DRAFT);
        resume.setActiveVersion(1);
        resume.setCreatedAt(Instant.now());
        resume.setUpdatedAt(Instant.now());
        resume = resumeRepository.save(resume);

        ResumeVersion v = createVersion(resume.getId(), 1, req);
        versionRepository.save(v);

        auditService.log("CREATE_RESUME", "RESUME", resume.getId(), new HashMap<>());
        return toResponse(resume, v.getContent());
    }

    public ResumeResponse update(String id, ResumeRequest req) {
        String email = SecurityUtils.getCurrentUserEmail();
        ensureUserNotBlocked(email);
        Resume resume = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getUserId().equals(userRepository.findByEmail(email).get().getId())) {
            throw new UnauthorizedActionException("Not your resume");
        }
        if (resume.getState() != Resume.ResumeState.DRAFT) {
            throw new InvalidStateTransitionException("Can only edit resumes in DRAFT state");
        }

        int newVer = resume.getActiveVersion() + 1;
        resume.setActiveVersion(newVer);
        resume.setTitle(req.getTitle());
        resume.setUpdatedAt(Instant.now());
        resume = resumeRepository.save(resume);

        ResumeVersion v = createVersion(resume.getId(), newVer, req);
        versionRepository.save(v);

        auditService.log("UPDATE_RESUME", "RESUME", resume.getId(), new HashMap<>());
        return toResponse(resume, v.getContent());
    }

    public ResumeResponse submit(String id) {
        String email = SecurityUtils.getCurrentUserEmail();
        ensureUserNotBlocked(email);
        Resume resume = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getUserId().equals(userRepository.findByEmail(email).get().getId())) {
            throw new UnauthorizedActionException("Not your resume");
        }
        if (resume.getState() != Resume.ResumeState.DRAFT) {
            throw new InvalidStateTransitionException("Can only submit DRAFT resumes");
        }

        resume.setState(Resume.ResumeState.SUBMITTED);
        resume.setUpdatedAt(Instant.now());
        resume = resumeRepository.save(resume);

        ResumeVersion v = versionRepository.findByResumeIdAndVersion(resume.getId(), resume.getActiveVersion())
                .orElseThrow(() -> new ResourceNotFoundException("Version not found"));
        auditService.log("SUBMIT_RESUME", "RESUME", resume.getId(), new HashMap<>());
        return toResponse(resume, v.getContent());
    }

    public List<ResumeResponse> getMyResumes() {
        String email = SecurityUtils.getCurrentUserEmail();
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<Resume> list = resumeRepository.findByUserIdOrderByUpdatedAtDesc(user.getId());
        List<ResumeResponse> result = new ArrayList<>();
        for (Resume r : list) {
            var v = versionRepository.findByResumeIdAndVersion(r.getId(), r.getActiveVersion());
            result.add(toResponse(r, v.map(ResumeVersion::getContent).orElse(null)));
        }
        return result;
    }

    public ResumeResponse getById(String id) {
        String email = SecurityUtils.getCurrentUserEmail();
        Resume resume = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getUserId().equals(userRepository.findByEmail(email).get().getId())) {
            throw new UnauthorizedActionException("Not your resume");
        }
        var v = versionRepository.findByResumeIdAndVersion(resume.getId(), resume.getActiveVersion())
                .orElseThrow(() -> new ResourceNotFoundException("Version not found"));
        return toResponse(resume, v.getContent());
    }

    public List<ResumeVersionResponse> getVersions(String id) {
        String email = SecurityUtils.getCurrentUserEmail();
        Resume resume = resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getUserId().equals(userRepository.findByEmail(email).get().getId())) {
            throw new UnauthorizedActionException("Not your resume");
        }
        return versionRepository.findByResumeIdOrderByVersionDesc(resume.getId()).stream()
                .map(this::toVersionResponse)
                .collect(Collectors.toList());
    }

    private ResumeVersion createVersion(String resumeId, int version, ResumeRequest req) {
        ResumeVersion rv = new ResumeVersion();
        rv.setId(UUID.randomUUID().toString());
        rv.setResumeId(resumeId);
        rv.setVersion(version);
        rv.setContent(mapContent(req));
        rv.setCreatedAt(Instant.now());
        return rv;
    }

    private ResumeVersion.ResumeContent mapContent(ResumeRequest req) {
        ResumeVersion.ResumeContent c = new ResumeVersion.ResumeContent();
        if (req.getPersonalInfo() != null) {
            ResumeVersion.PersonalInfo pi = new ResumeVersion.PersonalInfo();
            pi.setFullName(req.getPersonalInfo().getFullName());
            pi.setTitle(req.getPersonalInfo().getTitle());
            pi.setEmail(req.getPersonalInfo().getEmail());
            pi.setPhone(req.getPersonalInfo().getPhone());
            pi.setLocation(req.getPersonalInfo().getLocation());
            c.setPersonalInfo(pi);
        }
        c.setSummary(req.getSummary());
        if (req.getExperience() != null) {
            c.setExperience(req.getExperience().stream().map(e -> {
                ResumeVersion.Experience ex = new ResumeVersion.Experience();
                ex.setCompany(e.getCompany());
                ex.setRole(e.getRole());
                ex.setDuration(e.getDuration());
                ex.setDescription(e.getDescription());
                return ex;
            }).collect(Collectors.toList()));
        }
        if (req.getEducation() != null) {
            c.setEducation(req.getEducation().stream().map(e -> {
                ResumeVersion.Education ed = new ResumeVersion.Education();
                ed.setInstitution(e.getInstitution());
                ed.setDegree(e.getDegree());
                ed.setYear(e.getYear());
                return ed;
            }).collect(Collectors.toList()));
        }
        c.setSkills(req.getSkills());
        if (req.getProjects() != null) {
            c.setProjects(req.getProjects().stream().map(p -> {
                ResumeVersion.Project pr = new ResumeVersion.Project();
                pr.setName(p.getName());
                pr.setDescription(p.getDescription());
                pr.setLink(p.getLink());
                return pr;
            }).collect(Collectors.toList()));
        }
        if (req.getLinks() != null) {
            ResumeVersion.Links ln = new ResumeVersion.Links();
            ln.setGithub(req.getLinks().getGithub());
            ln.setLinkedin(req.getLinks().getLinkedin());
            ln.setPortfolio(req.getLinks().getPortfolio());
            c.setLinks(ln);
        }
        return c;
    }

    private ResumeResponse.ResumeContentResponse mapContentResponse(ResumeVersion.ResumeContent content) {
        if (content == null) return null;
        ResumeResponse.ResumeContentResponse cr = new ResumeResponse.ResumeContentResponse();
        if (content.getPersonalInfo() != null) {
            ResumeRequest.PersonalInfoDto d = new ResumeRequest.PersonalInfoDto();
            d.setFullName(content.getPersonalInfo().getFullName());
            d.setTitle(content.getPersonalInfo().getTitle());
            d.setEmail(content.getPersonalInfo().getEmail());
            d.setPhone(content.getPersonalInfo().getPhone());
            d.setLocation(content.getPersonalInfo().getLocation());
            cr.setPersonalInfo(d);
        }
        cr.setSummary(content.getSummary());
        if (content.getExperience() != null) {
            cr.setExperience(content.getExperience().stream().map(e -> {
                ResumeRequest.ExperienceDto d = new ResumeRequest.ExperienceDto();
                d.setCompany(e.getCompany());
                d.setRole(e.getRole());
                d.setDuration(e.getDuration());
                d.setDescription(e.getDescription());
                return d;
            }).collect(Collectors.toList()));
        }
        if (content.getEducation() != null) {
            cr.setEducation(content.getEducation().stream().map(e -> {
                ResumeRequest.EducationDto d = new ResumeRequest.EducationDto();
                d.setInstitution(e.getInstitution());
                d.setDegree(e.getDegree());
                d.setYear(e.getYear());
                return d;
            }).collect(Collectors.toList()));
        }
        cr.setSkills(content.getSkills());
        if (content.getProjects() != null) {
            cr.setProjects(content.getProjects().stream().map(p -> {
                ResumeRequest.ProjectDto d = new ResumeRequest.ProjectDto();
                d.setName(p.getName());
                d.setDescription(p.getDescription());
                d.setLink(p.getLink());
                return d;
            }).collect(Collectors.toList()));
        }
        if (content.getLinks() != null) {
            ResumeRequest.LinksDto ln = new ResumeRequest.LinksDto();
            ln.setGithub(content.getLinks().getGithub());
            ln.setLinkedin(content.getLinks().getLinkedin());
            ln.setPortfolio(content.getLinks().getPortfolio());
            cr.setLinks(ln);
        }
        return cr;
    }

    public ResumeResponse toResponse(Resume r, ResumeVersion.ResumeContent content) {
        ResumeResponse res = new ResumeResponse();
        res.setId(r.getId());
        res.setUserId(r.getUserId());
        res.setTitle(r.getTitle());
        res.setState(r.getState().name());
        res.setActiveVersion(r.getActiveVersion());
        res.setCreatedAt(r.getCreatedAt());
        res.setUpdatedAt(r.getUpdatedAt());
        res.setContent(mapContentResponse(content));
        return res;
    }

    private ResumeVersionResponse toVersionResponse(ResumeVersion v) {
        ResumeVersionResponse r = new ResumeVersionResponse();
        r.setId(v.getId());
        r.setResumeId(v.getResumeId());
        r.setVersion(v.getVersion());
        r.setContent(mapContentResponse(v.getContent()));
        r.setCreatedAt(v.getCreatedAt());
        return r;
    }

    public Resume getResumeEntity(String id) {
        return resumeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
    }

    public ResumeVersion getActiveVersion(String resumeId) {
        Resume r = resumeRepository.findById(resumeId).orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        return versionRepository.findByResumeIdAndVersion(resumeId, r.getActiveVersion())
                .orElseThrow(() -> new ResourceNotFoundException("Version not found"));
    }
}
