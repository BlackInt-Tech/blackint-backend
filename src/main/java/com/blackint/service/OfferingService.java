package com.blackint.service;

import com.blackint.dto.request.OfferingRequest;
import com.blackint.dto.response.OfferingResponse;
import com.blackint.entity.Offering;
import com.blackint.entity.OfferingStatus;
import com.blackint.exception.ResourceNotFoundException;
import com.blackint.exception.SlugAlreadyExistsException;
import com.blackint.mapper.OfferingMapper;
import com.blackint.repository.OfferingRepository;
import com.blackint.utils.IdGenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferingService {

    private final OfferingRepository offeringRepository;

    // ================= CREATE =================

    public OfferingResponse create(OfferingRequest request) {

        offeringRepository.findBySlug(request.getSlug())
                .ifPresent(o -> {
                    throw new SlugAlreadyExistsException("Slug already exists");
                });

        Offering offering = OfferingMapper.toEntity(request);

        offering.setPublicId(IdGenerator.generate("OFFER"));
        offering.setStatus(OfferingStatus.DRAFT);
        offering.setIsDeleted(false);
        offering.setCreatedAt(LocalDateTime.now());
        offering.setUpdatedAt(LocalDateTime.now());

        return OfferingMapper.toResponse(offeringRepository.save(offering));
    }

    // ================= UPDATE =================

    public OfferingResponse update(String publicId, OfferingRequest request) {

        Offering offering = offeringRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        // Slug validation (if changed)
        if (!offering.getSlug().equals(request.getSlug())) {
            offeringRepository.findBySlug(request.getSlug())
                    .ifPresent(o -> {
                        throw new SlugAlreadyExistsException("Slug already exists");
                    });
        }

        offering.setTitle(request.getTitle());
        offering.setSlug(request.getSlug());
        offering.setShortDescription(request.getShortDescription());
        offering.setFullContent(request.getFullContent());
        offering.setIcon(request.getIcon());
        offering.setFeaturedImage(request.getFeaturedImage());
        offering.setPrice(request.getPrice());
        offering.setIsFeatured(request.getIsFeatured());
        offering.setSeoTitle(request.getSeoTitle());
        offering.setSeoDescription(request.getSeoDescription());
        offering.setUpdatedAt(LocalDateTime.now());

        return OfferingMapper.toResponse(offeringRepository.save(offering));
    }

    // ================= SOFT DELETE =================

    public void softDelete(String publicId) {

        Offering offering = offeringRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        offering.setIsDeleted(true);
        offering.setUpdatedAt(LocalDateTime.now());

        offeringRepository.save(offering);
    }

    // ================= RESTORE =================

    public void restore(String publicId) {

        Offering offering = offeringRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        offering.setIsDeleted(false);
        offering.setUpdatedAt(LocalDateTime.now());

        offeringRepository.save(offering);
    }

    // ================= PUBLISH =================

    public OfferingResponse publish(String publicId) {

        Offering offering = offeringRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        if (Boolean.TRUE.equals(offering.getIsDeleted())) {
            throw new IllegalStateException("Cannot publish a deleted offering");
        }

        offering.setStatus(OfferingStatus.PUBLISHED);
        offering.setPublishedAt(LocalDateTime.now());
        offering.setUpdatedAt(LocalDateTime.now());

        return OfferingMapper.toResponse(offeringRepository.save(offering));
    }

    // ================= PUBLIC =================

    public List<OfferingResponse> getPublished() {

        return offeringRepository.findByStatusAndIsDeletedFalse(OfferingStatus.PUBLISHED)
                .stream()
                .map(OfferingMapper::toResponse)
                .collect(Collectors.toList());
    }

    public OfferingResponse getBySlug(String slug) {

        Offering offering = offeringRepository
                .findBySlugAndStatusAndIsDeletedFalse(slug, OfferingStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Offering not found"));

        return OfferingMapper.toResponse(offering);
    }

    // ================= ADMIN =================

    public List<OfferingResponse> getAllForAdmin() {

        return offeringRepository.findAll()
                .stream()
                .map(OfferingMapper::toResponse)
                .collect(Collectors.toList());
    }
}
