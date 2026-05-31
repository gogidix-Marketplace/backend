package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.RollupResponseDto;
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
class RollupResponseDto_TargetDtoTest {

        @Test
    void testBuilder() {
        RollupResponseDto.TargetDto dto = RollupResponseDto.TargetDto.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-targetType", dto.getTargetType());
        assertTrue(dto.getIsOnTrack());
    }

    @Test
    void testSettersAndGetters() {
        RollupResponseDto.TargetDto dto = new RollupResponseDto.TargetDto();
        dto.setTargetType("val-targetType");
        dto.setIsOnTrack(true);
        assertEquals("val-targetType", dto.getTargetType());
        assertTrue(dto.getIsOnTrack());
    }

    @Test
    void testEqualsAndHashCode() {
        RollupResponseDto.TargetDto dto1 = RollupResponseDto.TargetDto.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        RollupResponseDto.TargetDto dto2 = RollupResponseDto.TargetDto.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RollupResponseDto.TargetDto dto = RollupResponseDto.TargetDto.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(null)
            .remaining(null)
            .isOnTrack(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}