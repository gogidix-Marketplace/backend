package com.gogidix.shared.infrastructure.services.communication.notification.domain.port.in;

import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.request.SendNotificationRequestDto;
import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.response.NotificationResponseDto;

import java.util.List;

/**
 * Notification use case port.
 */
public interface NotificationPort {

    /**
     * Sends a notification.
     */
    NotificationResponseDto sendNotification(SendNotificationRequestDto request);

    /**
     * Gets notification by ID.
     */
    NotificationResponseDto getNotification(String notificationId);

    /**
     * Lists notifications for current tenant.
     */
    List<NotificationResponseDto> listNotifications(String userId);

    /**
     * Retries a failed notification.
     */
    NotificationResponseDto retryNotification(String notificationId);

    /**
     * Cancels a pending notification.
     */
    void cancelNotification(String notificationId);
}
