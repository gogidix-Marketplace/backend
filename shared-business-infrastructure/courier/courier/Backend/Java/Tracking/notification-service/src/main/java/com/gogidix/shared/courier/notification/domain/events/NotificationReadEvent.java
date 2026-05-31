package com.gogidix.shared.courier.notification.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event fired when notification is read
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReadEvent {

    private String tenantId;
    private String notificationId;
    private String recipientId;
    private LocalDateTime readAt;
}
