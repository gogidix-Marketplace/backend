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
class NotificationPreferenceResponseDto_CategoryPreferenceDtoTest {

        @Test
    void testBuilder() {
        NotificationPreferenceResponseDto.CategoryPreferenceDto dto = NotificationPreferenceResponseDto.CategoryPreferenceDto.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getEnabled());
        assertTrue(dto.getImmediate());
    }

    @Test
    void testSettersAndGetters() {
        NotificationPreferenceResponseDto.CategoryPreferenceDto dto = new NotificationPreferenceResponseDto.CategoryPreferenceDto();
        dto.setEnabled(true);
        dto.setImmediate(true);
        assertTrue(dto.getEnabled());
        assertTrue(dto.getImmediate());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreferenceResponseDto.CategoryPreferenceDto dto1 = NotificationPreferenceResponseDto.CategoryPreferenceDto.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        NotificationPreferenceResponseDto.CategoryPreferenceDto dto2 = NotificationPreferenceResponseDto.CategoryPreferenceDto.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationPreferenceResponseDto.CategoryPreferenceDto dto = NotificationPreferenceResponseDto.CategoryPreferenceDto.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}