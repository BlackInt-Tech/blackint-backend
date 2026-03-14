package com.blackint.email;

import com.blackint.entity.Contact;

public interface EmailService {

    void queueLeadSubmissionEmail(Contact contact);

    void queueAdminNotification(Contact contact);

    void queueConvertedEmail(Contact contact);

}