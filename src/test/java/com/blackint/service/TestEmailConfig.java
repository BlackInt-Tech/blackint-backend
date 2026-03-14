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
            public void queueLeadSubmissionEmail(Contact contact) {
                // No-op for tests
            }

            @Override
            public void queueAdminNotification(Contact contact) {
                // No-op for tests
            }

            @Override
            public void queueConvertedEmail(Contact contact) {
                // No-op for tests
            }
        };
    }
}