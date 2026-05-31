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
class CommunicationChannel_RetryPolicyTest {

        @Test
    void testBuilder() {
        CommunicationChannel.RetryPolicy dto = CommunicationChannel.RetryPolicy.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .backoffMultiplier(null)
            .retryableErrors(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsEnabled());
        assertEquals(42, dto.getMaxRetries());
        assertEquals(42, dto.getRetryIntervalSeconds());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannel.RetryPolicy dto = new CommunicationChannel.RetryPolicy();
        dto.setIsEnabled(true);
        dto.setMaxRetries(99);
        dto.setRetryIntervalSeconds(99);
        assertTrue(dto.getIsEnabled());
        assertEquals(99, dto.getMaxRetries());
        assertEquals(99, dto.getRetryIntervalSeconds());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannel.RetryPolicy dto1 = CommunicationChannel.RetryPolicy.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .backoffMultiplier(null)
            .retryableErrors(Collections.emptyList())
            .build();
        CommunicationChannel.RetryPolicy dto2 = CommunicationChannel.RetryPolicy.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .backoffMultiplier(null)
            .retryableErrors(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannel.RetryPolicy dto = CommunicationChannel.RetryPolicy.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .backoffMultiplier(null)
            .retryableErrors(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}