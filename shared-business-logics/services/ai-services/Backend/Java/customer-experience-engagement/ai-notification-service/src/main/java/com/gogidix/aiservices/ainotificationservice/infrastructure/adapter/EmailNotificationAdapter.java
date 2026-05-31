package com.gogidix.aiservices.ainotificationservice.infrastructure.adapter;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationSenderPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EmailNotificationAdapter implements NotificationSenderPort {

    @Override
    public boolean send(Notification notification) {
        if (notification.getType() != NotificationType.EMAIL) {
            return false;
        }
        try {
            log.info("Sending email to {}: {}", notification.getRecipientId(), notification.getSubject());
            return true;
        } catch (Exception e) {
            log.error("Failed to send email", e);
            return false;
        }
    }

    @Override
    public List<Notification> sendBatch(List<Notification> notifications) {
        return notifications.stream()
                .filter(n -> n.getType() == NotificationType.EMAIL)
                .filter(this::send)
                .toList();
    }

    @Override
    public boolean markAsDelivered(String notificationId) {
        log.info("Email {} marked as delivered", notificationId);
        return true;
    }

    @Override
    public boolean validateRecipient(String recipientId, String recipientType) {
        return recipientId != null && recipientId.contains("@");
    }
}
