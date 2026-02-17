package com.blackint.service;

import com.blackint.service.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestEmailConfig {

    @Bean
    public EmailService emailService() {
        return new EmailService(null, null) {
            @Override
            public void sendUserConfirmation(String to, String fullName) {
                // do nothing
            }

            @Override
            public void notifyAdmin(String name, String email, String phone, String subject, String message) {
                // do nothing
            }
        };
    }
}
