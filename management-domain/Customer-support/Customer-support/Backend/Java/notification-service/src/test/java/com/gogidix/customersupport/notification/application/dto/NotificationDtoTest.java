package com.gogidix.customersupport.notification.application.dto;

import com.gogidix.customersupport.notification.application.dto.NotificationDto;
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
class NotificationDtoTest {

        @Test
    void testBuilder() {
        NotificationDto dto = NotificationDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .status("test-status")
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .retryCount(42)
            .maxRetries(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-notificationId", dto.getNotificationId());
        assertEquals("test-type", dto.getType());
        assertEquals("test-recipientId", dto.getRecipientId());
        assertEquals("test-recipientEmail", dto.getRecipientEmail());
        assertEquals("test-recipientPhone", dto.getRecipientPhone());
        assertEquals("test-channel", dto.getChannel());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-priority", dto.getPriority());
        assertEquals("test-failureReason", dto.getFailureReason());
        assertEquals(42, dto.getRetryCount());
        assertEquals(42, dto.getMaxRetries());
        assertEquals("test-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("test-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testSettersAndGetters() {
        NotificationDto dto = new NotificationDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setNotificationId("val-notificationId");
        dto.setType("val-type");
        dto.setRecipientId("val-recipientId");
        dto.setRecipientEmail("val-recipientEmail");
        dto.setRecipientPhone("val-recipientPhone");
        dto.setChannel("val-channel");
        dto.setStatus("val-status");
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setTemplateId("val-templateId");
        dto.setPriority("val-priority");
        dto.setFailureReason("val-failureReason");
        dto.setRetryCount(99);
        dto.setMaxRetries(99);
        dto.setRelatedEntityType("val-relatedEntityType");
        dto.setRelatedEntityId("val-relatedEntityId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-notificationId", dto.getNotificationId());
        assertEquals("val-type", dto.getType());
        assertEquals("val-recipientId", dto.getRecipientId());
        assertEquals("val-recipientEmail", dto.getRecipientEmail());
        assertEquals("val-recipientPhone", dto.getRecipientPhone());
        assertEquals("val-channel", dto.getChannel());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-priority", dto.getPriority());
        assertEquals("val-failureReason", dto.getFailureReason());
        assertEquals(99, dto.getRetryCount());
        assertEquals(99, dto.getMaxRetries());
        assertEquals("val-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("val-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationDto dto1 = NotificationDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .status("test-status")
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .retryCount(42)
            .maxRetries(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        NotificationDto dto2 = NotificationDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .status("test-status")
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .retryCount(42)
            .maxRetries(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationDto dto = NotificationDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .notificationId("test-notificationId")
            .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .status("test-status")
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .retryCount(42)
            .maxRetries(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}