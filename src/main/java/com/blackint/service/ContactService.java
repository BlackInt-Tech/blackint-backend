package com.blackint.service;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.ContactRequest;
import com.blackint.dto.response.ContactResponse;
import com.blackint.dto.response.LeadAnalyticsResponse;
import com.blackint.email.EmailService;
import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import com.blackint.exception.ResourceNotFoundException;
import com.blackint.mapper.ContactMapper;
import com.blackint.repository.ContactRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository repository;
    private final EmailService emailService;

    // ================= PUBLIC SUBMIT =================

    public ApiResponse<Void> submit(ContactRequest request, String ip) {

        
        Contact contact = ContactMapper.toEntity(
                request,
                ip,
                "WEBSITE"
        );

        repository.save(contact);

        emailService.sendLeadSubmissionEmail(contact);
        emailService.sendAdminNotification(contact);

        return ApiResponse.successMessage(
                "Thank you! Our team will contact you shortly."
        );
    }

    // ================= ADMIN FETCH =================

    public ApiResponse<Page<ContactResponse>> getAll(
            int page,
            int size,
            LeadStatus status,
            String search
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Contact> result;

        if (status != null) {
            result = repository.findByIsDeletedFalseAndStatus(status, pageable);

        } else if (search != null && !search.isBlank()) {
            result = repository
                    .findByIsDeletedFalseAndFullNameContainingIgnoreCaseOrIsDeletedFalseAndEmailContainingIgnoreCase(
                            search,
                            search,
                            pageable
                    );
        } else {
            result = repository.findByIsDeletedFalse(pageable);
        }

        return ApiResponse.success(
                result.map(ContactMapper::toResponse)
        );
    }

    // ================= UPDATE STATUS =================

    public ApiResponse<Void> updateStatus(
            String publicId,
            LeadStatus status
    ) {

        Contact contact = repository.findByPublicId(publicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lead not found"));

        contact.setStatus(status);
        contact.setUpdatedAt(LocalDateTime.now());

        repository.save(contact);

        log.info("Lead Status Updated | publicId={} | status={}",
                publicId,
                status
        );
        emailService.sendLeadStatusUpdateEmail(contact);

        return ApiResponse.successMessage("Lead status updated successfully");
    }

    // ================= SOFT DELETE =================

    public ApiResponse<Void> delete(String publicId) {

        Contact contact = repository.findByPublicId(publicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lead not found"));

        contact.setIsDeleted(true);
        contact.setUpdatedAt(LocalDateTime.now());

        repository.save(contact);

        log.warn("Lead Soft Deleted | publicId={}", publicId);

        return ApiResponse.successMessage("Lead deleted successfully");
    }

    // ================= ANALYTICS =================

    public ApiResponse<LeadAnalyticsResponse> getAnalytics() {

        LocalDateTime todayStart =
                LocalDate.now().atStartOfDay();

        LocalDateTime monthStart =
                LocalDate.now().withDayOfMonth(1).atStartOfDay();

        long total = repository.countByIsDeletedFalse();
        long newLeads = repository.countByIsDeletedFalseAndStatus(LeadStatus.NEW);
        long contacted = repository.countByIsDeletedFalseAndStatus(LeadStatus.CONTACTED);
        long converted = repository.countByIsDeletedFalseAndStatus(LeadStatus.CONVERTED);

        long todayLeads =
                repository.countByIsDeletedFalseAndCreatedAtAfter(todayStart);

        long monthLeads =
                repository.countByIsDeletedFalseAndCreatedAtAfter(monthStart);

        LeadAnalyticsResponse analytics = LeadAnalyticsResponse.builder()
                .totalLeads(total)
                .newLeads(newLeads)
                .contactedLeads(contacted)
                .convertedLeads(converted)
                .todayLeads(todayLeads)
                .monthLeads(monthLeads)
                .build();

        return ApiResponse.success(analytics);
    }
}
