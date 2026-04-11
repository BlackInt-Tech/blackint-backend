package com.blackint.mapper;

import com.blackint.dto.request.OfferingRequest;
import com.blackint.dto.response.OfferingResponse;
import com.blackint.entity.Offering;
import com.blackint.entity.OfferingStatus;
import com.blackint.entity.OfferingType;

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

                .offeringType(
                        OfferingType.valueOf(
                                request.getOfferingType().toUpperCase()
                        )
                )

                .isFeatured(request.getIsFeatured())
                .seoTitle(request.getSeoTitle())
                .seoDescription(request.getSeoDescription())
                .status(OfferingStatus.DRAFT)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())

                .build();
    }

    public static OfferingResponse toResponse(Offering response) {

        return OfferingResponse.builder()
                .publicId(response.getPublicId())
                .title(response.getTitle())
                .slug(response.getSlug())
                .shortDescription(response.getShortDescription())
                .fullContent(response.getFullContent())
                .icon(response.getIcon())
                .featuredImage(response.getFeaturedImage())
                .price(response.getPrice())
                .offeringType(response.getOfferingType().name())
                .isFeatured(response.getIsFeatured())
                .status(response.getStatus())
                .seoTitle(response.getSeoTitle())
                .seoDescription(response.getSeoDescription())
                .createdAt(response.getCreatedAt())
                .publishedAt(response.getPublishedAt())
                .build();
    }
}