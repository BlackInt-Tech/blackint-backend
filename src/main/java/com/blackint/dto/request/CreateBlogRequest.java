package com.blackint.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CreateBlogRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String excerpt;

    @NotBlank
    private String content;

    private String featuredImage;
    private String metaTitle;
    private String metaDescription;

    private String categorySlug;
    private Set<String> tagSlugs;

    private Boolean isFeatured;
}