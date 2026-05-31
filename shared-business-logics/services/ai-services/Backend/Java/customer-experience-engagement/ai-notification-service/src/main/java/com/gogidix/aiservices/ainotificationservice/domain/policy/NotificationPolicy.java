package com.gogidix.aiservices.ainotificationservice.domain.policy;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationPriority;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NotificationPolicy {
    private static final int MAX_CONTENT_LENGTH = 5000;
    private static final int MAX_SUBJECT_LENGTH = 200;
    private static final int MAX_RETRIES = 3;
    private static final long MIN_SEND_INTERVAL_SECONDS = 5;

    public void validateNotification(Notification notification) {
        if (notification.getContent() != null &&
            notification.getContent().length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Content exceeds maximum length of " + MAX_CONTENT_LENGTH);
        }
        if (notification.getSubject() != null &&
            notification.getSubject().length() > MAX_SUBJECT_LENGTH) {
            throw new IllegalArgumentException("Subject exceeds maximum length of " + MAX_SUBJECT_LENGTH);
        }
    }

    public boolean canSendNow(Notification notification) {
        if (notification.isScheduled()) {
            return notification.getScheduledFor().isBefore(Instant.now()) ||
                   notification.getScheduledFor().equals(Instant.now());
        }
        return !notification.isExpired();
    }

    public boolean shouldRetry(Notification notification) {
        return notification.canRetry() && notification.getRetryCount() < MAX_RETRIES;
    }

    public int calculateRetryDelay(int attemptNumber) {
        return (int) Math.min(300, Math.pow(2, attemptNumber) * 5);
    }

    public List<Notification> prioritizeNotifications(List<Notification> notifications) {
        return notifications.stream()
                .sorted((a, b) -> {
                    int priorityCompare = b.getPriority().compareTo(a.getPriority());
                    if (priorityCompare != 0) {
                        return priorityCompare;
                    }
                    return a.getCreatedAt().compareTo(b.getCreatedAt());
                })
                .toList();
    }

    public boolean rateLimitAllowed(String recipientId, Instant lastSentTime) {
        if (lastSentTime == null) {
            return true;
        }
        long secondsSinceLastSend = ChronoUnit.SECONDS.between(lastSentTime, Instant.now());
        return secondsSinceLastSend >= MIN_SEND_INTERVAL_SECONDS;
    }

    public boolean requiresTemplate(NotificationType type) {
        return type == NotificationType.EMAIL;
    }

    public int getMaxRetries() {
        return MAX_RETRIES;
    }

    public boolean isHighPriority(Notification notification) {
        return notification.getPriority() == NotificationPriority.HIGH ||
               notification.getPriority() == NotificationPriority.URGENT;
    }
}
