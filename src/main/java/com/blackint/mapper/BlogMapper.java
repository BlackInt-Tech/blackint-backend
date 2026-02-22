package com.blackint.mapper;

import com.blackint.dto.response.BlogResponse;
import com.blackint.entity.Blog;
import com.blackint.entity.BlogTag;

import java.util.Set;
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
                                .filter(t -> t != null)
                                .map(BlogTag::getName)
                                .collect(Collectors.toSet())
                        : Set.of())
                .publishedAt(blog.getPublishedAt())
                .createdAt(blog.getCreatedAt())
                .build();
    }
}