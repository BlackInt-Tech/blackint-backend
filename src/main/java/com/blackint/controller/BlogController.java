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

    //get all published blog
    @GetMapping("/published")
    public Page<BlogResponse> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return blogService.getPublished(page, size);
    }

    //get blog by slug
    @GetMapping("/slug/{slug}")
    public BlogResponse getBySlug(@PathVariable String slug) {
        return blogService.getBySlug(slug);
    }

    //get featured blog
    @GetMapping("/featured")
    public Page<BlogResponse> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return blogService.getFeatured(page, size);
    }

    //get blog by category
    @GetMapping("/category/{slug}")
    public Page<BlogResponse> getByCategory(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return blogService.getByCategory(slug, page, size);
    }

    //get blog by tag
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

    //Admin create blog
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse create(@Valid @RequestBody CreateBlogRequest request) {
        return blogService.create(request);
    }

    //Admin update blog
    @PutMapping("/update/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateBlogRequest request
    ) {
        return blogService.update(id, request);
    }

    //Admin publish project
    @PutMapping("/publish/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse publish(@PathVariable UUID id) {
        return blogService.changeStatus(id, BlogStatus.PUBLISHED, null);
    }

    //Admin schedule blog
    @PutMapping("/schedule/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public BlogResponse schedule(
            @PathVariable UUID id,
            @RequestParam LocalDateTime scheduledAt
    ) {
        return blogService.changeStatus(id, BlogStatus.SCHEDULED, scheduledAt);
    }

    // Admin delete blog
    @DeleteMapping("/delete/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        blogService.softDelete(id);
    }
}