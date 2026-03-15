package com.resume.dashboard.service;

import com.resume.dashboard.dto.blog.*;
import com.resume.dashboard.entity.BlogPost;
import com.resume.dashboard.entity.Resume;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.BlogPostRepository;
import com.resume.dashboard.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    private final BlogPostRepository blogRepo;
    private final ResumeRepository resumeRepo;
    private final CloudinaryService cloudinaryService;

    public BlogPostService(BlogPostRepository blogRepo,
            ResumeRepository resumeRepo,
            CloudinaryService cloudinaryService) {
this.blogRepo = blogRepo;
this.resumeRepo = resumeRepo;
this.cloudinaryService = cloudinaryService;
}

    /*
     * CREATE BLOG
     */
    public BlogResponse createBlog(String userId,
            CreateBlogRequest request,
            MultipartFile coverFile) {

        Resume resume = resumeRepo.findById(request.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        String slug = generateSlug(request.getTitle(), resume.getId());

        BlogPost blog = new BlogPost();
        blog.setId(UUID.randomUUID().toString());
        blog.setResumeId(resume.getId());
        blog.setTitle(request.getTitle());
        blog.setSlug(slug);
        String coverUrl = null;

        if (coverFile != null && !coverFile.isEmpty()) {
            coverUrl = cloudinaryService.uploadImage(
                    coverFile,
                    "resume/blogs/" + resume.getId()
            );
        }

        blog.setCoverImage(coverUrl);
        blog.setContent(request.getContent());
        blog.setTags(request.getTags());
        
        String visibility = request.getVisibility() != null ? request.getVisibility() : "Draft";
        blog.setVisibility(visibility);
        if ("Public".equals(visibility)) {
            blog.setPublishedAt(Instant.now());
        }
        
        blog.setViewCount(0);
        blog.setCreatedAt(Instant.now());
        blog.setUpdatedAt(Instant.now());

        return mapToResponse(blogRepo.save(blog));
    }

    /*
     * UPDATE BLOG
     */
    public BlogResponse updateBlog(String userId,
            String blogId,
            UpdateBlogRequest request,
            MultipartFile coverFile) {

        BlogPost blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        Resume resume = resumeRepo.findById(blog.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        blog.setTitle(request.getTitle());
        if (coverFile != null && !coverFile.isEmpty()) {

            // Optional: delete old image here (advanced step)
            String newUrl = cloudinaryService.uploadImage(
                    coverFile,
                    "resume/blogs/" + resume.getId()
            );

            blog.setCoverImage(newUrl);
        }	
        blog.setContent(request.getContent());
        blog.setTags(request.getTags());
        blog.setVisibility(request.getVisibility() != null
                ? request.getVisibility()
                : blog.getVisibility());

        if ("Public".equals(request.getVisibility())) {
            blog.setPublishedAt(Instant.now());
        }

        blog.setUpdatedAt(Instant.now());

        return mapToResponse(blogRepo.save(blog));
    }

    /*
     * DELETE
     */
    public void deleteBlog(String userId, String blogId) {

        BlogPost blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        Resume resume = resumeRepo.findById(blog.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        blogRepo.delete(blog);
    }

    /*
     * GET PRIVATE BLOGS
     */
    public List<BlogResponse> getBlogs(String userId, String resumeId) {

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized");
        }

        return blogRepo.findByResumeIdOrderByPublishedAtDesc(resumeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /*
     * GET PUBLIC BLOG
     */
    public PublicBlogResponse getPublicBlog(String resumeId, String slug) {

        BlogPost blog = blogRepo.findByResumeIdAndSlug(resumeId, slug)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        if (!"Public".equals(blog.getVisibility())) {
            throw new IllegalStateException("Not public");
        }

        blog.setViewCount(blog.getViewCount() + 1);
        blogRepo.save(blog);

        return mapToPublicResponse(blog);
    }

    /*
     * MAPPERS
     */
    private BlogResponse mapToResponse(BlogPost blog) {

        BlogResponse res = new BlogResponse();
        res.setId(blog.getId());
        res.setResumeId(blog.getResumeId());
        res.setTitle(blog.getTitle());
        res.setSlug(blog.getSlug());
        res.setCoverImage(blog.getCoverImage());
        res.setContent(blog.getContent());
        res.setTags(blog.getTags());
        res.setVisibility(blog.getVisibility());
        res.setViewCount(blog.getViewCount());
        res.setPublishedAt(blog.getPublishedAt());
        res.setCreatedAt(blog.getCreatedAt());
        res.setUpdatedAt(blog.getUpdatedAt());

        return res;
    }

    private PublicBlogResponse mapToPublicResponse(BlogPost blog) {

        PublicBlogResponse res = new PublicBlogResponse();
        res.setTitle(blog.getTitle());
        res.setSlug(blog.getSlug());
        res.setCoverImage(blog.getCoverImage());
        res.setContent(blog.getContent());
        res.setTags(blog.getTags());
        res.setViewCount(blog.getViewCount());
        res.setPublishedAt(blog.getPublishedAt());

        return res;
    }

    private String generateSlug(String title, String resumeId) {

        String base = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");

        return base + "-" + resumeId.substring(0, 6);
    }
}