package com.gogidix.digitalmarketing.integration.application.dto;

import com.gogidix.digitalmarketing.integration.application.dto.IntegrationResponseDto;
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
class IntegrationResponseDtoTest {

        @Test
    void testBuilder() {
        IntegrationResponseDto dto = IntegrationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-type", dto.getType());
        assertEquals("test-provider", dto.getProvider());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-webhookUrl", dto.getWebhookUrl());
        assertEquals("test-apiKey", dto.getApiKey());
        assertEquals("test-configuration", dto.getConfiguration());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        IntegrationResponseDto dto = new IntegrationResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setProvider("val-provider");
        dto.setStatus("val-status");
        dto.setWebhookUrl("val-webhookUrl");
        dto.setApiKey("val-apiKey");
        dto.setConfiguration("val-configuration");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
        assertEquals("val-provider", dto.getProvider());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-webhookUrl", dto.getWebhookUrl());
        assertEquals("val-apiKey", dto.getApiKey());
        assertEquals("val-configuration", dto.getConfiguration());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        IntegrationResponseDto dto1 = IntegrationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        IntegrationResponseDto dto2 = IntegrationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        IntegrationResponseDto dto = IntegrationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}