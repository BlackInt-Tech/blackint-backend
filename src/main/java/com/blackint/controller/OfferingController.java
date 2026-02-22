package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.OfferingRequest;
import com.blackint.dto.response.OfferingResponse;
import com.blackint.service.OfferingService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offerings")
@RequiredArgsConstructor
public class OfferingController {

    private final OfferingService service;

    // ================= ADMIN CREATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ApiResponse<OfferingResponse> create(
            @RequestBody OfferingRequest request
    ) {
        return ApiResponse.success(service.create(request));
    }

    // ================= ADMIN UPDATE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{publicId}")
    public ApiResponse<OfferingResponse> update(
            @PathVariable String publicId,
            @RequestBody OfferingRequest request
    ) {
        return ApiResponse.success(service.update(publicId, request));
    }

    // ================= ADMIN DELETE =================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{publicId}")
    public ApiResponse<Void> delete(@PathVariable String publicId) {
        service.softDelete(publicId);
        return ApiResponse.success(null);
    }

    // ================= ADMIN RESTORE =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restore/{publicId}")
    public ApiResponse<Void> restore(@PathVariable String publicId) {
        service.restore(publicId);
        return ApiResponse.success(null);
    }

    // ================= ADMIN PUBLISH =================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/publish/{publicId}")
    public ApiResponse<OfferingResponse> publish(
            @PathVariable String publicId
    ) {
        return ApiResponse.success(service.publish(publicId));
    }

    // ================= PUBLIC =================
    @GetMapping("/published")
    public ApiResponse<List<OfferingResponse>> getPublished() {
        return ApiResponse.success(service.getPublished());
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<OfferingResponse> getBySlug(
            @PathVariable String slug
    ) {
        return ApiResponse.success(service.getBySlug(slug));
    }

    // ================= ADMIN VIEW ALL =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ApiResponse<List<OfferingResponse>> getAllForAdmin() {
        return ApiResponse.success(service.getAllForAdmin());
    }
}
