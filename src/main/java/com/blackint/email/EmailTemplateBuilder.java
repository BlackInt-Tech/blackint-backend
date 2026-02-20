package com.blackint.email;

import com.blackint.entity.Contact;

public class EmailTemplateBuilder {

    public static String buildUserConfirmationTemplate(Contact contact) {

        return """
        <html>
        <body style="font-family: Arial; background:#f4f4f4; padding:20px;">
            <div style="background:white; padding:30px; border-radius:10px;">
                <h2 style="color:#FF4D00;">Hi %s 👋</h2>
                <p>Thank you for reaching out to <strong>BlackInt</strong>.</p>
                <p>We've received your inquiry regarding:</p>
                <p><strong>%s</strong></p>
                <p>Our team will contact you within 24 hours.</p>
                <hr/>
                <p style="font-size:12px;color:gray;">
                    BlackInt – Digital Growth Partner
                </p>
            </div>
        </body>
        </html>
        """.formatted(contact.getFullName(), contact.getSubject());
    }

    public static String buildAdminNotificationTemplate(Contact contact) {

        return """
        <html>
        <body style="font-family: Arial;">
            <h3>New Lead Received 🚀</h3>
            <p><strong>Name:</strong> %s</p>
            <p><strong>Email:</strong> %s</p>
            <p><strong>Phone:</strong> %s</p>
            <p><strong>Subject:</strong> %s</p>
            <p><strong>Message:</strong></p>
            <p>%s</p>
            <hr/>
            <p>Public ID: %s</p>
        </body>
        </html>
        """.formatted(
                contact.getFullName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getSubject(),
                contact.getMessage(),
                contact.getPublicId()
        );
    }

    public static String buildConvertedTemplate(Contact contact) {

        return """
        <html>
        <body style="font-family: Arial; background:#f4f4f4; padding:20px;">
            <div style="background:white; padding:30px; border-radius:10px;">
                <h2 style="color:#FF4D00;">Welcome Aboard %s 🎉</h2>
                <p>We're excited to start working with you!</p>
                <p>Our strategy team will connect shortly.</p>
                <hr/>
                <p>BlackInt Team 🚀</p>
            </div>
        </body>
        </html>
        """.formatted(contact.getFullName());
    }
}