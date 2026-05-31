package com.gogidix.finance.forecasting.domain.port.in;

import com.gogidix.finance.forecasting.domain.model.ForecastMetric;
import com.gogidix.finance.forecasting.domain.port.in.ForecastQuery;
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
class ForecastQuery_GetForecastMetricsQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastMetricsQuery dto = new ForecastQuery.GetForecastMetricsQuery();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setMetricCategory("val-metricCategory");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-metricCategory", dto.getMetricCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastMetricsQuery dto1 = new ForecastQuery.GetForecastMetricsQuery();
        ForecastQuery.GetForecastMetricsQuery dto2 = new ForecastQuery.GetForecastMetricsQuery();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setMetricCategory("test");
        dto1.setMetricType(ForecastMetric.MetricType.REVENUE);
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setMetricCategory("test");
        dto2.setMetricType(ForecastMetric.MetricType.REVENUE);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastMetricsQuery dto = new ForecastQuery.GetForecastMetricsQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setMetricCategory("test");
        dto.setMetricType(ForecastMetric.MetricType.REVENUE);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastMetricsQuery dto = new ForecastQuery.GetForecastMetricsQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setMetricCategory("test");
        dto.setMetricType(ForecastMetric.MetricType.REVENUE);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}