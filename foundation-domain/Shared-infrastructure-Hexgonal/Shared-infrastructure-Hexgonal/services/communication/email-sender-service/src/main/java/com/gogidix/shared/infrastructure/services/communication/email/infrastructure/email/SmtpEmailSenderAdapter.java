package com.gogidix.shared.infrastructure.services.communication.email.infrastructure.email;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * SMTP email sender adapter implementation using JavaMail API.
 *
 * <p>This implementation sends emails via SMTP protocol with support for:
 * <ul>
 *   <li>TLS/STARTTLS encryption</li>
 *   <li>SSL encryption</li>
 *   <li>Authentication</li>
 *   <li>HTML and plain text emails</li>
 *   <li>Attachments</li>
 *   <li>Bulk sending with rate limiting</li>
 * </ul>
 *
 * <p>Configuration:
 * <pre>
 * email.provider=smtp
 * email.smtp.host=smtp.gmail.com
 * email.smtp.port=587
 * email.smtp.username=your-email@gmail.com
 * email.smtp.password=your-app-password
 * email.smtp.auth=true
 * email.smtp.starttls.enable=true
 * email.smtp.ssl.enable=false
 * email.from=noreply@yourdomain.com
 * email.from.name=Your Application
 * email.smtp.connection.timeout=5000
 * email.smtp.timeout=5000
 * email.smtp.writetimeout=5000
 * </pre>
 */
@Component
@ConditionalOnProperty(name = "email.provider", havingValue = "smtp", matchIfMissing = true)
public class SmtpEmailSenderAdapter implements EmailSenderGateway {

    private static final Logger logger = LoggerFactory.getLogger(SmtpEmailSenderAdapter.class);

    private final String smtpHost;
    private final int smtpPort;
    private final String smtpUsername;
    private final String smtpPassword;
    private final boolean authEnabled;
    private final boolean starttlsEnabled;
    private final boolean sslEnabled;
    private final String fromEmail;
    private final String fromName;
    private final int connectionTimeout;
    private final int timeout;
    private final int writeTimeout;

    private Session mailSession;
    private final boolean configured;

    public SmtpEmailSenderAdapter(
            @Value("${email.smtp.host:}") String smtpHost,
            @Value("${email.smtp.port:587}") int smtpPort,
            @Value("${email.smtp.username:}") String smtpUsername,
            @Value("${email.smtp.password:}") String smtpPassword,
            @Value("${email.smtp.auth:true}") boolean authEnabled,
            @Value("${email.smtp.starttls.enable:true}") boolean starttlsEnabled,
            @Value("${email.smtp.ssl.enable:false}") boolean sslEnabled,
            @Value("${email.from:}") String fromEmail,
            @Value("${email.from.name:}") String fromName,
            @Value("${email.smtp.connection.timeout:5000}") int connectionTimeout,
            @Value("${email.smtp.timeout:5000}") int timeout,
            @Value("${email.smtp.writetimeout:5000}") int writeTimeout) {

        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.authEnabled = authEnabled;
        this.starttlsEnabled = starttlsEnabled;
        this.sslEnabled = sslEnabled;
        this.fromEmail = fromEmail != null && !fromEmail.isEmpty() ? fromEmail : "noreply@localhost";
        this.fromName = fromName != null && !fromName.isEmpty() ? fromName : "Application";
        this.connectionTimeout = connectionTimeout;
        this.timeout = timeout;
        this.writeTimeout = writeTimeout;

        this.configured = smtpHost != null && !smtpHost.isEmpty() &&
                         smtpUsername != null && !smtpUsername.isEmpty() &&
                         smtpPassword != null && !smtpPassword.isEmpty();

        if (configured) {
            initializeMailSession();
            logger.info("SMTP email sender initialized with host: {}, port: {}, from: {}",
                    smtpHost, smtpPort, fromEmail);
        } else {
            logger.warn("SMTP email sender not configured. Please set email.smtp.host, email.smtp.username, and email.smtp.password");
        }
    }

