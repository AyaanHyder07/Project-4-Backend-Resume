package com.resume.dashboard.service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resume.dashboard.dto.resume.CreateResumeRequest;
import com.resume.dashboard.entity.ApprovalStatus;
import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.ResolvedTheme;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.TemplateDefaultTheme;
import com.resume.dashboard.entity.Theme;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.entity.VisibilityType;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.LayoutRepository;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.ThemeRepository;
import com.resume.dashboard.repository.UserRepository;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final SubscriptionService subscriptionService;
    private final TemplateRepository templateRepository;
    private final LayoutRepository layoutRepository;
    private final ThemeRepository themeRepository;
    private final ResumeVersionService resumeVersionService;
    private final UserRepository userRepository;
    private final PortfolioSectionConfigService sectionService;
    private final UserProfileService userProfileService;
    private final ProfessionTaxonomyService professionTaxonomyService;

    public ResumeService(
            ResumeRepository resumeRepository,
            SubscriptionService subscriptionService,
            TemplateRepository templateRepository,
            LayoutRepository layoutRepository,
            ThemeRepository themeRepository,
            ResumeVersionService resumeVersionService,
            UserRepository userRepository,
            PortfolioSectionConfigService sectionService,
            UserProfileService userProfileService,
            ProfessionTaxonomyService professionTaxonomyService) {
        this.resumeRepository = resumeRepository;
        this.subscriptionService = subscriptionService;
        this.templateRepository = templateRepository;
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
        this.resumeVersionService = resumeVersionService;
        this.userRepository = userRepository;
        this.sectionService = sectionService;
        this.userProfileService = userProfileService;
        this.professionTaxonomyService = professionTaxonomyService;
    }

    @Transactional
    public Resume createResume(String userId, CreateResumeRequest request) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long existingCount = resumeRepository.countByUserId(userId);
        subscriptionService.validateResumeCreation(userId, existingCount);

        if (!subscriptionService.isTemplateAllowed(userId, request.getTemplateId())) {
            throw new IllegalStateException("Your subscription plan does not allow this template. Please upgrade.");
        }

        Template template = templateRepository.findByIdAndActiveTrue(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        Layout layout = layoutRepository.findById(template.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        String themeId = template.getDefaultThemeId();
        if (request.getThemeOverrideId() != null && !request.getThemeOverrideId().isBlank()) {
            try {
                subscriptionService.validateThemeCustomization(userId);
                themeId = request.getThemeOverrideId();
            } catch (RuntimeException ignored) {
                themeId = template.getDefaultThemeId();
            }
        }

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        ProfessionType professionType = professionTaxonomyService.resolveProfessionType(request.getProfessionType());
        MotionPreset selectedMotion = request.getMotionPreset() != null
                ? request.getMotionPreset()
                : (template.getDefaultMotionPreset() != null ? template.getDefaultMotionPreset() : layout.getDefaultMotionPreset());

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle(request.getTitle().trim());
        resume.setProfessionType(professionType.name());
        resume.setProfessionCategory(professionType.getCategory());
        resume.setTemplateId(template.getId());
        resume.setTemplateKey(resolveTemplateKey(template, request.getTemplateKey()));
        resume.setTemplateVersion(template.getVersion());
        resume.setLayoutId(layout.getId());
        resume.setLayoutVersion(layout.getVersion());
        resume.setThemeId(theme.getId());
        resume.setThemeVersion(theme.getVersion());
        resume.setMotionPreset(selectedMotion);
        resume.setResolvedTheme(buildResolvedTheme(template, theme, selectedMotion));
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setPublished(false);
        resume.setApprovalStatus(ApprovalStatus.DRAFT);
        resume.setRejectionReason(null);
        resume.setVersion(1);
        resume.setViewCount(0L);
        resume.setContent(request.getContent());
        resume.setCreatedAt(Instant.now());
        resume.setUpdatedAt(Instant.now());

        Resume saved = resumeRepository.save(resume);
        sectionService.initializeDefaultSections(saved.getId());
        userProfileService.createEmptyProfile(saved.getId());
        return saved;
    }

    @Transactional
    public Resume publishResume(String userId, String resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);

        if (resume.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new IllegalStateException("Resume must be approved before publishing");
        }
        if (!subscriptionService.isSubscriptionActive(userId)) {
            throw new IllegalStateException("Your subscription is inactive or expired");
        }

        long publishedCount = resumeRepository.countByUserIdAndPublishedTrue(userId);
        subscriptionService.validatePublicPublish(userId, publishedCount);
        resumeVersionService.createVersionInternal(resumeId, resume.getUserId(), "Auto snapshot on publish");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String finalSlug = (resume.getSlug() != null && !resume.getSlug().isBlank())
                ? normalizeSlugCandidate(resume.getSlug())
                : generateDefaultSlug(user.getUsername(), publishedCount);

        if (resumeRepository.existsBySlugAndIdNot(finalSlug, resume.getId())) {
            throw new IllegalStateException("That public URL is not available. Please choose another slug.");
        }

        resume.setSlug(finalSlug);
        resume.setPublished(true);
        resume.setVisibility(VisibilityType.PUBLIC);
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public Resume updateMeta(String userId, String resumeId, String title, String profession) {
        Resume resume = getOwnedResume(userId, resumeId);
        ProfessionType professionType = professionTaxonomyService.resolveProfessionType(profession);
        resume.setTitle(title);
        resume.setProfessionType(professionType.name());
        resume.setProfessionCategory(professionType.getCategory());
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public Resume changeTheme(String userId, String resumeId, String newThemeId) {
        Resume resume = getOwnedResume(userId, resumeId);
        Theme theme = themeRepository.findById(newThemeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));
        resume.setThemeId(theme.getId());
        resume.setThemeVersion(theme.getVersion());
        resume.setResolvedTheme(buildResolvedTheme(null, theme, resume.getMotionPreset()));
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public Resume changeTemplate(String userId, String resumeId, String newTemplateId) {
        Resume resume = getOwnedResume(userId, resumeId);
        subscriptionService.validateTemplateChange(userId);
        if (!subscriptionService.isTemplateAllowed(userId, newTemplateId)) {
            throw new IllegalStateException("Your subscription plan does not allow this template. Please upgrade.");
        }

        Template template = templateRepository.findByIdAndActiveTrue(newTemplateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        Layout layout = null;
        if (template.getLayoutId() != null && !template.getLayoutId().isBlank()) {
            layout = layoutRepository.findById(template.getLayoutId()).orElse(null);
        }
        if (layout == null && resume.getLayoutId() != null && !resume.getLayoutId().isBlank()) {
            layout = layoutRepository.findById(resume.getLayoutId()).orElse(null);
        }

        Theme theme = null;
        if (template.getDefaultThemeId() != null && !template.getDefaultThemeId().isBlank()) {
            theme = themeRepository.findById(template.getDefaultThemeId()).orElse(null);
        }
        if (theme == null && resume.getThemeId() != null && !resume.getThemeId().isBlank()) {
            theme = themeRepository.findById(resume.getThemeId()).orElse(null);
        }

        MotionPreset selectedMotion = template.getDefaultMotionPreset() != null
                ? template.getDefaultMotionPreset()
                : layout != null
                    ? layout.getDefaultMotionPreset()
                    : resume.getMotionPreset();

        resume.setTemplateId(template.getId());
        resume.setTemplateKey(resolveTemplateKey(template, template.getTemplateKey()));
        resume.setTemplateVersion(template.getVersion());
        if (layout != null) {
            resume.setLayoutId(layout.getId());
            resume.setLayoutVersion(layout.getVersion());
        }
        if (theme != null) {
            resume.setThemeId(theme.getId());
            resume.setThemeVersion(theme.getVersion());
        }
        resume.setMotionPreset(selectedMotion);
        resume.setResolvedTheme(buildResolvedTheme(template, theme, selectedMotion));
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public Resume submitForApproval(String userId, String resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);
        if (resume.getApprovalStatus() != ApprovalStatus.DRAFT) {
            throw new IllegalStateException("Only draft resumes can be submitted");
        }
        resume.setApprovalStatus(ApprovalStatus.PENDING);
        resume.setRejectionReason(null);
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public Resume unpublishResume(String userId, String resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);
        resume.setPublished(false);
        resume.setVisibility(VisibilityType.PRIVATE);
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public Resume getByIdForOwner(String userId, String resumeId) {
        return getOwnedResume(userId, resumeId);
    }

    public boolean isSlugAvailable(String userId, String resumeId, String slug) {
        getOwnedResume(userId, resumeId);
        String normalized = normalizeSlugCandidate(slug);
        return !resumeRepository.existsBySlugAndIdNot(normalized, resumeId);
    }

    public Resume updateSlug(String userId, String resumeId, String requestedSlug) {
        Resume resume = getOwnedResume(userId, resumeId);
        subscriptionService.validateCustomSlug(userId);
        String normalized = normalizeSlugCandidate(requestedSlug);
        if (resumeRepository.existsBySlugAndIdNot(normalized, resume.getId())) {
            throw new IllegalStateException("That public URL is not available. Please choose another slug.");
        }
        resume.setSlug(normalized);
        resume.setUpdatedAt(Instant.now());
        return resumeRepository.save(resume);
    }

    public Resume getPublicBySlug(String slug) {
        Resume resume = resumeRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        incrementViewCount(resume);
        return resume;
    }

    public void deleteResume(String userId, String resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);
        resumeRepository.delete(resume);
    }

    private Resume getOwnedResume(String userId, String resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }
        return resume;
    }

    private void incrementViewCount(Resume resume) {
        resume.setViewCount(resume.getViewCount() + 1);
        resumeRepository.save(resume);
    }

    public List<Resume> getAllByUser(String userId) {
        return resumeRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    private String resolveTemplateKey(Template template, String requestTemplateKey) {
        if (template.getTemplateKey() != null && !template.getTemplateKey().isBlank()) {
            return template.getTemplateKey();
        }
        if (requestTemplateKey != null && !requestTemplateKey.isBlank()) {
            return requestTemplateKey.trim().toUpperCase(Locale.ROOT);
        }
        return template.getName() == null ? null : template.getName()
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }

    private ResolvedTheme buildResolvedTheme(Template template, Theme theme, MotionPreset motionPreset) {
        TemplateDefaultTheme templateTheme = template != null ? template.getDefaultTheme() : null;
        var palette = theme != null ? theme.getColorPalette() : null;
        var typography = theme != null ? theme.getTypography() : null;
        var effects = theme != null ? theme.getEffects() : null;
        ResolvedTheme value = new ResolvedTheme();
        value.setPrimaryColor(templateTheme != null && templateTheme.getPrimaryColor() != null ? templateTheme.getPrimaryColor() : palette != null ? palette.getPrimary() : null);
        value.setAccentColor(templateTheme != null && templateTheme.getAccentColor() != null ? templateTheme.getAccentColor() : palette != null ? palette.getAccent() : null);
        value.setBackgroundColor(templateTheme != null && templateTheme.getBackgroundColor() != null ? templateTheme.getBackgroundColor() : palette != null ? palette.getPageBackground() : null);
        value.setTextColor(templateTheme != null && templateTheme.getTextColor() != null ? templateTheme.getTextColor() : palette != null ? palette.getTextPrimary() : null);
        value.setFontFamily(templateTheme != null && templateTheme.getFontFamily() != null ? templateTheme.getFontFamily() : typography != null ? (typography.getBodyFont() != null ? typography.getBodyFont() : typography.getHeadingFont()) : null);
        value.setBorderRadius(templateTheme != null && templateTheme.getBorderRadius() != null ? templateTheme.getBorderRadius() : mapBorderRadius(effects));
        value.setMotionLevel(templateTheme != null && templateTheme.getMotionLevel() != null ? templateTheme.getMotionLevel() : mapMotionLevel(motionPreset));
        return value;
    }

    private String mapBorderRadius(ThemeEffects effects) {
        String radius = effects != null ? effects.getCardBorderRadius() : null;
        if (radius == null || radius.isBlank()) return "sm";
        if (radius.contains("0")) return "none";
        if (radius.contains("24") || radius.contains("22") || radius.contains("20")) return "lg";
        if (radius.contains("18") || radius.contains("16") || radius.contains("14")) return "md";
        return "sm";
    }

    private String mapMotionLevel(MotionPreset motionPreset) {
        if (motionPreset == null) return "subtle";
        return switch (motionPreset) {
            case NONE -> "none";
            case SUBTLE, EDITORIAL -> "subtle";
            case PLAYFUL, PARALLAX, SLIDESHOW -> "moderate";
            case CINEMATIC, IMMERSIVE -> "rich";
        };
    }

    private String normalizeSlugCandidate(String slug) {
        String normalized = slug == null ? "" : slug.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9-]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-+|-+$", "");
        if (normalized.length() < 3 || normalized.length() > 40) {
            throw new IllegalArgumentException("Public URL must be 3-40 characters using letters, numbers, or hyphens.");
        }
        return normalized;
    }

    private String generateDefaultSlug(String username, long publishedCount) {
        String baseSlug = normalizeSlugCandidate(username);
        String finalSlug = publishedCount == 0 ? baseSlug : baseSlug + "-v" + (publishedCount + 1);
        int counter = (int) publishedCount + 1;
        while (resumeRepository.existsBySlug(finalSlug)) {
            counter++;
            finalSlug = baseSlug + "-v" + counter;
        }
        return finalSlug;
    }
}





