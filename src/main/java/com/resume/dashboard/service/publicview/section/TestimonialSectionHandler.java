package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicTestimonialDTO;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Testimonial;
import com.resume.dashboard.repository.TestimonialRepository;

@Component
public class TestimonialSectionHandler implements SectionHandler {

    private final TestimonialRepository repository;

    public TestimonialSectionHandler(TestimonialRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.TESTIMONIALS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    private PublicTestimonialDTO map(Testimonial t) {
        PublicTestimonialDTO dto = new PublicTestimonialDTO();
        dto.setClientName(t.getClientName());
        dto.setClientCompany(t.getClientCompany());
        dto.setRating(t.getRating());
        dto.setDateGiven(t.getDateGiven());
        dto.setTestimonialText(t.getTestimonialText());
        return dto;
    }
}
