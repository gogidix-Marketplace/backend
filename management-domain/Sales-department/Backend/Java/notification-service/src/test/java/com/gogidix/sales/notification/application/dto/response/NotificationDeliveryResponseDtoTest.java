package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.application.dto.response.NotificationDeliveryResponseDto;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificationDeliveryResponseDtoTest {

        @Test
    void testBuilder() {
        NotificationDeliveryResponseDto dto = NotificationDeliveryResponseDto.builder()
                        .id("test-id")
            .deliveryId("test-deliveryId")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .channel(NotificationChannel.EMAIL)
            .recipientAddress("test-recipientAddress")
            .status(NotificationStatus.DRAFT)
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .errorMessage("test-errorMessage")
            .retryCount(42)
            .externalMessageId("test-externalMessageId")
            .providerMetadata(Collections.emptyMap())
            .deliveryAttempts(42)
            .nextRetryAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-deliveryId", dto.getDeliveryId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-notificationId", dto.getNotificationId());
        assertEquals("test-recipientId", dto.getRecipientId());
        assertEquals("test-recipientName", dto.getRecipientName());
        assertEquals("test-recipientType", dto.getRecipientType());
        assertEquals("test-recipientAddress", dto.getRecipientAddress());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertEquals(42, dto.getRetryCount());
        assertEquals("test-externalMessageId", dto.getExternalMessageId());
        assertEquals(42, dto.getDeliveryAttempts());
    }

    @Test
    void testSettersAndGetters() {
        NotificationDeliveryResponseDto dto = new NotificationDeliveryResponseDto();
        dto.setId("val-id");
        dto.setDeliveryId("val-deliveryId");
        dto.setTenantId("val-tenantId");
        dto.setNotificationId("val-notificationId");
        dto.setRecipientId("val-recipientId");
        dto.setRecipientName("val-recipientName");
        dto.setRecipientType("val-recipientType");
        dto.setRecipientAddress("val-recipientAddress");
        dto.setErrorMessage("val-errorMessage");
        dto.setRetryCount(99);
        dto.setExternalMessageId("val-externalMessageId");
        dto.setDeliveryAttempts(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-deliveryId", dto.getDeliveryId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-notificationId", dto.getNotificationId());
        assertEquals("val-recipientId", dto.getRecipientId());
        assertEquals("val-recipientName", dto.getRecipientName());
        assertEquals("val-recipientType", dto.getRecipientType());
        assertEquals("val-recipientAddress", dto.getRecipientAddress());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertEquals(99, dto.getRetryCount());
        assertEquals("val-externalMessageId", dto.getExternalMessageId());
        assertEquals(99, dto.getDeliveryAttempts());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationDeliveryResponseDto dto1 = NotificationDeliveryResponseDto.builder()
                        .id("test-id")
            .deliveryId("test-deliveryId")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .channel(NotificationChannel.EMAIL)
            .recipientAddress("test-recipientAddress")
            .status(NotificationStatus.DRAFT)
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .errorMessage("test-errorMessage")
            .retryCount(42)
            .externalMessageId("test-externalMessageId")
            .providerMetadata(Collections.emptyMap())
            .deliveryAttempts(42)
            .nextRetryAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        NotificationDeliveryResponseDto dto2 = NotificationDeliveryResponseDto.builder()
                        .id("test-id")
            .deliveryId("test-deliveryId")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .channel(NotificationChannel.EMAIL)
            .recipientAddress("test-recipientAddress")
            .status(NotificationStatus.DRAFT)
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .errorMessage("test-errorMessage")
            .retryCount(42)
            .externalMessageId("test-externalMessageId")
            .providerMetadata(Collections.emptyMap())
            .deliveryAttempts(42)
            .nextRetryAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationDeliveryResponseDto dto = NotificationDeliveryResponseDto.builder()
                        .id("test-id")
            .deliveryId("test-deliveryId")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .channel(NotificationChannel.EMAIL)
            .recipientAddress("test-recipientAddress")
            .status(NotificationStatus.DRAFT)
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .errorMessage("test-errorMessage")
            .retryCount(42)
            .externalMessageId("test-externalMessageId")
            .providerMetadata(Collections.emptyMap())
            .deliveryAttempts(42)
            .nextRetryAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}