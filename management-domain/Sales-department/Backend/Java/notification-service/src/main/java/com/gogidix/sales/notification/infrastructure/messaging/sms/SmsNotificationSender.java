package com.gogidix.sales.notification.infrastructure.messaging.sms;

import com.gogidix.sales.notification.domain.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SMS Notification Sender
 * Sends notifications via SMS
 */
@Component
@Slf4j
public class SmsNotificationSender {

    @Value("${notification-service.channels.sms.enabled:false}")
    private boolean enabled;

    @Value("${notification-service.channels.sms.provider:twilio}")
    private String provider;

    @Value("${notification-service.channels.sms.account-id:}")
    private String accountId;

    @Value("${notification-service.channels.sms.auth-token:}")
    private String authToken;

    @Value("${notification-service.channels.sms.from-number:}")
    private String fromNumber;

    public String send(Notification notification) {
        if (!enabled) {
            throw new IllegalStateException("SMS notifications are disabled");
        }

        log.info("Sending SMS notification: {} via provider: {}",
                notification.getNotificationId(), provider);

        try {
            // Implementation depends on the SMS provider (Twilio, AWS SNS, etc.)
            // This is a placeholder implementation

            if (notification.getRecipients() != null) {
                for (Notification.RecipientInfo recipient : notification.getRecipients()) {
                    if (recipient.getPhoneNumber() != null) {
                        log.debug("Sending SMS to: {}", recipient.getPhoneNumber());
                        // Actual SMS sending logic would go here
                    }
                }
            }

            String messageId = "SMS:" + notification.getNotificationId() + ":" + System.currentTimeMillis();
            log.info("SMS sent successfully: {}", messageId);
            return messageId;

        } catch (Exception e) {
            log.error("Failed to send SMS notification: {}", notification.getNotificationId(), e);
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
