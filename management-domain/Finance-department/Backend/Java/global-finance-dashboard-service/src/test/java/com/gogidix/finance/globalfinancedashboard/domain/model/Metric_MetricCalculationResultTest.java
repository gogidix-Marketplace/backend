package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.Metric;
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
class Metric_MetricCalculationResultTest {

        @Test
    void testBuilder() {
        Metric.MetricCalculationResult dto = Metric.MetricCalculationResult.builder()
                        .metricId("test-metricId")
            .value(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .trend(null)
            .formattedValue("test-formattedValue")
            .dataPoints(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .status(null)
            .errorMessage("test-errorMessage")
            .build();
        assertNotNull(dto);
        assertEquals("test-metricId", dto.getMetricId());
        assertEquals(BigDecimal.TEN, dto.getValue());
        assertEquals(BigDecimal.TEN, dto.getPreviousValue());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals("test-formattedValue", dto.getFormattedValue());
        assertEquals("test-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testSettersAndGetters() {
        Metric.MetricCalculationResult dto = new Metric.MetricCalculationResult();
        dto.setMetricId("val-metricId");
        dto.setValue(BigDecimal.ONE);
        dto.setPreviousValue(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setFormattedValue("val-formattedValue");
        dto.setErrorMessage("val-errorMessage");
        assertEquals("val-metricId", dto.getMetricId());
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals(BigDecimal.ONE, dto.getPreviousValue());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals("val-formattedValue", dto.getFormattedValue());
        assertEquals("val-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        Metric.MetricCalculationResult dto1 = Metric.MetricCalculationResult.builder()
                        .metricId("test-metricId")
            .value(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .trend(null)
            .formattedValue("test-formattedValue")
            .dataPoints(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .status(null)
            .errorMessage("test-errorMessage")
            .build();
        Metric.MetricCalculationResult dto2 = Metric.MetricCalculationResult.builder()
                        .metricId("test-metricId")
            .value(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .trend(null)
            .formattedValue("test-formattedValue")
            .dataPoints(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .status(null)
            .errorMessage("test-errorMessage")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Metric.MetricCalculationResult dto = Metric.MetricCalculationResult.builder()
                        .metricId("test-metricId")
            .value(BigDecimal.TEN)
            .previousValue(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .trend(null)
            .formattedValue("test-formattedValue")
            .dataPoints(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .status(null)
            .errorMessage("test-errorMessage")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}