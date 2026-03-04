package com.resume.dashboard.service.publicview.section;

import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.PortfolioSectionType;

public interface SectionHandler {

    PortfolioSectionType getSectionType();

    Object getSectionData(Resume resume);
}