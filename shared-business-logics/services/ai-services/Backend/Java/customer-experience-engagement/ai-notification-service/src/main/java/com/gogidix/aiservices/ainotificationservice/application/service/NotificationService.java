package com.gogidix.aiservices.ainotificationservice.application.service;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationStatus;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationRepository;
import com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationSenderPort;
import com.gogidix.aiservices.ainotificationservice.domain.policy.NotificationPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderPort notificationSender;
    private final EventPublisherPort eventPublisher;
    private final NotificationPolicy policy;

    public Notification sendNotification(String recipientId, NotificationType type,
                                       String subject, String content) {
        Notification notification = Notification.create(recipientId, type, subject, content);
        notificationRepository.save(notification);

        if (policy.canSendNow(notification)) {
            boolean sent = notificationSender.send(notification);
            if (sent) {
                notification.markAsSent();
                eventPublisher.publishNotificationSent(notification.getNotificationId(), recipientId);
            } else {
                notification.markAsFailed("Failed to send");
            }
            notificationRepository.save(notification);
        }

        return notification;
    }

    public Notification getNotification(String notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    }

    public List<Notification> getUserNotifications(String recipientId) {
        return notificationRepository.findByRecipientId(recipientId);
    }

    public void cancelNotification(String notificationId) {
        Notification notification = getNotification(notificationId);
        if (!notification.isSent()) {
            notification.markAsCancelled();
            notificationRepository.save(notification);
        }
    }

    public List<Notification> processPendingNotifications() {
        List<Notification> pending = notificationRepository.findPendingNotifications(100);

        for (Notification notification : pending) {
            if (policy.canSendNow(notification)) {
                boolean sent = notificationSender.send(notification);
                if (sent) {
                    notification.markAsSent();
                    eventPublisher.publishNotificationSent(
                            notification.getNotificationId(),
                            notification.getRecipientId()
                    );
                } else {
                    notification.markAsFailed("Delivery failed");
                    eventPublisher.publishNotificationFailed(
                            notification.getNotificationId(),
                            notification.getRecipientId(),
                            "Delivery failed"
                    );
                }
                notificationRepository.save(notification);
            }
        }

        return pending;
    }
}
