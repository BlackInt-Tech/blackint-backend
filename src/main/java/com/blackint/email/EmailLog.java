package com.blackint.email;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId;

    private String recipient;

    private String subject;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer retryCount = 0;

    private LocalDateTime nextRetryAt;

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String company;

    private String serviceType;
    private String serviceName;
    private String servicePrice;

    private String budget;

    @Column(columnDefinition = "TEXT")
    private String projectIdea;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailType emailType;
    
}