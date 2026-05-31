package com.gogidix.sales.analytics.domain.model;

import com.gogidix.sales.analytics.domain.model.PipelineMetric;
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
class PipelineMetric_StageMetricTest {

        @Test
    void testBuilder() {
        PipelineMetric.StageMetric dto = PipelineMetric.StageMetric.builder()
                        .stageName("test-stageName")
            .value(BigDecimal.TEN)
            .dealCount(42)
            .averageDealSize(BigDecimal.TEN)
            .conversionRate(BigDecimal.TEN)
            .averageDurationDays(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-stageName", dto.getStageName());
        assertEquals(BigDecimal.TEN, dto.getValue());
        assertEquals(42, dto.getDealCount());
        assertEquals(BigDecimal.TEN, dto.getAverageDealSize());
        assertEquals(BigDecimal.TEN, dto.getConversionRate());
        assertEquals(BigDecimal.TEN, dto.getAverageDurationDays());
    }

    @Test
    void testSettersAndGetters() {
        PipelineMetric.StageMetric dto = new PipelineMetric.StageMetric();
        dto.setStageName("val-stageName");
        dto.setValue(BigDecimal.ONE);
        dto.setDealCount(99);
        dto.setAverageDealSize(BigDecimal.ONE);
        dto.setConversionRate(BigDecimal.ONE);
        dto.setAverageDurationDays(BigDecimal.ONE);
        assertEquals("val-stageName", dto.getStageName());
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals(99, dto.getDealCount());
        assertEquals(BigDecimal.ONE, dto.getAverageDealSize());
        assertEquals(BigDecimal.ONE, dto.getConversionRate());
        assertEquals(BigDecimal.ONE, dto.getAverageDurationDays());
    }

    @Test
    void testEqualsAndHashCode() {
        PipelineMetric.StageMetric dto1 = PipelineMetric.StageMetric.builder()
                        .stageName("test-stageName")
            .value(BigDecimal.TEN)
            .dealCount(42)
            .averageDealSize(BigDecimal.TEN)
            .conversionRate(BigDecimal.TEN)
            .averageDurationDays(BigDecimal.TEN)
            .build();
        PipelineMetric.StageMetric dto2 = PipelineMetric.StageMetric.builder()
                        .stageName("test-stageName")
            .value(BigDecimal.TEN)
            .dealCount(42)
            .averageDealSize(BigDecimal.TEN)
            .conversionRate(BigDecimal.TEN)
            .averageDurationDays(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PipelineMetric.StageMetric dto = PipelineMetric.StageMetric.builder()
                        .stageName("test-stageName")
            .value(BigDecimal.TEN)
            .dealCount(42)
            .averageDealSize(BigDecimal.TEN)
            .conversionRate(BigDecimal.TEN)
            .averageDurationDays(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}