package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Insight;
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
class Insight_MetricReferenceTest {

        @Test
    void testBuilder() {
        Insight.MetricReference dto = Insight.MetricReference.builder()
                        .metricName("test-metricName")
            .metricCode("test-metricCode")
            .currentValue(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .variancePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        assertNotNull(dto);
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-metricCode", dto.getMetricCode());
        assertEquals(BigDecimal.TEN, dto.getCurrentValue());
        assertEquals(BigDecimal.TEN, dto.getPreviousValue());
        assertEquals(BigDecimal.TEN, dto.getTargetValue());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals("test-varianceDirection", dto.getVarianceDirection());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals("test-trend", dto.getTrend());
    }

    @Test
    void testSettersAndGetters() {
        Insight.MetricReference dto = new Insight.MetricReference();
        dto.setMetricName("val-metricName");
        dto.setMetricCode("val-metricCode");
        dto.setCurrentValue(BigDecimal.ONE);
        dto.setPreviousValue(BigDecimal.ONE);
        dto.setTargetValue(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setVarianceDirection("val-varianceDirection");
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setTrend("val-trend");
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-metricCode", dto.getMetricCode());
        assertEquals(BigDecimal.ONE, dto.getCurrentValue());
        assertEquals(BigDecimal.ONE, dto.getPreviousValue());
        assertEquals(BigDecimal.ONE, dto.getTargetValue());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals("val-varianceDirection", dto.getVarianceDirection());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals("val-trend", dto.getTrend());
    }

    @Test
    void testEqualsAndHashCode() {
        Insight.MetricReference dto1 = Insight.MetricReference.builder()
                        .metricName("test-metricName")
            .metricCode("test-metricCode")
            .currentValue(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .variancePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        Insight.MetricReference dto2 = Insight.MetricReference.builder()
                        .metricName("test-metricName")
            .metricCode("test-metricCode")
            .currentValue(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .variancePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Insight.MetricReference dto = Insight.MetricReference.builder()
                        .metricName("test-metricName")
            .metricCode("test-metricCode")
            .currentValue(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .targetValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .varianceDirection("test-varianceDirection")
            .variancePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}