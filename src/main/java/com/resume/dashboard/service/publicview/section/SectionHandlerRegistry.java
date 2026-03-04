package com.resume.dashboard.service.publicview.section;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.resume.dashboard.entity.PortfolioSectionType;

@Component
public class SectionHandlerRegistry {

    private final Map<PortfolioSectionType, SectionHandler> handlers;

    public SectionHandlerRegistry(java.util.List<SectionHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(
                        SectionHandler::getSectionType,
                        h -> h
                ));
    }

    public SectionHandler getHandler(PortfolioSectionType type) {
        return handlers.get(type);
    }
}