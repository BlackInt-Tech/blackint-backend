package com.blackint.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingRequest {

    private String title;
    private String slug;
    private String shortDescription;
    private String fullContent;
    private String icon;
    private String featuredImage;
    private String price;
    private Boolean isFeatured;
    private String seoTitle;
    private String seoDescription;
}
