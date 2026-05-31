package com.gogidix.shared.infrastructure.services.communication.notification.infrastructure.sender;

import com.gogidix.shared.infrastructure.services.communication.notification.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Notification sender component.
 * <p>
 * Sends notifications via different channels (email, SMS, push).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender {

    private final JavaMailSender mailSender;

    /**
     * Sends a notification based on its channel.
     *
     * @param notification the notification to send
     */
    public void send(Notification notification) {
        switch (notification.getChannel()) {
            case EMAIL -> sendEmail(notification);
            case SMS -> sendSms(notification);
            case PUSH -> sendPush(notification);
            case IN_APP -> sendInApp(notification);
        }
    }

    /**
     * Sends an email notification.
     */
    private void sendEmail(Notification notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getRecipient());
            message.setSubject(notification.getSubject());
            message.setText(notification.getBody());

            mailSender.send(message);

            notification.markAsSent();
            log.info("Email sent successfully to: {}", notification.getRecipient());

        } catch (Exception e) {
            log.error("Failed to send email to: {}", notification.getRecipient(), e);
            notification.markAsFailed(e.getMessage());
        }
    }

    /**
     * Sends an SMS notification.
     */
    private void sendSms(Notification notification) {
        // TODO: Integrate with SMS provider (Twilio, AWS SNS, etc.)
        log.info("SMS sending not yet implemented for: {}", notification.getRecipient());
    }

    /**
     * Sends a push notification.
     */
    private void sendPush(Notification notification) {
        // TODO: Integrate with push notification service (Firebase, OneSignal, etc.)
        log.info("Push notification sending not yet implemented for: {}", notification.getRecipient());
    }

    /**
     * Sends an in-app notification.
     */
    private void sendInApp(Notification notification) {
        // TODO: Store in-app notification for user to fetch
        log.info("In-app notification stored for user: {}", notification.getUserId());
    }
}
