package com.gogidix.digitalmarketing.integration.application.dto;

import com.gogidix.digitalmarketing.integration.application.dto.IntegrationRequestDto;
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
class IntegrationRequestDtoTest {

        @Test
    void testBuilder() {
        IntegrationRequestDto dto = IntegrationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-type", dto.getType());
        assertEquals("test-provider", dto.getProvider());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-webhookUrl", dto.getWebhookUrl());
        assertEquals("test-apiKey", dto.getApiKey());
        assertEquals("test-configuration", dto.getConfiguration());
    }

    @Test
    void testSettersAndGetters() {
        IntegrationRequestDto dto = new IntegrationRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setProvider("val-provider");
        dto.setStatus("val-status");
        dto.setWebhookUrl("val-webhookUrl");
        dto.setApiKey("val-apiKey");
        dto.setConfiguration("val-configuration");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
        assertEquals("val-provider", dto.getProvider());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-webhookUrl", dto.getWebhookUrl());
        assertEquals("val-apiKey", dto.getApiKey());
        assertEquals("val-configuration", dto.getConfiguration());
    }

    @Test
    void testEqualsAndHashCode() {
        IntegrationRequestDto dto1 = IntegrationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .build();
        IntegrationRequestDto dto2 = IntegrationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        IntegrationRequestDto dto = IntegrationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .provider("test-provider")
            .status("test-status")
            .webhookUrl("test-webhookUrl")
            .apiKey("test-apiKey")
            .configuration("test-configuration")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}