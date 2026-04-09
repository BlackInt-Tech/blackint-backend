package com.blackint.dto.response;

import com.blackint.entity.LeadStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContactResponse {

    private String publicId;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;
    private String company;

    private String serviceType;
    private String serviceName;
    private String servicePrice;
    private String budget;
    private String projectIdea;

    private String message;

    private LeadStatus status;
    private String source;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}