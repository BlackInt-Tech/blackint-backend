package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offering {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String fullContent;

    private String icon;
    private String featuredImage;
    private String price;

    private Boolean isFeatured;
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private OfferingStatus status;

    private String seoTitle;
    private String seoDescription;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
}