    private void initializeMailSession() {
        Properties props = new Properties();

        // SMTP host configuration
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        // Authentication
        if (authEnabled) {
            props.put("mail.smtp.auth", "true");
        }

        // TLS/STARTTLS
        if (starttlsEnabled && !sslEnabled) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "false");
        }

        // SSL
        if (sslEnabled) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.port", smtpPort);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        // Timeouts
        props.put("mail.smtp.connectiontimeout", connectionTimeout);
        props.put("mail.smtp.timeout", timeout);
        props.put("mail.smtp.writetimeout", writeTimeout);

        // Debug mode (can be enabled via configuration)
        props.put("mail.debug", "false");

        // Create session with authenticator if auth is enabled
        if (authEnabled) {
            mailSession = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });
        } else {
            mailSession = Session.getInstance(props);
        }

        mailSession.setDebug(false);
    }

    @Override
    public boolean sendEmail(String to, String subject, String content) {
        try {
            Message message = createMessage(to, subject);
            message.setText(content);
            Transport.send(message);
            logger.info("Email sent successfully to: {}", to);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new EmailSendingException("Failed to send email: " + e.getMessage(), to, e);
        }
    }

    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            Message message = createMessage(to, subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            Transport.send(message);
            logger.info("HTML email sent successfully to: {}", to);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send HTML email to {}: {}", to, e.getMessage());
            throw new EmailSendingException("Failed to send HTML email: " + e.getMessage(), to, e);
        }
    }

    @Override
    public boolean sendEmailWithAttachments(String to, String subject, String content,
                                           boolean isHtml, List<EmailAttachment> attachments) {
        try {
            Message message = createMessage(to, subject);

            // Create multipart message
            Multipart multipart = new MimeMultipart();

            // Add main content
            MimeBodyPart contentPart = new MimeBodyPart();
            if (isHtml) {
                contentPart.setContent(content, "text/html; charset=UTF-8");
            } else {
                contentPart.setText(content);
            }
            multipart.addBodyPart(contentPart);

            // Add attachments
            for (EmailAttachment attachment : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource dataSource = new ByteArrayDataSource(
                    attachment.content(),
                    attachment.contentType()
                );
                attachmentPart.setDataHandler(new DataHandler(dataSource));
                try {
                    attachmentPart.setFileName(MimeUtility.encodeText(attachment.filename()));
                } catch (java.io.UnsupportedEncodingException e) {
                    attachmentPart.setFileName(attachment.filename());
                }
                attachmentPart.setDisposition(attachment.disposition());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);
            logger.info("Email with {} attachments sent successfully to: {}", attachments.size(), to);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send email with attachments to {}: {}", to, e.getMessage());
            throw new EmailSendingException("Failed to send email with attachments: " + e.getMessage(), to, e);
        }
    }

    @Override
    public boolean sendTemplatedEmail(String to, String templateName, Map<String, Object> variables) {
        // For template processing, you would typically integrate with a template engine
        // like Thymeleaf, FreeMarker, or Handlebars
        // For now, we'll create a simple template substitution

        String template = getTemplate(templateName);
        String content = substituteTemplateVariables(template, variables);

        return sendHtmlEmail(to, "Your Requested Information", content);
    }

    @Override
    public int sendBulkEmails(List<String> recipients, String subject, String content, boolean isHtml) {
        if (!configured) {
            logger.warn("SMTP not configured, skipping bulk email send");
            return 0;
        }

        int successCount = 0;
        int rateLimitDelayMs = 100; // 100ms between emails to avoid rate limiting

        for (String recipient : recipients) {
            try {
                if (isHtml) {
                    sendHtmlEmail(recipient, subject, content);
                } else {
                    sendEmail(recipient, subject, content);
                }
                successCount++;

                // Rate limiting delay
                if (rateLimitDelayMs > 0) {
                    Thread.sleep(rateLimitDelayMs);
                }
            } catch (EmailSendingException e) {
                logger.error("Failed to send bulk email to {}: {}", recipient, e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Bulk email sending interrupted");
                break;
            }
        }

        logger.info("Bulk email sending completed: {}/{} successful", successCount, recipients.size());
        return successCount;
    }

    @Override
    public boolean isHealthy() {
        if (!configured) {
            return false;
        }

        try {
            // Test connectivity by connecting to the SMTP server
            Transport transport = mailSession.getTransport("smtp");
            transport.connect();
            boolean connected = transport.isConnected();
            transport.close();
            return connected;
        } catch (MessagingException e) {
            logger.warn("SMTP health check failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getProvider() {
        return "smtp";
    }

    private Message createMessage(String to, String subject) throws MessagingException {
        Message message = new MimeMessage(mailSession);
        try {
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(MimeUtility.encodeText(subject));
        } catch (java.io.UnsupportedEncodingException e) {
            // Fall back to non-encoded subject
            try {
                message.setFrom(new InternetAddress(fromEmail, fromName));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
            } catch (Exception ex) {
                throw new MessagingException("Failed to create message", ex);
            }
        }
        message.setSentDate(new java.util.Date());
        return message;
    }

    private String getTemplate(String templateName) {
        // In a real implementation, this would load templates from:
        // - Database
        // - File system
        // - Classpath resources
        // - Template service

        return switch (templateName) {
            case "verification" -> """
                <html>
                <body>
                    <h2>Email Verification</h2>
                    <p>Please click the link below to verify your email:</p>
                    <p><a href="${verificationUrl}">Verify Email</a></p>
                    <p>This link expires in 24 hours.</p>
                </body>
                </html>
                """;
            case "password-reset" -> """
                <html>
                <body>
                    <h2>Password Reset</h2>
                    <p>Click the link below to reset your password:</p>
                    <p><a href="${resetUrl}">Reset Password</a></p>
                    <p>This link expires in 1 hour.</p>
                </body>
                </html>
                """;
            case "welcome" -> """
                <html>
                <body>
                    <h2>Welcome to Our Service!</h2>
                    <p>Dear ${userName},</p>
                    <p>Welcome to our platform! We're excited to have you.</p>
                    <p>Click <a href="${dashboardUrl}">here</a> to access your dashboard.</p>
                </body>
                </html>
                """;
            default -> """
                <html>
                <body>
                    <p>${content}</p>
                </body>
                </html>
                """;
        };
    }

    private String substituteTemplateVariables(String template, Map<String, Object> variables) {
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    /**
     * Simple byte array data source for attachments.
     */
    private static class ByteArrayDataSource implements DataSource {
        private final byte[] data;
        private final String contentType;

        public ByteArrayDataSource(byte[] data, String contentType) {
            this.data = data;
            this.contentType = contentType;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Cannot write to this read-only data source");
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public String getName() {
            return "ByteArrayDataSource";
        }
    }
}
