package com.blackint.email;

import com.blackint.entity.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateBuilder {

    @Value("${blackint.frontend.url}")
    private String frontendUrl;

    @Value("${blackint.assets.url}")
    private String assetsUrl;

    // ================= IMAGE URLS =================

    private String logoUrl() {
        return assetsUrl + "/logo.png";
    }

    private String facebookIcon() {
        return assetsUrl + "/facebook.png";
    }

    private String instagramIcon() {
        return assetsUrl + "/instagram.png";
    }

    private String linkedinIcon() {
        return assetsUrl + "/linkedin.png";
    }

    private String twitterIcon() {
        return assetsUrl + "/twitter.png";
    }

    private String welcomeImage() {
        return assetsUrl + "/email-welcome.jpg";
    }

    private String conversionImage() {
        return assetsUrl + "/email-conversion.jpg";
    }

    // ================= FRONTEND LINKS =================

    private String websiteUrl() {
        return frontendUrl;
    }

    private String insightsUrl() {
        return frontendUrl + "/insights";
    }

    private String privacyUrl() {
        return frontendUrl + "/privacy-policy";
    }

    private String termsUrl() {
        return frontendUrl + "/terms-of-service";
    }

    // ================= SERVICE FORMATTER =================

    private static String formatServices(String type, String name, String price) {

        if ((name == null || name.isBlank()) &&
            (type == null || type.isBlank()) &&
            (price == null || price.isBlank())) {

            return "<div style='color:#777;'>No services selected</div>";
        }

        StringBuilder builder = new StringBuilder();

        if (name != null && !name.isBlank()) {
            builder.append("""
                <div style="margin-bottom:6px;">
                    • <span style="color:#FFFFFF;">%s</span>
                </div>
            """.formatted(name.trim()));
        }

        if (type != null && !type.isBlank()) {
            builder.append("""
                <div style="margin-bottom:6px;">
                    • <span style="color:#FFFFFF;">Type: %s</span>
                </div>
            """.formatted(type.trim()));
        }

        if (price != null && !price.isBlank()) {
            builder.append("""
                <div style="margin-bottom:6px;">
                    • <span style="color:#FFFFFF;">Price: %s</span>
                </div>
            """.formatted(price.trim()));
        }

        return builder.toString();
    }
    /*
    =========================================
    USER CONFIRMATION TEMPLATE
    =========================================
    */

    public String buildUserConfirmationTemplate(Contact contact) {

        String servicesHtml = formatServices(contact.getServiceType(), contact.getServiceName(), contact.getServicePrice());

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <title>Welcome to BlackInt</title>
        </head>

        <body style="margin:0;padding:0;background:#ffffff;font-family:Arial,Helvetica,sans-serif;">

            <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;background:#ffffff;">
                <tr>
                    <td align="center">

                        <table width="620" cellpadding="0" cellspacing="0"
                            style="background:#111111;border-radius:14px;overflow:hidden;max-width:95%%;">

                            <!-- HEADER -->
                            <tr>
                                <td align="center" style="padding:40px 20px 30px 20px;">

                                    <div style="font-size:26px;font-weight:800;letter-spacing:0.2px;">
                                        <img src="%s" style="height:40px;vertical-align:middle;margin-right:6px;">
                                        <span style="color:#FFFFFF;">Black</span>
                                        <span style="color:#FF4D00;">Int</span>
                                    </div>

                                    <h1 style="color:#FF4D00;margin:25px 0 10px 0;font-size:20px;">
                                        Welcome %s 👋
                                    </h1>

                                    <p style="color:#CCCCCC;font-size:15px;max-width:480px;">
                                        Thank you for reaching out to
                                        <strong style="color:#FFFFFF;">BlackInt.</strong><br/>
                                        We're excited to explore your vision.
                                    </p>

                                </td>
                            </tr>

                            <!-- IMAGE -->
                            <tr>
                                <td align="center">
                                    <img src="%s"
                                        width="620"
                                        style="width:100%%;max-width:620px;display:block;"
                                        alt="BlackInt Welcome"/>
                                </td>
                            </tr>

                            <!-- DETAILS -->
                            <tr>
                                <td style="padding:35px 40px;color:#CCCCCC;font-size:15px;line-height:1.7;">

                                    <p>We’ve received your inquiry regarding:</p>

                                    <p><strong style="color:#FFFFFF;">Services :</strong></p>

                                    <div style="background:#1A1A1A;
                                                padding:18px;
                                                border-left:4px solid #FF4D00;
                                                border-radius:6px;
                                                margin:20px 0;
                                                color:#FFFFFF;">
                                        %s
                                    </div>

                                    <p>
                                        Our strategy team is reviewing your request.
                                        You can expect callback within
                                        <strong style="color:#FFFFFF;">24 hours</strong>.
                                    </p>

                                </td>
                            </tr>

                            <!-- CTA -->
                            <tr>
                                <td align="center" style="padding:10px 20px 40px 20px;">

                                    <table cellpadding="0" cellspacing="0">
                                        <tr>

                                            <td align="center" style="padding:8px;">
                                                <a href="%s"
                                                style="background:#FF4D00;
                                                        color:#FFFFFF;
                                                        text-decoration:none;
                                                        padding:12px 24px;
                                                        border-radius:6px;
                                                        font-size:14px;
                                                        display:inline-block;">
                                                    Visit Website
                                                </a>
                                            </td>

                                            <td align="center" style="padding:8px;">
                                                <a href="%s"
                                                style="border:1px solid #FF4D00;
                                                        color:#FF4D00;
                                                        text-decoration:none;
                                                        padding:12px 24px;
                                                        border-radius:6px;
                                                        font-size:14px;
                                                        display:inline-block;">
                                                    Read Our Insights
                                                </a>
                                            </td>

                                        </tr>
                                    </table>

                                </td>
                            </tr>
                            
                            <!-- SIGN OFF -->
                            <tr>
                                <td style="padding:30px 40px;color:#CCCCCC;font-size:14px;line-height:1.6;">

                                    <p style="margin:0 0 10px 0;">
                                        Best Regards,
                                    </p>

                                    <p style="margin:0;color:#FFFFFF;font-weight:bold;">
                                        BlackInt Strategy Team
                                    </p>

                                </td>
                            </tr>

                            <!-- ================= FOOTER ================= -->
                            <tr>
                                <td style="background:#0E0E0E;padding:30px 20px;text-align:center;font-size:12px;color:#777777;">

                                    <p style="margin:5px 0;">
                                        BlackInt – Digital Growth Partner
                                    </p>

                                    <p style="margin:5px 0;">
                                        Greater Noida, Uttar Pradesh, India
                                    </p>

                                    <!-- POLICY LINKS -->
                                    <p style="margin:8px 0;">
                                        <a href="%s"
                                           style="color:#777777;text-decoration:none;margin:0 8px;">
                                            Privacy Policy
                                        </a>
                                        |
                                        <a href="%s"
                                           style="color:#777777;text-decoration:none;margin:0 8px;">
                                            Terms of Service
                                        </a>
                                    </p>

                                    <!-- SOCIAL LINKS -->
                                    <div style="margin-top:15px;">

                                        <a href="https://instagram.com"
                                           style="margin:0 6px;">
                                            <img src="%s"
                                                 width="18"
                                                 height="18"
                                                 style="opacity:0.8;"/>
                                        </a>

                                        <a href="https://linkedin.com"
                                           style="margin:0 6px;">
                                            <img src="%s"
                                                 width="18"
                                                 height="18"
                                                 style="opacity:0.8;"/>
                                        </a>

                                        <a href="https://facebook.com"
                                           style="margin:0 6px;">
                                            <img src="%s"
                                                 width="18"
                                                 height="18"
                                                 style="opacity:0.8;"/>
                                        </a>

                                        <a href="https://twitter.com"
                                           style="margin:0 6px;">
                                            <img src="%s"
                                                 width="18"
                                                 height="18"
                                                 style="opacity:0.8;"/>
                                        </a>

                                    </div>

                                    <p style="margin-top:15px;">
                                        © 2026 BlackInt. All rights reserved.
                                    </p>

                                </td>
                            </tr>

                        </table>

                    </td>
                </tr>
            </table>

        </body>
        </html>
        """.formatted(
            logoUrl(),
            contact.getFirstName() + " " + contact.getLastName(),
            welcomeImage(),
            servicesHtml,
            websiteUrl(),
            insightsUrl(),
            privacyUrl(),
            termsUrl(),
            instagramIcon(),
            linkedinIcon(),
            facebookIcon(),
            twitterIcon()
        );
    }
/*
=========================================
ADMIN NOTIFICATION TEMPLATE
=========================================
*/

public String buildAdminNotificationTemplate(Contact contact) {

    String servicesHtml = formatServices(contact.getServiceType(), contact.getServiceName(), contact.getServicePrice());

    return """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>New Lead - BlackInt</title>
    </head>

    <body style="margin:0;padding:0;background:#ffffff;font-family:Arial,Helvetica,sans-serif;">

        <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;">
            <tr>
                <td align="center">

                    <table width="620" cellpadding="0" cellspacing="0"
                        style="background:#111111;border-radius:14px;overflow:hidden;max-width:95%%;">

                        <!-- HEADER -->
                        <tr>
                            <td align="center" style="padding:30px;">
                                <div style="font-size:26px;font-weight:800;">
                                    <img src="%s" style="height:40px;vertical-align:middle;margin-right:6px;">
                                    <span style="color:#FFFFFF;">Black</span>
                                    <span style="color:#FF4D00;">Int</span>
                                </div>

                                <h2 style="color:#FF4D00;margin-top:20px;">
                                    New Lead Received
                                </h2>
                            </td>
                        </tr>

                        <!-- LEAD DETAILS -->
                        <tr>
                            <td style="padding:30px 40px;color:#CCCCCC;font-size:14px;line-height:1.7;">

                                <div style="background:#1A1A1A;padding:20px;border-radius:8px;">

                                    <p><strong style="color:#FFFFFF;">Name:</strong> %s</p>
                                    <p><strong style="color:#FFFFFF;">Email:</strong> %s</p>
                                    <p><strong style="color:#FFFFFF;">Phone:</strong> %s</p>

                                    <p><strong style="color:#FFFFFF;">Services :</strong></p>

                                    <div style="background:#1A1A1A;
                                                padding:18px;
                                                border-left:4px solid #FF4D00;
                                                border-radius:6px;">
                                        %s
                                    </div>

                                    <p style="margin-top:20px;">
                                        <strong style="color:#FFFFFF;">Project Description :</strong><br/>
                                        %s
                                    </p>

                                </div>

                                <p style="margin-top:25px;font-size:12px;color:#777;">
                                    Lead Public ID: %s
                                </p>

                            </td>
                        </tr>

                        <!-- FOOTER -->
                        <tr>
                            <td style="background:#0E0E0E;padding:20px;text-align:center;font-size:12px;color:#777;">
                                BlackInt Admin Notification System
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>

    </body>
    </html>
    """.formatted(
            logoUrl(),
            contact.getFirstName() + " " + contact.getLastName(),
            contact.getEmail(),
            contact.getPhone(),
            servicesHtml,
            contact.getProjectIdea(),
            contact.getPublicId()
    );
}


/*
=========================================
CONVERTED CLIENT TEMPLATE
=========================================
*/

public String buildConvertedTemplate(Contact contact) {

    String servicesHtml = formatServices(contact.getServiceType(), contact.getServiceName(), contact.getServicePrice());

    return """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Welcome to BlackInt</title>
    </head>

    <body style="margin:0;padding:0;background:#ffffff;font-family:Arial,Helvetica,sans-serif;">

        <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;background:#ffffff;">
            <tr>
                <td align="center">

                    <table width="620" cellpadding="0" cellspacing="0"
                           style="background:#111111;border-radius:14px;overflow:hidden;max-width:95%%;">

                        <!-- HEADER -->
                        <tr>
                            <td align="center" style="padding:40px 20px 30px 20px;">

                                <div style="font-size:26px;font-weight:800;">
                                    <img src="%s" style="height:40px;vertical-align:middle;margin-right:6px;">
                                    <span style="color:#FFFFFF;">Black</span>
                                    <span style="color:#FF4D00;">Int</span>
                                </div>

                                <h1 style="color:#FF4D00;margin:25px 0 10px 0;font-size:26px;">
                                    Welcome Aboard %s 🎉
                                </h1>

                                <p style="color:#CCCCCC;font-size:15px;">
                                    We're excited to officially begin working with you.
                                </p>

                            </td>
                        </tr>

                        <!-- IMAGE -->
                        <tr>
                            <td align="center">
                                <img src="%s"
                                     width="620"
                                     style="width:100%%;max-width:620px;display:block;"
                                     alt="BlackInt Welcome"/>
                            </td>
                        </tr>

                        <!-- CONTENT -->
                        <tr>
                            <td style="padding:35px 40px;color:#CCCCCC;font-size:15px;line-height:1.7;">

                                <p>
                                    Our team will contact you shortly to begin onboarding.
                                </p>

                                <p><strong style="color:#FFFFFF;">Services :</strong></p>

                                <div style="background:#1A1A1A;
                                            padding:18px;
                                            border-left:4px solid #FF4D00;
                                            border-radius:6px;
                                            margin:20px 0;
                                            color:#FFFFFF;">
                                    %s
                                </div>

                            </td>
                        </tr>

                        <!-- FOOTER -->
                        <tr>
                            <td style="background:#0E0E0E;padding:30px 20px;text-align:center;font-size:12px;color:#777777;">
                                © 2026 BlackInt. All rights reserved.
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>

    </body>
    </html>
    """.formatted(
            logoUrl(),
            contact.getFirstName() + " " + contact.getLastName(),
            conversionImage(),
            servicesHtml
    );
}

    public String buildUserConfirmationTemplateFromLog(EmailLog log) {

        Contact contact = Contact.builder()
                .publicId(log.getPublicId())
                .firstName(log.getFirstName())
                .lastName(log.getLastName())
                .email(log.getRecipient())
                .phone(log.getPhone())
                .company(log.getCompany())
                .serviceType(log.getServiceType())
                .serviceName(log.getServiceName())
                .servicePrice(log.getServicePrice())
                .budget(log.getBudget())
                .projectIdea(log.getProjectIdea())
                .message(log.getMessage())
                .build();

        return buildUserConfirmationTemplate(contact);
    }

    public String buildAdminNotificationTemplateFromLog(EmailLog log) {

        Contact contact = Contact.builder()
                .firstName(log.getFirstName())
                .lastName(log.getLastName())
                .email(log.getRecipient())
                .phone(log.getPhone())
                .company(log.getCompany())
                .serviceType(log.getServiceType())
                .serviceName(log.getServiceName())
                .servicePrice(log.getServicePrice())
                .budget(log.getBudget())
                .projectIdea(log.getProjectIdea())
                .message(log.getMessage())
                .publicId(log.getPublicId())
                .build();

        return buildAdminNotificationTemplate(contact);
    }

    public String buildConvertedTemplateFromLog(EmailLog log) {

        Contact contact = Contact.builder()
                .publicId(log.getPublicId())
                .firstName(log.getFirstName())
                .lastName(log.getLastName())
                .email(log.getRecipient())
                .phone(log.getPhone())
                .company(log.getCompany())
                .serviceType(log.getServiceType())
                .serviceName(log.getServiceName())
                .servicePrice(log.getServicePrice())
                .budget(log.getBudget())
                .projectIdea(log.getProjectIdea())
                .message(log.getMessage())
                .build();

        return buildConvertedTemplate(contact);
    }

}






