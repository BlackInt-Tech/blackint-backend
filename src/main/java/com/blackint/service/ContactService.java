package com.blackint.service;

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

    public void submit(ContactRequest request, String ip) {

        Contact contact = ContactMapper.toEntity(request, ip, "WEBSITE");

        repository.save(contact);

        emailService.sendLeadSubmissionEmail(contact);
        emailService.sendAdminNotification(contact);
    }

    public Page<ContactResponse> getAll(
            int page,
            int size,
            LeadStatus status,
            String search
    ) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());

        Page<Contact> result;

        if (status != null) {
            result = repository.findByIsDeletedFalseAndStatus(status, pageable);
        } else if (search != null && !search.isBlank()) {
            result = repository
                    .findByIsDeletedFalseAndFullNameContainingIgnoreCaseOrIsDeletedFalseAndEmailContainingIgnoreCase(
                            search, search, pageable);
        } else {
            result = repository.findByIsDeletedFalse(pageable);
        }

        return result.map(ContactMapper::toResponse);
    }

    public void updateStatus(String publicId, LeadStatus status) {

        Contact contact = repository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

        contact.setStatus(status);
        contact.setUpdatedAt(LocalDateTime.now());

        repository.save(contact);
        emailService.sendLeadStatusUpdateEmail(contact);
    }

    public void delete(String publicId) {

        Contact contact = repository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

        contact.setIsDeleted(true);
        contact.setUpdatedAt(LocalDateTime.now());

        repository.save(contact);
    }

    public LeadAnalyticsResponse getAnalytics() {

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        return LeadAnalyticsResponse.builder()
                .totalLeads(repository.countByIsDeletedFalse())
                .newLeads(repository.countByIsDeletedFalseAndStatus(LeadStatus.NEW))
                .contactedLeads(repository.countByIsDeletedFalseAndStatus(LeadStatus.CONTACTED))
                .convertedLeads(repository.countByIsDeletedFalseAndStatus(LeadStatus.CONVERTED))
                .todayLeads(repository.countByIsDeletedFalseAndCreatedAtAfter(todayStart))
                .monthLeads(repository.countByIsDeletedFalseAndCreatedAtAfter(monthStart))
                .build();
    }
}