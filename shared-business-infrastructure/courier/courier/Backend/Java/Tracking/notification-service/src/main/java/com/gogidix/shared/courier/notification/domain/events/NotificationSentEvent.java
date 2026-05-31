package com.gogidix.shared.courier.notification.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event fired when notification is sent
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSentEvent {

    private String tenantId;
    private String notificationId;
    private String recipientId;
    private String recipientType;
    private String notificationType;
    private String channel;
    private LocalDateTime sentAt;
}
