package com.blackint.service;

import com.blackint.dto.request.ContactRequest;
import com.blackint.common.ApiResponse;
import com.blackint.dto.response.ContactResponse;
import com.blackint.dto.response.LeadAnalyticsResponse;
import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import com.blackint.exception.ResourceNotFoundException;
import com.blackint.mapper.ContactMapper;
import com.blackint.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository repository;
    private final EmailService emailService;

    /**
     * Public Lead Submission
     */
    public ApiResponse<Void> submit(ContactRequest request) {

        Contact contact = ContactMapper.toEntity(request);
        repository.save(contact);

        // Async Emails
        try {
        emailService.sendUserConfirmation(
                request.getEmail(),
                request.getFullName()
        );

        emailService.notifyAdmin(
                request.getFullName(),
                request.getEmail(),
                request.getPhone(),
                request.getSubject(),
                request.getMessage()
        );

        } catch (Exception e) {
        log.error("Email sending failed for {}", request.getEmail(), e);
        }

        log.info("Lead Created | email={} | phone={} | subject={}",
        contact.getEmail(),
        contact.getPhone(),
        contact.getSubject());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Thank you for contacting BlackInt. Our team will reach out soon.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Admin: Get All Active Leads
     */
    public ApiResponse<Page<ContactResponse>> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Contact> contactPage = repository.findByIsDeletedFalse(pageable);

        Page<ContactResponse> responsePage =
                contactPage.map(ContactMapper::toResponse);

        return ApiResponse.<Page<ContactResponse>>builder()
                .success(true)
                .message("Leads fetched successfully")
                .data(responsePage)
                .timestamp(LocalDateTime.now())
                .build();
        }

    /**
     * Admin: Update Lead Status
     */
    public ApiResponse<Void> updateStatus(UUID id, LeadStatus status) {

        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

        contact.setStatus(status);
        contact.setUpdatedAt(LocalDateTime.now());
        repository.save(contact);

        log.info("Lead Status Updated | id={} | newStatus={}", id, status);


        return ApiResponse.<Void>builder()
                .success(true)
                .message("Lead status updated successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Admin: Soft Delete Lead
     */
    public ApiResponse<Void> delete(UUID id) {

        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

        contact.setIsDeleted(true);
        contact.setUpdatedAt(LocalDateTime.now());
        repository.save(contact);

        log.warn("Lead Soft Deleted | id={}", id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Lead deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse<LeadAnalyticsResponse> getAnalytics() {

    LeadAnalyticsResponse analytics = LeadAnalyticsResponse.builder()
            .totalLeads(repository.countByIsDeletedFalse())
            .newLeads(repository.countByStatus(LeadStatus.NEW))
            .contactedLeads(repository.countByStatus(LeadStatus.CONTACTED))
            .convertedLeads(repository.countByStatus(LeadStatus.CONVERTED))
            .build();

    return ApiResponse.<LeadAnalyticsResponse>builder()
            .success(true)
            .message("Analytics fetched successfully")
            .data(analytics)
            .timestamp(LocalDateTime.now())
            .build();
}
}
