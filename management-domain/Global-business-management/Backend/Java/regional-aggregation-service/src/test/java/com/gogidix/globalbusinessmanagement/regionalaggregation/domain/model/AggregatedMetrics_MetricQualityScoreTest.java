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
class AggregatedMetrics_MetricQualityScoreTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.MetricQualityScore dto = AggregatedMetrics.MetricQualityScore.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .validity(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .qualityGrade("test-qualityGrade")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCompleteness());
        assertEquals(BigDecimal.TEN, dto.getAccuracy());
        assertEquals(BigDecimal.TEN, dto.getTimeliness());
        assertEquals(BigDecimal.TEN, dto.getConsistency());
        assertEquals(BigDecimal.TEN, dto.getValidity());
        assertEquals(BigDecimal.TEN, dto.getOverallScore());
        assertEquals("test-qualityGrade", dto.getQualityGrade());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.MetricQualityScore dto = new AggregatedMetrics.MetricQualityScore();
        dto.setCompleteness(BigDecimal.ONE);
        dto.setAccuracy(BigDecimal.ONE);
        dto.setTimeliness(BigDecimal.ONE);
        dto.setConsistency(BigDecimal.ONE);
        dto.setValidity(BigDecimal.ONE);
        dto.setOverallScore(BigDecimal.ONE);
        dto.setQualityGrade("val-qualityGrade");
        assertEquals(BigDecimal.ONE, dto.getCompleteness());
        assertEquals(BigDecimal.ONE, dto.getAccuracy());
        assertEquals(BigDecimal.ONE, dto.getTimeliness());
        assertEquals(BigDecimal.ONE, dto.getConsistency());
        assertEquals(BigDecimal.ONE, dto.getValidity());
        assertEquals(BigDecimal.ONE, dto.getOverallScore());
        assertEquals("val-qualityGrade", dto.getQualityGrade());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.MetricQualityScore dto1 = AggregatedMetrics.MetricQualityScore.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .validity(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .qualityGrade("test-qualityGrade")
            .build();
        AggregatedMetrics.MetricQualityScore dto2 = AggregatedMetrics.MetricQualityScore.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .validity(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .qualityGrade("test-qualityGrade")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.MetricQualityScore dto = AggregatedMetrics.MetricQualityScore.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .validity(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .qualityGrade("test-qualityGrade")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}