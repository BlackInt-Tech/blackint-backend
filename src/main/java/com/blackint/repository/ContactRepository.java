package com.blackint.repository;

import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

    Page<Contact> findByIsDeletedFalse(Pageable pageable);

    long countByIsDeletedFalse();

    long countByStatus(LeadStatus status);

}
