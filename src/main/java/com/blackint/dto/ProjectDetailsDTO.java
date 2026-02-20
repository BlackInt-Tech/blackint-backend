package com.blackint.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@AllArgsConstructor
public class ProjectDetailsDTO {

    private String publicId;
    private String title;
    private String slug;
    private String shortDescription;
    private String fullContent;
    private String featuredImage;
    private String clientName;
    private String projectUrl;
    private Boolean isFeatured;
    private String seoTitle;
    private String seoDescription;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}

