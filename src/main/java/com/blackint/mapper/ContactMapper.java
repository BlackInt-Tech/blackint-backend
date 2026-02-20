package com.blackint.mapper;

import com.blackint.dto.request.ContactRequest;
import com.blackint.dto.response.ContactResponse;
import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import com.blackint.utils.IdGenerator;

import java.time.LocalDateTime;

public class ContactMapper {

    // ================= ENTITY =================

    public static Contact toEntity(
            ContactRequest request,
            String ip,
            String source
    ) {
        return Contact.builder()
                .publicId(IdGenerator.generate("LEAD"))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(LeadStatus.NEW)
                .source(source != null ? source : "WEBSITE")
                .ipAddress(ip)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    // ================= RESPONSE =================

    public static ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .publicId(contact.getPublicId())
                .fullName(contact.getFullName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .source(contact.getSource())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}
