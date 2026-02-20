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
}