package com.blackint.repository;

import com.blackint.dto.ProjectSummaryDTO;
import com.blackint.entity.Project;
import com.blackint.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    // BASIC LOOKUPS

    Optional<Project> findByPublicId(String publicId);

    Optional<Project> findBySlug(String slug);

    Optional<Project> findBySlugAndStatusAndIsDeletedFalse(
            String slug,
            ProjectStatus status
    );

    List<Project> findByStatusAndIsDeletedFalse(ProjectStatus status);

    List<Project> findByIsDeletedFalse();

    // OPTIMIZED PUBLIC QUERIES

    @Query("""
        SELECT new com.blackint.dto.ProjectSummaryDTO(
            p.publicId,
            p.title,
            p.slug,
            p.shortDescription,
            p.featuredImage
        )
        FROM Project p
        WHERE p.status = 'PUBLISHED'
        AND p.isFeatured = true
        AND p.isDeleted = false
        ORDER BY p.publishedAt DESC
    """)
    List<ProjectSummaryDTO> findFeaturedPublishedProjects();

    boolean existsBySlug(String slug);
}
