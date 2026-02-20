package com.blackint.service;

import com.blackint.dto.request.ProjectRequest;
import com.blackint.dto.response.ProjectResponse;
import com.blackint.entity.Project;
import com.blackint.entity.ProjectStatus;
import com.blackint.exception.ResourceNotFoundException;
import com.blackint.exception.SlugAlreadyExistsException;
import com.blackint.mapper.ProjectMapper;
import com.blackint.repository.ProjectRepository;
import com.blackint.utils.IdGenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final IdGenerator idGenerator;

    // ================= CREATE =================
    @Transactional
    public ProjectResponse create(ProjectRequest request) {

        if (projectRepository.existsBySlug(request.getSlug())) {
            throw new SlugAlreadyExistsException("Slug already exists");
        }

        Project project = ProjectMapper.toEntity(request);

        project.setPublicId(idGenerator.generate("PROJ"));
        project.setStatus(ProjectStatus.DRAFT);
        project.setIsDeleted(false);
        project.setCreatedAt(LocalDateTime.now());

        return ProjectMapper.toResponse(projectRepository.save(project));
    }

    // ================= UPDATE =================
    @Transactional
    public ProjectResponse update(String publicId, ProjectRequest request) {

        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Slug validation only if changed
        if (!project.getSlug().equals(request.getSlug())
                && projectRepository.existsBySlug(request.getSlug())) {
            throw new SlugAlreadyExistsException("Slug already exists");
        }

        project.setTitle(request.getTitle());
        project.setSlug(request.getSlug());
        project.setShortDescription(request.getShortDescription());
        project.setFullContent(request.getFullContent());
        project.setFeaturedImage(request.getFeaturedImage());
        project.setClientName(request.getClientName());
        project.setProjectUrl(request.getProjectUrl());
        project.setIsFeatured(request.getIsFeatured());
        project.setSeoTitle(request.getSeoTitle());
        project.setSeoDescription(request.getSeoDescription());
        project.setUpdatedAt(LocalDateTime.now());

        return ProjectMapper.toResponse(projectRepository.save(project));
    }

    // ================= SOFT DELETE =================
    @Transactional
    public void softDelete(String publicId) {

        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setIsDeleted(true);
        project.setUpdatedAt(LocalDateTime.now());
    }

    // ================= RESTORE =================
    @Transactional
    public void restore(String publicId) {

        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setIsDeleted(false);
        project.setUpdatedAt(LocalDateTime.now());
    }

    // ================= PUBLISH =================
    @Transactional
    public ProjectResponse publish(String publicId) {

        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setStatus(ProjectStatus.PUBLISHED);
        project.setPublishedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        return ProjectMapper.toResponse(project);
    }

    // ================= UNPUBLISH =================
    @Transactional
    public ProjectResponse unpublish(String publicId) {

        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setStatus(ProjectStatus.DRAFT);
        project.setPublishedAt(null);
        project.setUpdatedAt(LocalDateTime.now());

        return ProjectMapper.toResponse(project);
    }

    // ================= PUBLIC =================
    public List<ProjectResponse> getPublished() {

        return projectRepository
                .findByStatusAndIsDeletedFalse(ProjectStatus.PUBLISHED)
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse getBySlug(String slug) {

        Project project = projectRepository
                .findBySlugAndStatusAndIsDeletedFalse(slug, ProjectStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        return ProjectMapper.toResponse(project);
    }

    // ================= ADMIN =================
    public List<ProjectResponse> getAllForAdmin() {

        return projectRepository.findAll()
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }
}
