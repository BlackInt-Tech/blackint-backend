package com.blackint.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private static final int MAX_RETRY = 3;

    @Scheduled(fixedRate = 300000)
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

                Email from = new Email("no-reply@yourdomain.com");
                Email to = new Email(logEntry.getRecipient());
                Content content = new Content("text/html",
                        "Retrying previously failed email.");

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