package com.blackint.mapper;

import com.blackint.dto.request.OfferingRequest;
import com.blackint.dto.response.OfferingResponse;
import com.blackint.entity.Offering;
import com.blackint.entity.OfferingStatus;
import com.blackint.entity.OfferingType;

import java.time.LocalDateTime;
import java.util.List;

public class OfferingMapper {

    // ================= TO ENTITY =================
    public static Offering toEntity(OfferingRequest request) {

        return Offering.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .shortDescription(request.getShortDescription()) // List<String>
                .fullContent(request.getFullContent())
                .icon(request.getIcon())
                .featuredImage(request.getFeaturedImage())
                .price(request.getPrice())

                .offeringType(
                        request.getOfferingType() != null
                                ? OfferingType.valueOf(request.getOfferingType().toUpperCase())
                                : null
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

    // ================= TO RESPONSE =================
    public static OfferingResponse toResponse(Offering offering) {

        return OfferingResponse.builder()
                .publicId(offering.getPublicId())
                .title(offering.getTitle())
                .slug(offering.getSlug())

                .shortDescription(
                        offering.getShortDescription() != null
                                ? offering.getShortDescription()
                                : List.of()
                )

                .fullContent(offering.getFullContent())
                .icon(offering.getIcon())
                .featuredImage(offering.getFeaturedImage())
                .price(offering.getPrice())

                .offeringType(
                        offering.getOfferingType() != null
                                ? offering.getOfferingType().name()
                                : null
                )

                .isFeatured(offering.getIsFeatured())
                .status(offering.getStatus())
                .seoTitle(offering.getSeoTitle())
                .seoDescription(offering.getSeoDescription())
                .createdAt(offering.getCreatedAt())
                .publishedAt(offering.getPublishedAt())
                .build();
    }
}