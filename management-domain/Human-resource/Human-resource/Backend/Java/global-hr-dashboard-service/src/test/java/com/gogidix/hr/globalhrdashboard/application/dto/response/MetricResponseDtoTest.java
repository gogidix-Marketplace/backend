package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.MetricResponseDto;
import com.gogidix.hr.globalhrdashboard.domain.model.AggregationLevel;
import com.gogidix.hr.globalhrdashboard.domain.model.ExecutiveLevel;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricTrend;
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
class MetricResponseDtoTest {

        @Test
    void testBuilder() {
        MetricResponseDto dto = MetricResponseDto.builder()
                        .id("test-id")
            .metricName("test-metricName")
            .metricCategory(MetricCategory.HEADCOUNT)
            .executiveLevel(ExecutiveLevel.CHRO)
            .value(null)
            .previousValue(null)
            .targetValue(null)
            .period("test-period")
            .trend(MetricTrend.UP)
            .aggregationLevel(AggregationLevel.GLOBAL)
            .regionalBreakdown(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .isActive(true)
            .lastAggregated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-period", dto.getPeriod());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        MetricResponseDto dto = new MetricResponseDto();
        dto.setId("val-id");
        dto.setMetricName("val-metricName");
        dto.setPeriod("val-period");
        dto.setIsActive(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-period", dto.getPeriod());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        MetricResponseDto dto1 = MetricResponseDto.builder()
                        .id("test-id")
            .metricName("test-metricName")
            .metricCategory(MetricCategory.HEADCOUNT)
            .executiveLevel(ExecutiveLevel.CHRO)
            .value(null)
            .previousValue(null)
            .targetValue(null)
            .period("test-period")
            .trend(MetricTrend.UP)
            .aggregationLevel(AggregationLevel.GLOBAL)
            .regionalBreakdown(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .isActive(true)
            .lastAggregated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        MetricResponseDto dto2 = MetricResponseDto.builder()
                        .id("test-id")
            .metricName("test-metricName")
            .metricCategory(MetricCategory.HEADCOUNT)
            .executiveLevel(ExecutiveLevel.CHRO)
            .value(null)
            .previousValue(null)
            .targetValue(null)
            .period("test-period")
            .trend(MetricTrend.UP)
            .aggregationLevel(AggregationLevel.GLOBAL)
            .regionalBreakdown(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .isActive(true)
            .lastAggregated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MetricResponseDto dto = MetricResponseDto.builder()
                        .id("test-id")
            .metricName("test-metricName")
            .metricCategory(MetricCategory.HEADCOUNT)
            .executiveLevel(ExecutiveLevel.CHRO)
            .value(null)
            .previousValue(null)
            .targetValue(null)
            .period("test-period")
            .trend(MetricTrend.UP)
            .aggregationLevel(AggregationLevel.GLOBAL)
            .regionalBreakdown(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .isActive(true)
            .lastAggregated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}