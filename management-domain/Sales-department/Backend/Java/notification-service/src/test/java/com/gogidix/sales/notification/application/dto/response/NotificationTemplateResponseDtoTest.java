package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.application.dto.response.NotificationTemplateResponseDto;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
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
class NotificationTemplateResponseDtoTest {

        @Test
    void testBuilder() {
        NotificationTemplateResponseDto dto = NotificationTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .code("test-code")
            .name("test-name")
            .description("test-description")
            .channel(NotificationChannel.EMAIL)
            .subjectTemplate("test-subjectTemplate")
            .contentTemplate("test-contentTemplate")
            .htmlContentTemplate("test-htmlContentTemplate")
            .variables(Collections.emptyMap())
            .locale("test-locale")
            .isActive(true)
            .version(42)
            .validFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags("test-tags")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-code", dto.getCode());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-subjectTemplate", dto.getSubjectTemplate());
        assertEquals("test-contentTemplate", dto.getContentTemplate());
        assertEquals("test-htmlContentTemplate", dto.getHtmlContentTemplate());
        assertEquals("test-locale", dto.getLocale());
        assertTrue(dto.getIsActive());
        assertEquals(42, dto.getVersion());
        assertEquals("test-tags", dto.getTags());
    }

    @Test
    void testSettersAndGetters() {
        NotificationTemplateResponseDto dto = new NotificationTemplateResponseDto();
        dto.setId("val-id");
        dto.setTemplateId("val-templateId");
        dto.setTenantId("val-tenantId");
        dto.setCode("val-code");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setSubjectTemplate("val-subjectTemplate");
        dto.setContentTemplate("val-contentTemplate");
        dto.setHtmlContentTemplate("val-htmlContentTemplate");
        dto.setLocale("val-locale");
        dto.setIsActive(true);
        dto.setVersion(99);
        dto.setTags("val-tags");
        assertEquals("val-id", dto.getId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-code", dto.getCode());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-subjectTemplate", dto.getSubjectTemplate());
        assertEquals("val-contentTemplate", dto.getContentTemplate());
        assertEquals("val-htmlContentTemplate", dto.getHtmlContentTemplate());
        assertEquals("val-locale", dto.getLocale());
        assertTrue(dto.getIsActive());
        assertEquals(99, dto.getVersion());
        assertEquals("val-tags", dto.getTags());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationTemplateResponseDto dto1 = NotificationTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .code("test-code")
            .name("test-name")
            .description("test-description")
            .channel(NotificationChannel.EMAIL)
            .subjectTemplate("test-subjectTemplate")
            .contentTemplate("test-contentTemplate")
            .htmlContentTemplate("test-htmlContentTemplate")
            .variables(Collections.emptyMap())
            .locale("test-locale")
            .isActive(true)
            .version(42)
            .validFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags("test-tags")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        NotificationTemplateResponseDto dto2 = NotificationTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .code("test-code")
            .name("test-name")
            .description("test-description")
            .channel(NotificationChannel.EMAIL)
            .subjectTemplate("test-subjectTemplate")
            .contentTemplate("test-contentTemplate")
            .htmlContentTemplate("test-htmlContentTemplate")
            .variables(Collections.emptyMap())
            .locale("test-locale")
            .isActive(true)
            .version(42)
            .validFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags("test-tags")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationTemplateResponseDto dto = NotificationTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .code("test-code")
            .name("test-name")
            .description("test-description")
            .channel(NotificationChannel.EMAIL)
            .subjectTemplate("test-subjectTemplate")
            .contentTemplate("test-contentTemplate")
            .htmlContentTemplate("test-htmlContentTemplate")
            .variables(Collections.emptyMap())
            .locale("test-locale")
            .isActive(true)
            .version(42)
            .validFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags("test-tags")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}