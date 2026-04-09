package com.blackint.repository;

import com.blackint.entity.Offering;
import com.blackint.entity.OfferingStatus;
import com.blackint.entity.OfferingType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferingRepository extends JpaRepository<Offering, UUID> {

    Optional<Offering> findBySlug(String slug);

    Optional<Offering> findByPublicId(String publicId);

    Optional<Offering> findBySlugAndStatusAndIsDeletedFalse(
            String slug,
            OfferingStatus status
    );

    List<Offering> findByStatusAndIsDeletedFalse(OfferingStatus status);

    List<Offering> findByIsDeletedFalse();

    @Query("""
        SELECT o FROM Offering o
        WHERE o.status = 'PUBLISHED'
        AND o.isDeleted = false
        ORDER BY o.createdAt DESC
    """)
    List<Offering> findTopPublishedOfferings(Pageable pageable);

    List<Offering> findByStatusAndIsDeletedFalseAndOfferingType(
            OfferingStatus status,
            OfferingType offeringType
    );
}

