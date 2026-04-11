package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
    name = "offerings",
    indexes = {
        @Index(name = "idx_offering_slug", columnList = "slug"),
        @Index(name = "idx_offering_status", columnList = "status"),
        @Index(name = "idx_offering_featured", columnList = "is_featured")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offering {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String publicId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @ElementCollection
    @CollectionTable(
        name = "offering_short_descriptions",
        joinColumns = @JoinColumn(
            name = "offering_id",
            referencedColumnName = "public_id"
        )
    )
    @Column(name = "description")
    private List<String> shortDescription;

    @Column(columnDefinition = "TEXT")
    private String fullContent;

    private String icon;
    private String featuredImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferingType offeringType;
    
    private String price;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private OfferingStatus status = OfferingStatus.DRAFT;

    private String seoTitle;
    private String seoDescription;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.status = OfferingStatus.DRAFT;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

