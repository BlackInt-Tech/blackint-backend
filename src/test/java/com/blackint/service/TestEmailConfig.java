package com.blackint.service;

import com.blackint.email.EmailService;
import com.blackint.entity.Contact;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestEmailConfig {

    @Bean
    public EmailService emailService() {

        return new EmailService() {

            @Override
            public void sendLeadSubmissionEmail(Contact contact) {
                // No-op for tests
            }

            @Override
            public void sendAdminNotification(Contact contact) {
                // No-op for tests
            }

            @Override
            public void sendLeadStatusUpdateEmail(Contact contact) {
                // No-op for tests
            }
        };
    }
}