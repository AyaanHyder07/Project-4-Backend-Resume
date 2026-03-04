package com.resume.dashboard.controller;

import com.resume.dashboard.dto.blog.*;
import com.resume.dashboard.service.BlogPostService;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogPostController {

    private final BlogPostService blogService;

    public BlogPostController(BlogPostService blogService) {
        this.blogService = blogService;
    }

    /* =========================================================
       CREATE BLOG (OWNER)
    ========================================================= */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> create(
            @AuthenticationPrincipal String userId,
            @RequestPart("data") CreateBlogRequest request,
            @RequestPart(value = "coverFile", required = false) MultipartFile coverFile) {

        return ResponseEntity.ok(
                blogService.createBlog(userId, request, coverFile)
        );
    }

    /* =========================================================
       UPDATE BLOG (OWNER)
    ========================================================= */
    @PutMapping(
            value = "/{blogId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BlogResponse> update(
            @AuthenticationPrincipal String userId,
            @PathVariable String blogId,
            @RequestPart("data") @Valid UpdateBlogRequest request,
            @RequestPart(value = "coverFile", required = false) MultipartFile coverFile) {

        return ResponseEntity.ok(
                blogService.updateBlog(userId, blogId, request, coverFile)
        );
    }

    /* =========================================================
       DELETE BLOG (OWNER)
    ========================================================= */
    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String userId,
            @PathVariable String blogId) {

        blogService.deleteBlog(userId, blogId);
        return ResponseEntity.noContent().build();
    }

    /* =========================================================
       GET PRIVATE BLOGS (OWNER)
    ========================================================= */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<BlogResponse>> getPrivateBlogs(
            @AuthenticationPrincipal String userId,
            @PathVariable String resumeId) {

        return ResponseEntity.ok(
                blogService.getBlogs(userId, resumeId)
        );
    }

    /* =========================================================
       GET PUBLIC BLOG
       Example:
       /api/blogs/public/{resumeId}/{slug}
    ========================================================= */
    @GetMapping("/public/{resumeId}/{slug}")
    public ResponseEntity<PublicBlogResponse> getPublicBlog(
            @PathVariable String resumeId,
            @PathVariable String slug) {

        return ResponseEntity.ok(
                blogService.getPublicBlog(resumeId, slug)
        );
    }
}