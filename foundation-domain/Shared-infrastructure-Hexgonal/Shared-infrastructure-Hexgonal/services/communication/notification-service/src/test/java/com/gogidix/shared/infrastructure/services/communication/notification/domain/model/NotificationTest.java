package com.gogidix.shared.infrastructure.services.communication.notification.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Notification domain model.
 */
@DisplayName("Notification Domain Model Tests")
class NotificationTest {

    @Test
    @DisplayName("Should create notification with builder")
    void shouldCreateNotificationWithBuilder() {
        Notification notification = Notification.builder()
                .userId("user123")
                .type(Notification.NotificationType.WELCOME)
                .channel(Notification.NotificationChannel.EMAIL)
                .recipient("test@example.com")
                .subject("Welcome")
                .body("Welcome to our service")
                .status(Notification.NotificationStatus.PENDING)
                .retryCount(0)
                .build();

        assertNotNull(notification);
        assertEquals("user123", notification.getUserId());
        assertEquals(Notification.NotificationType.WELCOME, notification.getType());
        assertEquals(Notification.NotificationChannel.EMAIL, notification.getChannel());
        assertEquals("test@example.com", notification.getRecipient());
        assertEquals("Welcome", notification.getSubject());
        assertEquals("Welcome to our service", notification.getBody());
        assertEquals(Notification.NotificationStatus.PENDING, notification.getStatus());
        assertEquals(0, notification.getRetryCount());
    }

    @Test
    @DisplayName("Should mark notification as sent")
    void shouldMarkNotificationAsSent() {
        Notification notification = Notification.builder()
                .status(Notification.NotificationStatus.PENDING)
                .build();

        notification.markAsSent();

        assertEquals(Notification.NotificationStatus.SENT, notification.getStatus());
        assertNotNull(notification.getSentAt());
    }

    @Test
    @DisplayName("Should mark notification as failed with error message")
    void shouldMarkNotificationAsFailed() {
        Notification notification = Notification.builder()
                .status(Notification.NotificationStatus.SENDING)
                .build();

        notification.markAsFailed("Connection timeout");

        assertEquals(Notification.NotificationStatus.FAILED, notification.getStatus());
        assertEquals("Connection timeout", notification.getErrorMessage());
    }

    @Test
    @DisplayName("Should increment retry count and set status to retrying")
    void shouldIncrementRetryCount() {
        Notification notification = Notification.builder()
                .status(Notification.NotificationStatus.FAILED)
                .retryCount(0)
                .build();

        notification.incrementRetryCount();

        assertEquals(1, notification.getRetryCount());
        assertEquals(Notification.NotificationStatus.RETRYING, notification.getStatus());
    }

    @Test
    @DisplayName("Should increment retry count from existing value")
    void shouldIncrementRetryCountFromExistingValue() {
        Notification notification = Notification.builder()
                .status(Notification.NotificationStatus.FAILED)
                .retryCount(2)
                .build();

        notification.incrementRetryCount();

        assertEquals(3, notification.getRetryCount());
        assertEquals(Notification.NotificationStatus.RETRYING, notification.getStatus());
    }

    @Test
    @DisplayName("Should handle null retry count on increment")
    void shouldHandleNullRetryCountOnIncrement() {
        Notification notification = Notification.builder()
                .status(Notification.NotificationStatus.FAILED)
                .retryCount(null)
                .build();

        notification.incrementRetryCount();

        assertEquals(1, notification.getRetryCount());
        assertEquals(Notification.NotificationStatus.RETRYING, notification.getStatus());
    }

    @Test
    @DisplayName("Should verify all notification types")
    void shouldVerifyAllNotificationTypes() {
        assertEquals(10, Notification.NotificationType.values().length);
        assertEquals(Notification.NotificationType.WELCOME, Notification.NotificationType.valueOf("WELCOME"));
        assertEquals(Notification.NotificationType.PASSWORD_RESET, Notification.NotificationType.valueOf("PASSWORD_RESET"));
        assertEquals(Notification.NotificationType.EMAIL_VERIFICATION, Notification.NotificationType.valueOf("EMAIL_VERIFICATION"));
        assertEquals(Notification.NotificationType.ACCOUNT_LOCKED, Notification.NotificationType.valueOf("ACCOUNT_LOCKED"));
        assertEquals(Notification.NotificationType.ACCOUNT_UNLOCKED, Notification.NotificationType.valueOf("ACCOUNT_UNLOCKED"));
        assertEquals(Notification.NotificationType.INVITATION, Notification.NotificationType.valueOf("INVITATION"));
        assertEquals(Notification.NotificationType.ALERT, Notification.NotificationType.valueOf("ALERT"));
        assertEquals(Notification.NotificationType.MARKETING, Notification.NotificationType.valueOf("MARKETING"));
        assertEquals(Notification.NotificationType.TRANSACTIONAL, Notification.NotificationType.valueOf("TRANSACTIONAL"));
        assertEquals(Notification.NotificationType.SYSTEM, Notification.NotificationType.valueOf("SYSTEM"));
    }

