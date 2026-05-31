package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.WidgetResponseDto;
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
class WidgetResponseDto_TargetInfoDtoTest {

        @Test
    void testBuilder() {
        WidgetResponseDto.TargetInfoDto dto = WidgetResponseDto.TargetInfoDto.builder()
                        .target(null)
            .actual(null)
            .achievement(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsOnTrack());
    }

    @Test
    void testSettersAndGetters() {
        WidgetResponseDto.TargetInfoDto dto = new WidgetResponseDto.TargetInfoDto();
        dto.setIsOnTrack(true);
        assertTrue(dto.getIsOnTrack());
    }

    @Test
    void testEqualsAndHashCode() {
        WidgetResponseDto.TargetInfoDto dto1 = WidgetResponseDto.TargetInfoDto.builder()
                        .target(null)
            .actual(null)
            .achievement(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        WidgetResponseDto.TargetInfoDto dto2 = WidgetResponseDto.TargetInfoDto.builder()
                        .target(null)
            .actual(null)
            .achievement(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        WidgetResponseDto.TargetInfoDto dto = WidgetResponseDto.TargetInfoDto.builder()
                        .target(null)
            .actual(null)
            .achievement(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}