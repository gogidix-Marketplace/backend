package com.gogidix.shared.courier.notification.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Query object for notifications
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQuery {

    private String tenantId;
    private String recipientId;
    private String recipientType;
    private String notificationType;
    private NotificationStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;

    public enum NotificationStatus {
        SENT,
        DELIVERED,
        READ,
        FAILED
    }
}
