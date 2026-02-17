package com.blackint.service;

import org.springframework.context.annotation.Profile;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile({"dev", "prod"}) 
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendUserConfirmation(String to, String fullName) {
        try {
            Context context = new Context();
            context.setVariable("name", fullName);

            String htmlContent = templateEngine.process(
                    "email/user-confirmation",
                    context
            );

            sendHtmlEmail(to, "We received your message | BlackInt", htmlContent);

        } catch (Exception e) {
            log.error("User confirmation email failed: {}", e.getMessage());
        }
    }

    public void notifyAdmin(String name, String email, String phone, String subject, String message) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("email", email);
            context.setVariable("phone", phone);
            context.setVariable("subject", subject);
            context.setVariable("message", message);

            String htmlContent = templateEngine.process(
                    "email/admin-notification",
                    context
            );

            sendHtmlEmail("admin@blackint.com",
                    "🚀 New Lead Received | BlackInt",
                    htmlContent);

        } catch (Exception e) {
            log.error("Admin notification email failed: {}", e.getMessage());
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("BlackInt <your-email@gmail.com>");

        mailSender.send(message);
    }
}
