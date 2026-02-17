package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.ContactRequest;
import com.blackint.dto.response.ContactResponse;
import com.blackint.entity.LeadStatus;
import com.blackint.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ContactController {

    private final ContactService contactservice;

    // Public API
    @PostMapping("/contact")
    public String submit(@Valid @RequestBody ContactRequest request) {
        contactservice.submit(request);
        return "Thank you! Our team will contact you shortly.";
    }

    // Admin APIs
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/contacts")
    ApiResponse<Page<ContactResponse>> getAll() {
        return contactservice.getAll(0, 0);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/contacts/{id}/status")
    public void updateStatus(@PathVariable UUID id,
                             @RequestParam LeadStatus status) {
        contactservice.updateStatus(id, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/contacts/{id}")
    public void delete(@PathVariable UUID id) {
        contactservice.delete(id);
    }
}
