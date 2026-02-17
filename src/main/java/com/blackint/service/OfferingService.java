package com.blackint.service;

import org.springframework.stereotype.Service;
import com.blackint.dto.request.OfferingRequest;
import com.blackint.dto.response.OfferingResponse;
import com.blackint.entity.OfferingStatus;
import com.blackint.exception.ResourceNotFoundException;
import com.blackint.exception.SlugAlreadyExistsException;
import com.blackint.mapper.OfferingMapper;
import com.blackint.repository.OfferingRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.blackint.entity.Offering;

@Service
@RequiredArgsConstructor
public class OfferingService {

    private final OfferingRepository repository;

    public OfferingResponse create(OfferingRequest request) {

        repository.findBySlug(request.getSlug())
                .ifPresent(s -> {
                    throw new SlugAlreadyExistsException("Slug already exists");
                });

        Offering offering = OfferingMapper.toEntity(request);

        return OfferingMapper.toResponse(repository.save(offering));
    }

    public OfferingResponse update(UUID id, OfferingRequest request) {

        Offering offering = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        offering.setTitle(request.getTitle());
        offering.setSlug(request.getSlug());
        offering.setShortDescription(request.getShortDescription());
        offering.setFullContent(request.getFullContent());
        offering.setIcon(request.getIcon());
        offering.setFeaturedImage(request.getFeaturedImage());
        offering.setPrice(request.getPrice());
        offering.setIsFeatured(request.getIsFeatured());
        offering.setUpdatedAt(LocalDateTime.now());

        return OfferingMapper.toResponse(repository.save(offering));
    }

    public void softDelete(UUID id) {
        Offering offering = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        offering.setIsDeleted(true);
        repository.save(offering);
    }

    public void restore(UUID id) {
        Offering offering = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        offering.setIsDeleted(false);
        repository.save(offering);
    }

    public OfferingResponse publish(UUID id) {
        Offering offering = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        offering.setStatus(OfferingStatus.PUBLISHED);
        offering.setPublishedAt(LocalDateTime.now());

        return OfferingMapper.toResponse(repository.save(offering));
    }

    public List<OfferingResponse> getPublished() {
        return repository.findByStatusAndIsDeletedFalse(OfferingStatus.PUBLISHED)
                .stream()
                .map(OfferingMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OfferingResponse> getAllForAdmin() {
        return repository.findAll()
                .stream()
                .map(OfferingMapper::toResponse)
                .collect(Collectors.toList());
    }

    public OfferingResponse getBySlug(String slug) {

        Offering offering = repository
                .findBySlugAndStatusAndIsDeletedFalse(slug, OfferingStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        return OfferingMapper.toResponse(offering);
    }
}
