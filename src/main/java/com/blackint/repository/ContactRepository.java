package com.blackint.repository;

import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

    // ================= BASIC =================

    Optional<Contact> findByPublicId(String publicId);

    Page<Contact> findByIsDeletedFalse(Pageable pageable);

    Page<Contact> findByIsDeletedFalseAndStatus(
            LeadStatus status,
            Pageable pageable
    );

    Page<Contact> findByIsDeletedFalseAndFullNameContainingIgnoreCaseOrIsDeletedFalseAndEmailContainingIgnoreCase(
            String nameKeyword,
            String emailKeyword,
            Pageable pageable
    );

    // ================= COUNTING =================

    long countByIsDeletedFalse();

    long countByIsDeletedFalseAndStatus(LeadStatus status);

    long countByIsDeletedFalseAndCreatedAtAfter(LocalDateTime dateTime);

    long countByIsDeletedFalseAndStatusAndCreatedAtAfter(
            LeadStatus status,
            LocalDateTime dateTime
    );

}
