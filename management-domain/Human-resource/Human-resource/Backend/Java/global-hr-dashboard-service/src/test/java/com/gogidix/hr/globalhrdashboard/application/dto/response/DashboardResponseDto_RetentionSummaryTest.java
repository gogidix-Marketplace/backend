package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_RetentionSummaryTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.RetentionSummary dto = DashboardResponseDto.RetentionSummary.builder()
                        .averageRetentionRate(null)
            .averageTurnoverRate(null)
            .averageTenure(null)
            .totalDepartures(42)
            .healthyRetentionCount(42L)
            .highTurnoverCount(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getTotalDepartures());
        assertEquals(42L, dto.getHealthyRetentionCount());
        assertEquals(42L, dto.getHighTurnoverCount());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.RetentionSummary dto = new DashboardResponseDto.RetentionSummary();
        dto.setTotalDepartures(99);
        assertEquals(99, dto.getTotalDepartures());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.RetentionSummary dto1 = DashboardResponseDto.RetentionSummary.builder()
                        .averageRetentionRate(null)
            .averageTurnoverRate(null)
            .averageTenure(null)
            .totalDepartures(42)
            .healthyRetentionCount(42L)
            .highTurnoverCount(42L)
            .build();
        DashboardResponseDto.RetentionSummary dto2 = DashboardResponseDto.RetentionSummary.builder()
                        .averageRetentionRate(null)
            .averageTurnoverRate(null)
            .averageTenure(null)
            .totalDepartures(42)
            .healthyRetentionCount(42L)
            .highTurnoverCount(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.RetentionSummary dto = DashboardResponseDto.RetentionSummary.builder()
                        .averageRetentionRate(null)
            .averageTurnoverRate(null)
            .averageTenure(null)
            .totalDepartures(42)
            .healthyRetentionCount(42L)
            .highTurnoverCount(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}