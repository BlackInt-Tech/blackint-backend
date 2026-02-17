package com.blackint.dto.response;

import com.blackint.entity.OfferingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingResponse {

    private UUID id;
    private String title;
    private String slug;
    private String shortDescription;
    private String fullContent;
    private String icon;
    private String featuredImage;
    private String price;
    private Boolean isFeatured;
    private OfferingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}
