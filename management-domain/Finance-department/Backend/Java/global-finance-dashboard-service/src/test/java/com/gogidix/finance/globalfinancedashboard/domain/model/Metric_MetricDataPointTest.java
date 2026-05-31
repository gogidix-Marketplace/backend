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
class Metric_MetricDataPointTest {

        @Test
    void testBuilder() {
        Metric.MetricDataPoint dto = Metric.MetricDataPoint.builder()
                        .date(LocalDate.of(2025,1,15))
            .value(BigDecimal.TEN)
            .label("test-label")
            .formattedValue("test-formattedValue")
            .attributes(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getDate());
        assertEquals(BigDecimal.TEN, dto.getValue());
        assertEquals("test-label", dto.getLabel());
        assertEquals("test-formattedValue", dto.getFormattedValue());
    }

    @Test
    void testSettersAndGetters() {
        Metric.MetricDataPoint dto = new Metric.MetricDataPoint();
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setValue(BigDecimal.ONE);
        dto.setLabel("val-label");
        dto.setFormattedValue("val-formattedValue");
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals("val-label", dto.getLabel());
        assertEquals("val-formattedValue", dto.getFormattedValue());
    }

    @Test
    void testEqualsAndHashCode() {
        Metric.MetricDataPoint dto1 = Metric.MetricDataPoint.builder()
                        .date(LocalDate.of(2025,1,15))
            .value(BigDecimal.TEN)
            .label("test-label")
            .formattedValue("test-formattedValue")
            .attributes(Collections.emptyMap())
            .build();
        Metric.MetricDataPoint dto2 = Metric.MetricDataPoint.builder()
                        .date(LocalDate.of(2025,1,15))
            .value(BigDecimal.TEN)
            .label("test-label")
            .formattedValue("test-formattedValue")
            .attributes(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Metric.MetricDataPoint dto = Metric.MetricDataPoint.builder()
                        .date(LocalDate.of(2025,1,15))
            .value(BigDecimal.TEN)
            .label("test-label")
            .formattedValue("test-formattedValue")
            .attributes(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}