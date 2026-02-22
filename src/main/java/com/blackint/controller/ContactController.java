package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.ContactRequest;
import com.blackint.dto.response.ContactResponse;
import com.blackint.dto.response.LeadAnalyticsResponse;
import com.blackint.entity.LeadStatus;
import com.blackint.service.ContactService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService service;

    @PostMapping("/submit")
    public ApiResponse<Void> submit(
            @Valid @RequestBody ContactRequest request,
            HttpServletRequest httpRequest
    ) {
        service.submit(request, httpRequest.getRemoteAddr());
        return ApiResponse.successMessage(
                "Thank you! Our team will contact you shortly."
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<Page<ContactResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) String search
    ) {
        return ApiResponse.success(service.getAll(page, size, status, search));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{publicId}")
    public ApiResponse<Void> updateStatus(
            @PathVariable String publicId,
            @RequestParam LeadStatus status
    ) {
        service.updateStatus(publicId, status);
        return ApiResponse.successMessage("Lead status updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{publicId}")
    public ApiResponse<Void> delete(@PathVariable String publicId) {
        service.delete(publicId);
        return ApiResponse.successMessage("Lead deleted successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/analytics")
    public ApiResponse<LeadAnalyticsResponse> analytics() {
        return ApiResponse.success(service.getAnalytics());
    }
}