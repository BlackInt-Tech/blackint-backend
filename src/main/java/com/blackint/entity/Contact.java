package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "contacts",
    indexes = {
        @Index(name = "idx_contact_email", columnList = "email"),
        @Index(name = "idx_contact_status", columnList = "status"),
        @Index(name = "idx_contact_created_at", columnList = "createdAt")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "public_id", unique = true, nullable = false, length = 32)
    private String publicId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private LeadStatus status;

    private String source;
    private String ipAddress;

    private Boolean isDeleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
