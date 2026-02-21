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

    // ================= PUBLIC =================

    @PostMapping("/submit")
    public ApiResponse<Void> submit(
            @Valid @RequestBody ContactRequest request,
            HttpServletRequest httpRequest
    ) {
        String ip = httpRequest.getRemoteAddr();
        return service.submit(request, ip);
    }

    // ================= ADMIN =================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ApiResponse<Page<ContactResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) String search
    ) {
        return service.getAll(page, size, status, search);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateStatus/{publicId}")
    public ApiResponse<Void> updateStatus(
            @PathVariable String publicId,
            @RequestParam LeadStatus status
    ) {
        return service.updateStatus(publicId, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{publicId}")
    public ApiResponse<Void> delete(
            @PathVariable String publicId
    ) {
        return service.delete(publicId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/analytics")
    public ApiResponse<LeadAnalyticsResponse> analytics() {
        return service.getAnalytics();
    }
}
