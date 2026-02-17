package com.blackint.mapper;

import com.blackint.dto.request.ContactRequest;
import com.blackint.dto.response.ContactResponse;
import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;

import java.time.LocalDateTime;

public class ContactMapper {

    public static Contact toEntity(ContactRequest request) {
        return Contact.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(LeadStatus.NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    public static ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .createdAt(contact.getCreatedAt())
                .build();
    }
}
