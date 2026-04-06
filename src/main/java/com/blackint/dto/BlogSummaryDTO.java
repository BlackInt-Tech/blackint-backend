package com.blackint.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class BlogSummaryDTO {

    private String publicId;
    private String title;
    private String slug;
    private String excerpt;
    private String featuredImage;

    public BlogSummaryDTO(
            String publicId,
            String title,
            String slug,
            String excerpt,
            String featuredImage
    ) {
        this.publicId = publicId;
        this.title = title;
        this.slug = slug;
        this.excerpt = excerpt;
        this.featuredImage = featuredImage;
    }

    
}