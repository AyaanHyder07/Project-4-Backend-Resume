package com.resume.dashboard.service.publicview.section;

import org.springframework.stereotype.Component;

import com.resume.dashboard.dto.publicview.PublicBlogPostDTO;
import com.resume.dashboard.entity.BlogPost;
import com.resume.dashboard.entity.PortfolioSectionType;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.entity.VisibilityType;
import com.resume.dashboard.repository.BlogPostRepository;

@Component
public class BlogSectionHandler implements SectionHandler {

    private final BlogPostRepository repository;

    public BlogSectionHandler(BlogPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public PortfolioSectionType getSectionType() {
        return PortfolioSectionType.BLOG_POSTS;
    }

    @Override
    public Object getSectionData(Resume resume) {

        return repository.findByResumeIdAndVisibility(resume.getId(), VisibilityType.PUBLIC)
                .stream()
                .map(this::map)
                .toList();
    }

    private PublicBlogPostDTO map(BlogPost b) {
        PublicBlogPostDTO dto = new PublicBlogPostDTO();
        dto.setTitle(b.getTitle());
        dto.setSlug(b.getSlug());
        dto.setCoverImage(b.getCoverImage());
        dto.setTags(b.getTags());
        dto.setPublishedAt(b.getPublishedAt());
        return dto;
    }
}
