package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.AggregatedMetrics;
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
class AggregatedMetrics_RegionalComparisonTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.RegionalComparison dto = AggregatedMetrics.RegionalComparison.builder()
                        .compareRegionCode("test-compareRegionCode")
            .compareRegionName("test-compareRegionName")
            .metricName("test-metricName")
            .thisRegionValue(BigDecimal.TEN)
            .compareRegionValue(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .differencePercentage(BigDecimal.TEN)
            .comparisonResult("test-comparisonResult")
            .build();
        assertNotNull(dto);
        assertEquals("test-compareRegionCode", dto.getCompareRegionCode());
        assertEquals("test-compareRegionName", dto.getCompareRegionName());
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals(BigDecimal.TEN, dto.getThisRegionValue());
        assertEquals(BigDecimal.TEN, dto.getCompareRegionValue());
        assertEquals(BigDecimal.TEN, dto.getDifference());
        assertEquals(BigDecimal.TEN, dto.getDifferencePercentage());
        assertEquals("test-comparisonResult", dto.getComparisonResult());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.RegionalComparison dto = new AggregatedMetrics.RegionalComparison();
        dto.setCompareRegionCode("val-compareRegionCode");
        dto.setCompareRegionName("val-compareRegionName");
        dto.setMetricName("val-metricName");
        dto.setThisRegionValue(BigDecimal.ONE);
        dto.setCompareRegionValue(BigDecimal.ONE);
        dto.setDifference(BigDecimal.ONE);
        dto.setDifferencePercentage(BigDecimal.ONE);
        dto.setComparisonResult("val-comparisonResult");
        assertEquals("val-compareRegionCode", dto.getCompareRegionCode());
        assertEquals("val-compareRegionName", dto.getCompareRegionName());
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals(BigDecimal.ONE, dto.getThisRegionValue());
        assertEquals(BigDecimal.ONE, dto.getCompareRegionValue());
        assertEquals(BigDecimal.ONE, dto.getDifference());
        assertEquals(BigDecimal.ONE, dto.getDifferencePercentage());
        assertEquals("val-comparisonResult", dto.getComparisonResult());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.RegionalComparison dto1 = AggregatedMetrics.RegionalComparison.builder()
                        .compareRegionCode("test-compareRegionCode")
            .compareRegionName("test-compareRegionName")
            .metricName("test-metricName")
            .thisRegionValue(BigDecimal.TEN)
            .compareRegionValue(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .differencePercentage(BigDecimal.TEN)
            .comparisonResult("test-comparisonResult")
            .build();
        AggregatedMetrics.RegionalComparison dto2 = AggregatedMetrics.RegionalComparison.builder()
                        .compareRegionCode("test-compareRegionCode")
            .compareRegionName("test-compareRegionName")
            .metricName("test-metricName")
            .thisRegionValue(BigDecimal.TEN)
            .compareRegionValue(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .differencePercentage(BigDecimal.TEN)
            .comparisonResult("test-comparisonResult")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.RegionalComparison dto = AggregatedMetrics.RegionalComparison.builder()
                        .compareRegionCode("test-compareRegionCode")
            .compareRegionName("test-compareRegionName")
            .metricName("test-metricName")
            .thisRegionValue(BigDecimal.TEN)
            .compareRegionValue(BigDecimal.TEN)
            .difference(BigDecimal.TEN)
            .differencePercentage(BigDecimal.TEN)
            .comparisonResult("test-comparisonResult")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}