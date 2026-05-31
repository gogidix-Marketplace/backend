package com.gogidix.hr.notification.domain.enums;

/**
 * NotificationStatus enum for notification lifecycle
 */
public enum NotificationStatus {
    DRAFT,
    SCHEDULED,
    SENT,
    DELIVERED,
    READ,
    FAILED,
    ARCHIVED,
    CANCELLED
}
