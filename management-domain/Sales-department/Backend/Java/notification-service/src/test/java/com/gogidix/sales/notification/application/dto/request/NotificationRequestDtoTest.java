package com.gogidix.sales.notification.application.dto.request;

import com.gogidix.sales.notification.application.dto.request.NotificationRequestDto;
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
class NotificationRequestDtoTest {

        @Test
    void testBuilder() {
        NotificationRequestDto dto = NotificationRequestDto.builder()
                        .tenantId("test-tenantId")
            .recipientId("test-recipientId")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-recipientId", dto.getRecipientId());
        assertEquals("test-channel", dto.getChannel());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-priority", dto.getPriority());
        assertEquals("test-templateId", dto.getTemplateId());
    }

    @Test
    void testSettersAndGetters() {
        NotificationRequestDto dto = new NotificationRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setRecipientId("val-recipientId");
        dto.setChannel("val-channel");
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setPriority("val-priority");
        dto.setTemplateId("val-templateId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-recipientId", dto.getRecipientId());
        assertEquals("val-channel", dto.getChannel());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-priority", dto.getPriority());
        assertEquals("val-templateId", dto.getTemplateId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationRequestDto dto1 = NotificationRequestDto.builder()
                        .tenantId("test-tenantId")
            .recipientId("test-recipientId")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .build();
        NotificationRequestDto dto2 = NotificationRequestDto.builder()
                        .tenantId("test-tenantId")
            .recipientId("test-recipientId")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationRequestDto dto = NotificationRequestDto.builder()
                        .tenantId("test-tenantId")
            .recipientId("test-recipientId")
            .channel("test-channel")
            .subject("test-subject")
            .content("test-content")
            .priority("test-priority")
            .templateId("test-templateId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}