package com.blackint.dto.response;

import com.blackint.entity.BlogStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {

    private String publicId;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private String featuredImage;

    private String authorName;

    private BlogStatus status;
    private Boolean isFeatured;

    private Integer readTime;
    private Long viewCount;

    private String category;
    private Set<String> tags;

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
}