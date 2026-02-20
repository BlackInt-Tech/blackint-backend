package com.blackint.email;

import com.blackint.entity.Contact;
import com.blackint.entity.LeadStatus;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${blackint.admin.email}")
    private String adminEmail;

    // ================= USER CONFIRMATION =================

    @Async
    @Override
    public void sendLeadSubmissionEmail(Contact contact) {

        String subject = "We've received your message – BlackInt";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(contact.getEmail());
            helper.setSubject(subject);
            helper.setText(
                    EmailTemplateBuilder.buildUserConfirmationTemplate(contact),
                    true
            );

            mailSender.send(message);

            emailLogRepository.save(
                    EmailLog.builder()
                            .publicId(contact.getPublicId())
                            .recipient(contact.getEmail())
                            .subject(subject)
                            .status(EmailStatus.SUCCESS)
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            log.info("User email sent | publicId={}", contact.getPublicId());

        } catch (Exception e) {

            emailLogRepository.save(
                    EmailLog.builder()
                            .publicId(contact.getPublicId())
                            .recipient(contact.getEmail())
                            .subject(subject)
                            .status(EmailStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .retryCount(0)
                            .nextRetryAt(LocalDateTime.now().plusMinutes(5))
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            log.error("User email failed | publicId={}", contact.getPublicId(), e);
        }
    }

    // ================= ADMIN NOTIFICATION =================

    @Async
    @Override
    public void sendAdminNotification(Contact contact) {

        String subject = "New Lead Received – " + contact.getSubject();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(adminEmail);
            helper.setSubject(subject);
            helper.setText(
                    EmailTemplateBuilder.buildAdminNotificationTemplate(contact),
                    true
            );

            mailSender.send(message);

            emailLogRepository.save(
                    EmailLog.builder()
                            .publicId(contact.getPublicId())
                            .recipient(adminEmail)
                            .subject(subject)
                            .status(EmailStatus.SUCCESS)
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            log.info("Admin notification sent | publicId={}", contact.getPublicId());

        } catch (Exception e) {

            emailLogRepository.save(
                    EmailLog.builder()
                            .publicId(contact.getPublicId())
                            .recipient(adminEmail)
                            .subject(subject)
                            .status(EmailStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .retryCount(0)
                            .nextRetryAt(LocalDateTime.now().plusMinutes(5))
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            log.error("Admin email failed | publicId={}", contact.getPublicId(), e);
        }
    }

    // ================= STATUS UPDATE EMAIL =================

    @Async
    @Override
    public void sendLeadStatusUpdateEmail(Contact contact) {

        if (contact.getStatus() != LeadStatus.CONVERTED) {
            return;
        }

        String subject = "Welcome Aboard – BlackInt 🚀";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(contact.getEmail());
            helper.setSubject(subject);
            helper.setText(
                    EmailTemplateBuilder.buildConvertedTemplate(contact),
                    true
            );

            mailSender.send(message);

            emailLogRepository.save(
                    EmailLog.builder()
                            .publicId(contact.getPublicId())
                            .recipient(contact.getEmail())
                            .subject(subject)
                            .status(EmailStatus.SUCCESS)
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            log.info("Converted email sent | publicId={}", contact.getPublicId());

        } catch (Exception e) {

            emailLogRepository.save(
                    EmailLog.builder()
                            .publicId(contact.getPublicId())
                            .recipient(contact.getEmail())
                            .subject(subject)
                            .status(EmailStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .retryCount(0)
                            .nextRetryAt(LocalDateTime.now().plusMinutes(5))
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            log.error("Converted email failed | publicId={}", contact.getPublicId(), e);
        }
    }
}
