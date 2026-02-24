package com.blackint.email;

import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final SendGrid sendGrid;
    private final EmailLogRepository emailLogRepository;

    @Value("${blackint.from.email}")
    private String fromEmail;

    @Value("${blackint.admin.email}")
    private String adminEmail;

    private static final int MAX_RETRY = 3;

    // ================= USER CONFIRMATION =================

    @Async
    @Override
    public void sendLeadSubmissionEmail(Contact contact) {

        String subject = "We've received your message – BlackInt";
        String htmlContent = EmailTemplateBuilder.buildUserConfirmationTemplate(contact);

        sendEmail(contact.getPublicId(), contact.getEmail(), subject, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent);
    }

    // ================= ADMIN NOTIFICATION =================

    @Async
    @Override
    public void sendAdminNotification(Contact contact) {

        String subject = "New Lead Received – " + contact.getSubject();
        String htmlContent = EmailTemplateBuilder.buildAdminNotificationTemplate(contact);

        sendEmail(contact.getPublicId(), adminEmail, subject, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent);
    }

    // ================= STATUS UPDATE =================

    @Async
    @Override
    public void sendLeadStatusUpdateEmail(Contact contact) {

        if (contact.getStatus() != LeadStatus.CONVERTED) return;

        String subject = "Welcome Aboard – BlackInt 🚀";
        String htmlContent = EmailTemplateBuilder.buildConvertedTemplate(contact);

        sendEmail(contact.getPublicId(), contact.getEmail(), subject, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent, htmlContent);
    }

    // ================= CORE SEND METHOD =================

    private void sendEmail(String publicId,
                           String recipient,
                           String subject,
                           String htmlContent,
                           String firstName,
                           String lastName,
                           String company,
                           String phone,
                           String services,
                           String budget,
                           String projectIdea,
                           String message) {

        if (recipient == null || recipient.isBlank()) {
            log.error("Recipient email is null or empty | publicId={}", publicId);
            return;
        }

        Email from = new Email(fromEmail);
        Email to = new Email(recipient);
        Content content = new Content("text/html", htmlContent);

        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            int statusCode = response.getStatusCode();
            String responseBody = response.getBody();

            // SendGrid returns 202 for success
            if (statusCode == 202) {

                emailLogRepository.save(
                EmailLog.builder()
                        .publicId(publicId)
                        .recipient(recipient)
                        .subject(subject)
                        .status(EmailStatus.FAILED)
                        .createdAt(LocalDateTime.now())
                        .firstName(firstName)
                        .lastName(lastName)
                        .company(company)
                        .phone(phone)
                        .services(String.join(", ", services))
                        .budget(budget)
                        .projectIdea(projectIdea)
                        .message(message)

                        .build()
        );

                log.info("Email sent successfully | publicId={} | recipient={}", publicId, recipient);

            } else {

                saveFailure(publicId, recipient, subject,
                        "SendGrid status: " + statusCode + " | body: " + responseBody, responseBody, responseBody, responseBody, responseBody, responseBody, responseBody, responseBody, responseBody);
            }

        } catch (IOException ex) {

            saveFailure(publicId, recipient, subject, ex.getMessage(), message, message, message, message, message, message, message, message);

        } catch (Exception ex) {

            saveFailure(publicId, recipient, subject, ex.getMessage(), message, message, message, message, message, message, message, message);
        }
    }

    // ================= FAILURE HANDLER =================

    private void saveFailure(String publicId,
                             String recipient,
                             String subject,
                             String errorMessage,
                             String firstName,
                             String lastName,
                             String company,
                             String phone,
                             String services,
                             String budget,
                             String projectIdea,
                             String message) {

        emailLogRepository.save(
                EmailLog.builder()
                        .publicId(publicId)
                        .recipient(recipient)
                        .subject(subject)
                        .status(EmailStatus.FAILED)
                        .errorMessage(errorMessage)
                        .retryCount(0)
                        .nextRetryAt(LocalDateTime.now().plusMinutes(5))
                        .createdAt(LocalDateTime.now())
                        .firstName(firstName)
                        .lastName(lastName)
                        .company(company)
                        .phone(phone)
                        .services(String.join(", ", services))
                        .budget(budget)
                        .projectIdea(projectIdea)
                        .message(message)

                        .build()
        );

        log.error("Email failed | publicId={} | recipient={} | error={}",
                publicId, recipient, errorMessage);
    }
}