package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.CommunicationChannelResponseDto;
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
class CommunicationChannelResponseDtoTest {

        @Test
    void testBuilder() {
        CommunicationChannelResponseDto dto = CommunicationChannelResponseDto.builder()
                        .id("test-id")
            .channelId("test-channelId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .type(CommunicationChannelResponseDto.ChannelTypeDto.EMAIL)
            .status(CommunicationChannelResponseDto.ChannelStatusDto.ACTIVE)
            .isDefault(true)
            .config(null)
            .webhookConfig(null)
            .priority(42)
            .rateLimiting(null)
            .retryPolicy(null)
            .allowedSenders(Collections.emptyList())
            .blockedRecipients(Collections.emptyList())
            .timeZone("test-timeZone")
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalSent(42L)
            .totalDelivered(42L)
            .totalFailed(42L)
            .deliveryRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-channelId", dto.getChannelId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(CommunicationChannelResponseDto.ChannelTypeDto.EMAIL, dto.getType());
        assertEquals(CommunicationChannelResponseDto.ChannelStatusDto.ACTIVE, dto.getStatus());
        assertTrue(dto.getIsDefault());
        assertEquals(42, dto.getPriority());
        assertEquals("test-timeZone", dto.getTimeZone());
        assertEquals(42L, dto.getTotalSent());
        assertEquals(42L, dto.getTotalDelivered());
        assertEquals(42L, dto.getTotalFailed());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannelResponseDto dto = new CommunicationChannelResponseDto();
        dto.setId("val-id");
        dto.setChannelId("val-channelId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setType(CommunicationChannelResponseDto.ChannelTypeDto.EMAIL);
        dto.setStatus(CommunicationChannelResponseDto.ChannelStatusDto.ACTIVE);
        dto.setIsDefault(true);
        dto.setPriority(99);
        dto.setTimeZone("val-timeZone");
        assertEquals("val-id", dto.getId());
        assertEquals("val-channelId", dto.getChannelId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(CommunicationChannelResponseDto.ChannelTypeDto.EMAIL, dto.getType());
        assertEquals(CommunicationChannelResponseDto.ChannelStatusDto.ACTIVE, dto.getStatus());
        assertTrue(dto.getIsDefault());
        assertEquals(99, dto.getPriority());
        assertEquals("val-timeZone", dto.getTimeZone());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannelResponseDto dto1 = CommunicationChannelResponseDto.builder()
                        .id("test-id")
            .channelId("test-channelId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .type(CommunicationChannelResponseDto.ChannelTypeDto.EMAIL)
            .status(CommunicationChannelResponseDto.ChannelStatusDto.ACTIVE)
            .isDefault(true)
            .config(null)
            .webhookConfig(null)
            .priority(42)
            .rateLimiting(null)
            .retryPolicy(null)
            .allowedSenders(Collections.emptyList())
            .blockedRecipients(Collections.emptyList())
            .timeZone("test-timeZone")
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalSent(42L)
            .totalDelivered(42L)
            .totalFailed(42L)
            .deliveryRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CommunicationChannelResponseDto dto2 = CommunicationChannelResponseDto.builder()
                        .id("test-id")
            .channelId("test-channelId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .type(CommunicationChannelResponseDto.ChannelTypeDto.EMAIL)
            .status(CommunicationChannelResponseDto.ChannelStatusDto.ACTIVE)
            .isDefault(true)
            .config(null)
            .webhookConfig(null)
            .priority(42)
            .rateLimiting(null)
            .retryPolicy(null)
            .allowedSenders(Collections.emptyList())
            .blockedRecipients(Collections.emptyList())
            .timeZone("test-timeZone")
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalSent(42L)
            .totalDelivered(42L)
            .totalFailed(42L)
            .deliveryRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannelResponseDto dto = CommunicationChannelResponseDto.builder()
                        .id("test-id")
            .channelId("test-channelId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .type(CommunicationChannelResponseDto.ChannelTypeDto.EMAIL)
            .status(CommunicationChannelResponseDto.ChannelStatusDto.ACTIVE)
            .isDefault(true)
            .config(null)
            .webhookConfig(null)
            .priority(42)
            .rateLimiting(null)
            .retryPolicy(null)
            .allowedSenders(Collections.emptyList())
            .blockedRecipients(Collections.emptyList())
            .timeZone("test-timeZone")
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalSent(42L)
            .totalDelivered(42L)
            .totalFailed(42L)
            .deliveryRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}