package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.MetricRollup;
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
class MetricRollup_PerformanceIndicatorsTest {

        @Test
    void testBuilder() {
        MetricRollup.PerformanceIndicators dto = MetricRollup.PerformanceIndicators.builder()
                        .overallStatus("test-overallStatus")
            .score(BigDecimal.TEN)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .threats(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-overallStatus", dto.getOverallStatus());
        assertEquals(BigDecimal.TEN, dto.getScore());
        assertEquals("test-trend", dto.getTrend());
        assertEquals("test-recommendation", dto.getRecommendation());
        assertEquals(42, dto.getRiskLevel());
    }

    @Test
    void testSettersAndGetters() {
        MetricRollup.PerformanceIndicators dto = new MetricRollup.PerformanceIndicators();
        dto.setOverallStatus("val-overallStatus");
        dto.setScore(BigDecimal.ONE);
        dto.setTrend("val-trend");
        dto.setRecommendation("val-recommendation");
        dto.setRiskLevel(99);
        assertEquals("val-overallStatus", dto.getOverallStatus());
        assertEquals(BigDecimal.ONE, dto.getScore());
        assertEquals("val-trend", dto.getTrend());
        assertEquals("val-recommendation", dto.getRecommendation());
        assertEquals(99, dto.getRiskLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        MetricRollup.PerformanceIndicators dto1 = MetricRollup.PerformanceIndicators.builder()
                        .overallStatus("test-overallStatus")
            .score(BigDecimal.TEN)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .threats(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .build();
        MetricRollup.PerformanceIndicators dto2 = MetricRollup.PerformanceIndicators.builder()
                        .overallStatus("test-overallStatus")
            .score(BigDecimal.TEN)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .threats(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MetricRollup.PerformanceIndicators dto = MetricRollup.PerformanceIndicators.builder()
                        .overallStatus("test-overallStatus")
            .score(BigDecimal.TEN)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .threats(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}