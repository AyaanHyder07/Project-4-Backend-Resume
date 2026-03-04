package com.resume.dashboard.repository;

import com.resume.dashboard.entity.Testimonial;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TestimonialRepository extends MongoRepository<Testimonial, String> {

    List<Testimonial> findByResumeId(String resumeId);

    List<Testimonial> findByResumeIdOrderByDisplayOrderAsc(String resumeId);

    List<Testimonial> findByResumeIdAndVerifiedTrueOrderByDisplayOrderAsc(String resumeId);

    long countByResumeId(String resumeId);
}