package com.blackint.email;

import com.blackint.entity.Contact;

public interface EmailService {

    void sendLeadSubmissionEmail(Contact contact);

    void sendAdminNotification(Contact contact);

    void sendLeadStatusUpdateEmail(Contact contact);

}