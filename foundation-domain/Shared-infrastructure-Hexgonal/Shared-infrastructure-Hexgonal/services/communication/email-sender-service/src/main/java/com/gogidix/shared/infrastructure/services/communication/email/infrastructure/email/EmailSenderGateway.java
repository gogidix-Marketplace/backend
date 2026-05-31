package com.gogidix.shared.infrastructure.services.communication.email.infrastructure.email;

import java.util.List;
import java.util.Map;

/**
 * Email sender gateway interface for sending emails.
 *
 * <p>This abstraction supports multiple email service providers:
 * <ul>
 *   <li>SMTP (JavaMail)</li>
 *   <li>SendGrid</li>
 *   <li>AWS SES</li>
 *   <li>Mailgun</li>
 *   <li>SparkPost</li>
 * </ul>
 *
 * <p>Implementation is selected via configuration:
 * <pre>
 * email.provider=smtp|sendgrid|aws-ses|mailgun
 * email.smtp.host=smtp.gmail.com
 * email.smtp.port=587
 * email.smtp.username=your-email@gmail.com
 * email.smtp.password=your-app-password
 * email.smtp.auth=true
 * email.smtp.starttls=true
 * email.from=noreply@yourdomain.com
 * email.from.name=Your Application
 * </pre>
 */
public interface EmailSenderGateway {

    /**
     * Send a plain text email.
     *
     * @param to the recipient email address
     * @param subject the email subject
     * @param content the plain text content
     * @return true if sent successfully
     * @throws EmailSendingException if sending fails
     */
    boolean sendEmail(String to, String subject, String content);

    /**
     * Send an HTML email.
     *
     * @param to the recipient email address
     * @param subject the email subject
     * @param htmlContent the HTML content
     * @return true if sent successfully
     * @throws EmailSendingException if sending fails
     */
    boolean sendHtmlEmail(String to, String subject, String htmlContent);

    /**
     * Send an email with attachments.
     *
     * @param to the recipient email address
     * @param subject the email subject
     * @param content the email content
     * @param isHtml whether content is HTML
     * @param attachments list of attachment metadata
     * @return true if sent successfully
     * @throws EmailSendingException if sending fails
     */
    boolean sendEmailWithAttachments(String to, String subject, String content,
                                     boolean isHtml, List<EmailAttachment> attachments);

    /**
     * Send a templated email.
     *
     * @param to the recipient email address
     * @param templateName the template name
     * @param variables the template variables
     * @return true if sent successfully
     * @throws EmailSendingException if sending fails
     */
    boolean sendTemplatedEmail(String to, String templateName, Map<String, Object> variables);

    /**
     * Send bulk emails to multiple recipients.
     *
     * @param recipients list of recipient email addresses
     * @param subject the email subject
     * @param content the email content
     * @param isHtml whether content is HTML
     * @return number of successfully sent emails
     */
    int sendBulkEmails(List<String> recipients, String subject, String content, boolean isHtml);

    /**
     * Check if the email service is healthy/configured.
     *
     * @return true if service is operational
     */
    boolean isHealthy();

    /**
     * Get the provider name.
     *
     * @return the email provider (e.g., "smtp", "sendgrid", "aws-ses")
     */
    String getProvider();

    /**
     * Email attachment metadata.
     */
    record EmailAttachment(
        String filename,
        byte[] content,
        String contentType,
        String disposition
    ) {
        public EmailAttachment(String filename, byte[] content, String contentType) {
            this(filename, content, contentType, "attachment");
        }
    }

    /**
     * Exception thrown when email sending fails.
     */
    class EmailSendingException extends RuntimeException {
        private final String recipient;
        private final String errorCode;

        public EmailSendingException(String message, String recipient) {
            super(message);
            this.recipient = recipient;
            this.errorCode = null;
        }

        public EmailSendingException(String message, String recipient, String errorCode) {
            super(message);
            this.recipient = recipient;
            this.errorCode = errorCode;
        }

        public EmailSendingException(String message, String recipient, Throwable cause) {
            super(message, cause);
            this.recipient = recipient;
            this.errorCode = null;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }
}
