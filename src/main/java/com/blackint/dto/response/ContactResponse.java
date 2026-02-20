package com.blackint.dto.response;

import com.blackint.entity.LeadStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContactResponse {

    private String publicId;

    private String fullName;
    private String email;
    private String phone;

    private String subject;
    private String message;

    private LeadStatus status;
    private String source;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
