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
class CommunicationChannelResponseDto_RateLimitingDtoTest {

        @Test
    void testBuilder() {
        CommunicationChannelResponseDto.RateLimitingDto dto = CommunicationChannelResponseDto.RateLimitingDto.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsEnabled());
        assertEquals(42, dto.getMaxMessagesPerMinute());
        assertEquals(42, dto.getMaxMessagesPerHour());
        assertEquals(42, dto.getMaxMessagesPerDay());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannelResponseDto.RateLimitingDto dto = new CommunicationChannelResponseDto.RateLimitingDto();
        dto.setIsEnabled(true);
        dto.setMaxMessagesPerMinute(99);
        dto.setMaxMessagesPerHour(99);
        dto.setMaxMessagesPerDay(99);
        assertTrue(dto.getIsEnabled());
        assertEquals(99, dto.getMaxMessagesPerMinute());
        assertEquals(99, dto.getMaxMessagesPerHour());
        assertEquals(99, dto.getMaxMessagesPerDay());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannelResponseDto.RateLimitingDto dto1 = CommunicationChannelResponseDto.RateLimitingDto.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .build();
        CommunicationChannelResponseDto.RateLimitingDto dto2 = CommunicationChannelResponseDto.RateLimitingDto.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannelResponseDto.RateLimitingDto dto = CommunicationChannelResponseDto.RateLimitingDto.builder()
                        .isEnabled(true)
            .maxMessagesPerMinute(42)
            .maxMessagesPerHour(42)
            .maxMessagesPerDay(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}