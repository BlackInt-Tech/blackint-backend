package com.blackint.service;

import com.blackint.dto.request.ProjectRequest;
import com.blackint.dto.response.ProjectResponse;
import com.blackint.entity.Project;
import com.blackint.entity.ProjectStatus;
import com.blackint.exception.ResourceNotFoundException;
import com.blackint.exception.SlugAlreadyExistsException;
import com.blackint.mapper.ProjectMapper;
import com.blackint.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectResponse create(ProjectRequest request) {

        repository.findBySlug(request.getSlug())
                .ifPresent(p -> {
                    throw new SlugAlreadyExistsException("Slug already exists");
                });

        Project project = ProjectMapper.toEntity(request);

        return ProjectMapper.toResponse(repository.save(project));
    }

    public ProjectResponse update(UUID id, ProjectRequest request) {

        Project project = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setTitle(request.getTitle());
        project.setSlug(request.getSlug());
        project.setShortDescription(request.getShortDescription());
        project.setFullContent(request.getFullContent());
        project.setUpdatedAt(LocalDateTime.now());

        return ProjectMapper.toResponse(repository.save(project));
    }

    public void softDelete(UUID id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setIsDeleted(true);
        repository.save(project);
    }

    public void restore(UUID id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setIsDeleted(false);
        repository.save(project);
    }

    public ProjectResponse publish(UUID id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setStatus(ProjectStatus.PUBLISHED);
        project.setPublishedAt(LocalDateTime.now());

        return ProjectMapper.toResponse(repository.save(project));
    }

    public List<ProjectResponse> getPublished() {
        return repository.findByStatusAndIsDeletedFalse(ProjectStatus.PUBLISHED)
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> getAllForAdmin() {
        return repository.findAll()
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse getBySlug(String slug) {
        Project project = repository
                .findBySlugAndStatusAndIsDeletedFalse(slug, ProjectStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        return ProjectMapper.toResponse(project);
    }
}
