package com.blackint.repository;

import com.blackint.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BlogRepository extends JpaRepository<Blog, UUID> {

    Optional<Blog> findByPublicIdAndIsDeletedFalse(String publicId);

    Optional<Blog> findBySlugAndIsDeletedFalse(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndPublicIdNot(String slug, String publicId);

    Page<Blog> findByStatusAndIsDeletedFalse(
            BlogStatus status,
            Pageable pageable
    );

    Page<Blog> findByStatusAndIsFeaturedTrueAndIsDeletedFalse(
            BlogStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT b FROM Blog b
        WHERE b.category.slug = :slug
        AND b.status = 'PUBLISHED'
        AND b.isDeleted = false
    """)
    Page<Blog> findByCategorySlug(String slug, Pageable pageable);

    @Query("""
        SELECT b FROM Blog b
        JOIN b.tags t
        WHERE t.slug = :slug
        AND b.status = 'PUBLISHED'
        AND b.isDeleted = false
    """)
    Page<Blog> findByTagSlug(String slug, Pageable pageable);
}