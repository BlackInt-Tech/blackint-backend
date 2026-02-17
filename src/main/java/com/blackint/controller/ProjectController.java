package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.ProjectRequest;
import com.blackint.dto.response.LeadAnalyticsResponse;
import com.blackint.dto.response.ProjectResponse;
import com.blackint.service.ProjectService;
import com.blackint.service.ContactService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;
    private final ContactService contactService;

    // ================= ADMIN CREATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProjectResponse create(@RequestBody ProjectRequest request) {
        return service.create(request);
    }

    // ================= ADMIN UPDATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable UUID id,
                                  @RequestBody ProjectRequest request) {
        return service.update(id, request);
    }

    // ================= ADMIN DELETE =================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.softDelete(id);
    }

    // ================= ADMIN RESTORE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/restore")
    public void restore(@PathVariable UUID id) {
        service.restore(id);
    }

    // ================= ADMIN PUBLISH =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/publish")
    public ProjectResponse publish(@PathVariable UUID id) {
        return service.publish(id);
    }

    // ================= PUBLIC =================
    @GetMapping("/published")
    public List<ProjectResponse> getPublished() {
        return service.getPublished();
    }

    @GetMapping("/{slug}")
    public ProjectResponse getBySlug(@PathVariable String slug) {
        return service.getBySlug(slug);
    }

    // ================= ADMIN VIEW ALL =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<ProjectResponse> getAllForAdmin() {
        return service.getAllForAdmin();
    }

    // ================= ADMIN ANALYTICS =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/analytics")
    public ApiResponse<LeadAnalyticsResponse> analytics() {
        return contactService.getAnalytics();
    }
}
