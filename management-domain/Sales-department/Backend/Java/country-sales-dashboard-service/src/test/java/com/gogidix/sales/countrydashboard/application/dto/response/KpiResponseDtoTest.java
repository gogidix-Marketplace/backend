package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.KpiResponseDto;
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
class KpiResponseDtoTest {

        @Test
    void testBuilder() {
        KpiResponseDto dto = KpiResponseDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(BigDecimal.TEN)
            .currency("test-currency")
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
        assertEquals(BigDecimal.TEN, dto.getMonetaryValue());
        assertEquals("test-currency", dto.getCurrency());
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
        KpiResponseDto dto = new KpiResponseDto();
        dto.setKpiId("val-kpiId");
        dto.setName("val-name");
        dto.setMonetaryValue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setPercentageValue(BigDecimal.ONE);
        dto.setTarget("val-target");
        dto.setAchievement(BigDecimal.ONE);
        dto.setTrend("val-trend");
        dto.setTrendValue(BigDecimal.ONE);
        dto.setWeight(99);
        dto.setIsCritical(true);
        assertEquals("val-kpiId", dto.getKpiId());
        assertEquals("val-name", dto.getName());
        assertEquals(BigDecimal.ONE, dto.getMonetaryValue());
        assertEquals("val-currency", dto.getCurrency());
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
        KpiResponseDto dto1 = KpiResponseDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(BigDecimal.TEN)
            .currency("test-currency")
            .percentageValue(BigDecimal.TEN)
            .target("test-target")
            .achievement(BigDecimal.TEN)
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .weight(42)
            .isCritical(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        KpiResponseDto dto2 = KpiResponseDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(BigDecimal.TEN)
            .currency("test-currency")
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
        KpiResponseDto dto = KpiResponseDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .type(MetricType.TOTAL_REVENUE)
            .value(null)
            .monetaryValue(BigDecimal.TEN)
            .currency("test-currency")
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