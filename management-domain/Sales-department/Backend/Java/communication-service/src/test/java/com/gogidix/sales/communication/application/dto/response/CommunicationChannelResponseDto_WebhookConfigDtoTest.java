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
class CommunicationChannelResponseDto_WebhookConfigDtoTest {

        @Test
    void testBuilder() {
        CommunicationChannelResponseDto.WebhookConfigDto dto = CommunicationChannelResponseDto.WebhookConfigDto.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-webhookUrl", dto.getWebhookUrl());
        assertTrue(dto.getIsEnabled());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannelResponseDto.WebhookConfigDto dto = new CommunicationChannelResponseDto.WebhookConfigDto();
        dto.setWebhookUrl("val-webhookUrl");
        dto.setIsEnabled(true);
        assertEquals("val-webhookUrl", dto.getWebhookUrl());
        assertTrue(dto.getIsEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannelResponseDto.WebhookConfigDto dto1 = CommunicationChannelResponseDto.WebhookConfigDto.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .build();
        CommunicationChannelResponseDto.WebhookConfigDto dto2 = CommunicationChannelResponseDto.WebhookConfigDto.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannelResponseDto.WebhookConfigDto dto = CommunicationChannelResponseDto.WebhookConfigDto.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}