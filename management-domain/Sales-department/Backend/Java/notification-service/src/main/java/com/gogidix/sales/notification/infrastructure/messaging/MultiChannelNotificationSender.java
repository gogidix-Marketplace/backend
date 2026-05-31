package com.gogidix.sales.notification.infrastructure.messaging;

import com.gogidix.sales.notification.domain.model.Notification;
import com.gogidix.sales.notification.domain.port.out.NotificationSender;
import com.gogidix.sales.notification.infrastructure.messaging.email.EmailNotificationSender;
import com.gogidix.sales.notification.infrastructure.messaging.push.PushNotificationSender;
import com.gogidix.sales.notification.infrastructure.messaging.sms.SmsNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Multi-Channel Notification Sender
 * Delegates notification sending to channel-specific implementations
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MultiChannelNotificationSender implements NotificationSender {

    private final EmailNotificationSender emailSender;
    private final SmsNotificationSender smsSender;
    private final PushNotificationSender pushSender;

    private volatile boolean ready = true;

    @Override
    public String send(Notification notification) {
        log.info("Sending notification: {} via channel: {}",
                notification.getNotificationId(), notification.getChannel());

        try {
            return switch (notification.getChannel()) {
                case EMAIL -> emailSender.send(notification);
                case SMS -> smsSender.send(notification);
                case PUSH_NOTIFICATION -> pushSender.send(notification);
                case IN_APP -> sendInApp(notification);
                case WEBHOOK -> sendWebhook(notification);
            };
        } catch (Exception e) {
            log.error("Failed to send notification: {}", notification.getNotificationId(), e);
            ready = false;
            throw e;
        }
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public boolean isChannelEnabled(String channel) {
        return switch (channel.toLowerCase()) {
            case "email" -> emailSender.isEnabled();
            case "sms" -> smsSender.isEnabled();
            case "push" -> pushSender.isEnabled();
            case "in_app" -> true;
            case "webhook" -> true;
            default -> false;
        };
    }

    private String sendInApp(Notification notification) {
        log.debug("Sending in-app notification: {}", notification.getNotificationId());
        // In-app notifications are stored and retrieved via queries
        // Return a simple ID for tracking
        return "IN_APP:" + notification.getNotificationId();
    }

    private String sendWebhook(Notification notification) {
        log.debug("Sending webhook notification: {}", notification.getNotificationId());
        // Webhook notifications would be sent to configured URLs
        // Implementation depends on webhook configuration
        return "WEBHOOK:" + notification.getNotificationId();
    }
}
