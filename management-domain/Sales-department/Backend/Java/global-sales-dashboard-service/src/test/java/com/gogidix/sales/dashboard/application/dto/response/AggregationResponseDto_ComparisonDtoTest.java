package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.AggregationResponseDto;
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
class AggregationResponseDto_ComparisonDtoTest {

        @Test
    void testBuilder() {
        AggregationResponseDto.ComparisonDto dto = AggregationResponseDto.ComparisonDto.builder()
                        .previousPeriodRevenue(null)
            .growthRate(null)
            .difference(null)
            .trend("test-trend")
            .build();
        assertNotNull(dto);
        assertEquals("test-trend", dto.getTrend());
    }

    @Test
    void testSettersAndGetters() {
        AggregationResponseDto.ComparisonDto dto = new AggregationResponseDto.ComparisonDto();
        dto.setTrend("val-trend");
        assertEquals("val-trend", dto.getTrend());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregationResponseDto.ComparisonDto dto1 = AggregationResponseDto.ComparisonDto.builder()
                        .previousPeriodRevenue(null)
            .growthRate(null)
            .difference(null)
            .trend("test-trend")
            .build();
        AggregationResponseDto.ComparisonDto dto2 = AggregationResponseDto.ComparisonDto.builder()
                        .previousPeriodRevenue(null)
            .growthRate(null)
            .difference(null)
            .trend("test-trend")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregationResponseDto.ComparisonDto dto = AggregationResponseDto.ComparisonDto.builder()
                        .previousPeriodRevenue(null)
            .growthRate(null)
            .difference(null)
            .trend("test-trend")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}