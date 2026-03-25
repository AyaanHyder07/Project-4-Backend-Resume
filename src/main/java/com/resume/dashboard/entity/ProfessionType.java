package com.resume.dashboard.entity;

import java.util.Locale;

public enum ProfessionType {
    GENERALIST(ProfessionCategory.PROFESSIONAL_CORPORATE),
    SOFTWARE_ENGINEER(ProfessionCategory.TECH_ENGINEERING),
    FRONTEND_DEVELOPER(ProfessionCategory.TECH_ENGINEERING),
    BACKEND_DEVELOPER(ProfessionCategory.TECH_ENGINEERING),
    FULL_STACK_DEVELOPER(ProfessionCategory.TECH_ENGINEERING),
    MOBILE_DEVELOPER(ProfessionCategory.TECH_ENGINEERING),
    DEVOPS_ENGINEER(ProfessionCategory.TECH_ENGINEERING),
    DATA_SCIENTIST(ProfessionCategory.TECH_ENGINEERING),
    DATA_ANALYST(ProfessionCategory.TECH_ENGINEERING),
    PRODUCT_MANAGER(ProfessionCategory.TECH_ENGINEERING),
    UX_DESIGNER(ProfessionCategory.DESIGN_CREATIVE),
    UI_DESIGNER(ProfessionCategory.DESIGN_CREATIVE),
    GRAPHIC_DESIGNER(ProfessionCategory.DESIGN_CREATIVE),
    BRAND_DESIGNER(ProfessionCategory.DESIGN_CREATIVE),
    ILLUSTRATOR(ProfessionCategory.ARTIST_ILLUSTRATION),
    VISUAL_ARTIST(ProfessionCategory.ARTIST_ILLUSTRATION),
    ANIMATOR(ProfessionCategory.ARTIST_ILLUSTRATION),
    MOTION_DESIGNER(ProfessionCategory.DESIGN_CREATIVE),
    PHOTOGRAPHER(ProfessionCategory.PHOTOGRAPHY_FILM),
    VIDEOGRAPHER(ProfessionCategory.PHOTOGRAPHY_FILM),
    FILMMAKER(ProfessionCategory.PHOTOGRAPHY_FILM),
    WRITER(ProfessionCategory.WRITING_PUBLISHING),
    CONTENT_STRATEGIST(ProfessionCategory.WRITING_PUBLISHING),
    JOURNALIST(ProfessionCategory.WRITING_PUBLISHING),
    DOCTOR(ProfessionCategory.HEALTHCARE_MEDICAL),
    NURSE(ProfessionCategory.HEALTHCARE_MEDICAL),
    THERAPIST(ProfessionCategory.HEALTHCARE_MEDICAL),
    LAWYER(ProfessionCategory.LEGAL_PUBLIC_POLICY),
    LEGAL_CONSULTANT(ProfessionCategory.LEGAL_PUBLIC_POLICY),
    ACCOUNTANT(ProfessionCategory.FINANCE_BUSINESS),
    FINANCIAL_ANALYST(ProfessionCategory.FINANCE_BUSINESS),
    INVESTMENT_BANKER(ProfessionCategory.FINANCE_BUSINESS),
    TEACHER(ProfessionCategory.EDUCATION_RESEARCH),
    RESEARCHER(ProfessionCategory.EDUCATION_RESEARCH),
    PROFESSOR(ProfessionCategory.EDUCATION_RESEARCH),
    MARKETING_SPECIALIST(ProfessionCategory.MARKETING_SALES),
    SALES_MANAGER(ProfessionCategory.MARKETING_SALES),
    BUSINESS_CONSULTANT(ProfessionCategory.CONSULTING_COACHING),
    COACH(ProfessionCategory.CONSULTING_COACHING),
    ARCHITECT(ProfessionCategory.REAL_ESTATE_ARCHITECTURE),
    INTERIOR_DESIGNER(ProfessionCategory.REAL_ESTATE_ARCHITECTURE),
    REAL_ESTATE_AGENT(ProfessionCategory.REAL_ESTATE_ARCHITECTURE),
    CHEF(ProfessionCategory.HOSPITALITY_EVENTS),
    EVENT_PLANNER(ProfessionCategory.HOSPITALITY_EVENTS),
    MUSICIAN(ProfessionCategory.MEDIA_ENTERTAINMENT),
    ACTOR(ProfessionCategory.MEDIA_ENTERTAINMENT),
    MODEL(ProfessionCategory.MEDIA_ENTERTAINMENT),
    NONPROFIT_LEADER(ProfessionCategory.NONPROFIT_SOCIAL_IMPACT),
    STUDENT(ProfessionCategory.STUDENT_EARLY_CAREER);

    private final ProfessionCategory category;

    ProfessionType(ProfessionCategory category) {
        this.category = category;
    }

    public ProfessionCategory getCategory() {
        return category;
    }

    public static ProfessionType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return GENERALIST;
        }

        String normalized = value.trim()
                .replace('-', '_')
                .replace(' ', '_')
                .toUpperCase(Locale.ROOT);
        try {
            return ProfessionType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return GENERALIST;
        }
    }
}
