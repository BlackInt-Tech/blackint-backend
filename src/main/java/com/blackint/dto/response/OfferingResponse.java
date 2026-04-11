package com.blackint.dto.response;

import com.blackint.entity.OfferingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingResponse {

    private String publicId;
    private String title;
    private String slug;
    private List<String> shortDescription;
    private String fullContent;
    private String icon;
    private String featuredImage;
    private String price;
    private String offeringType;
    private Boolean isFeatured;
    private OfferingStatus status;
    private String seoTitle;
    private String seoDescription;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}
