package com.resume.dashboard.entity;

public enum LayoutType {

    // ─── PROFESSIONAL / CORPORATE ───────────────────────────────────
    SINGLE_COLUMN,           // Clean, traditional resume
    TWO_COLUMN,              // Classic split: sidebar + main
    LEFT_SIDEBAR,            // Sidebar on left, content on right
    RIGHT_SIDEBAR,           // Sidebar on right, content on left

    // ─── CREATIVE / PORTFOLIO-FIRST ─────────────────────────────────
    FULLSCREEN_HERO,         // Giant image/video hero at top, content below (artists, photographers)
    MAGAZINE,                // Multi-zone editorial layout (writers, journalists, designers)
    MASONRY_GRID,            // Pinterest-style irregular card grid (illustrators, photographers)
    BENTO_GRID,              // Modern boxed grid with varying card sizes (tech, product designers)
    SPLIT_SCREEN,            // Left: visual identity, Right: content (musicians, personal brand)

    // ─── BOLD / EXPRESSIVE ──────────────────────────────────────────
    BOLD_TYPOGRAPHIC,        // Giant headline-driven, text as visual element (copywriters, poets)
    DIAGONAL_SLASH,          // Diagonal dividers and angled sections (athletes, dancers, performers)
    LAYERED_OVERLAP,         // Overlapping cards and floating elements (art directors, DJs)
    IMMERSIVE_DARK,          // Full dark bg with glowing accent elements (musicians, gamers, hackers)
    CINEMATIC,               // Film-poster aesthetic with full bleed imagery (actors, filmmakers)

    // ─── STRUCTURED / DATA-RICH ─────────────────────────────────────
    TIMELINE,                // Vertical/horizontal journey timeline (academics, historians)
    INFOGRAPHIC,             // Data visualisation-forward layout (analysts, scientists, engineers)
    DASHBOARD_PANEL,         // Panel/widget style layout (developers, data scientists, finance)
    MODERN_GRID,             // Clean equal-width card grid (consultants, architects)

    // ─── SPECIALTY / NICHE ──────────────────────────────────────────
    GALLERY_FOCUS,           // Almost all visual, minimal text (painters, sculptors, fashion)
    CARD_STACK,              // Flip/stack card interaction style (speakers, coaches)
    SCROLL_STORY,            // Narrative scroll with section reveals (journalists, storytellers)
    MINIMAL_ZEN,             // Extreme whitespace, one element at a time (therapists, meditators)
    LUXURY_EDITORIAL,        // High-fashion editorial grid (models, luxury brand managers)
    MEDICAL_CLINICAL,        // Clean structured sections, no distractions (doctors, nurses)
    LEGAL_FORMAL,            // Serif-heavy, structured, dignified (lawyers, politicians, judges)
    FINANCIAL_STRUCTURED,    // Table-forward, precision layout (accountants, bankers, analysts)
    ACADEMIC_CV,             // Long-form, publication-ready (professors, researchers)
    STARTUP_PITCH            // Product-pitch aesthetic (founders, VCs, product managers)
}