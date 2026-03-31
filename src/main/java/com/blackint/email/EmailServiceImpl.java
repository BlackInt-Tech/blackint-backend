package com.blackint.email;

import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final EmailLogRepository emailLogRepository;

    @Value("${blackint.admin.email}")
    private String adminEmail;

    /*
     =========================================
     USER CONFIRMATION EMAIL (QUEUED)
     =========================================
    */

    @Override
    public void queueLeadSubmissionEmail(Contact contact) {

        try {

            EmailLog logEntry = EmailLog.builder()
                    .emailType(EmailType.USER_CONFIRMATION)
                    .publicId(contact.getPublicId())
                    .recipient(contact.getEmail())
                    .subject("We've received your message – BlackInt")
                    .status(EmailStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .retryCount(0)

                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .company(contact.getCompany())
                    .phone(contact.getPhone())
                    .services(contact.getServices())
                    .budget(contact.getBudget())
                    .projectIdea(contact.getProjectIdea())
                    .message(contact.getMessage())

                    .build();

            emailLogRepository.save(logEntry);

            log.info("User confirmation email queued | publicId={}", contact.getPublicId());

        } catch (Exception ex) {

            log.error("Failed to queue user email | publicId={}", contact.getPublicId(), ex);

        }
    }

    /*
     =========================================
     ADMIN NOTIFICATION EMAIL (QUEUED)
     =========================================
    */

    @Override
    public void queueAdminNotification(Contact contact) {

        try {

            String subject =
                    "New Lead – "
                            + contact.getFirstName()
                            + " "
                            + contact.getLastName()
                            + " | "
                            + contact.getServices();

            EmailLog logEntry = EmailLog.builder()
                    .emailType(EmailType.ADMIN_NOTIFICATION)
                    .publicId(contact.getPublicId())
                    .recipient(adminEmail)
                    .subject(subject)
                    .status(EmailStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .retryCount(0)

                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .email(contact.getEmail())
                    .company(contact.getCompany())
                    .phone(contact.getPhone())
                    .services(contact.getServices())
                    .budget(contact.getBudget())
                    .projectIdea(contact.getProjectIdea())
                    .message(contact.getMessage())
                    .build();

            emailLogRepository.save(logEntry);

            log.info("Admin notification email queued | publicId={}", contact.getPublicId());

        } catch (Exception ex) {

            log.error("Failed to queue admin email | publicId={}", contact.getPublicId(), ex);

        }
    }

    /*
     =========================================
     CONVERTED CLIENT EMAIL (QUEUED)
     =========================================
    */

    @Override
    public void queueConvertedEmail(Contact contact) {

        try {

            if (contact.getStatus() != LeadStatus.CONVERTED) {
                return;
            }

            EmailLog logEntry = EmailLog.builder()
                    .emailType(EmailType.CONVERTED_CLIENT)
                    .publicId(contact.getPublicId())
                    .recipient(contact.getEmail())
                    .subject("Welcome Aboard – BlackInt 🚀")
                    .status(EmailStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .retryCount(0)

                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .company(contact.getCompany())
                    .phone(contact.getPhone())
                    .services(contact.getServices())
                    .budget(contact.getBudget())
                    .projectIdea(contact.getProjectIdea())
                    .message(contact.getMessage())

                    .build();

            emailLogRepository.save(logEntry);

            log.info("Converted client email queued | publicId={}", contact.getPublicId());

        } catch (Exception ex) {

            log.error("Failed to queue converted email | publicId={}", contact.getPublicId(), ex);

        }
    }

}
