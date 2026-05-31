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
class Insight_InsightTrendsTest {

        @Test
    void testBuilder() {
        Insight.InsightTrends dto = Insight.InsightTrends.builder()
                        .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendDuration(42)
            .trendPattern("test-trendPattern")
            .seasonalityIndex(BigDecimal.TEN)
            .historicalValues(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-trendDirection", dto.getTrendDirection());
        assertEquals(BigDecimal.TEN, dto.getTrendStrength());
        assertEquals(42, dto.getTrendDuration());
        assertEquals("test-trendPattern", dto.getTrendPattern());
        assertEquals(BigDecimal.TEN, dto.getSeasonalityIndex());
    }

    @Test
    void testSettersAndGetters() {
        Insight.InsightTrends dto = new Insight.InsightTrends();
        dto.setTrendDirection("val-trendDirection");
        dto.setTrendStrength(BigDecimal.ONE);
        dto.setTrendDuration(99);
        dto.setTrendPattern("val-trendPattern");
        dto.setSeasonalityIndex(BigDecimal.ONE);
        assertEquals("val-trendDirection", dto.getTrendDirection());
        assertEquals(BigDecimal.ONE, dto.getTrendStrength());
        assertEquals(99, dto.getTrendDuration());
        assertEquals("val-trendPattern", dto.getTrendPattern());
        assertEquals(BigDecimal.ONE, dto.getSeasonalityIndex());
    }

    @Test
    void testEqualsAndHashCode() {
        Insight.InsightTrends dto1 = Insight.InsightTrends.builder()
                        .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendDuration(42)
            .trendPattern("test-trendPattern")
            .seasonalityIndex(BigDecimal.TEN)
            .historicalValues(Collections.emptyList())
            .build();
        Insight.InsightTrends dto2 = Insight.InsightTrends.builder()
                        .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendDuration(42)
            .trendPattern("test-trendPattern")
            .seasonalityIndex(BigDecimal.TEN)
            .historicalValues(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Insight.InsightTrends dto = Insight.InsightTrends.builder()
                        .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendDuration(42)
            .trendPattern("test-trendPattern")
            .seasonalityIndex(BigDecimal.TEN)
            .historicalValues(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}