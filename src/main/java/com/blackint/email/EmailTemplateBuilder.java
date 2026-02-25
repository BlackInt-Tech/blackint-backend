package com.blackint.email;

import com.blackint.entity.Contact;

public class EmailTemplateBuilder {

    private static final String logo_url = "https://blackint-dev.onrender.com/images/logo.png";
    private static final String fb_icon = "https://blackint-dev.onrender.com/images/facebook.png";
    private static final String insta_icon = "https://blackint-dev.onrender.com/images/insta.png";
    private static final String linkedin_icon = "https://blackint-dev.onrender.com/images/linkedin.png";
    private static final String twitter_icon = "https://blackint-dev.onrender.com/images/twitter.png";
    private static final String welcome_image = "https://blackint-dev.onrender.com/images/email-welcome.jpg";
    private static final String hero_image = "https://blackint-dev.onrender.com/images/hero-image.png";

    private static String formatServices(String services) {

        if (services == null || services.isBlank()) {
            return "<div style='color:#777;'>No services selected</div>";
        }

        String[] servicesArray = services.split(",");

        StringBuilder builder = new StringBuilder();

        for (String service : servicesArray) {
            builder.append("""
                <div style="margin-bottom:6px;">
                    • <span style="color:#FFFFFF;">%s</span>
                </div>
            """.formatted(service.trim()));
        }

        return builder.toString();
    }
    
    public static String buildUserConfirmationTemplate(Contact contact) {

        String servicesHtml = formatServices(contact.getServices());

    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <title>Welcome to BlackInt</title>
        </head>

        <body style="margin:0;padding:0;background:#0B0B0B;font-family:Arial,Helvetica,sans-serif;">

            <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;background:#0B0B0B;">
                <tr>
                    <td align="center">

                        <table width="620" cellpadding="0" cellspacing="0"
                            style="background:#111111;border-radius:14px;overflow:hidden;max-width:95%%;">

                            <!-- ================= HEADER ================= -->
                            <tr>
                                <td align="center" style="padding:40px 20px 30px 20px;">

                                    <!-- Brand -->
                                    <div style="font-size:26px;font-weight:800;letter-spacing:1px;">
                                        <img src="%s" style="height:30px;vertical-align:middle;margin-right:6px;">
                                        <span style="color:#FFFFFF;">Black</span><span style="color:#FF4D00;">Int</span>                                      
                                    </div>

                                    <!-- Welcome -->
                                    <h1 style="color:#FF4D00;margin:25px 0 10px 0;font-size:20px;">
                                        Welcome %s 👋
                                    </h1>

                                    <p style="color:#CCCCCC;font-size:15px;max-width:480px;">
                                        Thank you for reaching out to <strong style="color:#FFFFFF;">BlackInt</strong>.
                                        We're excited to explore your vision.
                                    </p>

                                </td>
                            </tr>

                            <!-- ================= IMAGE ================= -->
                            <tr>
                                <td align="center">
                                    <img src="%s"
                                        width="620"
                                        style="width:100%%;max-width:620px;display:block;"
                                        alt="BlackInt Welcome"/>
                                </td>
                            </tr>

                            <!-- ================= DETAILS ================= -->
                            <tr>
                                <td style="padding:35px 40px;color:#CCCCCC;font-size:15px;line-height:1.7;">

                                    <p>We’ve received your inquiry regarding:</p>

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
                                        You can expect a detailed response within
                                        <strong style="color:#FFFFFF;">24 hours</strong>.
                                    </p>

                                </td>
                            </tr>

                            <!-- ================= CTA BUTTONS ================= -->
                            <tr>
                                <td align="center" style="padding:10px 20px 40px 20px;">

                                    <table cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td align="center" style="padding:8px;">
                                                <a href="https://blackint.tech"
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
                                                <a href="https://blackint.tech/insights"
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

                            <!-- ================= SIGN OFF ================= -->
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

                                    <p style="margin:8px 0;">
                                        <a href="https://blackint.tech/privacy"
                                        style="color:#777777;text-decoration:none;margin:0 8px;">
                                            Privacy Policy
                                        </a> |
                                        <a href="https://blackint.tech/terms"
                                        style="color:#777777;text-decoration:none;margin:0 8px;">
                                            Terms of Service
                                        </a>
                                    </p>

                                    <!-- Social Links -->
                                    <div style="margin-top:15px;">
                                        <a href="https://instagram.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
                                        </a>
                                        <a href="https://linkedin.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
                                        </a>
                                        <a href="https://facebook.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
                                        </a>
                                        <a href="https://twitter.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
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
            logo_url,
            contact.getFirstName() + " " + contact.getLastName(),
            welcome_image,
            servicesHtml,
            insta_icon,
            linkedin_icon,
            fb_icon,
            twitter_icon
    );
}


    public static String buildAdminNotificationTemplate(Contact contact) {

        String servicesHtml = formatServices(contact.getServices());

    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <title>New Lead - BlackInt</title>
        </head>

        <body style="margin:0;padding:0;background:#0B0B0B;font-family:Arial,Helvetica,sans-serif;">

            <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;">
                <tr>
                    <td align="center">

                        <table width="620" cellpadding="0" cellspacing="0"
                            style="background:#111111;border-radius:14px;overflow:hidden;max-width:95%%;">

                            <!-- HEADER -->
                            <tr>
                                <td align="center" style="padding:30px;">
                                    <div style="font-size:26px;font-weight:800;letter-spacing:1px;">
                                        <img src="%s" style="height:30px;vertical-align:middle;margin-right:6px;">
                                        <span style="color:#FFFFFF;">Black</span><span style="color:#FF4D00;">Int</span>                                      
                                    </div>
                                    <h2 style="color:#FF4D00;margin-top:20px;">
                                        New Lead Received
                                    </h2>
                                </td>
                            </tr>

                            <!-- LEAD DETAILS -->
                            <tr>
                                <td style="padding:30px 40px;color:#CCCCCC;font-size:14px;line-height:1.7;">

                                    <div style="background:#1A1A1A;padding:20px;border-radius:8px;margin-bottom:20px;">

                                        <p><strong style="color:#FFFFFF;">Name:</strong> %s</p>
                                        <p><strong style="color:#FFFFFF;">Email:</strong> %s</p>
                                        <p><strong style="color:#FFFFFF;">Phone:</strong> %s</p>
                                        <p style="margin-top:20px;"><strong style="color:#FFFFFF;">Message:</strong></p>
                                        <div style="background:#1A1A1A;
                                                    padding:18px;
                                                    border-left:4px solid #FF4D00;
                                                    border-radius:6px;">
                                            %s
                                        </div>
                                        <p><strong style="color:#FFFFFF;">Project Desc.:</strong> %s</p>

                                    </div>

                                    <p style="margin-top:25px;font-size:12px;color:#777;">
                                        Public ID: %s
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
                logo_url,
                contact.getFirstName() + " " + contact.getLastName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getProjectIdea(),
                servicesHtml,
                contact.getPublicId()
        );
    }


