package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.MessageTemplateResponseDto;
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
class MessageTemplateResponseDtoTest {

        @Test
    void testBuilder() {
        MessageTemplateResponseDto dto = MessageTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .code("test-code")
            .channelType(MessageTemplateResponseDto.ChannelTypeDto.EMAIL)
            .type(MessageTemplateResponseDto.TemplateTypeDto.MARKETING)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .variables(Collections.emptyList())
            .status(MessageTemplateResponseDto.TemplateStatusDto.DRAFT)
            .category("test-category")
            .tags(Collections.emptyList())
            .language("test-language")
            .locale("test-locale")
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .isSystemTemplate(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-code", dto.getCode());
        assertEquals(MessageTemplateResponseDto.ChannelTypeDto.EMAIL, dto.getChannelType());
        assertEquals(MessageTemplateResponseDto.TemplateTypeDto.MARKETING, dto.getType());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-htmlContent", dto.getHtmlContent());
        assertEquals(MessageTemplateResponseDto.TemplateStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-language", dto.getLanguage());
        assertEquals("test-locale", dto.getLocale());
        assertEquals(42, dto.getVersion());
        assertEquals("test-parentTemplateId", dto.getParentTemplateId());
        assertTrue(dto.getIsSystemTemplate());
        assertEquals(42L, dto.getUsageCount());
    }

    @Test
    void testSettersAndGetters() {
        MessageTemplateResponseDto dto = new MessageTemplateResponseDto();
        dto.setId("val-id");
        dto.setTemplateId("val-templateId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setCode("val-code");
        dto.setChannelType(MessageTemplateResponseDto.ChannelTypeDto.EMAIL);
        dto.setType(MessageTemplateResponseDto.TemplateTypeDto.MARKETING);
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setHtmlContent("val-htmlContent");
        dto.setStatus(MessageTemplateResponseDto.TemplateStatusDto.DRAFT);
        dto.setCategory("val-category");
        dto.setLanguage("val-language");
        dto.setLocale("val-locale");
        dto.setVersion(99);
        dto.setParentTemplateId("val-parentTemplateId");
        dto.setIsSystemTemplate(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-code", dto.getCode());
        assertEquals(MessageTemplateResponseDto.ChannelTypeDto.EMAIL, dto.getChannelType());
        assertEquals(MessageTemplateResponseDto.TemplateTypeDto.MARKETING, dto.getType());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-htmlContent", dto.getHtmlContent());
        assertEquals(MessageTemplateResponseDto.TemplateStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-language", dto.getLanguage());
        assertEquals("val-locale", dto.getLocale());
        assertEquals(99, dto.getVersion());
        assertEquals("val-parentTemplateId", dto.getParentTemplateId());
        assertTrue(dto.getIsSystemTemplate());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageTemplateResponseDto dto1 = MessageTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .code("test-code")
            .channelType(MessageTemplateResponseDto.ChannelTypeDto.EMAIL)
            .type(MessageTemplateResponseDto.TemplateTypeDto.MARKETING)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .variables(Collections.emptyList())
            .status(MessageTemplateResponseDto.TemplateStatusDto.DRAFT)
            .category("test-category")
            .tags(Collections.emptyList())
            .language("test-language")
            .locale("test-locale")
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .isSystemTemplate(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        MessageTemplateResponseDto dto2 = MessageTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .code("test-code")
            .channelType(MessageTemplateResponseDto.ChannelTypeDto.EMAIL)
            .type(MessageTemplateResponseDto.TemplateTypeDto.MARKETING)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .variables(Collections.emptyList())
            .status(MessageTemplateResponseDto.TemplateStatusDto.DRAFT)
            .category("test-category")
            .tags(Collections.emptyList())
            .language("test-language")
            .locale("test-locale")
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .isSystemTemplate(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MessageTemplateResponseDto dto = MessageTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .code("test-code")
            .channelType(MessageTemplateResponseDto.ChannelTypeDto.EMAIL)
            .type(MessageTemplateResponseDto.TemplateTypeDto.MARKETING)
            .subject("test-subject")
            .content("test-content")
            .htmlContent("test-htmlContent")
            .variables(Collections.emptyList())
            .status(MessageTemplateResponseDto.TemplateStatusDto.DRAFT)
            .category("test-category")
            .tags(Collections.emptyList())
            .language("test-language")
            .locale("test-locale")
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .isSystemTemplate(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}