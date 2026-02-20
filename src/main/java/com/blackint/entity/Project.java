package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "projects",
    indexes = {
        @Index(name = "idx_project_slug", columnList = "slug"),
        @Index(name = "idx_project_status", columnList = "status"),
        @Index(name = "idx_project_featured", columnList = "is_featured")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id; // Internal DB ID

    @Column(name = "public_id", unique = true, nullable = false, length = 32)
    private String publicId; // Exposed to frontend

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, unique = true, length = 160)
    private String slug;

    @Column(name = "short_description", length = 300)
    private String shortDescription;

    @Column(name = "full_content", columnDefinition = "TEXT")
    private String fullContent;

    @Column(name = "featured_image")
    private String featuredImage;

    private String clientName;

    private String projectUrl;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted=false;

    private String seoTitle;

    private String seoDescription;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}


