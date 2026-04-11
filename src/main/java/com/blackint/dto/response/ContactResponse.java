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
    private String offeringType;
    private String offeringName;
    private String offeringPrice;
    private String projectIdea;
    private LeadStatus status;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}