package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.RegionalData;
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
class RegionalData_RegionalBenchmarkingTest {

        @Test
    void testBuilder() {
        RegionalData.RegionalBenchmarking dto = RegionalData.RegionalBenchmarking.builder()
                        .vsGlobalAverage(BigDecimal.TEN)
            .vsGlobalAverageDirection("test-vsGlobalAverageDirection")
            .vsTopPerformer(BigDecimal.TEN)
            .vsPreviousPeriod(BigDecimal.TEN)
            .vsPreviousPeriodDirection("test-vsPreviousPeriodDirection")
            .globalRank(42)
            .regionalRank(42)
            .performanceScore(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getVsGlobalAverage());
        assertEquals("test-vsGlobalAverageDirection", dto.getVsGlobalAverageDirection());
        assertEquals(BigDecimal.TEN, dto.getVsTopPerformer());
        assertEquals(BigDecimal.TEN, dto.getVsPreviousPeriod());
        assertEquals("test-vsPreviousPeriodDirection", dto.getVsPreviousPeriodDirection());
        assertEquals(42, dto.getGlobalRank());
        assertEquals(42, dto.getRegionalRank());
        assertEquals(BigDecimal.TEN, dto.getPerformanceScore());
    }

    @Test
    void testSettersAndGetters() {
        RegionalData.RegionalBenchmarking dto = new RegionalData.RegionalBenchmarking();
        dto.setVsGlobalAverage(BigDecimal.ONE);
        dto.setVsGlobalAverageDirection("val-vsGlobalAverageDirection");
        dto.setVsTopPerformer(BigDecimal.ONE);
        dto.setVsPreviousPeriod(BigDecimal.ONE);
        dto.setVsPreviousPeriodDirection("val-vsPreviousPeriodDirection");
        dto.setGlobalRank(99);
        dto.setRegionalRank(99);
        dto.setPerformanceScore(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getVsGlobalAverage());
        assertEquals("val-vsGlobalAverageDirection", dto.getVsGlobalAverageDirection());
        assertEquals(BigDecimal.ONE, dto.getVsTopPerformer());
        assertEquals(BigDecimal.ONE, dto.getVsPreviousPeriod());
        assertEquals("val-vsPreviousPeriodDirection", dto.getVsPreviousPeriodDirection());
        assertEquals(99, dto.getGlobalRank());
        assertEquals(99, dto.getRegionalRank());
        assertEquals(BigDecimal.ONE, dto.getPerformanceScore());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalData.RegionalBenchmarking dto1 = RegionalData.RegionalBenchmarking.builder()
                        .vsGlobalAverage(BigDecimal.TEN)
            .vsGlobalAverageDirection("test-vsGlobalAverageDirection")
            .vsTopPerformer(BigDecimal.TEN)
            .vsPreviousPeriod(BigDecimal.TEN)
            .vsPreviousPeriodDirection("test-vsPreviousPeriodDirection")
            .globalRank(42)
            .regionalRank(42)
            .performanceScore(BigDecimal.TEN)
            .build();
        RegionalData.RegionalBenchmarking dto2 = RegionalData.RegionalBenchmarking.builder()
                        .vsGlobalAverage(BigDecimal.TEN)
            .vsGlobalAverageDirection("test-vsGlobalAverageDirection")
            .vsTopPerformer(BigDecimal.TEN)
            .vsPreviousPeriod(BigDecimal.TEN)
            .vsPreviousPeriodDirection("test-vsPreviousPeriodDirection")
            .globalRank(42)
            .regionalRank(42)
            .performanceScore(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalData.RegionalBenchmarking dto = RegionalData.RegionalBenchmarking.builder()
                        .vsGlobalAverage(BigDecimal.TEN)
            .vsGlobalAverageDirection("test-vsGlobalAverageDirection")
            .vsTopPerformer(BigDecimal.TEN)
            .vsPreviousPeriod(BigDecimal.TEN)
            .vsPreviousPeriodDirection("test-vsPreviousPeriodDirection")
            .globalRank(42)
            .regionalRank(42)
            .performanceScore(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}