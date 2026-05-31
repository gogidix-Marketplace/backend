package com.gogidix.shared.infrastructure.services.communication.notification.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.request.SendNotificationRequestDto;
import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.response.NotificationResponseDto;
import com.gogidix.shared.infrastructure.services.communication.notification.domain.model.Notification;
import com.gogidix.shared.infrastructure.services.communication.notification.infrastructure.sender.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NotificationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Notification Service Tests")
class NotificationServiceTest {

    @Mock
    private NotificationSender notificationSender;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private NotificationService notificationService;

    private static final String TENANT_ID = "tenant123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
    }

    @Test
    @DisplayName("Should send notification successfully")
    void shouldSendNotificationSuccessfully() {
        SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                .userId("user123")
                .type(Notification.NotificationType.WELCOME)
                .channel(Notification.NotificationChannel.EMAIL)
                .recipient("test@example.com")
                .subject("Welcome")
                .body("Welcome to our service")
                .build();

        doNothing().when(notificationSender).send(any(Notification.class));

        NotificationResponseDto response = notificationService.sendNotification(request);

        assertNotNull(response);
        assertEquals("user123", response.getUserId());
        assertEquals(Notification.NotificationType.WELCOME, response.getType());
        assertEquals(Notification.NotificationChannel.EMAIL, response.getChannel());
        assertEquals("test@example.com", response.getRecipient());
        assertEquals("Welcome", response.getSubject());
        assertEquals("Welcome to our service", response.getBody());
        assertEquals(Notification.NotificationStatus.PENDING, response.getStatus());

        verify(notificationSender, times(1)).send(any(Notification.class));
    }

    @Test
    @DisplayName("Should send notification with template variables")
    void shouldSendNotificationWithTemplateVariables() {
        Map<String, Object> templateVars = Map.of("name", "John", "code", "123456");

        SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                .userId("user123")
                .type(Notification.NotificationType.EMAIL_VERIFICATION)
                .channel(Notification.NotificationChannel.EMAIL)
                .recipient("john@example.com")
                .subject("Verify Email")
                .body("Please verify")
                .templateId("verify-template")
                .templateVariables(templateVars)
                .build();

        doNothing().when(notificationSender).send(any(Notification.class));

        NotificationResponseDto response = notificationService.sendNotification(request);

        assertNotNull(response);
        assertEquals("verify-template", response.getTemplateId());
        assertEquals(templateVars, response.getTemplateVariables());
    }

    @Test
    @DisplayName("Should send SMS notification")
    void shouldSendSmsNotification() {
        SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                .userId("user123")
                .type(Notification.NotificationType.PASSWORD_RESET)
                .channel(Notification.NotificationChannel.SMS)
                .recipient("+1234567890")
                .body("Your reset code is 123456")
                .build();

        doNothing().when(notificationSender).send(any(Notification.class));

        NotificationResponseDto response = notificationService.sendNotification(request);

        assertNotNull(response);
        assertEquals(Notification.NotificationChannel.SMS, response.getChannel());
        assertEquals("+1234567890", response.getRecipient());
    }

    @Test
    @DisplayName("Should send push notification")
    void shouldSendPushNotification() {
        SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                .userId("user123")
                .type(Notification.NotificationType.ALERT)
                .channel(Notification.NotificationChannel.PUSH)
                .recipient("device_token_123")
                .subject("New Alert")
                .body("You have a new notification")
                .build();

        doNothing().when(notificationSender).send(any(Notification.class));

        NotificationResponseDto response = notificationService.sendNotification(request);

        assertNotNull(response);
        assertEquals(Notification.NotificationChannel.PUSH, response.getChannel());
    }

    @Test
    @DisplayName("Should send in-app notification")
    void shouldSendInAppNotification() {
        SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                .userId("user123")
                .type(Notification.NotificationType.TRANSACTIONAL)
                .channel(Notification.NotificationChannel.IN_APP)
                .recipient("user123")
                .subject("Payment Received")
                .body("Your payment was successful")
                .build();

        doNothing().when(notificationSender).send(any(Notification.class));

        NotificationResponseDto response = notificationService.sendNotification(request);

        assertNotNull(response);
        assertEquals(Notification.NotificationChannel.IN_APP, response.getChannel());
    }

    @Test
    @DisplayName("Should handle notification with scheduled time")
    void shouldHandleNotificationWithScheduledTime() {
        LocalDateTime scheduledTime = LocalDateTime.now().plusHours(1);

        SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                .userId("user123")
                .type(Notification.NotificationType.MARKETING)
                .channel(Notification.NotificationChannel.EMAIL)
                .recipient("marketing@example.com")
                .subject("Special Offer")
                .body("Check out our deals")
                .build();

        Notification notificationToSend = Notification.builder()
                .userId("user123")
                .type(Notification.NotificationType.MARKETING)
                .channel(Notification.NotificationChannel.EMAIL)
                .recipient("marketing@example.com")
                .subject("Special Offer")
                .body("Check out our deals")
                .status(Notification.NotificationStatus.PENDING)
                .retryCount(0)
                .scheduledAt(scheduledTime)
                .build();

        doNothing().when(notificationSender).send(any(Notification.class));

        NotificationResponseDto response = notificationService.sendNotification(request);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should return null for getNotification (not implemented)")
    void shouldReturnNullForGetNotification() {
        NotificationResponseDto response = notificationService.getNotification("notif123");

        assertNull(response);
    }

    @Test
    @DisplayName("Should return empty list for listNotifications (not implemented)")
    void shouldReturnEmptyListForListNotifications() {
        List<NotificationResponseDto> response = notificationService.listNotifications("user123");

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("Should return null for retryNotification (not implemented)")
    void shouldReturnNullForRetryNotification() {
        NotificationResponseDto response = notificationService.retryNotification("notif123");

        assertNull(response);
    }

    @Test
    @DisplayName("Should handle cancelNotification gracefully (not implemented)")
    void shouldHandleCancelNotificationGracefully() {
        assertDoesNotThrow(() -> notificationService.cancelNotification("notif123"));
    }

    @Test
    @DisplayName("Should create notification with all system types")
    void shouldCreateNotificationWithAllSystemTypes() {
        Notification.NotificationType[] types = Notification.NotificationType.values();

        for (Notification.NotificationType type : types) {
            SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                    .userId("user123")
                    .type(type)
                    .channel(Notification.NotificationChannel.EMAIL)
                    .recipient("test@example.com")
                    .body("Test message")
                    .build();

            doNothing().when(notificationSender).send(any(Notification.class));

            NotificationResponseDto response = notificationService.sendNotification(request);

            assertEquals(type, response.getType());
        }
    }

    @Test
    @DisplayName("Should use tenant context holder")
    void shouldUseTenantContextHolder() {
        SendNotificationRequestDto request = SendNotificationRequestDto.builder()
                .userId("user123")
                .type(Notification.NotificationType.WELCOME)
                .channel(Notification.NotificationChannel.EMAIL)
                .recipient("test@example.com")
                .body("Welcome")
                .build();

        doNothing().when(notificationSender).send(any(Notification.class));

        notificationService.sendNotification(request);

        verify(tenantContextHolder, times(1)).getRequiredTenantId();
    }
}
