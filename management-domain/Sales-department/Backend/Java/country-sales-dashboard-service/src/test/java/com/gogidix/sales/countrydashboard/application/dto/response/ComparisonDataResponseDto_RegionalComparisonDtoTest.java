package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.ComparisonDataResponseDto;
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
class ComparisonDataResponseDto_RegionalComparisonDtoTest {

        @Test
    void testBuilder() {
        ComparisonDataResponseDto.RegionalComparisonDto dto = ComparisonDataResponseDto.RegionalComparisonDto.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(BigDecimal.TEN)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-region", dto.getRegion());
        assertEquals(BigDecimal.TEN, dto.getRegionalAverage());
        assertEquals("test-rank", dto.getRank());
        assertEquals(BigDecimal.TEN, dto.getPercentile());
    }

    @Test
    void testSettersAndGetters() {
        ComparisonDataResponseDto.RegionalComparisonDto dto = new ComparisonDataResponseDto.RegionalComparisonDto();
        dto.setRegion("val-region");
        dto.setRegionalAverage(BigDecimal.ONE);
        dto.setRank("val-rank");
        dto.setPercentile(BigDecimal.ONE);
        assertEquals("val-region", dto.getRegion());
        assertEquals(BigDecimal.ONE, dto.getRegionalAverage());
        assertEquals("val-rank", dto.getRank());
        assertEquals(BigDecimal.ONE, dto.getPercentile());
    }

    @Test
    void testEqualsAndHashCode() {
        ComparisonDataResponseDto.RegionalComparisonDto dto1 = ComparisonDataResponseDto.RegionalComparisonDto.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(BigDecimal.TEN)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        ComparisonDataResponseDto.RegionalComparisonDto dto2 = ComparisonDataResponseDto.RegionalComparisonDto.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(BigDecimal.TEN)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComparisonDataResponseDto.RegionalComparisonDto dto = ComparisonDataResponseDto.RegionalComparisonDto.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(BigDecimal.TEN)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}