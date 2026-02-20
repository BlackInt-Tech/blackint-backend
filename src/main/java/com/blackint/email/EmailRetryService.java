package com.blackint.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailRetryService {

    private final EmailLogRepository emailLogRepository;
    private final JavaMailSender mailSender;

    private static final int MAX_RETRY = 3;

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void retryFailedEmails() {

        List<EmailLog> failedEmails =
                emailLogRepository.findByStatusAndNextRetryAtBefore(
                        EmailStatus.FAILED,
                        LocalDateTime.now()
                );

        for (EmailLog logEntry : failedEmails) {

            if (logEntry.getRetryCount() >= MAX_RETRY) {
                log.warn("Max retry reached for email id={}", logEntry.getId());
                continue;
            }

            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(logEntry.getRecipient());
                helper.setSubject(logEntry.getSubject());
                helper.setText("Retrying previously failed email.", true);

                mailSender.send(message);

                logEntry.setStatus(EmailStatus.SUCCESS);
                logEntry.setErrorMessage(null);
                logEntry.setNextRetryAt(null);

                emailLogRepository.save(logEntry);

                log.info("Email retry successful | id={}", logEntry.getId());

            } catch (Exception e) {

                logEntry.setRetryCount(logEntry.getRetryCount() + 1);
                logEntry.setNextRetryAt(LocalDateTime.now().plusMinutes(5));
                logEntry.setErrorMessage(e.getMessage());

                emailLogRepository.save(logEntry);

                log.error("Email retry failed | id={}", logEntry.getId(), e);
            }
        }
    }
}