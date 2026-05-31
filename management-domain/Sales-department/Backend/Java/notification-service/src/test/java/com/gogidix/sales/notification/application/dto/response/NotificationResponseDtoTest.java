package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.application.dto.response.NotificationResponseDto;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
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
class NotificationResponseDtoTest {

        @Test
    void testBuilder() {
        NotificationResponseDto dto = NotificationResponseDto.builder()
                        .id("test-id")
            .notificationId("test-notificationId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(NotificationChannel.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .templateId("test-templateId")
            .status(NotificationStatus.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .priority(NotificationPriority.LOW)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .category("test-category")
            .actionType("test-actionType")
            .actionUrl("test-actionUrl")
            .metadata(Collections.emptyMap())
            .groupId("test-groupId")
            .isBatched(true)
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .retryCount(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-notificationId", dto.getNotificationId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-htmlContent", dto.getHtmlContent());
        assertEquals("test-templateId", dto.getTemplateId());
        assertTrue(dto.getIsRead());
        assertEquals("test-readBy", dto.getReadBy());
        assertEquals("test-externalMessageId", dto.getExternalMessageId());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-actionType", dto.getActionType());
        assertEquals("test-actionUrl", dto.getActionUrl());
        assertEquals("test-groupId", dto.getGroupId());
        assertTrue(dto.getIsBatched());
        assertEquals(42, dto.getRetryCount());
        assertEquals("test-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testSettersAndGetters() {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId("val-id");
        dto.setNotificationId("val-notificationId");
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setHtmlContent("val-htmlContent");
        dto.setTemplateId("val-templateId");
        dto.setIsRead(true);
        dto.setReadBy("val-readBy");
        dto.setExternalMessageId("val-externalMessageId");
        dto.setCategory("val-category");
        dto.setActionType("val-actionType");
        dto.setActionUrl("val-actionUrl");
        dto.setGroupId("val-groupId");
        dto.setIsBatched(true);
        dto.setRetryCount(99);
        dto.setErrorMessage("val-errorMessage");
        assertEquals("val-id", dto.getId());
        assertEquals("val-notificationId", dto.getNotificationId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-htmlContent", dto.getHtmlContent());
        assertEquals("val-templateId", dto.getTemplateId());
        assertTrue(dto.getIsRead());
        assertEquals("val-readBy", dto.getReadBy());
        assertEquals("val-externalMessageId", dto.getExternalMessageId());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-actionType", dto.getActionType());
        assertEquals("val-actionUrl", dto.getActionUrl());
        assertEquals("val-groupId", dto.getGroupId());
        assertTrue(dto.getIsBatched());
        assertEquals(99, dto.getRetryCount());
        assertEquals("val-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationResponseDto dto1 = NotificationResponseDto.builder()
                        .id("test-id")
            .notificationId("test-notificationId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(NotificationChannel.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .templateId("test-templateId")
            .status(NotificationStatus.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .priority(NotificationPriority.LOW)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .category("test-category")
            .actionType("test-actionType")
            .actionUrl("test-actionUrl")
            .metadata(Collections.emptyMap())
            .groupId("test-groupId")
            .isBatched(true)
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .retryCount(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        NotificationResponseDto dto2 = NotificationResponseDto.builder()
                        .id("test-id")
            .notificationId("test-notificationId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(NotificationChannel.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .templateId("test-templateId")
            .status(NotificationStatus.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .priority(NotificationPriority.LOW)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .category("test-category")
            .actionType("test-actionType")
            .actionUrl("test-actionUrl")
            .metadata(Collections.emptyMap())
            .groupId("test-groupId")
            .isBatched(true)
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .retryCount(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationResponseDto dto = NotificationResponseDto.builder()
                        .id("test-id")
            .notificationId("test-notificationId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(NotificationChannel.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .templateId("test-templateId")
            .status(NotificationStatus.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .priority(NotificationPriority.LOW)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .category("test-category")
            .actionType("test-actionType")
            .actionUrl("test-actionUrl")
            .metadata(Collections.emptyMap())
            .groupId("test-groupId")
            .isBatched(true)
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .retryCount(42)
            .errorMessage("test-errorMessage")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}