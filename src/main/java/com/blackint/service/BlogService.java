package com.blackint.service;

import com.blackint.dto.request.CreateBlogRequest;
import com.blackint.dto.response.BlogResponse;
import com.blackint.entity.*;
import com.blackint.mapper.BlogMapper;
import com.blackint.repository.*;
import com.blackint.utils.ReadTimeUtil;
import com.blackint.utils.SlugUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final BlogCategoryRepository categoryRepository;
    private final BlogTagRepository tagRepository;

    // =================================================
    // CREATE
    // =================================================

    public BlogResponse create(CreateBlogRequest request) {

        String slug = generateUniqueSlug(request.getTitle());

        Blog blog = buildEntity(new Blog(), request, slug);

        blog.setPublicId(UUID.randomUUID().toString());
        blog.setStatus(BlogStatus.DRAFT);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        blog.setViewCount(0L);
        blog.setIsDeleted(false);

        blogRepository.save(blog);

        return BlogMapper.toResponse(blog);
    }

    // =================================================
    // UPDATE
    // =================================================

    public BlogResponse update(UUID id, CreateBlogRequest request) {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        if (!blog.getTitle().equals(request.getTitle())) {
            blog.setSlug(generateUniqueSlug(request.getTitle()));
        }

        buildEntity(blog, request, blog.getSlug());
        blog.setUpdatedAt(LocalDateTime.now());

        blogRepository.save(blog);

        return BlogMapper.toResponse(blog);
    }

    // =================================================
    // PUBLISH / SCHEDULE
    // =================================================

    public BlogResponse changeStatus(UUID id,
                                     BlogStatus status,
                                     LocalDateTime scheduledAt) {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        if (status == BlogStatus.PUBLISHED) {
            blog.setStatus(BlogStatus.PUBLISHED);
            blog.setPublishedAt(LocalDateTime.now());
        }

        if (status == BlogStatus.SCHEDULED) {
            blog.setStatus(BlogStatus.SCHEDULED);
            blog.setScheduledAt(scheduledAt);
        }

        blog.setUpdatedAt(LocalDateTime.now());

        blogRepository.save(blog);

        return BlogMapper.toResponse(blog);
    }

    // =================================================
    // SOFT DELETE
    // =================================================

    public void softDelete(UUID id) {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setIsDeleted(true);
        blog.setUpdatedAt(LocalDateTime.now());

        blogRepository.save(blog);
    }

    // =================================================
    // PUBLIC READ APIs (DB Optimized)
    // =================================================

    public Page<BlogResponse> getPublished(int page, int size) {

        return blogRepository
                .findByStatusAndIsDeletedFalse(
                        BlogStatus.PUBLISHED,
                        PageRequest.of(page, size,
                                Sort.by("publishedAt").descending())
                )
                .map(BlogMapper::toResponse);
    }

    public Page<BlogResponse> getFeatured(int page, int size) {

        return blogRepository
                .findByStatusAndIsFeaturedTrueAndIsDeletedFalse(
                        BlogStatus.PUBLISHED,
                        PageRequest.of(page, size,
                                Sort.by("publishedAt").descending())
                )
                .map(BlogMapper::toResponse);
    }

    public Page<BlogResponse> getByCategory(String slug,
                                            int page,
                                            int size) {

        return blogRepository
                .findByCategorySlug(
                        slug,
                        PageRequest.of(page, size,
                                Sort.by("publishedAt").descending())
                )
                .map(BlogMapper::toResponse);
    }

    public Page<BlogResponse> getByTag(String slug,
                                       int page,
                                       int size) {

        return blogRepository
                .findByTagSlug(
                        slug,
                        PageRequest.of(page, size,
                                Sort.by("publishedAt").descending())
                )
                .map(BlogMapper::toResponse);
    }

    // =================================================
    // GET BY SLUG (VIEW COUNTER SAFE)
    // =================================================

    @Transactional
    public BlogResponse getBySlug(String slug) {

        Blog blog = blogRepository
                .findBySlugAndIsDeletedFalse(slug)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setViewCount(blog.getViewCount() + 1);

        return BlogMapper.toResponse(blog);
    }

    // =================================================
    // INTERNAL BUILDER
    // =================================================

    private Blog buildEntity(Blog blog,
                             CreateBlogRequest request,
                             String slug) {

        BlogCategory category = request.getCategorySlug() != null ?
                categoryRepository.findBySlug(request.getCategorySlug())
                        .orElse(null)
                : null;

        Set<BlogTag> tags = request.getTagSlugs() != null ?
                request.getTagSlugs().stream()
                        .map(tagSlug -> tagRepository
                                .findBySlug(tagSlug)
                                .orElse(null))
                        .collect(Collectors.toSet())
                : null;

        blog.setTitle(request.getTitle());
        blog.setSlug(slug);
        blog.setExcerpt(request.getExcerpt());
        blog.setContent(request.getContent());
        blog.setFeaturedImage(request.getFeaturedImage());
        blog.setMetaTitle(request.getMetaTitle());
        blog.setMetaDescription(request.getMetaDescription());
        blog.setAuthorName("BlackInt");
        blog.setReadTime(ReadTimeUtil.calculate(request.getContent()));
        blog.setIsFeatured(request.getIsFeatured());
        blog.setCategory(category);
        blog.setTags(tags);

        return blog;
    }

    private String generateUniqueSlug(String title) {

        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (blogRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }
}