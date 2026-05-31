package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.domain.model.CommunicationChannel;
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
class CommunicationChannel_WebhookConfigTest {

        @Test
    void testBuilder() {
        CommunicationChannel.WebhookConfig dto = CommunicationChannel.WebhookConfig.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .secretKey("test-secretKey")
            .retryAttempts(42)
            .timeoutSeconds(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-webhookUrl", dto.getWebhookUrl());
        assertTrue(dto.getIsEnabled());
        assertEquals("test-secretKey", dto.getSecretKey());
        assertEquals(42, dto.getRetryAttempts());
        assertEquals(42, dto.getTimeoutSeconds());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannel.WebhookConfig dto = new CommunicationChannel.WebhookConfig();
        dto.setWebhookUrl("val-webhookUrl");
        dto.setIsEnabled(true);
        dto.setSecretKey("val-secretKey");
        dto.setRetryAttempts(99);
        dto.setTimeoutSeconds(99);
        assertEquals("val-webhookUrl", dto.getWebhookUrl());
        assertTrue(dto.getIsEnabled());
        assertEquals("val-secretKey", dto.getSecretKey());
        assertEquals(99, dto.getRetryAttempts());
        assertEquals(99, dto.getTimeoutSeconds());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannel.WebhookConfig dto1 = CommunicationChannel.WebhookConfig.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .secretKey("test-secretKey")
            .retryAttempts(42)
            .timeoutSeconds(42)
            .build();
        CommunicationChannel.WebhookConfig dto2 = CommunicationChannel.WebhookConfig.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .secretKey("test-secretKey")
            .retryAttempts(42)
            .timeoutSeconds(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannel.WebhookConfig dto = CommunicationChannel.WebhookConfig.builder()
                        .webhookUrl("test-webhookUrl")
            .isEnabled(true)
            .subscribedEvents(Collections.emptyList())
            .secretKey("test-secretKey")
            .retryAttempts(42)
            .timeoutSeconds(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}