package com.blackint.email;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findByStatusAndNextRetryAtBefore(
            EmailStatus status,
            LocalDateTime time
    );
    
}