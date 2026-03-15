package com.blackint.email;

import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailQueueWorker {

    private final EmailLogRepository emailLogRepository;
    private final SendGrid sendGrid;

    private final EmailTemplateBuilder emailTemplateBuilder;

    @Value("${blackint.from.email}")
    private String fromEmail;

    @Scheduled(fixedRate = 20000)
    public void processQueue() {

        List<EmailLog> pendingEmails =
                emailLogRepository.findTop10ByStatus(EmailStatus.PENDING);

        for (EmailLog logEntry : pendingEmails) {

            try {

                logEntry.setStatus(EmailStatus.PROCESSING);
                emailLogRepository.save(logEntry);

                Email from = new Email(fromEmail, "BlackInt");
                Email to = new Email(logEntry.getRecipient());

                String htmlContent;

                switch (logEntry.getEmailType()) {

                    case USER_CONFIRMATION ->
                            htmlContent = emailTemplateBuilder
                                    .buildUserConfirmationTemplateFromLog(logEntry);

                    case ADMIN_NOTIFICATION ->
                            htmlContent = emailTemplateBuilder
                                    .buildAdminNotificationTemplateFromLog(logEntry);

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

                if (response.getStatusCode() == 202) {

                    logEntry.setStatus(EmailStatus.SUCCESS);

                } else {

                    throw new RuntimeException(response.getBody());
                }

            } catch (Exception e) {

                logEntry.setStatus(EmailStatus.FAILED);
                logEntry.setRetryCount(logEntry.getRetryCount() + 1);
                logEntry.setNextRetryAt(LocalDateTime.now().plusMinutes(5));

                log.error("Email queue failed {}", logEntry.getId(), e);
            }

            emailLogRepository.save(logEntry);
        }
    }
}
