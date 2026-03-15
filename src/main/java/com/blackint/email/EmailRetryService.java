package com.blackint.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailRetryService {

    private final EmailLogRepository emailLogRepository;
    private final SendGrid sendGrid;

    private final EmailTemplateBuilder emailTemplateBuilder;

    @Value("${blackint.from.email}")
    private String fromEmail;

    @Value("${blackint.admin.email}")
    private String adminEmail;
    
    private static final int MAX_RETRY = 3;

    @Scheduled(fixedRate = 120000)
    public void retryFailedEmails() {

        List<EmailLog> failedEmails =
                emailLogRepository.findByStatusInAndNextRetryAtBefore(
                        List.of(EmailStatus.FAILED),
                        LocalDateTime.now()
                );

        for (EmailLog logEntry : failedEmails) {

            logEntry.setStatus(EmailStatus.PROCESSING);
            emailLogRepository.save(logEntry);

            if (logEntry.getRetryCount() >= MAX_RETRY) {
                log.warn("Max retry reached for email id={}", logEntry.getId());
                continue;
            }

            try {

                if (logEntry.getRecipient() == null || logEntry.getRecipient().isBlank()) {
                    log.error("Invalid recipient for email log id={}", logEntry.getId());
                    logEntry.setStatus(EmailStatus.FAILED);
                    emailLogRepository.save(logEntry);
                    continue;
                }

                if (logEntry.getSubject() == null || logEntry.getSubject().isBlank()) {
                    log.error("Missing subject for email log id={}", logEntry.getId());
                    logEntry.setStatus(EmailStatus.FAILED);
                    emailLogRepository.save(logEntry);
                    continue;
                }

                Email from = new Email(fromEmail, "BlackInt");
                Email to = new Email(logEntry.getRecipient());
                String htmlContent;

                switch (logEntry.getEmailType()) {

                    case ADMIN_NOTIFICATION ->
                            htmlContent = emailTemplateBuilder
                                    .buildAdminNotificationTemplateFromLog(logEntry);

                    case USER_CONFIRMATION ->
                            htmlContent = emailTemplateBuilder
                                    .buildUserConfirmationTemplateFromLog(logEntry);

                    case CONVERTED_CLIENT ->
                            htmlContent = emailTemplateBuilder
                                    .buildConvertedTemplateFromLog(logEntry);

                    default ->
                            throw new IllegalStateException("Unknown email type");
                }

                Content content = new Content("text/html", htmlContent);

                Mail mail = new Mail(from, logEntry.getSubject(), to, content);

                Request request = new Request();
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                Response response = sendGrid.api(request);

                if (response.getStatusCode() >= 200 &&
                        response.getStatusCode() < 300) {

                    logEntry.setStatus(EmailStatus.SUCCESS);
                    logEntry.setErrorMessage(null);
                    logEntry.setNextRetryAt(null);

                } else {

                    throw new RuntimeException(response.getBody());
                }

            } catch (IOException e) {

                logEntry.setRetryCount(logEntry.getRetryCount() + 1);
                logEntry.setNextRetryAt(LocalDateTime.now().plusMinutes(5));
                logEntry.setErrorMessage(e.getMessage());

                log.error("Email retry failed | id={}",
                        logEntry.getId(), e);
            }

            emailLogRepository.save(logEntry);
        }
    }
}