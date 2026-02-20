package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String publicId;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(length = 500)
    private String excerpt;

    @Lob
    @Column(nullable = false)
    private String content;

    private String featuredImage;

    private String metaTitle;
    private String metaDescription;

    private String authorName;

    private Integer readTime;

    private Long viewCount = 0L;

    @Enumerated(EnumType.STRING)
    private BlogStatus status;

    private Boolean isFeatured = false;

    private LocalDateTime scheduledAt;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean isDeleted = false;

    @ManyToOne
    private BlogCategory category;

    @ManyToMany
    @JoinTable(
            name = "blog_tags_mapping",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<BlogTag> tags;
}

