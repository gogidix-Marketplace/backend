package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_TimeSeriesDataDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.TimeSeriesDataDto dto = DashboardResponseDto.TimeSeriesDataDto.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .conversionRate(null)
            .averageDealSize(null)
            .region("test-region")
            .build();
        assertNotNull(dto);
        assertEquals("test-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,1,15), dto.getDate());
        assertEquals(42, dto.getDeals());
        assertEquals("test-region", dto.getRegion());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.TimeSeriesDataDto dto = new DashboardResponseDto.TimeSeriesDataDto();
        dto.setPeriod("val-period");
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setDeals(99);
        dto.setRegion("val-region");
        assertEquals("val-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertEquals(99, dto.getDeals());
        assertEquals("val-region", dto.getRegion());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.TimeSeriesDataDto dto1 = DashboardResponseDto.TimeSeriesDataDto.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .conversionRate(null)
            .averageDealSize(null)
            .region("test-region")
            .build();
        DashboardResponseDto.TimeSeriesDataDto dto2 = DashboardResponseDto.TimeSeriesDataDto.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .conversionRate(null)
            .averageDealSize(null)
            .region("test-region")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.TimeSeriesDataDto dto = DashboardResponseDto.TimeSeriesDataDto.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .conversionRate(null)
            .averageDealSize(null)
            .region("test-region")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}