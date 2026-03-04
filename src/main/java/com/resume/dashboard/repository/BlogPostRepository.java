package com.resume.dashboard.repository;

import com.resume.dashboard.entity.BlogPost;
import com.resume.dashboard.entity.VisibilityType;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends MongoRepository<BlogPost, String> {
    List<BlogPost> findByResumeId(String resumeId);
    List<BlogPost> findByResumeIdAndVisibility(String resumeId, VisibilityType visibility);
    Optional<BlogPost> findByResumeIdAndSlug(String resumeId, String slug);
    List<BlogPost> findByResumeIdOrderByPublishedAtDesc(String resumeId);
}
