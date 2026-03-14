package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(unique = true)
    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String company;

    @Column(columnDefinition = "TEXT")
    private String services;

    private String budget;

    @Column(columnDefinition = "TEXT")
    @Size(min = 20, message = "Project idea must contain at least 20 characters")
    private String projectIdea;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String source;
    private String subject;
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private LeadStatus status;

    private Boolean isDeleted = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}