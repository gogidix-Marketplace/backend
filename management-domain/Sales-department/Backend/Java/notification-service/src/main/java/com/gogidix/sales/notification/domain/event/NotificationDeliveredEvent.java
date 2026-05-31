package com.gogidix.sales.notification.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDeliveredEvent {

    private String notificationId;
    private String tenantId;
    private String userId;
    private String channel;
    private String externalMessageId;
    private Instant deliveredAt;
    private String eventType;
}
