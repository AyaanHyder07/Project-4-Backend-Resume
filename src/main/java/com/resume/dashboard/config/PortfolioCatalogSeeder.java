package com.resume.dashboard.config;

import com.resume.dashboard.entity.BlockType;
import com.resume.dashboard.entity.ContentMode;
import com.resume.dashboard.entity.Layout;
import com.resume.dashboard.entity.LayoutAudience;
import com.resume.dashboard.entity.LayoutStructureConfig;
import com.resume.dashboard.entity.LayoutType;
import com.resume.dashboard.entity.MotionPreset;
import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.ProfessionCategory;
import com.resume.dashboard.entity.ProfessionType;
import com.resume.dashboard.entity.Template;
import com.resume.dashboard.entity.TemplateDefaultTheme;
import com.resume.dashboard.entity.Theme;
import com.resume.dashboard.entity.ThemeBackground;
import com.resume.dashboard.entity.ThemeColorPalette;
import com.resume.dashboard.entity.ThemeEffects;
import com.resume.dashboard.entity.ThemeTypography;
import com.resume.dashboard.entity.VisualMood;
import com.resume.dashboard.repository.LayoutRepository;
import com.resume.dashboard.repository.TemplateRepository;
import com.resume.dashboard.repository.ThemeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class PortfolioCatalogSeeder implements ApplicationRunner {

    private static final Instant SEEDED_AT = Instant.parse("2026-03-26T00:00:00Z");

    private final LayoutRepository layoutRepository;
    private final ThemeRepository themeRepository;
    private final TemplateRepository templateRepository;

    public PortfolioCatalogSeeder(
            LayoutRepository layoutRepository,
            ThemeRepository themeRepository,
            TemplateRepository templateRepository) {
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean hasSystemTemplates = templateRepository.findAll().stream()
                .anyMatch(template -> template.getTemplateKey() != null && !template.getTemplateKey().isBlank());

        if (hasSystemTemplates) {
            return;
        }

        seedLayouts();
        seedThemes();
        seedTemplates();
    }

    private void seedLayouts() {
        layoutRepository.save(layout(
                "system-layout-devfolio",
                "Dev Console",
                "Single-column hero plus proof blocks for engineering and product portfolios.",
                LayoutType.SINGLE_COLUMN,
                PlanType.FREE,
                MotionPreset.CINEMATIC,
                List.of(LayoutAudience.SOFTWARE_ENGINEER, LayoutAudience.ENGINEER, LayoutAudience.PRODUCT_MANAGER, LayoutAudience.FREELANCER),
                List.of(ProfessionCategory.TECH_ENGINEERING, ProfessionCategory.CONSULTING_COACHING),
                List.of(ProfessionType.SOFTWARE_ENGINEER, ProfessionType.FRONTEND_DEVELOPER, ProfessionType.BACKEND_DEVELOPER, ProfessionType.FULL_STACK_DEVELOPER, ProfessionType.DEVOPS_ENGINEER),
                List.of(ContentMode.RESUME_FIRST, ContentMode.PORTFOLIO_FIRST),
                List.of(MotionPreset.SUBTLE, MotionPreset.CINEMATIC, MotionPreset.IMMERSIVE),
                List.of(BlockType.TIMELINE, BlockType.METRICS, BlockType.CTA, BlockType.CUSTOM_LIST),
                structure(1, "none", null, true, true, false, true, "vertical", null,
                        zone("hero", "Hero", list("PROFILE", "SUMMARY", "CONTACT"), "100%", false, 1),
                        zone("proof", "Proof", list("SKILLS", "EXPERIENCE", "PROJECTS"), "100%", false, 2),
                        zone("footer", "Footer", list("EDUCATION", "CERTIFICATIONS", "CONTACT"), "100%", true, 3))));

        layoutRepository.save(layout(
                "system-layout-classicpro",
                "Recruiter Sidebar",
                "Left-sidebar resume layout tuned for dense recruiter-friendly review.",
                LayoutType.LEFT_SIDEBAR,
                PlanType.FREE,
                MotionPreset.NONE,
                List.of(LayoutAudience.EXECUTIVE, LayoutAudience.CONSULTANT, LayoutAudience.RECRUITER, LayoutAudience.STUDENT),
                List.of(ProfessionCategory.PROFESSIONAL_CORPORATE, ProfessionCategory.FINANCE_BUSINESS, ProfessionCategory.LEGAL_PUBLIC_POLICY),
                List.of(ProfessionType.GENERALIST, ProfessionType.ACCOUNTANT, ProfessionType.FINANCIAL_ANALYST, ProfessionType.LAWYER, ProfessionType.STUDENT),
                List.of(ContentMode.RESUME_FIRST, ContentMode.SERVICE_FIRST),
                List.of(MotionPreset.NONE, MotionPreset.SUBTLE),
                List.of(BlockType.TIMELINE, BlockType.RICH_TEXT, BlockType.CUSTOM_LIST, BlockType.AVAILABILITY_CARD),
                structure(2, "left", "30%", false, true, true, false, "vertical", null,
                        zone("sidebar", "Sidebar", list("PROFILE", "CONTACT", "SKILLS", "CERTIFICATIONS", "SERVICES"), "30%", false, 1),
                        zone("main", "Main", list("EXPERIENCE", "EDUCATION", "PROJECTS", "PUBLICATIONS", "TESTIMONIALS"), "70%", false, 2))));

        layoutRepository.save(layout(
                "system-layout-editorial",
                "Editorial Split",
                "Story-led sidebar layout for design, writing, and presentation-heavy profiles.",
                LayoutType.EDITORIAL,
                PlanType.BASIC,
                MotionPreset.EDITORIAL,
                List.of(LayoutAudience.UX_DESIGNER, LayoutAudience.WRITER, LayoutAudience.JOURNALIST, LayoutAudience.ILLUSTRATOR),
                List.of(ProfessionCategory.DESIGN_CREATIVE, ProfessionCategory.WRITING_PUBLISHING, ProfessionCategory.ARTIST_ILLUSTRATION),
                List.of(ProfessionType.UX_DESIGNER, ProfessionType.UI_DESIGNER, ProfessionType.GRAPHIC_DESIGNER, ProfessionType.WRITER, ProfessionType.JOURNALIST),
                List.of(ContentMode.CASE_STUDY_FIRST, ContentMode.STORY_FIRST, ContentMode.PORTFOLIO_FIRST),
                List.of(MotionPreset.SUBTLE, MotionPreset.EDITORIAL),
                List.of(BlockType.CASE_STUDY, BlockType.QUOTE, BlockType.GALLERY, BlockType.PROCESS_STEPS),
                structure(2, "left", "28%", true, true, false, true, "vertical", null,
                        zone("intro", "Intro", list("PROFILE", "SUMMARY", "CONTACT"), "28%", false, 1),
                        zone("stories", "Stories", list("PROJECTS", "PUBLICATIONS", "TESTIMONIALS", "SERVICES"), "72%", false, 2))));
        layoutRepository.save(layout(
                "system-layout-showcase",
                "Immersive Showcase",
                "Full-bleed gallery storytelling for photography, art, and film.",
                LayoutType.FULLSCREEN_SHOWCASE,
                PlanType.PREMIUM,
                MotionPreset.CINEMATIC,
                List.of(LayoutAudience.PHOTOGRAPHER, LayoutAudience.FILMMAKER, LayoutAudience.ARTIST, LayoutAudience.ILLUSTRATOR),
                List.of(ProfessionCategory.PHOTOGRAPHY_FILM, ProfessionCategory.ARTIST_ILLUSTRATION, ProfessionCategory.MEDIA_ENTERTAINMENT),
                List.of(ProfessionType.PHOTOGRAPHER, ProfessionType.VIDEOGRAPHER, ProfessionType.FILMMAKER, ProfessionType.VISUAL_ARTIST),
                List.of(ContentMode.GALLERY_FIRST, ContentMode.PORTFOLIO_FIRST, ContentMode.STORY_FIRST),
                List.of(MotionPreset.CINEMATIC, MotionPreset.SLIDESHOW, MotionPreset.IMMERSIVE),
                List.of(BlockType.GALLERY, BlockType.QUOTE, BlockType.CTA, BlockType.CUSTOM_LIST),
                structure(1, "none", null, true, false, false, true, "vertical", null,
                        zone("hero", "Hero", list("PROFILE", "SUMMARY"), "100%", false, 1),
                        zone("gallery", "Gallery", list("PROJECTS", "EXHIBITIONS", "MEDIA_APPEARANCES"), "100%", false, 2),
                        zone("contact", "Contact", list("CONTACT"), "100%", true, 3))));

        layoutRepository.save(layout(
                "system-layout-services",
                "Trust Builder",
                "Conversion-first services layout for consultants, healthcare, and legal experts.",
                LayoutType.TOP_HERO,
                PlanType.PRO,
                MotionPreset.SUBTLE,
                List.of(LayoutAudience.CONSULTANT, LayoutAudience.DOCTOR, LayoutAudience.LAWYER, LayoutAudience.COACH),
                List.of(ProfessionCategory.CONSULTING_COACHING, ProfessionCategory.HEALTHCARE_MEDICAL, ProfessionCategory.LEGAL_PUBLIC_POLICY),
                List.of(ProfessionType.BUSINESS_CONSULTANT, ProfessionType.COACH, ProfessionType.DOCTOR, ProfessionType.THERAPIST, ProfessionType.LAWYER),
                List.of(ContentMode.SERVICE_FIRST, ContentMode.RESUME_FIRST),
                List.of(MotionPreset.NONE, MotionPreset.SUBTLE, MotionPreset.EDITORIAL),
                List.of(BlockType.SERVICES_GRID, BlockType.FAQ, BlockType.CTA),
                structure(1, "none", null, true, true, true, false, "vertical", null,
                        zone("hero", "Hero", list("PROFILE", "SUMMARY", "AVAILABILITY"), "100%", false, 1),
                        zone("services", "Services", list("SERVICES", "TESTIMONIALS", "FAQ", "CONTACT"), "100%", false, 2),
                        zone("credentials", "Credentials", list("EXPERIENCE", "CERTIFICATIONS", "EDUCATION"), "100%", true, 3))));
    }

    private void seedThemes() {
        themeRepository.save(theme("system-theme-devfolio", "Neon Terminal", "Futuristic tech dark theme.", VisualMood.FUTURISTIC_TECH, PlanType.FREE,
                "#0A0A0A", "#08131A", "#00FF88", "#111827", "#0A0A0A", "#E5E7EB", "#9CA3AF", "#6B7280", "#1F2937", "#1F2937", "rgba(0,255,136,0.35)", "rgba(0,0,0,0.55)", "rgba(0,255,136,0.08)", "#86EFAC",
                "JetBrains Mono", "JetBrains Mono", "JetBrains Mono", "#0A0A0A", true, true, true, "10px", "medium"));

        themeRepository.save(theme("system-theme-classicpro", "Executive Paper", "Clean professional theme for generalist and recruiter-first resumes.", VisualMood.CLEAN_MINIMAL, PlanType.FREE,
                "#1A1A2E", "#CBD5E1", "#E94560", "#FFFFFF", "#FFFFFF", "#111827", "#475569", "#94A3B8", "#E2E8F0", "#E2E8F0", "rgba(233,69,96,0.18)", "rgba(15,23,42,0.12)", "#F8FAFC", "#334155",
                "Inter", "Inter", "Inter", "#FFFFFF", false, false, false, "8px", "fast"));

        themeRepository.save(theme("system-theme-editorial", "Editorial Canvas", "High-contrast editorial theme for design and writing portfolios.", VisualMood.LUXURY_ELEGANT, PlanType.BASIC,
                "#111111", "#F3F4F6", "#FF5A36", "#FFFFFF", "#F7F4EE", "#111111", "#57534E", "#A8A29E", "#D6D3D1", "#E7E5E4", "rgba(255,90,54,0.16)", "rgba(28,25,23,0.18)", "#FFF1EB", "#C2410C",
                "DM Sans", "DM Sans", "Cormorant Garamond", "#F7F4EE", true, true, false, "14px", "medium"));

        themeRepository.save(theme("system-theme-showcase", "Cinematic Noir", "Dark immersive palette for photographers and filmmakers.", VisualMood.DARK_DRAMATIC, PlanType.PREMIUM,
                "#050505", "#171717", "#E8C547", "#111111", "#050505", "#FAFAFA", "#D4D4D8", "#A1A1AA", "#27272A", "#27272A", "rgba(232,197,71,0.28)", "rgba(0,0,0,0.7)", "rgba(255,255,255,0.06)", "#FDE68A",
                "Cormorant Garamond", "DM Sans", "Cormorant Garamond", "#050505", true, true, true, "0px", "slow"));

        themeRepository.save(theme("system-theme-services", "Trust Blue", "Warm premium services theme for healthcare, legal, and consulting.", VisualMood.CORPORATE_FORMAL, PlanType.PRO,
                "#0F172A", "#E2E8F0", "#38BDF8", "#FFFFFF", "#F8FAFC", "#0F172A", "#334155", "#64748B", "#CBD5E1", "#E2E8F0", "rgba(56,189,248,0.16)", "rgba(15,23,42,0.12)", "#EFF6FF", "#075985",
                "Plus Jakarta Sans", "Inter", "Plus Jakarta Sans", "#F8FAFC", true, false, false, "18px", "medium"));
    }

    private void seedTemplates() {
        templateRepository.save(template("DEVFOLIO", "DEVFOLIO", "core-devfolio", "DEVELOPER", "DevFolio", "Dark. Terminal. Bold.", "Command-line confidence for engineers shipping real work.", PlanType.FREE,
                "system-layout-devfolio", "system-theme-devfolio", defaultTheme("#0A0A0A", "#00FF88", "#0A0A0A", "#E5E7EB", "JetBrains Mono", "sm", "rich"), VisualMood.FUTURISTIC_TECH,
                List.of(LayoutAudience.SOFTWARE_ENGINEER, LayoutAudience.ENGINEER, LayoutAudience.FREELANCER), List.of("developer", "engineering", "full-stack"),
                List.of(ProfessionCategory.TECH_ENGINEERING), List.of(ProfessionType.SOFTWARE_ENGINEER, ProfessionType.FRONTEND_DEVELOPER, ProfessionType.BACKEND_DEVELOPER, ProfessionType.FULL_STACK_DEVELOPER, ProfessionType.DEVOPS_ENGINEER),
                List.of(ContentMode.RESUME_FIRST, ContentMode.PORTFOLIO_FIRST), list("SKILLS", "EXPERIENCE", "PROJECTS", "EDUCATION", "CERTIFICATIONS", "CONTACT"), list("SKILLS", "EXPERIENCE", "PROJECTS", "EDUCATION", "CERTIFICATIONS", "CONTACT"), list("PROFILE", "CONTACT"),
                List.of(BlockType.TIMELINE, BlockType.METRICS, BlockType.CTA, BlockType.CUSTOM_LIST), List.of(BlockType.TIMELINE, BlockType.METRICS, BlockType.CTA), List.of(MotionPreset.SUBTLE, MotionPreset.CINEMATIC, MotionPreset.IMMERSIVE), MotionPreset.CINEMATIC,
                "TOP_FIXED", true, true, true, 98, 1));

        templateRepository.save(template("CLASSICPRO", "CLASSICPRO", "core-classicpro", "GENERAL", "ClassicPro", "Clean. Professional. Scannable.", "Recruiter-first structure with zero wasted space.", PlanType.FREE,
                "system-layout-classicpro", "system-theme-classicpro", defaultTheme("#1A1A2E", "#E94560", "#FFFFFF", "#111827", "Inter", "sm", "none"), VisualMood.CLEAN_MINIMAL,
                List.of(LayoutAudience.EXECUTIVE, LayoutAudience.CONSULTANT, LayoutAudience.RECRUITER), List.of("resume", "executive", "professional"),
                List.of(ProfessionCategory.PROFESSIONAL_CORPORATE, ProfessionCategory.FINANCE_BUSINESS, ProfessionCategory.LEGAL_PUBLIC_POLICY), List.of(ProfessionType.GENERALIST, ProfessionType.ACCOUNTANT, ProfessionType.FINANCIAL_ANALYST, ProfessionType.LAWYER, ProfessionType.STUDENT),
                List.of(ContentMode.RESUME_FIRST, ContentMode.SERVICE_FIRST), list("EXPERIENCE", "EDUCATION", "SKILLS", "PROJECTS", "CERTIFICATIONS", "CONTACT"), list("EXPERIENCE", "EDUCATION", "SKILLS", "PROJECTS", "CERTIFICATIONS", "CONTACT"), list("PROFILE", "CONTACT"),
                List.of(BlockType.TIMELINE, BlockType.RICH_TEXT, BlockType.CUSTOM_LIST), List.of(BlockType.TIMELINE, BlockType.RICH_TEXT), List.of(MotionPreset.NONE, MotionPreset.SUBTLE), MotionPreset.NONE,
                "TOP_MINIMAL", true, true, false, 96, 2));

        templateRepository.save(template("DESIGNCASE", "DESIGNCASE", "core-editorial", "DESIGNER", "DesignCase", "Editorial. Sidebar. Case Studies.", "Bold editorial framing for designers and storytellers.", PlanType.FREE,
                "system-layout-editorial", "system-theme-editorial", defaultTheme("#FFFFFF", "#FF5A36", "#F7F4EE", "#111111", "DM Sans", "md", "moderate"), VisualMood.LUXURY_ELEGANT,
                List.of(LayoutAudience.UX_DESIGNER, LayoutAudience.GRAPHIC_DESIGNER, LayoutAudience.ILLUSTRATOR), List.of("design", "case-study", "editorial"),
                List.of(ProfessionCategory.DESIGN_CREATIVE, ProfessionCategory.ARTIST_ILLUSTRATION), List.of(ProfessionType.UX_DESIGNER, ProfessionType.UI_DESIGNER, ProfessionType.GRAPHIC_DESIGNER, ProfessionType.BRAND_DESIGNER, ProfessionType.ILLUSTRATOR),
                List.of(ContentMode.CASE_STUDY_FIRST, ContentMode.PORTFOLIO_FIRST, ContentMode.STORY_FIRST), list("PROJECTS", "SKILLS", "EXPERIENCE", "TESTIMONIALS", "CONTACT"), list("PROJECTS", "SKILLS", "EXPERIENCE", "TESTIMONIALS", "CONTACT"), list("PROFILE"),
                List.of(BlockType.CASE_STUDY, BlockType.PROCESS_STEPS, BlockType.QUOTE, BlockType.GALLERY), List.of(BlockType.CASE_STUDY, BlockType.PROCESS_STEPS), List.of(MotionPreset.SUBTLE, MotionPreset.EDITORIAL), MotionPreset.EDITORIAL,
                "SIDEBAR", true, true, true, 93, 3));

        templateRepository.save(template("LENSWORK", "LENSWORK", "core-showcase", "PHOTOGRAPHER", "LensWork", "Cinematic. Full Screen. Film Strip.", "Image-led immersive storytelling for photographers and filmmakers.", PlanType.FREE,
                "system-layout-showcase", "system-theme-showcase", defaultTheme("#050505", "#E8C547", "#050505", "#FAFAFA", "Cormorant Garamond", "none", "rich"), VisualMood.DARK_DRAMATIC,
                List.of(LayoutAudience.PHOTOGRAPHER, LayoutAudience.FILMMAKER, LayoutAudience.ARTIST), List.of("photography", "gallery", "film"),
                List.of(ProfessionCategory.PHOTOGRAPHY_FILM, ProfessionCategory.MEDIA_ENTERTAINMENT, ProfessionCategory.ARTIST_ILLUSTRATION), List.of(ProfessionType.PHOTOGRAPHER, ProfessionType.VIDEOGRAPHER, ProfessionType.FILMMAKER, ProfessionType.VISUAL_ARTIST),
                List.of(ContentMode.GALLERY_FIRST, ContentMode.PORTFOLIO_FIRST), list("PROJECTS", "CONTACT"), list("PROJECTS", "CONTACT"), list("PROFILE"),
                List.of(BlockType.GALLERY, BlockType.QUOTE, BlockType.CTA), List.of(BlockType.GALLERY, BlockType.QUOTE), List.of(MotionPreset.CINEMATIC, MotionPreset.SLIDESHOW, MotionPreset.IMMERSIVE), MotionPreset.CINEMATIC,
                "HAMBURGER_OVERLAY", true, false, true, 90, 4));
        templateRepository.save(template("MENTORHUB", "MENTORHUB", "core-services", "MENTOR", "MentorHub", "Premium. Warm. Book a Session.", "Trust-building services template for coaches, mentors, and consultants.", PlanType.PREMIUM,
                "system-layout-services", "system-theme-services", defaultTheme("#0F172A", "#38BDF8", "#F8FAFC", "#0F172A", "Plus Jakarta Sans", "lg", "moderate"), VisualMood.CORPORATE_FORMAL,
                List.of(LayoutAudience.CONSULTANT, LayoutAudience.COACH, LayoutAudience.SPEAKER), List.of("mentor", "coach", "consulting"),
                List.of(ProfessionCategory.CONSULTING_COACHING), List.of(ProfessionType.BUSINESS_CONSULTANT, ProfessionType.COACH),
                List.of(ContentMode.SERVICE_FIRST, ContentMode.RESUME_FIRST), list("SERVICES", "TESTIMONIALS", "EXPERIENCE", "CONTACT"), list("SERVICES", "TESTIMONIALS", "EXPERIENCE", "CONTACT"), list("PROFILE", "CONTACT"),
                List.of(BlockType.SERVICES_GRID, BlockType.QUOTE, BlockType.FAQ, BlockType.CTA), List.of(BlockType.SERVICES_GRID, BlockType.QUOTE, BlockType.CTA), List.of(MotionPreset.NONE, MotionPreset.SUBTLE, MotionPreset.EDITORIAL), MotionPreset.SUBTLE,
                "FLOATING_PILL", true, true, true, 88, 5));

        templateRepository.save(template("STARTUPFOUNDER", "STARTUPFOUNDER", "core-devfolio", "FOUNDER", "StartupFounder", "Impact. Metrics. Bold.", "High-conviction founder profile tuned for traction and proof.", PlanType.PREMIUM,
                "system-layout-devfolio", "system-theme-devfolio", defaultTheme("#000000", "#FF6B35", "#000000", "#FFFFFF", "Syne", "none", "rich"), VisualMood.BOLD_VIBRANT,
                List.of(LayoutAudience.ENTREPRENEUR, LayoutAudience.FOUNDER, LayoutAudience.PRODUCT_MANAGER), List.of("founder", "startup", "metrics"),
                List.of(ProfessionCategory.TECH_ENGINEERING, ProfessionCategory.CONSULTING_COACHING), List.of(ProfessionType.PRODUCT_MANAGER, ProfessionType.BUSINESS_CONSULTANT),
                List.of(ContentMode.PORTFOLIO_FIRST, ContentMode.RESUME_FIRST, ContentMode.STORY_FIRST), list("PROJECTS", "EXPERIENCE", "SKILLS", "CONTACT"), list("PROJECTS", "EXPERIENCE", "SKILLS", "CONTACT"), list("PROFILE"),
                List.of(BlockType.METRICS, BlockType.TIMELINE, BlockType.CTA, BlockType.STATS_GRID), List.of(BlockType.METRICS, BlockType.STATS_GRID, BlockType.CTA), List.of(MotionPreset.SUBTLE, MotionPreset.CINEMATIC, MotionPreset.IMMERSIVE), MotionPreset.CINEMATIC,
                "TOP_FIXED", true, false, true, 87, 6));

        templateRepository.save(template("FREELANCERKIT", "FREELANCERKIT", "core-services", "FREELANCER", "FreelancerKit", "Friendly. Clear Pricing. Easy to Hire.", "Service-first portfolio for freelancers and solo operators.", PlanType.FREE,
                "system-layout-services", "system-theme-services", defaultTheme("#0F172A", "#38BDF8", "#F8FAFC", "#0F172A", "Plus Jakarta Sans", "lg", "moderate"), VisualMood.CLEAN_MINIMAL,
                List.of(LayoutAudience.FREELANCER, LayoutAudience.CONSULTANT), List.of("freelancer", "services", "pricing"),
                List.of(ProfessionCategory.CONSULTING_COACHING, ProfessionCategory.MARKETING_SALES), List.of(ProfessionType.BUSINESS_CONSULTANT, ProfessionType.MARKETING_SPECIALIST, ProfessionType.SALES_MANAGER),
                List.of(ContentMode.SERVICE_FIRST, ContentMode.PORTFOLIO_FIRST), list("SERVICES", "TESTIMONIALS", "SKILLS", "EXPERIENCE", "CONTACT"), list("SERVICES", "TESTIMONIALS", "SKILLS", "EXPERIENCE", "CONTACT"), list("PROFILE", "CONTACT"),
                List.of(BlockType.SERVICES_GRID, BlockType.CTA, BlockType.QUOTE, BlockType.CLIENT_LOGOS), List.of(BlockType.SERVICES_GRID, BlockType.CTA), List.of(MotionPreset.NONE, MotionPreset.SUBTLE, MotionPreset.EDITORIAL), MotionPreset.SUBTLE,
                "SIDEBAR", true, false, true, 84, 7));

        templateRepository.save(template("WRITERSDESK", "WRITERSDESK", "core-editorial", "WRITER", "WritersDesk", "Editorial. Serif. Literary.", "Reading-led layout for writers, journalists, and content strategists.", PlanType.FREE,
                "system-layout-editorial", "system-theme-editorial", defaultTheme("#FFFDF7", "#92400E", "#FFFDF7", "#1C1917", "Playfair Display", "sm", "subtle"), VisualMood.EARTHY_ORGANIC,
                List.of(LayoutAudience.WRITER, LayoutAudience.JOURNALIST, LayoutAudience.POET), List.of("writer", "editorial", "publishing"),
                List.of(ProfessionCategory.WRITING_PUBLISHING), List.of(ProfessionType.WRITER, ProfessionType.JOURNALIST, ProfessionType.CONTENT_STRATEGIST),
                List.of(ContentMode.STORY_FIRST, ContentMode.PORTFOLIO_FIRST), list("BLOG_POSTS", "PUBLICATIONS", "CONTACT"), list("BLOG_POSTS", "PUBLICATIONS", "CONTACT"), list("PROFILE"),
                List.of(BlockType.RICH_TEXT, BlockType.QUOTE, BlockType.LINK_LIST), List.of(BlockType.RICH_TEXT, BlockType.QUOTE), List.of(MotionPreset.NONE, MotionPreset.SUBTLE, MotionPreset.EDITORIAL), MotionPreset.SUBTLE,
                "TOP_MINIMAL", true, false, true, 83, 8));

        templateRepository.save(template("MLRESEARCH", "MLRESEARCH", "core-classicpro", "RESEARCHER", "MLResearch", "Minimal. Numbered. Academic.", "Structured academic profile for researchers and technical authors.", PlanType.FREE,
                "system-layout-classicpro", "system-theme-classicpro", defaultTheme("#FFFFFF", "#2563EB", "#FFFFFF", "#0F172A", "IBM Plex Mono", "none", "subtle"), VisualMood.CLEAN_MINIMAL,
                List.of(LayoutAudience.RESEARCHER, LayoutAudience.ACADEMIC), List.of("research", "academic", "ml"),
                List.of(ProfessionCategory.EDUCATION_RESEARCH, ProfessionCategory.TECH_ENGINEERING), List.of(ProfessionType.RESEARCHER, ProfessionType.PROFESSOR, ProfessionType.DATA_SCIENTIST),
                List.of(ContentMode.RESUME_FIRST, ContentMode.STORY_FIRST), list("PROJECTS", "PUBLICATIONS", "SKILLS", "EDUCATION", "CONTACT"), list("PROJECTS", "PUBLICATIONS", "SKILLS", "EDUCATION", "CONTACT"), list("PROFILE", "CONTACT"),
                List.of(BlockType.RICH_TEXT, BlockType.TIMELINE, BlockType.LINK_LIST), List.of(BlockType.RICH_TEXT, BlockType.LINK_LIST), List.of(MotionPreset.NONE, MotionPreset.SUBTLE), MotionPreset.NONE,
                "TOP_MINIMAL", true, false, true, 82, 9));

        templateRepository.save(template("CAREPULSE", "CAREPULSE", "core-services", "DOCTOR", "CarePulse", "Clinical. Calm. Trustworthy.", "Healthcare-forward template for doctors, nurses, and therapists.", PlanType.FREE,
                "system-layout-services", "system-theme-services", defaultTheme("#0F172A", "#14B8A6", "#F8FAFC", "#0F172A", "Inter", "md", "subtle"), VisualMood.CLEAN_MINIMAL,
                List.of(LayoutAudience.DOCTOR, LayoutAudience.NURSE, LayoutAudience.THERAPIST), List.of("doctor", "healthcare", "clinic"),
                List.of(ProfessionCategory.HEALTHCARE_MEDICAL), List.of(ProfessionType.DOCTOR, ProfessionType.NURSE, ProfessionType.THERAPIST),
                List.of(ContentMode.SERVICE_FIRST, ContentMode.RESUME_FIRST), list("SERVICES", "EXPERIENCE", "CERTIFICATIONS", "CONTACT"), list("SERVICES", "EXPERIENCE", "CERTIFICATIONS", "CONTACT"), list("PROFILE", "CONTACT"),
                List.of(BlockType.SERVICES_GRID, BlockType.FAQ, BlockType.CTA, BlockType.AVAILABILITY_CARD), List.of(BlockType.SERVICES_GRID, BlockType.CTA, BlockType.AVAILABILITY_CARD), List.of(MotionPreset.NONE, MotionPreset.SUBTLE), MotionPreset.SUBTLE,
                "TOP_MINIMAL", true, true, true, 81, 10));

        templateRepository.save(template("LEGALLEDGER", "LEGALLEDGER", "core-classicpro", "LAWYER", "LegalLedger", "Formal. Credible. Precise.", "Authority-driven legal profile with clean hierarchy.", PlanType.PRO,
                "system-layout-classicpro", "system-theme-classicpro", defaultTheme("#0B132B", "#C59D5F", "#FFFFFF", "#111827", "Libre Baskerville", "sm", "none"), VisualMood.CORPORATE_FORMAL,
                List.of(LayoutAudience.LAWYER, LayoutAudience.CONSULTANT, LayoutAudience.EXECUTIVE), List.of("law", "legal", "policy"),
                List.of(ProfessionCategory.LEGAL_PUBLIC_POLICY), List.of(ProfessionType.LAWYER, ProfessionType.LEGAL_CONSULTANT),
                List.of(ContentMode.RESUME_FIRST, ContentMode.SERVICE_FIRST), list("EXPERIENCE", "EDUCATION", "PUBLICATIONS", "CONTACT"), list("EXPERIENCE", "EDUCATION", "PUBLICATIONS", "CONTACT"), list("PROFILE", "CONTACT"),
                List.of(BlockType.TIMELINE, BlockType.RICH_TEXT, BlockType.QUOTE), List.of(BlockType.TIMELINE, BlockType.RICH_TEXT), List.of(MotionPreset.NONE, MotionPreset.SUBTLE), MotionPreset.NONE,
                "TOP_FIXED", true, false, true, 80, 11));

        templateRepository.save(template("ARTCANVAS", "ARTCANVAS", "core-showcase", "ARTIST", "ArtCanvas", "Gallery. Let the Work Speak.", "Whitespace-heavy exhibition layout for artists and illustrators.", PlanType.FREE,
                "system-layout-showcase", "system-theme-showcase", defaultTheme("#FAFAFA", "#E11D48", "#FAFAFA", "#111111", "Cormorant Garamond", "none", "moderate"), VisualMood.ARTISTIC_EXPRESSIVE,
                List.of(LayoutAudience.ARTIST, LayoutAudience.ILLUSTRATOR), List.of("art", "gallery", "exhibitions"),
                List.of(ProfessionCategory.ARTIST_ILLUSTRATION), List.of(ProfessionType.VISUAL_ARTIST, ProfessionType.ILLUSTRATOR, ProfessionType.ANIMATOR),
                List.of(ContentMode.GALLERY_FIRST, ContentMode.PORTFOLIO_FIRST), list("PROJECTS", "EXHIBITIONS_AWARDS", "CONTACT"), list("PROJECTS", "EXHIBITIONS_AWARDS", "CONTACT"), list("PROFILE"),
                List.of(BlockType.GALLERY, BlockType.QUOTE, BlockType.CUSTOM_LIST), List.of(BlockType.GALLERY, BlockType.QUOTE), List.of(MotionPreset.NONE, MotionPreset.SUBTLE, MotionPreset.EDITORIAL), MotionPreset.SUBTLE,
                "NONE", true, false, true, 79, 12));
    }

    private Layout layout(String id, String name, String description, LayoutType layoutType, PlanType requiredPlan,
                          MotionPreset defaultMotionPreset, List<LayoutAudience> targetAudiences,
                          List<ProfessionCategory> supportedCategories, List<ProfessionType> supportedTypes,
                          List<ContentMode> supportedContentModes, List<MotionPreset> supportedMotionPresets,
                          List<BlockType> recommendedBlockTypes, LayoutStructureConfig structureConfig) {
        Layout layout = new Layout();
        layout.setId(id);
        layout.setName(name);
        layout.setDescription(description);
        layout.setLayoutType(layoutType);
        layout.setRequiredPlan(requiredPlan);
        layout.setDefaultMotionPreset(defaultMotionPreset);
        layout.setTargetAudiences(targetAudiences);
        layout.setSupportedProfessionCategories(supportedCategories);
        layout.setSupportedProfessionTypes(supportedTypes);
        layout.setSupportedContentModes(supportedContentModes);
        layout.setSupportedMotionPresets(supportedMotionPresets);
        layout.setRecommendedBlockTypes(recommendedBlockTypes);
        layout.setStructureConfig(structureConfig);
        layout.setActive(true);
        layout.setVersion(1);
        layout.setCreatedAt(SEEDED_AT);
        layout.setUpdatedAt(SEEDED_AT);
        return layout;
    }

    private Theme theme(String id, String name, String description, VisualMood mood, PlanType requiredPlan,
                        String primary, String secondary, String accent, String surface, String page, String textPrimary,
                        String textSecondary, String textMuted, String border, String divider, String glow,
                        String shadow, String tagBackground, String tagText, String headingFont, String bodyFont,
                        String accentFont, String backgroundColor, boolean scrollReveal, boolean hoverLift,
                        boolean grain, String borderRadius, String transitionSpeed) {
        Theme theme = new Theme();
        theme.setId(id);
        theme.setName(name);
        theme.setDescription(description);
        theme.setMood(mood);
        theme.setRequiredPlan(requiredPlan);
        theme.setColorPalette(colorPalette(primary, secondary, accent, surface, page, textPrimary, textSecondary, textMuted, border, divider, glow, shadow, tagBackground, tagText));
        theme.setTypography(typography(headingFont, bodyFont, accentFont));
        theme.setBackground(background(backgroundColor));
        theme.setEffects(effects(scrollReveal, hoverLift, grain, borderRadius, transitionSpeed));
        theme.setActive(true);
        theme.setFeatured(requiredPlan == PlanType.FREE || requiredPlan == PlanType.PREMIUM);
        theme.setVersion(1);
        theme.setCreatedAt(SEEDED_AT);
        theme.setUpdatedAt(SEEDED_AT);
        return theme;
    }

    private Template template(String templateKey, String renderFamily, String variantKey, String profession,
                              String name, String tagline, String description, PlanType planLevel,
                              String layoutId, String defaultThemeId, TemplateDefaultTheme defaultTheme,
                              VisualMood primaryMood, List<LayoutAudience> targetAudiences, List<String> professionTags,
                              List<ProfessionCategory> professionCategories, List<ProfessionType> professionTypes,
                              List<ContentMode> supportedContentModes, List<String> supportedSections,
                              List<String> sectionOrder, List<String> requiredSections,
                              List<BlockType> supportedBlockTypes, List<BlockType> recommendedBlockTypes,
                              List<MotionPreset> allowedMotionPresets, MotionPreset defaultMotionPreset,
                              String navStyle, boolean featured, boolean isNew, boolean globallySelectable,
                              int popularityScore, int premiumRank) {
        Template template = new Template();
        template.setName(name);
        template.setTemplateKey(templateKey);
        template.setRenderFamily(renderFamily);
        template.setVariantKey(variantKey);
        template.setProfession(profession);
        template.setTagline(tagline);
        template.setDescription(description);
        template.setPlanLevel(planLevel);
        template.setLayoutId(layoutId);
        template.setDefaultThemeId(defaultThemeId);
        template.setDefaultTheme(defaultTheme);
        template.setPrimaryMood(primaryMood);
        template.setTargetAudiences(targetAudiences);
        template.setProfessionTags(professionTags);
        template.setProfessionCategories(professionCategories);
        template.setProfessionTypes(professionTypes);
        template.setSupportedContentModes(supportedContentModes);
        template.setSupportedSections(supportedSections);
        template.setEnabledSections(supportedSections);
        template.setSectionOrder(sectionOrder);
        template.setRequiredSections(requiredSections);
        template.setSupportedBlockTypes(supportedBlockTypes);
        template.setRecommendedBlockTypes(recommendedBlockTypes);
        template.setAllowedMotionPresets(allowedMotionPresets);
        template.setDefaultMotionPreset(defaultMotionPreset);
        template.setNavStyle(navStyle);
        template.setActive(true);
        template.setFeatured(featured);
        template.setNew(isNew);
        template.setPopularityScore(popularityScore);
        template.setPremiumRank(premiumRank);
        template.setGloballySelectable(globallySelectable);
        template.setSystemTemplate(true);
        template.setEditableByAdmin(true);
        template.setSupportsPremiumCustomization(true);
        template.setVersion(1);
        template.setCreatedAt(SEEDED_AT);
        template.setUpdatedAt(SEEDED_AT);
        return template;
    }

    private ThemeColorPalette colorPalette(String primary, String secondary, String accent, String surface, String page,
                                           String textPrimary, String textSecondary, String textMuted,
                                           String border, String divider, String glow, String shadow,
                                           String tagBackground, String tagText) {
        ThemeColorPalette palette = new ThemeColorPalette();
        palette.setPrimary(primary);
        palette.setSecondary(secondary);
        palette.setAccent(accent);
        palette.setSurfaceBackground(surface);
        palette.setPageBackground(page);
        palette.setTextPrimary(textPrimary);
        palette.setTextSecondary(textSecondary);
        palette.setTextMuted(textMuted);
        palette.setBorderColor(border);
        palette.setDividerColor(divider);
        palette.setGlowColor(glow);
        palette.setShadowColor(shadow);
        palette.setTagBackground(tagBackground);
        palette.setTagText(tagText);
        return palette;
    }

    private ThemeTypography typography(String headingFont, String bodyFont, String accentFont) {
        ThemeTypography typography = new ThemeTypography();
        typography.setHeadingFont(headingFont);
        typography.setBodyFont(bodyFont);
        typography.setAccentFont(accentFont);
        typography.setBaseSize(1.0);
        typography.setHeadingScale(2.8);
        typography.setSubheadingScale(1.3);
        typography.setLabelScale(0.78);
        typography.setHeadingWeight(700);
        typography.setBodyWeight(400);
        typography.setHeadingTransform("none");
        typography.setHeadingStyle("normal");
        typography.setHeadingLetterSpacing(0.0);
        typography.setBodyLineHeight(1.6);
        typography.setHeadingLineHeight(1.12);
        return typography;
    }

    private ThemeBackground background(String color) {
        ThemeBackground background = new ThemeBackground();
        background.setType(ThemeBackground.BackgroundType.SOLID);
        background.setSolidColor(color);
        return background;
    }

    private ThemeEffects effects(boolean scrollReveal, boolean hoverLift, boolean grain, String borderRadius, String transitionSpeed) {
        ThemeEffects effects = new ThemeEffects();
        effects.setCardBorderRadius(borderRadius);
        effects.setCardShadow("0 18px 48px rgba(15, 23, 42, 0.12)");
        effects.setCardBorderStyle("solid 1px rgba(148, 163, 184, 0.16)");
        effects.setEnableScrollReveal(scrollReveal);
        effects.setEnableHoverLift(hoverLift);
        effects.setEnableParallax(false);
        effects.setTransitionSpeed(transitionSpeed);
        effects.setEnableGlassmorphism(false);
        effects.setEnableNeumorphism(false);
        effects.setEnableGrain(grain);
        effects.setGlobalGrainIntensity(grain ? 12 : 0);
        effects.setSectionDividerStyle("none");
        return effects;
    }

    private TemplateDefaultTheme defaultTheme(String primaryColor, String accentColor, String backgroundColor,
                                              String textColor, String fontFamily, String borderRadius,
                                              String motionLevel) {
        TemplateDefaultTheme theme = new TemplateDefaultTheme();
        theme.setPrimaryColor(primaryColor);
        theme.setAccentColor(accentColor);
        theme.setBackgroundColor(backgroundColor);
        theme.setTextColor(textColor);
        theme.setFontFamily(fontFamily);
        theme.setBorderRadius(borderRadius);
        theme.setMotionLevel(motionLevel);
        return theme;
    }

    private LayoutStructureConfig structure(Integer columnCount, String sidebarPosition, String sidebarWidthPercent,
                                            boolean hasHeroSection, boolean hasFloatingHeader,
                                            boolean hasStickyContact, boolean isScrollBased,
                                            String scrollDirection, LayoutStructureConfig.GridConfig gridConfig,
                                            LayoutStructureConfig.SectionZone... zones) {
        LayoutStructureConfig config = new LayoutStructureConfig();
        config.setColumnCount(columnCount);
        config.setSidebarPosition(sidebarPosition);
        config.setSidebarWidthPercent(sidebarWidthPercent);
        config.setHasHeroSection(hasHeroSection);
        config.setHasFloatingHeader(hasFloatingHeader);
        config.setHasStickyContact(hasStickyContact);
        config.setIsScrollBased(isScrollBased);
        config.setScrollDirection(scrollDirection);
        config.setGridConfig(gridConfig);
        config.setZones(List.of(zones));
        return config;
    }

    private LayoutStructureConfig.SectionZone zone(String zoneId, String label, List<String> allowedSections,
                                                   String defaultWidth, boolean optional, int displayOrder) {
        LayoutStructureConfig.SectionZone zone = new LayoutStructureConfig.SectionZone();
        zone.setZoneId(zoneId);
        zone.setLabel(label);
        zone.setAllowedSections(allowedSections);
        zone.setDefaultWidth(defaultWidth);
        zone.setOptional(optional);
        zone.setDisplayOrder(displayOrder);
        return zone;
    }

    private List<String> list(String... values) {
        return List.of(values);
    }
}
