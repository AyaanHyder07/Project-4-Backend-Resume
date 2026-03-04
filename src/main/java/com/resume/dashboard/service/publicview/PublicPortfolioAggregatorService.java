package com.resume.dashboard.service.publicview;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resume.dashboard.dto.publicview.*;
import com.resume.dashboard.entity.*;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.*;
import com.resume.dashboard.service.publicview.section.SectionHandler;
import com.resume.dashboard.service.publicview.section.SectionHandlerRegistry;
import com.resume.dashboard.service.user.AnalyticsService;

@Service
public class PublicPortfolioAggregatorService {

    private final ResumeRepository resumeRepository;
    private final LayoutRepository layoutRepository;
    private final ThemeRepository themeRepository;
    private final PortfolioSectionConfigRepository sectionConfigRepository;
    private final SectionHandlerRegistry registry;
    private final AnalyticsService analyticsService;

    public PublicPortfolioAggregatorService(
            ResumeRepository resumeRepository,
            LayoutRepository layoutRepository,
            ThemeRepository themeRepository,
            PortfolioSectionConfigRepository sectionConfigRepository,
            SectionHandlerRegistry registry,
            AnalyticsService analyticsService) {

        this.resumeRepository = resumeRepository;
        this.layoutRepository = layoutRepository;
        this.themeRepository = themeRepository;
        this.sectionConfigRepository = sectionConfigRepository;
        this.registry = registry;
        this.analyticsService = analyticsService;
    }

    public PublicPortfolioResponse getPublicPortfolio(String slug) {

        Resume resume = resumeRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        analyticsService.recordEvent(
                resume.getId(),
                AnalyticsEventType.VIEW,
                null
        );

        Layout layout = layoutRepository.findById(resume.getLayoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Layout not found"));

        Theme theme = themeRepository.findById(resume.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        List<PortfolioSectionConfig> enabledSections =
                sectionConfigRepository
                        .findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                        .stream()
                        .filter(PortfolioSectionConfig::isEnabled)
                        .collect(Collectors.toList());

        Map<String, Object> sections = new LinkedHashMap<>();

        for (PortfolioSectionConfig config : enabledSections) {

            SectionHandler handler = registry.getHandler(config.getSectionName());

            if (handler != null) {
                Object data = handler.getSectionData(resume);
                if (data != null) {
                    sections.put(config.getSectionName().name(), data);
                }
            }
        }

        LayoutDTO layoutDTO = new LayoutDTO();
        layoutDTO.setLayoutType(layout.getLayoutType().name());
        layoutDTO.setLayoutConfigJson(layout.getLayoutConfigJson());

        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setName(theme.getName());
        themeDTO.setThemeConfig(theme.getThemeConfig());

        PublicPortfolioResponse response = new PublicPortfolioResponse();
        response.setResumeId(resume.getId()); 
        response.setTitle(resume.getTitle());
        response.setProfessionType(resume.getProfessionType());
        response.setSlug(resume.getSlug());
        response.setLayout(layoutDTO);
        response.setTheme(themeDTO);
        response.setSections(sections);

        return response;
    }
}