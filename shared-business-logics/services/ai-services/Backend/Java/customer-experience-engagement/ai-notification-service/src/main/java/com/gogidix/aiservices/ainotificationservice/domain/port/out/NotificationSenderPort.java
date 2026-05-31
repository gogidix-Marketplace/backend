package com.gogidix.aiservices.ainotificationservice.domain.port.out;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;

import java.util.List;

public interface NotificationSenderPort {
    boolean send(Notification notification);

    List<Notification> sendBatch(List<Notification> notifications);

    boolean markAsDelivered(String notificationId);

    boolean validateRecipient(String recipientId, String recipientType);
}
