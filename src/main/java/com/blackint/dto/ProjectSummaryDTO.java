package com.blackint.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class ProjectSummaryDTO {

    private String publicId;
    private String title;
    private String slug;
    private String shortDescription;
    private String featuredImage;
}
