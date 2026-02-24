package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(unique = true)
    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String company;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String services; // store comma-separated OR JSON later

    private String budget;

    @Column(columnDefinition = "TEXT")
    private String projectIdea;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String source;

    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private LeadStatus status;

    private Boolean isDeleted = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}