    @Test
    @DisplayName("Should verify all notification channels")
    void shouldVerifyAllNotificationChannels() {
        assertEquals(4, Notification.NotificationChannel.values().length);
        assertEquals(Notification.NotificationChannel.EMAIL, Notification.NotificationChannel.valueOf("EMAIL"));
        assertEquals(Notification.NotificationChannel.SMS, Notification.NotificationChannel.valueOf("SMS"));
        assertEquals(Notification.NotificationChannel.PUSH, Notification.NotificationChannel.valueOf("PUSH"));
        assertEquals(Notification.NotificationChannel.IN_APP, Notification.NotificationChannel.valueOf("IN_APP"));
    }

    @Test
    @DisplayName("Should verify all notification statuses")
    void shouldVerifyAllNotificationStatuses() {
        assertEquals(6, Notification.NotificationStatus.values().length);
        assertEquals(Notification.NotificationStatus.PENDING, Notification.NotificationStatus.valueOf("PENDING"));
        assertEquals(Notification.NotificationStatus.SENDING, Notification.NotificationStatus.valueOf("SENDING"));
        assertEquals(Notification.NotificationStatus.SENT, Notification.NotificationStatus.valueOf("SENT"));
        assertEquals(Notification.NotificationStatus.FAILED, Notification.NotificationStatus.valueOf("FAILED"));
        assertEquals(Notification.NotificationStatus.RETRYING, Notification.NotificationStatus.valueOf("RETRYING"));
        assertEquals(Notification.NotificationStatus.CANCELLED, Notification.NotificationStatus.valueOf("CANCELLED"));
    }

    @Test
    @DisplayName("Should create notification with all fields")
    void shouldCreateNotificationWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> templateVars = Map.of("name", "John", "code", "123456");

        Notification notification = Notification.builder()
                .id("notif123")
                .userId("user123")
                .type(Notification.NotificationType.EMAIL_VERIFICATION)
                .channel(Notification.NotificationChannel.EMAIL)
                .recipient("john@example.com")
                .subject("Verify your email")
                .body("Please verify your email")
                .templateId("verification-template")
                .templateVariables(templateVars)
                .status(Notification.NotificationStatus.PENDING)
                .errorMessage(null)
                .retryCount(0)
                .scheduledAt(now)
                .sentAt(null)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals("notif123", notification.getId());
        assertEquals("verification-template", notification.getTemplateId());
        assertEquals(templateVars, notification.getTemplateVariables());
        assertEquals(now, notification.getScheduledAt());
        assertNull(notification.getSentAt());
    }

    @Test
    @DisplayName("Should create notification with no-args constructor")
    void shouldCreateNotificationWithNoArgsConstructor() {
        Notification notification = new Notification();

        assertNotNull(notification);
        assertNull(notification.getUserId());
        assertNull(notification.getType());
        assertNull(notification.getChannel());
        assertNull(notification.getRecipient());
    }

    @Test
    @DisplayName("Should create notification with all-args constructor")
    void shouldCreateNotificationWithAllArgsConstructor() {
        Notification notification = new Notification();
        notification.setUserId("user123");
        notification.setType(Notification.NotificationType.ALERT);
        notification.setChannel(Notification.NotificationChannel.SMS);
        notification.setRecipient("+1234567890");
        notification.setBody("Alert message");

        assertEquals("user123", notification.getUserId());
        assertEquals(Notification.NotificationType.ALERT, notification.getType());
        assertEquals(Notification.NotificationChannel.SMS, notification.getChannel());
        assertEquals("+1234567890", notification.getRecipient());
        assertEquals("Alert message", notification.getBody());
    }
}
