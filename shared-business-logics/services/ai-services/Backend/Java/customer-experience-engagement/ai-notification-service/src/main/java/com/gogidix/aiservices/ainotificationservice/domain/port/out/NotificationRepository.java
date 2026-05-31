package com.gogidix.aiservices.ainotificationservice.domain.port.out;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);

    Optional<Notification> findById(String notificationId);

    List<Notification> findByRecipientId(String recipientId);

    List<Notification> findByStatus(String status);

    void delete(String notificationId);

    List<Notification> findPendingNotifications(int limit);
}
