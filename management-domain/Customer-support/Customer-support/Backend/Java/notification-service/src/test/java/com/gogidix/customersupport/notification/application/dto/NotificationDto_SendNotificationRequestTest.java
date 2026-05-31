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
class NotificationDto_SendNotificationRequestTest {

        @Test
    void testBuilder() {
        NotificationDto.SendNotificationRequest dto = NotificationDto.SendNotificationRequest.builder()
                        .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-type", dto.getType());
        assertEquals("test-recipientId", dto.getRecipientId());
        assertEquals("test-recipientEmail", dto.getRecipientEmail());
        assertEquals("test-recipientPhone", dto.getRecipientPhone());
        assertEquals("test-channel", dto.getChannel());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-priority", dto.getPriority());
        assertEquals("test-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("test-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testSettersAndGetters() {
        NotificationDto.SendNotificationRequest dto = new NotificationDto.SendNotificationRequest();
        dto.setType("val-type");
        dto.setRecipientId("val-recipientId");
        dto.setRecipientEmail("val-recipientEmail");
        dto.setRecipientPhone("val-recipientPhone");
        dto.setChannel("val-channel");
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setTenantId("val-tenantId");
        dto.setTemplateId("val-templateId");
        dto.setPriority("val-priority");
        dto.setRelatedEntityType("val-relatedEntityType");
        dto.setRelatedEntityId("val-relatedEntityId");
        assertEquals("val-type", dto.getType());
        assertEquals("val-recipientId", dto.getRecipientId());
        assertEquals("val-recipientEmail", dto.getRecipientEmail());
        assertEquals("val-recipientPhone", dto.getRecipientPhone());
        assertEquals("val-channel", dto.getChannel());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-priority", dto.getPriority());
        assertEquals("val-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("val-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationDto.SendNotificationRequest dto1 = NotificationDto.SendNotificationRequest.builder()
                        .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .build();
        NotificationDto.SendNotificationRequest dto2 = NotificationDto.SendNotificationRequest.builder()
                        .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationDto.SendNotificationRequest dto = NotificationDto.SendNotificationRequest.builder()
                        .type("test-type")
            .recipientId("test-recipientId")
            .recipientEmail("test-recipientEmail")
            .recipientPhone("test-recipientPhone")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .priority("test-priority")
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .metadata(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}