public static String buildConvertedTemplate(Contact contact) {

    String servicesHtml = formatServices(contact.getServices());

    return """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Welcome to BlackInt</title>
    </head>

    <body style="margin:0;padding:0;background:#0B0B0B;font-family:Arial,Helvetica,sans-serif;">

        <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;background:#0B0B0B;">
            <tr>
                <td align="center">

                    <table width="620" cellpadding="0" cellspacing="0"
                           style="background:#111111;border-radius:14px;overflow:hidden;max-width:95%%;">

                        <!-- ================= HEADER ================= -->
                        <tr>
                            <td align="center" style="padding:40px 20px 30px 20px;">

                                <!-- Brand -->
                                <div style="font-size:26px;font-weight:800;letter-spacing:1px;">
                                    <img src="%s" style="height:30px;vertical-align:middle;margin-right:6px;">
                                    <span style="color:#FFFFFF;">Black</span><span style="color:#FF4D00;">Int</span>
                                </div>

                                <!-- Welcome -->
                                <h1 style="color:#FF4D00;margin:25px 0 10px 0;font-size:26px;">
                                    Welcome Aboard %s 🎉
                                </h1>

                                <p style="color:#CCCCCC;font-size:15px;max-width:480px;">
                                    We're excited to officially start working with you.
                                    This is where strategy meets execution.
                                </p>

                            </td>
                        </tr>

                        <!-- ================= HERO IMAGE ================= -->
                        <tr>
                            <td align="center">
                                <img src="%s"
                                     width="620"
                                     style="width:100%%;max-width:620px;display:block;"
                                     alt="BlackInt Welcome"/>
                            </td>
                        </tr>

                        <!-- ================= CONTENT ================= -->
                        <tr>
                            <td style="padding:35px 40px;color:#CCCCCC;font-size:15px;line-height:1.7;">

                                <p>
                                    Our strategy team will reach out shortly to begin onboarding
                                    and align on next steps for your project.

                                    <div style="background:#1A1A1A;
                                                padding:18px;
                                                border-left:4px solid #FF4D00;
                                                border-radius:6px;
                                                margin:20px 0;
                                                color:#FFFFFF;">
                                        %s
                                    </div>
                                </p>

                                <div style="background:#1A1A1A;
                                            padding:18px;
                                            border-left:4px solid #FF4D00;
                                            border-radius:6px;
                                            margin:20px 0;
                                            color:#FFFFFF;">
                                    We’re committed to delivering measurable growth
                                    and premium digital experiences.
                                </div>

                                <p>
                                    You’ll receive further updates soon.
                                    We’re looking forward to building something exceptional together.
                                </p>

                            </td>
                        </tr>

                        <!-- ================= CTA BUTTONS ================= -->
                        <tr>
                            <td align="center" style="padding:10px 20px 40px 20px;">

                                <table cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td align="center" style="padding:8px;">
                                            <a href="https://blackint.tech"
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
                                            <a href="https://blackint.tech/insights"
                                               style="border:1px solid #FF4D00;
                                                      color:#FF4D00;
                                                      text-decoration:none;
                                                      padding:12px 24px;
                                                      border-radius:6px;
                                                      font-size:14px;
                                                      display:inline-block;">
                                                Explore Insights
                                            </a>
                                        </td>
                                    </tr>
                                </table>

                            </td>
                        </tr>

                        <!-- ================= SIGN OFF ================= -->
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

                                <!-- Social Links -->
                                    <div style="margin-top:15px;">
                                        <a href="https://instagram.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
                                        </a>
                                        <a href="https://linkedin.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
                                        </a>
                                        <a href="https://facebook.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
                                        </a>
                                        <a href="https://twitter.com" style="margin:0 6px;">
                                            <img src="%s"
                                                width="18" style="opacity:0.8;"/>
                                        </a>
                                    </div>


                                <p style="margin:8px 0;">
                                    <a href="https://blackint.tech/privacy"
                                       style="color:#777777;text-decoration:none;margin:0 8px;">
                                        Privacy Policy
                                    </a> |
                                    <a href="https://blackint.tech/terms"
                                       style="color:#777777;text-decoration:none;margin:0 8px;">
                                        Terms of Service
                                    </a>
                                </p>

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
            logo_url,
            contact.getFirstName() + " " + contact.getLastName(),
            hero_image,
            servicesHtml,
            insta_icon,
            linkedin_icon,
            fb_icon,
            twitter_icon
            
    );
    }
}