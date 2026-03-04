package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicServiceOfferingDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.ServiceOffering;
import com.resume.dashboard.repository.ServiceOfferingRepository;

@Component
public class ServiceOfferingSectionHandler implements SectionHandler {

    private final ServiceOfferingRepository repository;

    public ServiceOfferingSectionHandler(ServiceOfferingRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.SERVICE_OFFERINGS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    private PublicServiceOfferingDTO map(ServiceOffering s) {
        PublicServiceOfferingDTO dto = new PublicServiceOfferingDTO();
        dto.setServiceTitle(s.getServiceTitle());
        dto.setServiceCategory(s.getServiceCategory());
        dto.setDescription(s.getDescription());
        dto.setPricingModel(s.getPricingModel().name());
        dto.setBasePrice(s.getBasePrice());
        dto.setCurrency(s.getCurrency().name());
        dto.setDeliverables(s.getDeliverables());
        dto.setFeatured(s.isFeatured());
        return dto;
    }
}