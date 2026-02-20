package com.blackint.controller;

import com.blackint.dto.request.CreateBlogRequest;
import com.blackint.dto.response.BlogResponse;
import com.blackint.entity.BlogStatus;
import com.blackint.service.BlogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // ==============================
    // PUBLIC APIs
    // ==============================

    // GET /api/blogs
    @GetMapping
    public Page<BlogResponse> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return blogService.getPublished(page, size);
    }

    // GET /api/blogs/{slug}
    @GetMapping("/{slug}")
    public BlogResponse getBySlug(@PathVariable String slug) {
        return blogService.getBySlug(slug);
    }

    // GET /api/blogs/featured
    @GetMapping("/featured")
    public Page<BlogResponse> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return blogService.getFeatured(page, size);
    }

    // GET /api/blogs/category/{slug}
    @GetMapping("/category/{slug}")
    public Page<BlogResponse> getByCategory(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return blogService.getByCategory(slug, page, size);
    }

    // GET /api/blogs/tag/{slug}
    @GetMapping("/tag/{slug}")
    public Page<BlogResponse> getByTag(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return blogService.getByTag(slug, page, size);
    }

    // ==============================
    // ADMIN APIs
    // ==============================

    // POST /api/admin/blogs
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse create(@Valid @RequestBody CreateBlogRequest request) {
        return blogService.create(request);
    }

    // PUT /api/admin/blogs/{id}
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateBlogRequest request
    ) {
        return blogService.update(id, request);
    }

    // PUT /api/admin/blogs/{id}/publish
    @PutMapping("/admin/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse publish(@PathVariable UUID id) {
        return blogService.changeStatus(id, BlogStatus.PUBLISHED, null);
    }

    // PUT /api/admin/blogs/{id}/schedule
    @PutMapping("/admin/{id}/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse schedule(
            @PathVariable UUID id,
            @RequestParam LocalDateTime scheduledAt
    ) {
        return blogService.changeStatus(id, BlogStatus.SCHEDULED, scheduledAt);
    }

    // DELETE /api/admin/blogs/{id}
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        blogService.softDelete(id);
    }
}