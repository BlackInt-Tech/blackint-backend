package com.blackint.mapper;

import com.blackint.dto.request.ProjectRequest;
import com.blackint.dto.response.ProjectResponse;
import com.blackint.entity.Project;
import com.blackint.entity.ProjectStatus;

import java.time.LocalDateTime;

public class ProjectMapper {

    public static Project toEntity(ProjectRequest request) {

        return Project.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .shortDescription(request.getShortDescription())
                .fullContent(request.getFullContent())
                .featuredImage(request.getFeaturedImage())
                .clientName(request.getClientName())
                .projectUrl(request.getProjectUrl())
                .isFeatured(request.getIsFeatured())
                .seoTitle(request.getSeoTitle())
                .seoDescription(request.getSeoDescription())
                .status(ProjectStatus.DRAFT)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ProjectResponse toResponse(Project project) {

        return ProjectResponse.builder()
                .publicId(project.getPublicId())
                .title(project.getTitle())
                .slug(project.getSlug())
                .shortDescription(project.getShortDescription())
                .fullContent(project.getFullContent())
                .featuredImage(project.getFeaturedImage())
                .clientName(project.getClientName())
                .projectUrl(project.getProjectUrl())
                .status(project.getStatus())
                .isFeatured(project.getIsFeatured())
                .createdAt(project.getCreatedAt())
                .publishedAt(project.getPublishedAt())
                .build();
    }
}
