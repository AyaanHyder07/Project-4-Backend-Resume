package com.resume.dashboard.service;

import com.resume.dashboard.dto.testimonial.*;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.Testimonial;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.TestimonialRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestimonialService {

    private final TestimonialRepository repository;
    private final ResumeRepository resumeRepository;

    public TestimonialService(TestimonialRepository repository,
                              ResumeRepository resumeRepository) {
        this.repository = repository;
        this.resumeRepository = resumeRepository;
    }

    /*
     * CREATE
     */
    public TestimonialResponse create(String userId,
                                      CreateTestimonialRequest request) {

        Resume resume = validateOwnership(request.getResumeId(), userId);

        int nextOrder = repository
                .findByResumeIdOrderByDisplayOrderAsc(resume.getId())
                .stream()
                .mapToInt(Testimonial::getDisplayOrder)
                .max()
                .orElse(0) + 1;

        Testimonial entity = new Testimonial();
        entity.setId(UUID.randomUUID().toString());
        entity.setResumeId(resume.getId());
        entity.setClientName(request.getClientName());
        entity.setClientCompany(request.getClientCompany());
        entity.setRating(request.getRating());
        entity.setDateGiven(request.getDateGiven());
        entity.setVerified(false); // default false
        entity.setTestimonialText(request.getTestimonialText());
        entity.setDisplayOrder(nextOrder);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return map(repository.save(entity));
    }

    /*
     * UPDATE
     */
    public TestimonialResponse update(String userId,
                                      String testimonialId,
                                      UpdateTestimonialRequest request) {

        Testimonial entity = repository.findById(testimonialId)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));

        validateOwnership(entity.getResumeId(), userId);

        if (request.getClientName() != null)
            entity.setClientName(request.getClientName());

        if (request.getClientCompany() != null)
            entity.setClientCompany(request.getClientCompany());

        if (request.getRating() != null) {
            if (request.getRating() < 1 || request.getRating() > 5)
                throw new IllegalArgumentException("Rating must be 1–5");
            entity.setRating(request.getRating());
        }

        if (request.getDateGiven() != null)
            entity.setDateGiven(request.getDateGiven());

        if (request.getVerified() != null)
            entity.setVerified(request.getVerified());

        if (request.getTestimonialText() != null)
            entity.setTestimonialText(request.getTestimonialText());

        entity.setUpdatedAt(Instant.now());

        return map(repository.save(entity));
    }

    /*
     * DELETE
     */
    public void delete(String userId, String testimonialId) {

        Testimonial entity = repository.findById(testimonialId)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));

        validateOwnership(entity.getResumeId(), userId);

        repository.delete(entity);
    }

    /*
     * PRIVATE LIST
     */
    public List<TestimonialResponse> getByResume(String userId, String resumeId) {

        validateOwnership(resumeId, userId);

        return repository.findByResumeIdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    /*
     * PUBLIC LIST
     */
    public List<PublicTestimonialResponse> getPublic(String resumeId) {

        return repository
                .findByResumeIdAndVerifiedTrueOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(this::mapPublic)
                .collect(Collectors.toList());
    }

    /*
     * REORDER
     */
    public void reorder(String userId,
                        String resumeId,
                        List<String> orderedIds) {

        validateOwnership(resumeId, userId);

        int order = 1;

        for (String id : orderedIds) {

            Testimonial entity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));

            entity.setDisplayOrder(order++);
            entity.setUpdatedAt(Instant.now());
            repository.save(entity);
        }
    }

    /*
     * OWNERSHIP CHECK
     */
    private Resume validateOwnership(String resumeId, String userId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access");
        }

        return resume;
    }

    /*
     * MAPPERS
     */
    private TestimonialResponse map(Testimonial t) {

        TestimonialResponse r = new TestimonialResponse();
        r.setId(t.getId());
        r.setResumeId(t.getResumeId());
        r.setClientName(t.getClientName());
        r.setClientCompany(t.getClientCompany());
        r.setRating(t.getRating());
        r.setDateGiven(t.getDateGiven());
        r.setVerified(t.isVerified());
        r.setTestimonialText(t.getTestimonialText());
        r.setDisplayOrder(t.getDisplayOrder());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());
        return r;
    }

    private PublicTestimonialResponse mapPublic(Testimonial t) {

        PublicTestimonialResponse r = new PublicTestimonialResponse();
        r.setClientName(t.getClientName());
        r.setClientCompany(t.getClientCompany());
        r.setRating(t.getRating());
        r.setDateGiven(t.getDateGiven());
        r.setTestimonialText(t.getTestimonialText());
        return r;
    }
}