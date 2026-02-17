package com.blackint.repository;

import com.blackint.entity.Offering;
import com.blackint.entity.OfferingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferingRepository extends JpaRepository<Offering, UUID> {

    Optional<Offering> findBySlug(String slug);

    Optional<Offering> findBySlugAndStatusAndIsDeletedFalse(
            String slug,
            OfferingStatus status
    );

    List<Offering> findByStatusAndIsDeletedFalse(OfferingStatus status);

    List<Offering> findByIsDeletedFalse();
}
