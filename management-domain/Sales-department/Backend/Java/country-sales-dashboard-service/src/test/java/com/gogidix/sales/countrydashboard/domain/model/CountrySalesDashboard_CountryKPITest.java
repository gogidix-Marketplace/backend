package com.gogidix.sales.countrydashboard.domain.model;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.valueobject.MetricType;
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
class CountrySalesDashboard_CountryKPITest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.CountryKPI dto = CountrySalesDashboard.CountryKPI.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(null)
            .percentageValue(BigDecimal.TEN)
            .target("test-target")
            .achievement(BigDecimal.TEN)
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .weight(42)
            .isCritical(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-kpiId", dto.getKpiId());
        assertEquals("test-name", dto.getName());
        assertEquals(BigDecimal.TEN, dto.getPercentageValue());
        assertEquals("test-target", dto.getTarget());
        assertEquals(BigDecimal.TEN, dto.getAchievement());
        assertEquals("test-trend", dto.getTrend());
        assertEquals(BigDecimal.TEN, dto.getTrendValue());
        assertEquals(42, dto.getWeight());
        assertTrue(dto.getIsCritical());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.CountryKPI dto = new CountrySalesDashboard.CountryKPI();
        dto.setKpiId("val-kpiId");
        dto.setName("val-name");
        dto.setPercentageValue(BigDecimal.ONE);
        dto.setTarget("val-target");
        dto.setAchievement(BigDecimal.ONE);
        dto.setTrend("val-trend");
        dto.setTrendValue(BigDecimal.ONE);
        dto.setWeight(99);
        dto.setIsCritical(true);
        assertEquals("val-kpiId", dto.getKpiId());
        assertEquals("val-name", dto.getName());
        assertEquals(BigDecimal.ONE, dto.getPercentageValue());
        assertEquals("val-target", dto.getTarget());
        assertEquals(BigDecimal.ONE, dto.getAchievement());
        assertEquals("val-trend", dto.getTrend());
        assertEquals(BigDecimal.ONE, dto.getTrendValue());
        assertEquals(99, dto.getWeight());
        assertTrue(dto.getIsCritical());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.CountryKPI dto1 = CountrySalesDashboard.CountryKPI.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(null)
            .percentageValue(BigDecimal.TEN)
            .target("test-target")
            .achievement(BigDecimal.TEN)
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .weight(42)
            .isCritical(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CountrySalesDashboard.CountryKPI dto2 = CountrySalesDashboard.CountryKPI.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(null)
            .percentageValue(BigDecimal.TEN)
            .target("test-target")
            .achievement(BigDecimal.TEN)
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .weight(42)
            .isCritical(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySalesDashboard.CountryKPI dto = CountrySalesDashboard.CountryKPI.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(null)
            .percentageValue(BigDecimal.TEN)
            .target("test-target")
            .achievement(BigDecimal.TEN)
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .weight(42)
            .isCritical(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}