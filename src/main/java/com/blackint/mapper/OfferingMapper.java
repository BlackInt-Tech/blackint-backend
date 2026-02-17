package com.blackint.mapper;

import com.blackint.dto.request.OfferingRequest;
import com.blackint.dto.response.OfferingResponse;
import com.blackint.entity.Offering;
import com.blackint.entity.OfferingStatus;

import java.time.LocalDateTime;

public class OfferingMapper {

    public static Offering toEntity(OfferingRequest request) {

        return Offering.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .shortDescription(request.getShortDescription())
                .fullContent(request.getFullContent())
                .icon(request.getIcon())
                .featuredImage(request.getFeaturedImage())
                .price(request.getPrice())
                .isFeatured(request.getIsFeatured())
                .seoTitle(request.getSeoTitle())
                .seoDescription(request.getSeoDescription())
                .status(OfferingStatus.DRAFT)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static OfferingResponse toResponse(Offering service) {

        return OfferingResponse.builder()
                .id(service.getId())
                .title(service.getTitle())
                .slug(service.getSlug())
                .shortDescription(service.getShortDescription())
                .fullContent(service.getFullContent())
                .icon(service.getIcon())
                .featuredImage(service.getFeaturedImage())
                .price(service.getPrice())
                .isFeatured(service.getIsFeatured())
                .status(service.getStatus())
                .createdAt(service.getCreatedAt())
                .publishedAt(service.getPublishedAt())
                .build();
    }
}
