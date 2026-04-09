package com.blackint.mapper;

import com.blackint.dto.request.ContactRequest;
import com.blackint.dto.response.ContactResponse;
import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import com.blackint.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
                .serviceType(request.getServiceType())
                .serviceName(request.getServiceName())
                .servicePrice(request.getServicePrice())
                .budget(request.getBudget())
                .projectIdea(request.getProjectIdea())
                .message(request.getMessage())
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
                .serviceType(contact.getServiceType())
                .serviceName(contact.getServiceName())
                .servicePrice(contact.getServicePrice())
                .budget(contact.getBudget())
                .projectIdea(contact.getProjectIdea())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .source(contact.getSource())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}