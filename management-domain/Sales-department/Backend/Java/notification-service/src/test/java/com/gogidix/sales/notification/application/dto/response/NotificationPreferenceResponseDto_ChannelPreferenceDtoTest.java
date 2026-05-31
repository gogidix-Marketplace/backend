package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.application.dto.response.NotificationPreferenceResponseDto;
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
class NotificationPreferenceResponseDto_ChannelPreferenceDtoTest {

        @Test
    void testBuilder() {
        NotificationPreferenceResponseDto.ChannelPreferenceDto dto = NotificationPreferenceResponseDto.ChannelPreferenceDto.builder()
                        .enabled(true)
            .allowBatching(true)
            .maxPerHour(42)
            .maxPerDay(42)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getEnabled());
        assertTrue(dto.getAllowBatching());
        assertEquals(42, dto.getMaxPerHour());
        assertEquals(42, dto.getMaxPerDay());
    }

    @Test
    void testSettersAndGetters() {
        NotificationPreferenceResponseDto.ChannelPreferenceDto dto = new NotificationPreferenceResponseDto.ChannelPreferenceDto();
        dto.setEnabled(true);
        dto.setAllowBatching(true);
        dto.setMaxPerHour(99);
        dto.setMaxPerDay(99);
        assertTrue(dto.getEnabled());
        assertTrue(dto.getAllowBatching());
        assertEquals(99, dto.getMaxPerHour());
        assertEquals(99, dto.getMaxPerDay());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreferenceResponseDto.ChannelPreferenceDto dto1 = NotificationPreferenceResponseDto.ChannelPreferenceDto.builder()
                        .enabled(true)
            .allowBatching(true)
            .maxPerHour(42)
            .maxPerDay(42)
            .build();
        NotificationPreferenceResponseDto.ChannelPreferenceDto dto2 = NotificationPreferenceResponseDto.ChannelPreferenceDto.builder()
                        .enabled(true)
            .allowBatching(true)
            .maxPerHour(42)
            .maxPerDay(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationPreferenceResponseDto.ChannelPreferenceDto dto = NotificationPreferenceResponseDto.ChannelPreferenceDto.builder()
                        .enabled(true)
            .allowBatching(true)
            .maxPerHour(42)
            .maxPerDay(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}