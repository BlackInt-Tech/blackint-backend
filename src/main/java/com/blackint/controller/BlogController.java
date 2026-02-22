package com.blackint.controller;

import com.blackint.common.ApiResponse;
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

    // ================= PUBLIC =================

    @GetMapping("/published")
    public ApiResponse<Page<BlogResponse>> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(blogService.getPublished(page, size));
    }

    @GetMapping("/{slug}")
    public ApiResponse<BlogResponse> getBySlug(@PathVariable String slug) {
        return ApiResponse.success(blogService.getBySlug(slug));
    }

    @GetMapping("/featured")
    public ApiResponse<Page<BlogResponse>> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ApiResponse.success(blogService.getFeatured(page, size));
    }

    @GetMapping("/category/{slug}")
    public ApiResponse<Page<BlogResponse>> getByCategory(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(blogService.getByCategory(slug, page, size));
    }

    @GetMapping("/tag/{slug}")
    public ApiResponse<Page<BlogResponse>> getByTag(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(blogService.getByTag(slug, page, size));
    }

    // ================= ADMIN =================

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BlogResponse> create(
            @Valid @RequestBody CreateBlogRequest request) {

        return ApiResponse.success(blogService.create(request));
    }

    @PutMapping("/update/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BlogResponse> update(
            @PathVariable String publicId,
            @Valid @RequestBody CreateBlogRequest request) {

        return ApiResponse.success(blogService.update(publicId, request));
    }

    @PutMapping("/publish/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BlogResponse> publish(@PathVariable String publicId) {

        return ApiResponse.success(
                blogService.changeStatus(publicId, BlogStatus.PUBLISHED, null)
        );
    }

    @PutMapping("/schedule/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BlogResponse> schedule(
            @PathVariable String publicId,
            @RequestParam LocalDateTime scheduledAt) {

        return ApiResponse.success(
                blogService.changeStatus(publicId, BlogStatus.SCHEDULED, scheduledAt)
        );
    }

    @DeleteMapping("/delete/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable String publicId) {

        blogService.softDelete(publicId);
        return ApiResponse.successMessage("Blog deleted successfully");
    }
}