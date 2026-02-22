package com.blackint.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequest {

    @NotBlank
    private String title;

    private String excerpt;

    @NotBlank
    private String content;

    private String featuredImage;

    private String metaTitle;
    private String metaDescription;

    private Boolean isFeatured = false;

    @NotBlank
    private String categorySlug;

    private Set<String> tagSlugs;
}