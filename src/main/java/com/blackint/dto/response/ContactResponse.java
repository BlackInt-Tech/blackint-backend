package com.blackint.dto.response;

import com.blackint.entity.LeadStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ContactResponse {

    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private LeadStatus status;
    private LocalDateTime createdAt;
}
