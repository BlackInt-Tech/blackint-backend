package com.blackint.mapper;

import com.blackint.dto.response.BlogResponse;
import com.blackint.entity.Blog;

import java.util.stream.Collectors;

public class BlogMapper {

    public static BlogResponse toResponse(Blog blog) {

        return BlogResponse.builder()
                .publicId(blog.getPublicId())
                .title(blog.getTitle())
                .slug(blog.getSlug())
                .excerpt(blog.getExcerpt())
                .content(blog.getContent())
                .featuredImage(blog.getFeaturedImage())
                .authorName(blog.getAuthorName())
                .status(blog.getStatus())
                .isFeatured(blog.getIsFeatured())
                .readTime(blog.getReadTime())
                .viewCount(blog.getViewCount())
                .category(blog.getCategory() != null ?
                        blog.getCategory().getName() : null)
                .tags(blog.getTags() != null ?
                        blog.getTags()
                                .stream()
                                .map(t -> t.getName())
                                .collect(Collectors.toSet())
                        : null)
                .publishedAt(blog.getPublishedAt())
                .createdAt(blog.getCreatedAt())
                .build();
    }
}