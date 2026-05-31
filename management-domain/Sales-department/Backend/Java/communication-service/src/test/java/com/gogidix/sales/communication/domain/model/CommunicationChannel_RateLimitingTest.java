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
class CommunicationChannel_RateLimitingTest {

        @Test
    void testBuilder() {
        CommunicationChannel.RateLimiting dto = CommunicationChannel.RateLimiting.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .burstSize(42)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsEnabled());
        assertEquals(42, dto.getMaxMessagesPerMinute());
        assertEquals(42, dto.getMaxMessagesPerHour());
        assertEquals(42, dto.getMaxMessagesPerDay());
        assertEquals(42, dto.getBurstSize());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannel.RateLimiting dto = new CommunicationChannel.RateLimiting();
        dto.setIsEnabled(true);
        dto.setMaxMessagesPerMinute(99);
        dto.setMaxMessagesPerHour(99);
        dto.setMaxMessagesPerDay(99);
        dto.setBurstSize(99);
        assertTrue(dto.getIsEnabled());
        assertEquals(99, dto.getMaxMessagesPerMinute());
        assertEquals(99, dto.getMaxMessagesPerHour());
        assertEquals(99, dto.getMaxMessagesPerDay());
        assertEquals(99, dto.getBurstSize());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannel.RateLimiting dto1 = CommunicationChannel.RateLimiting.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .burstSize(42)
            .build();
        CommunicationChannel.RateLimiting dto2 = CommunicationChannel.RateLimiting.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .burstSize(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannel.RateLimiting dto = CommunicationChannel.RateLimiting.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .burstSize(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}