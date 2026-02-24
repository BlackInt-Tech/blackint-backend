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

    // ================= USER CONFIRMATION =================

    @Async
    @Override
    public void sendLeadSubmissionEmail(Contact contact) {

        String subject = "We've received your message – BlackInt";
        String htmlContent = EmailTemplateBuilder.buildUserConfirmationTemplate(contact);

        sendEmail(contact, contact.getEmail(), subject, htmlContent);
    }

    // ================= ADMIN NOTIFICATION =================

    @Async
    @Override
    public void sendAdminNotification(Contact contact) {

        String subject = "New Lead Received – " + contact.getSubject();
        String htmlContent = EmailTemplateBuilder.buildAdminNotificationTemplate(contact);

        sendEmail(contact, adminEmail, subject, htmlContent);
    }

    // ================= STATUS UPDATE =================

    @Async
    @Override
    public void sendLeadStatusUpdateEmail(Contact contact) {

        if (contact.getStatus() != LeadStatus.CONVERTED) {
            return;
        }

        String subject = "Welcome Aboard – BlackInt 🚀";
        String htmlContent = EmailTemplateBuilder.buildConvertedTemplate(contact);

        sendEmail(contact, contact.getEmail(), subject, htmlContent);
    }

    // ================= CORE SEND METHOD =================

    private void sendEmail(Contact contact,
                           String recipient,
                           String subject,
                           String htmlContent) {

        if (recipient == null || recipient.isBlank()) {
            log.error("Recipient email is null or empty | publicId={}", contact.getPublicId());
            return;
        }

        try {

            Email from = new Email(fromEmail);
            Email to = new Email(recipient);
            Content content = new Content("text/html", htmlContent);

            Mail mail = new Mail(from, subject, to, content);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() == 202) {

                emailLogRepository.save(
                        EmailLog.builder()
                                .publicId(contact.getPublicId())
                                .recipient(recipient)
                                .subject(subject)
                                .status(EmailStatus.SUCCESS)
                                .createdAt(LocalDateTime.now())
                                .firstName(contact.getFirstName())
                                .lastName(contact.getLastName())
                                .company(contact.getCompany())
                                .phone(contact.getPhone())
                                .services(contact.getServices() != null
                                        ? String.join(", ", contact.getServices())
                                        : null)
                                .budget(contact.getBudget())
                                .projectIdea(contact.getProjectIdea())
                                .message(contact.getMessage())
                                .build()
                );

                log.info("Email sent successfully | publicId={} | recipient={}",
                        contact.getPublicId(), recipient);

            } else {

                saveFailure(contact, recipient, subject,
                        "SendGrid response: " + response.getBody());
            }

        } catch (Exception ex) {

            saveFailure(contact, recipient, subject, ex.getMessage());
        }
    }

    // ================= FAILURE HANDLER =================

    private void saveFailure(Contact contact,
                             String recipient,
                             String subject,
                             String errorMessage) {

        emailLogRepository.save(
                EmailLog.builder()
                        .publicId(contact.getPublicId())
                        .recipient(recipient)
                        .subject(subject)
                        .status(EmailStatus.FAILED)
                        .errorMessage(errorMessage)
                        .retryCount(0)
                        .nextRetryAt(LocalDateTime.now().plusMinutes(5))
                        .createdAt(LocalDateTime.now())
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .company(contact.getCompany())
                        .phone(contact.getPhone())
                        .services(contact.getServices() != null
                                ? String.join(", ", contact.getServices())
                                : null)
                        .budget(contact.getBudget())
                        .projectIdea(contact.getProjectIdea())
                        .message(contact.getMessage())
                        .build()
        );

        log.error("Email failed | publicId={} | recipient={} | error={}",
                contact.getPublicId(), recipient, errorMessage);
    }
}