package com.blackint.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {

    private String title;
    private String slug;
    private String shortDescription;
    private String fullContent;
    private String featuredImage;
    private String clientName;
    private String projectUrl;
    private Boolean isFeatured;
    private String seoTitle;
    private String seoDescription;
}
