package com.blackint.dto.response;

import com.blackint.entity.ProjectStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {

    private UUID id;
    private String title;
    private String slug;
    private String shortDescription;
    private String fullContent;
    private String featuredImage;
    private String clientName;
    private String projectUrl;
    private ProjectStatus status;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}
