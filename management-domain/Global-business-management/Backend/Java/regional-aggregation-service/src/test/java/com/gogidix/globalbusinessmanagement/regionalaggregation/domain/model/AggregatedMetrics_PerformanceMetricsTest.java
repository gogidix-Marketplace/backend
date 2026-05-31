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
class AggregatedMetrics_PerformanceMetricsTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.PerformanceMetrics dto = AggregatedMetrics.PerformanceMetrics.builder()
                        .efficiencyScore(BigDecimal.TEN)
            .productivityScore(BigDecimal.TEN)
            .qualityScore(BigDecimal.TEN)
            .innovationScore(BigDecimal.TEN)
            .agilityScore(BigDecimal.TEN)
            .overallPerformanceScore(BigDecimal.TEN)
            .performanceGrade("test-performanceGrade")
            .percentileRank(42)
            .yearOverYearImprovement(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getEfficiencyScore());
        assertEquals(BigDecimal.TEN, dto.getProductivityScore());
        assertEquals(BigDecimal.TEN, dto.getQualityScore());
        assertEquals(BigDecimal.TEN, dto.getInnovationScore());
        assertEquals(BigDecimal.TEN, dto.getAgilityScore());
        assertEquals(BigDecimal.TEN, dto.getOverallPerformanceScore());
        assertEquals("test-performanceGrade", dto.getPerformanceGrade());
        assertEquals(42, dto.getPercentileRank());
        assertEquals(BigDecimal.TEN, dto.getYearOverYearImprovement());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.PerformanceMetrics dto = new AggregatedMetrics.PerformanceMetrics();
        dto.setEfficiencyScore(BigDecimal.ONE);
        dto.setProductivityScore(BigDecimal.ONE);
        dto.setQualityScore(BigDecimal.ONE);
        dto.setInnovationScore(BigDecimal.ONE);
        dto.setAgilityScore(BigDecimal.ONE);
        dto.setOverallPerformanceScore(BigDecimal.ONE);
        dto.setPerformanceGrade("val-performanceGrade");
        dto.setPercentileRank(99);
        dto.setYearOverYearImprovement(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getEfficiencyScore());
        assertEquals(BigDecimal.ONE, dto.getProductivityScore());
        assertEquals(BigDecimal.ONE, dto.getQualityScore());
        assertEquals(BigDecimal.ONE, dto.getInnovationScore());
        assertEquals(BigDecimal.ONE, dto.getAgilityScore());
        assertEquals(BigDecimal.ONE, dto.getOverallPerformanceScore());
        assertEquals("val-performanceGrade", dto.getPerformanceGrade());
        assertEquals(99, dto.getPercentileRank());
        assertEquals(BigDecimal.ONE, dto.getYearOverYearImprovement());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.PerformanceMetrics dto1 = AggregatedMetrics.PerformanceMetrics.builder()
                        .efficiencyScore(BigDecimal.TEN)
            .productivityScore(BigDecimal.TEN)
            .qualityScore(BigDecimal.TEN)
            .innovationScore(BigDecimal.TEN)
            .agilityScore(BigDecimal.TEN)
            .overallPerformanceScore(BigDecimal.TEN)
            .performanceGrade("test-performanceGrade")
            .percentileRank(42)
            .yearOverYearImprovement(BigDecimal.TEN)
            .build();
        AggregatedMetrics.PerformanceMetrics dto2 = AggregatedMetrics.PerformanceMetrics.builder()
                        .efficiencyScore(BigDecimal.TEN)
            .productivityScore(BigDecimal.TEN)
            .qualityScore(BigDecimal.TEN)
            .innovationScore(BigDecimal.TEN)
            .agilityScore(BigDecimal.TEN)
            .overallPerformanceScore(BigDecimal.TEN)
            .performanceGrade("test-performanceGrade")
            .percentileRank(42)
            .yearOverYearImprovement(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.PerformanceMetrics dto = AggregatedMetrics.PerformanceMetrics.builder()
                        .efficiencyScore(BigDecimal.TEN)
            .productivityScore(BigDecimal.TEN)
            .qualityScore(BigDecimal.TEN)
            .innovationScore(BigDecimal.TEN)
            .agilityScore(BigDecimal.TEN)
            .overallPerformanceScore(BigDecimal.TEN)
            .performanceGrade("test-performanceGrade")
            .percentileRank(42)
            .yearOverYearImprovement(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}