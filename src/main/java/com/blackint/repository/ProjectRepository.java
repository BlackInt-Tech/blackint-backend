package com.blackint.repository;

import com.blackint.entity.Project;
import com.blackint.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    // Slug validation
    Optional<Project> findBySlug(String slug);

    // Public single project
    Optional<Project> findBySlugAndStatusAndIsDeletedFalse(
            String slug,
            ProjectStatus status
    );

    // Public published list
    List<Project> findByStatusAndIsDeletedFalse(ProjectStatus status);

    // Admin - non deleted
    List<Project> findByIsDeletedFalse();

}
