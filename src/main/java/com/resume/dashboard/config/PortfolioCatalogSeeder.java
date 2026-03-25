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
        seedLayouts();
        seedThemes();
        seedTemplates();
    }

    private void seedLayouts() {
        saveLayout(layout(
                "starter-v2-layout-executive-single-column",
                "Executive Single Column",
                "Clean one-column structure for professionals and early-career users.",
                LayoutType.SINGLE_COLUMN,
                PlanType.FREE,
                MotionPreset.SUBTLE,
                List.of(LayoutAudience.EXECUTIVE, LayoutAudience.CONSULTANT, LayoutAudience.STUDENT, LayoutAudience.FRESH_GRADUATE),
                List.of(VisualMood.CLEAN_MINIMAL, VisualMood.CORPORATE_FORMAL),
                List.of(ProfessionCategory.PROFESSIONAL_CORPORATE, ProfessionCategory.STUDENT_EARLY_CAREER, ProfessionCategory.CONSULTING_COACHING),
                List.of(ProfessionType.GENERALIST, ProfessionType.BUSINESS_CONSULTANT),
                List.of(ContentMode.RESUME_FIRST, ContentMode.SERVICE_FIRST),
                List.of(MotionPreset.NONE, MotionPreset.SUBTLE, MotionPreset.EDITORIAL),
                List.of(BlockType.RICH_TEXT, BlockType.METRICS, BlockType.CTA, BlockType.CUSTOM_LIST),
                structure(1, "none", null, true, false, false, false, "vertical", null,
                        zone("hero", "Hero", list("PROFILE", "SUMMARY", "CONTACT"), "100%", false, 1),
                        zone("main", "Main", list("EXPERIENCE", "EDUCATION", "SKILLS", "PROJECTS", "CERTIFICATIONS"), "100%", false, 2),
                        zone("extras", "Extras", list("CUSTOM", "TESTIMONIALS", "FAQ", "SERVICES"), "100%", true, 3))
        ));

        saveLayout(layout(
                "starter-v2-layout-product-case-study",
                "Product Case Study",
                "Case-study-first layout with a strong hero and structured proof blocks.",
                LayoutType.CASE_STUDY,
                PlanType.BASIC,
                MotionPreset.EDITORIAL,
                List.of(LayoutAudience.PRODUCT_MANAGER, LayoutAudience.UX_DESIGNER, LayoutAudience.GRAPHIC_DESIGNER, LayoutAudience.CONSULTANT),
                List.of(VisualMood.CLEAN_MINIMAL, VisualMood.LUXURY_ELEGANT),
                List.of(ProfessionCategory.DESIGN_CREATIVE, ProfessionCategory.TECH_ENGINEERING, ProfessionCategory.CONSULTING_COACHING),
                List.of(ProfessionType.PRODUCT_MANAGER, ProfessionType.UX_DESIGNER, ProfessionType.UI_DESIGNER),
                List.of(ContentMode.CASE_STUDY_FIRST, ContentMode.PORTFOLIO_FIRST),
                List.of(MotionPreset.SUBTLE, MotionPreset.EDITORIAL, MotionPreset.PARALLAX),
                List.of(BlockType.CASE_STUDY, BlockType.METRICS, BlockType.PROCESS_STEPS, BlockType.QUOTE, BlockType.CTA),
                structure(1, "none", null, true, true, false, true, "vertical", null,
                        zone("hero", "Hero", list("PROFILE", "SUMMARY", "PROJECTS"), "100%", false, 1),
                        zone("proof", "Proof", list("PROJECTS", "TESTIMONIALS", "CUSTOM"), "100%", false, 2),
                        zone("footer", "Footer", list("CONTACT", "SERVICES"), "100%", true, 3))
        ));

        saveLayout(layout(
                "starter-v2-layout-creator-sidebar-magazine",
                "Creator Sidebar Magazine",
                "Editorial sidebar structure for writers, creators, and multi-format portfolios.",
                LayoutType.LEFT_SIDEBAR,
                PlanType.PRO,
                MotionPreset.EDITORIAL,
                List.of(LayoutAudience.WRITER, LayoutAudience.JOURNALIST, LayoutAudience.FREELANCER, LayoutAudience.ENTREPRENEUR),
                List.of(VisualMood.LUXURY_ELEGANT, VisualMood.EARTHY_ORGANIC, VisualMood.ARTISTIC_EXPRESSIVE),
                List.of(ProfessionCategory.WRITING_PUBLISHING, ProfessionCategory.MEDIA_ENTERTAINMENT, ProfessionCategory.MARKETING_SALES),
                List.of(ProfessionType.WRITER, ProfessionType.JOURNALIST, ProfessionType.CONTENT_STRATEGIST),
                List.of(ContentMode.STORY_FIRST, ContentMode.PORTFOLIO_FIRST, ContentMode.SERVICE_FIRST),
                List.of(MotionPreset.SUBTLE, MotionPreset.EDITORIAL, MotionPreset.PLAYFUL),
                List.of(BlockType.RICH_TEXT, BlockType.LINK_LIST, BlockType.QUOTE, BlockType.CUSTOM_LIST, BlockType.AVAILABILITY_CARD),
                structure(2, "left", "28%", true, true, true, true, "vertical", null,
                        zone("sidebar", "Sidebar", list("PROFILE", "CONTACT", "SKILLS", "SERVICES"), "28%", false, 1),
                        zone("feature", "Feature", list("PROJECTS", "BLOG", "PUBLICATIONS", "CUSTOM"), "72%", false, 2),
                        zone("archive", "Archive", list("PROJECTS", "CUSTOM", "TESTIMONIALS"), "72%", true, 3))
        ));

        saveLayout(layout(
                "starter-v2-layout-cinematic-gallery-showcase",
                "Cinematic Gallery Showcase",
                "Immersive gallery-led layout for photographers, filmmakers, and artists.",
                LayoutType.FULLSCREEN_SHOWCASE,
                PlanType.PREMIUM,
                MotionPreset.CINEMATIC,
                List.of(LayoutAudience.PHOTOGRAPHER, LayoutAudience.FILMMAKER, LayoutAudience.ARTIST, LayoutAudience.ILLUSTRATOR),
                List.of(VisualMood.DARK_DRAMATIC, VisualMood.ARTISTIC_EXPRESSIVE, VisualMood.FUTURISTIC_TECH),
                List.of(ProfessionCategory.PHOTOGRAPHY_FILM, ProfessionCategory.ARTIST_ILLUSTRATION, ProfessionCategory.MEDIA_ENTERTAINMENT),
                List.of(ProfessionType.PHOTOGRAPHER, ProfessionType.FILMMAKER, ProfessionType.VIDEOGRAPHER, ProfessionType.VISUAL_ARTIST),
                List.of(ContentMode.GALLERY_FIRST, ContentMode.PORTFOLIO_FIRST, ContentMode.STORY_FIRST),
                List.of(MotionPreset.CINEMATIC, MotionPreset.SLIDESHOW, MotionPreset.IMMERSIVE, MotionPreset.PARALLAX),
                List.of(BlockType.GALLERY, BlockType.EMBED, BlockType.QUOTE, BlockType.LINK_LIST, BlockType.CTA),
                structure(null, "right", "30%", true, true, true, true, "horizontal", grid(6, null, "18px", "4:3", true),
                        zone("stage", "Stage", list("PROJECTS", "GALLERY", "CUSTOM"), "70%", false, 1),
                        zone("details", "Details", list("PROFILE", "CONTACT", "CUSTOM"), "30%", false, 2),
                        zone("strip", "Film Strip", list("GALLERY", "PROJECTS"), "100%", true, 3))
        ));
    }

    private void seedThemes() {
        saveTheme(theme(
                "starter-v2-theme-sandstone-editorial",
                "Sandstone Editorial",
                "Warm editorial neutral palette for polished professional portfolios.",
                PlanType.FREE,
                VisualMood.EARTHY_ORGANIC,
                List.of(LayoutAudience.EXECUTIVE, LayoutAudience.CONSULTANT, LayoutAudience.STUDENT, LayoutAudience.WRITER),
                palette("#8A6B48", "#D9CBB7", "#B07A4F", "#FFF9F2", "#F6EFE6", "#241F1A", "#5F564C", "#8D847A", "#D8CCBE", "#E7DCCF", "#C88C4A", "rgba(36,31,26,0.14)", "#EFE3D6", "#5B4633"),
                solidBackground("#FFF9F2"),
                typography("Cormorant Garamond", "Source Sans 3", "IBM Plex Sans", 1.0, 2.8, 1.4, 0.8, 700, 400, "none", "normal", 0.01, 1.65, 1.08),
                effects("18px", "0 20px 45px rgba(36,31,26,0.08)", "solid 1px rgba(138,107,72,0.16)", "none", true, true, false, "medium", false, false, false, 0, "none"),
                true,
                true,
                "/catalog/themes/sandstone-editorial.png"
        ));

        saveTheme(theme(
                "starter-v2-theme-slate-corporate",
                "Slate Corporate",
                "Cool restrained system for consultants, analysts, and clean business resumes.",
                PlanType.BASIC,
                VisualMood.CORPORATE_FORMAL,
                List.of(LayoutAudience.EXECUTIVE, LayoutAudience.CONSULTANT, LayoutAudience.ACCOUNTANT, LayoutAudience.FINANCIAL_ANALYST),
                palette("#1F3C5C", "#6A8198", "#3E7CB1", "#F7FAFC", "#FFFFFF", "#14202B", "#425466", "#7B8794", "#D8E2EA", "#E8EEF3", "#7FB3D5", "rgba(20,32,43,0.12)", "#EAF2F8", "#1F3C5C"),
                solidBackground("#F7FAFC"),
                typography("Manrope", "Inter", "IBM Plex Sans", 1.0, 2.35, 1.3, 0.78, 700, 400, "none", "normal", 0.0, 1.6, 1.12),
                effects("16px", "0 18px 32px rgba(20,32,43,0.06)", "solid 1px rgba(31,60,92,0.1)", "none", true, true, false, "fast", false, false, false, 0, "none"),
                true,
                false,
                "/catalog/themes/slate-corporate.png"
        ));
        saveTheme(theme(
                "starter-v2-theme-indigo-product-studio",
                "Indigo Product Studio",
                "Confident studio palette built for product stories and measurable outcomes.",
                PlanType.PRO,
                VisualMood.FUTURISTIC_TECH,
                List.of(LayoutAudience.PRODUCT_MANAGER, LayoutAudience.UX_DESIGNER, LayoutAudience.SOFTWARE_ENGINEER, LayoutAudience.GRAPHIC_DESIGNER),
                palette("#4F46E5", "#A5B4FC", "#22C55E", "#EEF2FF", "#FFFFFF", "#111827", "#374151", "#6B7280", "#D7DBF8", "#E5E7EB", "#818CF8", "rgba(79,70,229,0.18)", "#E0E7FF", "#3730A3"),
                gradientBackground("145deg", "#EEF2FF", "0%", "#E0E7FF", "55%", "#F8FAFC", "100%", false, 0),
                typography("Sora", "Inter", "Space Grotesk", 1.0, 2.45, 1.35, 0.82, 700, 400, "none", "normal", -0.01, 1.6, 1.08),
                effects("24px", "0 22px 50px rgba(79,70,229,0.12)", "solid 1px rgba(79,70,229,0.12)", "none", true, true, true, "medium", false, false, false, 0, "none"),
                true,
                true,
                "/catalog/themes/indigo-product-studio.png"
        ));

        saveTheme(theme(
                "starter-v2-theme-clay-creator-magazine",
                "Clay Creator Magazine",
                "Editorial clay tones for writers, strategists, and creator-led portfolios.",
                PlanType.PRO,
                VisualMood.LUXURY_ELEGANT,
                List.of(LayoutAudience.WRITER, LayoutAudience.JOURNALIST, LayoutAudience.ENTREPRENEUR, LayoutAudience.FREELANCER),
                palette("#8A5A44", "#D6B8A6", "#B86E40", "#FBF6F1", "#FFFDFC", "#231815", "#57443C", "#8B746A", "#E2D1C6", "#EEE1D8", "#CF8E67", "rgba(35,24,21,0.12)", "#F3E3D8", "#6D4532"),
                gradientBackground("180deg", "#FFFDFC", "0%", "#FBF6F1", "60%", "#F2E6DC", "100%", true, 10),
                typography("Fraunces", "Manrope", "IBM Plex Sans", 1.0, 2.7, 1.45, 0.78, 700, 400, "none", "normal", 0.0, 1.65, 1.06),
                effects("22px", "0 26px 60px rgba(109,69,50,0.09)", "solid 1px rgba(138,90,68,0.12)", "none", true, true, false, "medium", false, false, true, 8, "none"),
                true,
                false,
                "/catalog/themes/clay-creator-magazine.png"
        ));

        saveTheme(theme(
                "starter-v2-theme-midnight-gallery",
                "Midnight Gallery",
                "Dark cinematic system for image-led and motion-rich portfolios.",
                PlanType.PREMIUM,
                VisualMood.DARK_DRAMATIC,
                List.of(LayoutAudience.PHOTOGRAPHER, LayoutAudience.FILMMAKER, LayoutAudience.ARTIST, LayoutAudience.ILLUSTRATOR),
                palette("#F59E0B", "#4B5563", "#F97316", "#09090B", "#111217", "#F8FAFC", "#D1D5DB", "#9CA3AF", "#24262D", "#1A1C22", "#FBBF24", "rgba(245,158,11,0.3)", "rgba(245,158,11,0.16)", "#111827"),
                gradientBackground("160deg", "#09090B", "0%", "#111217", "55%", "#18181B", "100%", true, 14),
                typography("Archivo", "Inter", "Space Grotesk", 1.0, 2.25, 1.25, 0.76, 700, 400, "uppercase", "normal", 0.08, 1.65, 1.0),
                effects("20px", "0 26px 70px rgba(0,0,0,0.45)", "solid 1px rgba(245,158,11,0.18)", "blur(18px)", true, true, true, "slow", true, false, true, 14, "none"),
                true,
                true,
                "/catalog/themes/midnight-gallery.png"
        ));
    }

    private void seedTemplates() {
        saveTemplate(template(
                "starter-v2-template-clarity-free",
                "Clarity Free",
                "A crisp launch template for resumes and simple professional profiles.",
                "Professional basics, no friction.",
                PlanType.FREE,
                "starter-v2-layout-executive-single-column",
                "starter-v2-theme-sandstone-editorial",
                VisualMood.CLEAN_MINIMAL,
                MotionPreset.SUBTLE,
                10,
                true,
                true,
                1,
                List.of(LayoutAudience.EXECUTIVE, LayoutAudience.STUDENT, LayoutAudience.CONSULTANT),
                List.of("resume", "professional", "starter"),
                List.of(ProfessionCategory.PROFESSIONAL_CORPORATE, ProfessionCategory.STUDENT_EARLY_CAREER),
                List.of(ProfessionType.GENERALIST),
                List.of(ContentMode.RESUME_FIRST),
                list("PROFILE", "SUMMARY", "EXPERIENCE", "EDUCATION", "SKILLS", "PROJECTS", "CONTACT"),
                list("PROFILE", "EXPERIENCE", "CONTACT"),
                List.of(BlockType.RICH_TEXT, BlockType.CTA, BlockType.CUSTOM_LIST, BlockType.METRICS),
                List.of(BlockType.RICH_TEXT, BlockType.CTA),
                List.of(MotionPreset.NONE, MotionPreset.SUBTLE)
        ));

        saveTemplate(template(
                "starter-v2-template-ledger-basic",
                "Ledger Basic",
                "Structured business template for finance, consulting, and formal personal brands.",
                "Quiet authority with strong readability.",
                PlanType.BASIC,
                "starter-v2-layout-executive-single-column",
                "starter-v2-theme-slate-corporate",
                VisualMood.CORPORATE_FORMAL,
                MotionPreset.SUBTLE,
                35,
                false,
                true,
                2,
                List.of(LayoutAudience.CONSULTANT, LayoutAudience.FINANCIAL_ANALYST, LayoutAudience.ACCOUNTANT, LayoutAudience.EXECUTIVE),
                List.of("business", "consulting", "formal"),
                List.of(ProfessionCategory.FINANCE_BUSINESS, ProfessionCategory.CONSULTING_COACHING, ProfessionCategory.PROFESSIONAL_CORPORATE),
                List.of(ProfessionType.BUSINESS_CONSULTANT, ProfessionType.FINANCIAL_ANALYST, ProfessionType.ACCOUNTANT),
                List.of(ContentMode.RESUME_FIRST, ContentMode.SERVICE_FIRST),
                list("PROFILE", "SUMMARY", "EXPERIENCE", "EDUCATION", "SKILLS", "PROJECTS", "CERTIFICATIONS", "CONTACT"),
                list("PROFILE", "EXPERIENCE", "CONTACT"),
                List.of(BlockType.METRICS, BlockType.CTA, BlockType.SERVICES_GRID, BlockType.CUSTOM_LIST),
                List.of(BlockType.METRICS, BlockType.SERVICES_GRID),
                List.of(MotionPreset.NONE, MotionPreset.SUBTLE, MotionPreset.EDITORIAL)
        ));

        saveTemplate(template(
                "starter-v2-template-brief-basic",
                "Brief Basic",
                "Case-study starter for product thinkers and outcome-driven portfolios.",
                "Show the work, prove the impact.",
                PlanType.BASIC,
                "starter-v2-layout-product-case-study",
                "starter-v2-theme-indigo-product-studio",
                VisualMood.FUTURISTIC_TECH,
                MotionPreset.EDITORIAL,
                42,
                true,
                true,
                3,
                List.of(LayoutAudience.PRODUCT_MANAGER, LayoutAudience.UX_DESIGNER, LayoutAudience.GRAPHIC_DESIGNER),
                List.of("case-study", "product", "ux"),
                List.of(ProfessionCategory.DESIGN_CREATIVE, ProfessionCategory.TECH_ENGINEERING),
                List.of(ProfessionType.PRODUCT_MANAGER, ProfessionType.UX_DESIGNER, ProfessionType.UI_DESIGNER),
                List.of(ContentMode.CASE_STUDY_FIRST, ContentMode.PORTFOLIO_FIRST),
                list("PROFILE", "SUMMARY", "PROJECTS", "SKILLS", "CONTACT"),
                list("PROFILE", "PROJECTS", "CONTACT"),
                List.of(BlockType.CASE_STUDY, BlockType.METRICS, BlockType.PROCESS_STEPS, BlockType.QUOTE, BlockType.CTA),
                List.of(BlockType.CASE_STUDY, BlockType.METRICS, BlockType.PROCESS_STEPS),
                List.of(MotionPreset.SUBTLE, MotionPreset.EDITORIAL)
        ));
        saveTemplate(template(
                "starter-v2-template-studio-pro",
                "Studio Pro",
                "Premium-feeling product and design portfolio tuned for measurable work.",
                "A sharper studio story for serious client work.",
                PlanType.PRO,
                "starter-v2-layout-product-case-study",
                "starter-v2-theme-indigo-product-studio",
                VisualMood.FUTURISTIC_TECH,
                MotionPreset.EDITORIAL,
                70,
                true,
                true,
                4,
                List.of(LayoutAudience.UX_DESIGNER, LayoutAudience.PRODUCT_MANAGER, LayoutAudience.SOFTWARE_ENGINEER),
                List.of("portfolio", "studio", "impact"),
                List.of(ProfessionCategory.DESIGN_CREATIVE, ProfessionCategory.TECH_ENGINEERING),
                List.of(ProfessionType.UX_DESIGNER, ProfessionType.UI_DESIGNER, ProfessionType.PRODUCT_MANAGER),
                List.of(ContentMode.CASE_STUDY_FIRST, ContentMode.PORTFOLIO_FIRST),
                list("PROFILE", "SUMMARY", "PROJECTS", "SKILLS", "TESTIMONIALS", "CONTACT"),
                list("PROFILE", "PROJECTS", "CONTACT"),
                List.of(BlockType.CASE_STUDY, BlockType.METRICS, BlockType.PROCESS_STEPS, BlockType.QUOTE, BlockType.CLIENT_LOGOS, BlockType.CTA),
                List.of(BlockType.CASE_STUDY, BlockType.METRICS, BlockType.PROCESS_STEPS, BlockType.CLIENT_LOGOS),
                List.of(MotionPreset.SUBTLE, MotionPreset.EDITORIAL, MotionPreset.PARALLAX)
        ));

        saveTemplate(template(
                "starter-v2-template-columnist-pro",
                "Columnist Pro",
                "Editorial creator template for writing, speaking, and multi-format personal brands.",
                "Publish, package, and convert.",
                PlanType.PRO,
                "starter-v2-layout-creator-sidebar-magazine",
                "starter-v2-theme-clay-creator-magazine",
                VisualMood.LUXURY_ELEGANT,
                MotionPreset.EDITORIAL,
                68,
                false,
                true,
                5,
                List.of(LayoutAudience.WRITER, LayoutAudience.JOURNALIST, LayoutAudience.ENTREPRENEUR, LayoutAudience.SPEAKER),
                List.of("editorial", "creator", "writing"),
                List.of(ProfessionCategory.WRITING_PUBLISHING, ProfessionCategory.MEDIA_ENTERTAINMENT, ProfessionCategory.MARKETING_SALES),
                List.of(ProfessionType.WRITER, ProfessionType.JOURNALIST, ProfessionType.CONTENT_STRATEGIST),
                List.of(ContentMode.STORY_FIRST, ContentMode.SERVICE_FIRST, ContentMode.PORTFOLIO_FIRST),
                list("PROFILE", "SUMMARY", "PROJECTS", "PUBLICATIONS", "SERVICES", "CONTACT"),
                list("PROFILE", "PROJECTS", "CONTACT"),
                List.of(BlockType.RICH_TEXT, BlockType.LINK_LIST, BlockType.QUOTE, BlockType.AVAILABILITY_CARD, BlockType.CTA, BlockType.CUSTOM_LIST),
                List.of(BlockType.RICH_TEXT, BlockType.LINK_LIST, BlockType.AVAILABILITY_CARD),
                List.of(MotionPreset.SUBTLE, MotionPreset.EDITORIAL, MotionPreset.PLAYFUL)
        ));

        saveTemplate(template(
                "starter-v2-template-gallery-premium",
                "Gallery Premium",
                "Immersive photographer template with cinematic browsing and dark visual framing.",
                "Let the work take the whole screen.",
                PlanType.PREMIUM,
                "starter-v2-layout-cinematic-gallery-showcase",
                "starter-v2-theme-midnight-gallery",
                VisualMood.DARK_DRAMATIC,
                MotionPreset.CINEMATIC,
                92,
                true,
                true,
                6,
                List.of(LayoutAudience.PHOTOGRAPHER, LayoutAudience.FILMMAKER, LayoutAudience.ARTIST),
                List.of("gallery", "cinematic", "visual"),
                List.of(ProfessionCategory.PHOTOGRAPHY_FILM, ProfessionCategory.ARTIST_ILLUSTRATION, ProfessionCategory.MEDIA_ENTERTAINMENT),
                List.of(ProfessionType.PHOTOGRAPHER, ProfessionType.VIDEOGRAPHER, ProfessionType.FILMMAKER),
                List.of(ContentMode.GALLERY_FIRST, ContentMode.PORTFOLIO_FIRST),
                list("PROFILE", "PROJECTS", "GALLERY", "CONTACT"),
                list("PROFILE", "GALLERY", "CONTACT"),
                List.of(BlockType.GALLERY, BlockType.EMBED, BlockType.QUOTE, BlockType.LINK_LIST, BlockType.CTA),
                List.of(BlockType.GALLERY, BlockType.EMBED, BlockType.QUOTE),
                List.of(MotionPreset.CINEMATIC, MotionPreset.SLIDESHOW, MotionPreset.IMMERSIVE)
        ));

        saveTemplate(template(
                "starter-v2-template-frame-premium",
                "Frame Premium",
                "Gallery-meets-editorial template for artists and mixed-media portfolios.",
                "A collector-grade presentation layer.",
                PlanType.PREMIUM,
                "starter-v2-layout-cinematic-gallery-showcase",
                "starter-v2-theme-midnight-gallery",
                VisualMood.ARTISTIC_EXPRESSIVE,
                MotionPreset.IMMERSIVE,
                88,
                false,
                true,
                7,
                List.of(LayoutAudience.ARTIST, LayoutAudience.ILLUSTRATOR, LayoutAudience.FILMMAKER),
                List.of("art", "exhibition", "immersive"),
                List.of(ProfessionCategory.ARTIST_ILLUSTRATION, ProfessionCategory.MEDIA_ENTERTAINMENT, ProfessionCategory.PHOTOGRAPHY_FILM),
                List.of(ProfessionType.VISUAL_ARTIST, ProfessionType.MOTION_DESIGNER, ProfessionType.FILMMAKER),
                List.of(ContentMode.GALLERY_FIRST, ContentMode.STORY_FIRST, ContentMode.PORTFOLIO_FIRST),
                list("PROFILE", "PROJECTS", "GALLERY", "EXHIBITIONS_AWARDS", "CONTACT"),
                list("PROFILE", "GALLERY", "CONTACT"),
                List.of(BlockType.GALLERY, BlockType.QUOTE, BlockType.RICH_TEXT, BlockType.EMBED, BlockType.CTA, BlockType.CUSTOM_LIST),
                List.of(BlockType.GALLERY, BlockType.RICH_TEXT, BlockType.QUOTE),
                List.of(MotionPreset.CINEMATIC, MotionPreset.IMMERSIVE, MotionPreset.PARALLAX)
        ));
    }

    private void saveLayout(Layout layout) {
        boolean exists = layoutRepository.existsById(layout.getId())
                || layoutRepository.findAll().stream().anyMatch(item -> item.getName().equalsIgnoreCase(layout.getName()));
        if (!exists) {
            layoutRepository.save(layout);
        }
    }

    private void saveTheme(Theme theme) {
        boolean exists = themeRepository.existsById(theme.getId())
                || themeRepository.findAll().stream().anyMatch(item -> item.getName().equalsIgnoreCase(theme.getName()));
        if (!exists) {
            themeRepository.save(theme);
        }
    }

    private void saveTemplate(Template template) {
        boolean exists = templateRepository.existsById(template.getId())
                || templateRepository.findAll().stream().anyMatch(item -> item.getName().equalsIgnoreCase(template.getName()));
        if (!exists) {
            templateRepository.save(template);
        }
    }

    private Layout layout(String id, String name, String description, LayoutType type, PlanType plan,
                          MotionPreset defaultMotion, List<LayoutAudience> audiences, List<VisualMood> moods,
                          List<ProfessionCategory> categories, List<ProfessionType> professionTypes,
                          List<ContentMode> modes, List<MotionPreset> motions, List<BlockType> blocks,
                          LayoutStructureConfig config) {
        Instant now = Instant.now();
        Layout item = new Layout();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setLayoutType(type);
        item.setRequiredPlan(plan);
        item.setDefaultMotionPreset(defaultMotion);
        item.setTargetAudiences(audiences);
        item.setCompatibleMoods(moods);
        item.setSupportedProfessionCategories(categories);
        item.setSupportedProfessionTypes(professionTypes);
        item.setSupportedContentModes(modes);
        item.setSupportedMotionPresets(motions);
        item.setRecommendedBlockTypes(blocks);
        item.setProfessionTags(List.of());
        item.setStructureConfig(config);
        item.setPreviewImageUrl("/catalog/layouts/" + slug(id) + ".png");
        item.setActive(true);
        item.setVersion(2);
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        return item;
    }

    private Theme theme(String id, String name, String description, PlanType plan, VisualMood mood,
                        List<LayoutAudience> audiences, ThemeColorPalette palette, ThemeBackground background,
                        ThemeTypography typography, ThemeEffects effects, boolean active, boolean featured,
                        String previewImageUrl) {
        Instant now = Instant.now();
        Theme item = new Theme();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setRequiredPlan(plan);
        item.setMood(mood);
        item.setTargetAudiences(audiences);
        item.setColorPalette(palette);
        item.setBackground(background);
        item.setTypography(typography);
        item.setEffects(effects);
        item.setPreviewImageUrl(previewImageUrl);
        item.setActive(active);
        item.setFeatured(featured);
        item.setVersion(2);
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        return item;
    }
    private Template template(String id, String name, String description, String tagline, PlanType plan,
                              String layoutId, String themeId, VisualMood mood, MotionPreset defaultMotion,
                              int popularity, boolean featured, boolean global, int premiumRank,
                              List<LayoutAudience> audiences, List<String> tags,
                              List<ProfessionCategory> categories, List<ProfessionType> professionTypes,
                              List<ContentMode> modes, List<String> sections, List<String> requiredSections,
                              List<BlockType> supportedBlocks, List<BlockType> recommendedBlocks,
                              List<MotionPreset> motions) {
        Instant now = Instant.now();
        Template item = new Template();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setTagline(tagline);
        item.setPlanLevel(plan);
        item.setLayoutId(layoutId);
        item.setDefaultThemeId(themeId);
        item.setPrimaryMood(mood);
        item.setDefaultMotionPreset(defaultMotion);
        item.setTargetAudiences(audiences);
        item.setProfessionTags(tags);
        item.setProfessionCategories(categories);
        item.setProfessionTypes(professionTypes);
        item.setSupportedContentModes(modes);
        item.setSupportedSections(sections);
        item.setRequiredSections(requiredSections);
        item.setSupportedBlockTypes(supportedBlocks);
        item.setRecommendedBlockTypes(recommendedBlocks);
        item.setAllowedMotionPresets(motions);
        item.setPreviewImageUrl("/catalog/templates/" + slug(id) + ".png");
        item.setActive(true);
        item.setFeatured(featured);
        item.setNew(true);
        item.setPopularityScore(popularity);
        item.setPremiumRank(premiumRank);
        item.setGloballySelectable(global);
        item.setVersion(2);
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        return item;
    }

    private LayoutStructureConfig structure(Integer columns, String sidebarPosition, String sidebarWidth,
                                            boolean hero, boolean floatingHeader, boolean stickyContact,
                                            boolean scrollBased, String scrollDirection,
                                            LayoutStructureConfig.GridConfig gridConfig,
                                            LayoutStructureConfig.SectionZone... zones) {
        LayoutStructureConfig item = new LayoutStructureConfig();
        item.setColumnCount(columns);
        item.setSidebarPosition(sidebarPosition);
        item.setSidebarWidthPercent(sidebarWidth);
        item.setHasHeroSection(hero);
        item.setHasFloatingHeader(floatingHeader);
        item.setHasStickyContact(stickyContact);
        item.setIsScrollBased(scrollBased);
        item.setScrollDirection(scrollDirection);
        item.setGridConfig(gridConfig);
        item.setZones(List.of(zones));
        return item;
    }

    private LayoutStructureConfig.SectionZone zone(String id, String label, List<String> sections,
                                                   String width, boolean optional, int order) {
        LayoutStructureConfig.SectionZone item = new LayoutStructureConfig.SectionZone();
        item.setZoneId(id);
        item.setLabel(label);
        item.setAllowedSections(sections);
        item.setDefaultWidth(width);
        item.setOptional(optional);
        item.setDisplayOrder(order);
        return item;
    }

    private LayoutStructureConfig.GridConfig grid(Integer columns, Integer rows, String gap,
                                                  String aspectRatio, boolean variableSize) {
        LayoutStructureConfig.GridConfig item = new LayoutStructureConfig.GridConfig();
        item.setColumns(columns);
        item.setRows(rows);
        item.setGap(gap);
        item.setItemAspectRatio(aspectRatio);
        item.setAllowVariableSize(variableSize);
        return item;
    }

    private ThemeColorPalette palette(String primary, String secondary, String accent, String surface,
                                      String page, String textPrimary, String textSecondary, String textMuted,
                                      String border, String divider, String glow, String shadow,
                                      String tagBackground, String tagText) {
        ThemeColorPalette item = new ThemeColorPalette();
        item.setPrimary(primary);
        item.setSecondary(secondary);
        item.setAccent(accent);
        item.setSurfaceBackground(surface);
        item.setPageBackground(page);
        item.setTextPrimary(textPrimary);
        item.setTextSecondary(textSecondary);
        item.setTextMuted(textMuted);
        item.setBorderColor(border);
        item.setDividerColor(divider);
        item.setGlowColor(glow);
        item.setShadowColor(shadow);
        item.setTagBackground(tagBackground);
        item.setTagText(tagText);
        return item;
    }

    private ThemeBackground solidBackground(String color) {
        ThemeBackground item = new ThemeBackground();
        item.setType(ThemeBackground.BackgroundType.SOLID);
        item.setSolidColor(color);
        return item;
    }

    private ThemeBackground gradientBackground(String angle,
                                               String colorA, String posA,
                                               String colorB, String posB,
                                               String colorC, String posC,
                                               boolean grainy, int grainIntensity) {
        ThemeBackground item = new ThemeBackground();
        ThemeBackground.GradientConfig gradient = new ThemeBackground.GradientConfig();
        gradient.setGradientType(ThemeBackground.GradientType.LINEAR);
        gradient.setAngle(angle);
        gradient.setStops(List.of(stop(colorA, posA), stop(colorB, posB), stop(colorC, posC)));
        gradient.setGrainy(grainy);
        gradient.setGrainIntensity(grainIntensity);
        item.setType(ThemeBackground.BackgroundType.GRADIENT);
        item.setGradient(gradient);
        return item;
    }

    private ThemeBackground.GradientStop stop(String color, String position) {
        ThemeBackground.GradientStop item = new ThemeBackground.GradientStop();
        item.setColor(color);
        item.setPosition(position);
        return item;
    }

    private ThemeTypography typography(String headingFont, String bodyFont, String accentFont,
                                       double baseSize, double headingScale, double subheadingScale,
                                       double labelScale, int headingWeight, int bodyWeight,
                                       String headingTransform, String headingStyle,
                                       double headingLetterSpacing, double bodyLineHeight,
                                       double headingLineHeight) {
        ThemeTypography item = new ThemeTypography();
        item.setHeadingFont(headingFont);
        item.setBodyFont(bodyFont);
        item.setAccentFont(accentFont);
        item.setBaseSize(baseSize);
        item.setHeadingScale(headingScale);
        item.setSubheadingScale(subheadingScale);
        item.setLabelScale(labelScale);
        item.setHeadingWeight(headingWeight);
        item.setBodyWeight(bodyWeight);
        item.setHeadingTransform(headingTransform);
        item.setHeadingStyle(headingStyle);
        item.setHeadingLetterSpacing(headingLetterSpacing);
        item.setBodyLineHeight(bodyLineHeight);
        item.setHeadingLineHeight(headingLineHeight);
        return item;
    }

    private ThemeEffects effects(String radius, String shadow, String border, String backdrop,
                                 boolean reveal, boolean hoverLift, boolean parallax, String speed,
                                 boolean glass, boolean neumorphism, boolean grain, int grainIntensity,
                                 String divider) {
        ThemeEffects item = new ThemeEffects();
        item.setCardBorderRadius(radius);
        item.setCardShadow(shadow);
        item.setCardBorderStyle(border);
        item.setCardBackdropFilter(backdrop);
        item.setEnableScrollReveal(reveal);
        item.setEnableHoverLift(hoverLift);
        item.setEnableParallax(parallax);
        item.setTransitionSpeed(speed);
        item.setEnableGlassmorphism(glass);
        item.setEnableNeumorphism(neumorphism);
        item.setEnableGrain(grain);
        item.setGlobalGrainIntensity(grainIntensity);
        item.setSectionDividerStyle(divider);
        return item;
    }

    private List<String> list(String... values) {
        return List.of(values);
    }

    private String slug(String value) {
        return value.replace("starter-v2-", "");
    }
}
