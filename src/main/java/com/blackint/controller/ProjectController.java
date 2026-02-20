package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.ProjectRequest;
import com.blackint.dto.response.ProjectResponse;
import com.blackint.dto.response.LeadAnalyticsResponse;
import com.blackint.service.ProjectService;
import com.blackint.service.ContactService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ContactService contactService;

    // ================= ADMIN ROUTES =======================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/projects")
    public ApiResponse<ProjectResponse> create(
            @RequestBody ProjectRequest request) {

        return ApiResponse.success(projectService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/projects/{publicId}")
    public ApiResponse<ProjectResponse> update(
            @PathVariable String publicId,
            @RequestBody ProjectRequest request) {

        return ApiResponse.success(projectService.update(publicId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/admin/projects/{publicId}")
    public ApiResponse<Void> delete(@PathVariable String publicId) {

        projectService.softDelete(publicId);
        return ApiResponse.success(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/projects/{publicId}/restore")
    public ApiResponse<Void> restore(@PathVariable String publicId) {

        projectService.restore(publicId);
        return ApiResponse.success(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/projects/{publicId}/publish")
    public ApiResponse<ProjectResponse> publish(@PathVariable String publicId) {

        return ApiResponse.success(projectService.publish(publicId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/projects")
    public ApiResponse<List<ProjectResponse>> getAllForAdmin() {

        return ApiResponse.success(projectService.getAllForAdmin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/projects/analytics")
    public ApiResponse<LeadAnalyticsResponse> analytics() {

        return contactService.getAnalytics();
    }

    // ================= PUBLIC ROUTES ======================

    @GetMapping("/api/projects")
    public ApiResponse<List<ProjectResponse>> getPublished() {

        return ApiResponse.success(projectService.getPublished());
    }

    @GetMapping("/api/projects/{slug}")
    public ApiResponse<ProjectResponse> getBySlug(
            @PathVariable String slug) {

        return ApiResponse.success(projectService.getBySlug(slug));
    }
}
