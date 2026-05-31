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
class AggregatedMetrics_MetricVarianceTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.MetricVariance dto = AggregatedMetrics.MetricVariance.builder()
                        .metricName("test-metricName")
            .actualValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .isWithinTolerance(true)
            .severity("test-severity")
            .build();
        assertNotNull(dto);
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals(BigDecimal.TEN, dto.getActualValue());
        assertEquals(BigDecimal.TEN, dto.getTargetValue());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals("test-varianceDirection", dto.getVarianceDirection());
        assertTrue(dto.getIsWithinTolerance());
        assertEquals("test-severity", dto.getSeverity());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.MetricVariance dto = new AggregatedMetrics.MetricVariance();
        dto.setMetricName("val-metricName");
        dto.setActualValue(BigDecimal.ONE);
        dto.setTargetValue(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setVarianceDirection("val-varianceDirection");
        dto.setIsWithinTolerance(true);
        dto.setSeverity("val-severity");
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals(BigDecimal.ONE, dto.getActualValue());
        assertEquals(BigDecimal.ONE, dto.getTargetValue());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals("val-varianceDirection", dto.getVarianceDirection());
        assertTrue(dto.getIsWithinTolerance());
        assertEquals("val-severity", dto.getSeverity());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.MetricVariance dto1 = AggregatedMetrics.MetricVariance.builder()
                        .metricName("test-metricName")
            .actualValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .isWithinTolerance(true)
            .severity("test-severity")
            .build();
        AggregatedMetrics.MetricVariance dto2 = AggregatedMetrics.MetricVariance.builder()
                        .metricName("test-metricName")
            .actualValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .isWithinTolerance(true)
            .severity("test-severity")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.MetricVariance dto = AggregatedMetrics.MetricVariance.builder()
                        .metricName("test-metricName")
            .actualValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .isWithinTolerance(true)
            .severity("test-severity")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}