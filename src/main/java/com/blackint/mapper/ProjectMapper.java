package com.blackint.mapper;

import com.blackint.dto.request.ProjectRequest;
import com.blackint.dto.response.ProjectResponse;
import com.blackint.entity.Project;
import com.blackint.entity.ProjectStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Project toEntity(ProjectRequest request) {
        try{
            return Project.builder()
                    .title(request.getTitle())
                    .slug(request.getSlug())
                    .shortDescription(request.getShortDescription())
                    .fullContent(request.getFullContent())
                    .featuredImage(request.getFeaturedImage())
                    .clientName(request.getClientName())
                    .projectUrl(request.getProjectUrl())
                    .galleryImages(
                        request.getGalleryImages() != null
                            ? objectMapper.writeValueAsString(request.getGalleryImages())
                            : null
                    )
                    .isFeatured(request.getIsFeatured())
                    .seoTitle(request.getSeoTitle())
                    .seoDescription(request.getSeoDescription())
                    .status(ProjectStatus.DRAFT)
                    .isDeleted(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                } catch (Exception e) {
            throw new RuntimeException("Error mapping gallery images", e);
        }
    }

    public static ProjectResponse toResponse(Project project) {
        try{
            return ProjectResponse.builder()
                    .publicId(project.getPublicId())
                    .title(project.getTitle())
                    .slug(project.getSlug())
                    .shortDescription(project.getShortDescription())
                    .fullContent(project.getFullContent())
                    .featuredImage(project.getFeaturedImage())
                    .galleryImages(
                        project.getGalleryImages() != null
                            ? objectMapper.readValue(
                                project.getGalleryImages(),
                                new TypeReference<List<String>>() {}
                            )
                            : null
                        )
                    .clientName(project.getClientName())
                    .projectUrl(project.getProjectUrl())
                    .status(project.getStatus())
                    .isFeatured(project.getIsFeatured())
                    .createdAt(project.getCreatedAt())
                    .publishedAt(project.getPublishedAt())
                    .build();
            } catch (Exception e) {
                throw new RuntimeException("Error parsing gallery images", e);
            }
        }
}
