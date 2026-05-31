package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;
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
class KPIWidget_TargetInfoTest {

        @Test
    void testBuilder() {
        KPIWidget.TargetInfo dto = KPIWidget.TargetInfo.builder()
                        .target(BigDecimal.TEN)
            .actual(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getTarget());
        assertEquals(BigDecimal.TEN, dto.getActual());
        assertEquals(BigDecimal.TEN, dto.getAchievement());
        assertEquals(BigDecimal.TEN, dto.getRemaining());
        assertTrue(dto.getIsOnTrack());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.TargetInfo dto = new KPIWidget.TargetInfo();
        dto.setTarget(BigDecimal.ONE);
        dto.setActual(BigDecimal.ONE);
        dto.setAchievement(BigDecimal.ONE);
        dto.setRemaining(BigDecimal.ONE);
        dto.setIsOnTrack(true);
        assertEquals(BigDecimal.ONE, dto.getTarget());
        assertEquals(BigDecimal.ONE, dto.getActual());
        assertEquals(BigDecimal.ONE, dto.getAchievement());
        assertEquals(BigDecimal.ONE, dto.getRemaining());
        assertTrue(dto.getIsOnTrack());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.TargetInfo dto1 = KPIWidget.TargetInfo.builder()
                        .target(BigDecimal.TEN)
            .actual(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .build();
        KPIWidget.TargetInfo dto2 = KPIWidget.TargetInfo.builder()
                        .target(BigDecimal.TEN)
            .actual(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.TargetInfo dto = KPIWidget.TargetInfo.builder()
                        .target(BigDecimal.TEN)
            .actual(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .remaining(BigDecimal.TEN)
            .isOnTrack(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}