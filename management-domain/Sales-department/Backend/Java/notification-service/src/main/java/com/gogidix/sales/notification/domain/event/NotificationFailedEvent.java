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
public class NotificationFailedEvent {

    private String notificationId;
    private String tenantId;
    private String userId;
    private String channel;
    private String errorMessage;
    private Integer retryCount;
    private String eventType;
    private Instant timestamp;
}
