package com.blackint.mapper;

import com.blackint.dto.request.ContactRequest;
import com.blackint.dto.response.ContactResponse;
import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import com.blackint.utils.IdGenerator;
import java.time.LocalDateTime;

public class ContactMapper {

    public static Contact toEntity(
            ContactRequest request,
            String ip,
            String source
    ) {
        return Contact.builder()
                .publicId(IdGenerator.generate("LEAD"))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .company(request.getCompany())
                .offeringType(request.getOfferingType())
                .offeringName(request.getOfferingName())
                .offeringPrice(request.getOfferingPrice())
                .projectIdea(request.getProjectIdea())
                .status(LeadStatus.NEW)
                .source(source != null ? source : "WEBSITE")
                .ipAddress(ip)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    public static ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .publicId(contact.getPublicId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .company(contact.getCompany())
                .offeringType(contact.getOfferingType())
                .offeringName(contact.getOfferingName())
                .offeringPrice(contact.getOfferingPrice())
                .projectIdea(contact.getProjectIdea())
                .status(contact.getStatus())
                .source(contact.getSource())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}