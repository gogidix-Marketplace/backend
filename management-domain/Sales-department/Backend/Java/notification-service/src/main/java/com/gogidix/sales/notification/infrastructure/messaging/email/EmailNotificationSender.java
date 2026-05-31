package com.gogidix.sales.notification.infrastructure.messaging.email;

import com.gogidix.sales.notification.domain.model.Notification;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Email Notification Sender
 * Sends notifications via email
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationSender {

    private final JavaMailSender mailSender;

    @Value("${notification-service.channels.email.enabled:true}")
    private boolean enabled;

    @Value("${notification-service.channels.email.from:noreply@gogidix.com}")
    private String fromEmail;

    @Value("${notification-service.channels.email.from-name:Gogidix Notifications}")
    private String fromName;

    public String send(Notification notification) {
        if (!enabled) {
            throw new IllegalStateException("Email notifications are disabled");
        }

        log.info("Sending email notification: {} to {} recipients",
                notification.getNotificationId(),
                notification.getRecipients() != null ? notification.getRecipients().size() : 0);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setSubject(notification.getSubject() != null ? notification.getSubject() : "Notification");

            // Set recipients
            if (notification.getRecipients() != null) {
                String[] toAddresses = notification.getRecipients().stream()
                        .filter(r -> r.getEmailAddress() != null)
                        .map(Notification.RecipientInfo::getEmailAddress)
                        .toArray(String[]::new);
                helper.setTo(toAddresses);
            }

            // Set content
            if (notification.getHtmlContent() != null) {
                helper.setText(notification.getHtmlContent(), true);
            } else {
                helper.setText(notification.getContent() != null ? notification.getContent() : "");
            }

            mailSender.send(message);

            String messageId = message.getMessageID();
            log.info("Email sent successfully: {}", messageId);
            return messageId;

        } catch (Exception e) {
            log.error("Failed to send email notification: {}", notification.getNotificationId(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
