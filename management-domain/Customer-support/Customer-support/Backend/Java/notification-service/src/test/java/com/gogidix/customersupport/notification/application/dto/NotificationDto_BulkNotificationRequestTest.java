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
class NotificationDto_BulkNotificationRequestTest {

        @Test
    void testBuilder() {
        NotificationDto.BulkNotificationRequest dto = NotificationDto.BulkNotificationRequest.builder()
                        .tenantId("test-tenantId")
            .type("test-type")
            .recipientIds(Collections.emptyList())
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-type", dto.getType());
        assertEquals("test-channel", dto.getChannel());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-priority", dto.getPriority());
        assertEquals("test-templateId", dto.getTemplateId());
    }

    @Test
    void testSettersAndGetters() {
        NotificationDto.BulkNotificationRequest dto = new NotificationDto.BulkNotificationRequest();
        dto.setTenantId("val-tenantId");
        dto.setType("val-type");
        dto.setChannel("val-channel");
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setPriority("val-priority");
        dto.setTemplateId("val-templateId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-type", dto.getType());
        assertEquals("val-channel", dto.getChannel());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-priority", dto.getPriority());
        assertEquals("val-templateId", dto.getTemplateId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationDto.BulkNotificationRequest dto1 = NotificationDto.BulkNotificationRequest.builder()
                        .tenantId("test-tenantId")
            .type("test-type")
            .recipientIds(Collections.emptyList())
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .build();
        NotificationDto.BulkNotificationRequest dto2 = NotificationDto.BulkNotificationRequest.builder()
                        .tenantId("test-tenantId")
            .type("test-type")
            .recipientIds(Collections.emptyList())
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationDto.BulkNotificationRequest dto = NotificationDto.BulkNotificationRequest.builder()
                        .tenantId("test-tenantId")
            .type("test-type")
            .recipientIds(Collections.emptyList())
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .templateData(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}