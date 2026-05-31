package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.MetricRollup;
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
class MetricRollup_TargetTest {

        @Test
    void testBuilder() {
        MetricRollup.Target dto = MetricRollup.Target.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .targetDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .build();
        assertNotNull(dto);
        assertEquals("test-targetType", dto.getTargetType());
        assertEquals(BigDecimal.TEN, dto.getAchievementPercentage());
        assertEquals(BigDecimal.TEN, dto.getRemaining());
        assertTrue(dto.getIsOnTrack());
        assertEquals(LocalDate.of(2025,1,15), dto.getTargetDate());
        assertEquals("test-frequency", dto.getFrequency());
    }

    @Test
    void testSettersAndGetters() {
        MetricRollup.Target dto = new MetricRollup.Target();
        dto.setTargetType("val-targetType");
        dto.setAchievementPercentage(BigDecimal.ONE);
        dto.setRemaining(BigDecimal.ONE);
        dto.setIsOnTrack(true);
        dto.setTargetDate(LocalDate.of(2025,6,1));
        dto.setFrequency("val-frequency");
        assertEquals("val-targetType", dto.getTargetType());
        assertEquals(BigDecimal.ONE, dto.getAchievementPercentage());
        assertEquals(BigDecimal.ONE, dto.getRemaining());
        assertTrue(dto.getIsOnTrack());
        assertEquals(LocalDate.of(2025,6,1), dto.getTargetDate());
        assertEquals("val-frequency", dto.getFrequency());
    }

    @Test
    void testEqualsAndHashCode() {
        MetricRollup.Target dto1 = MetricRollup.Target.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .targetDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .build();
        MetricRollup.Target dto2 = MetricRollup.Target.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .targetDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MetricRollup.Target dto = MetricRollup.Target.builder()
                        .targetType("test-targetType")
            .targetValue(null)
            .currentValue(null)
            .achievementPercentage(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .targetDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}