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
class MetricRollup_RollupMetricsTest {

        @Test
    void testBuilder() {
        MetricRollup.RollupMetrics dto = MetricRollup.RollupMetrics.builder()
                        .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(BigDecimal.TEN)
            .newCustomers(42)
            .customerSatisfaction(BigDecimal.TEN)
            .margin(null)
            .marginPercentage(BigDecimal.TEN)
            .activitiesCompleted(42)
            .forecastAccuracy(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getDeals());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(42, dto.getOpportunities());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(42, dto.getNewCustomers());
        assertEquals(BigDecimal.TEN, dto.getCustomerSatisfaction());
        assertEquals(BigDecimal.TEN, dto.getMarginPercentage());
        assertEquals(42, dto.getActivitiesCompleted());
        assertEquals(BigDecimal.TEN, dto.getForecastAccuracy());
    }

    @Test
    void testSettersAndGetters() {
        MetricRollup.RollupMetrics dto = new MetricRollup.RollupMetrics();
        dto.setDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setOpportunities(99);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setNewCustomers(99);
        dto.setCustomerSatisfaction(BigDecimal.ONE);
        dto.setMarginPercentage(BigDecimal.ONE);
        dto.setActivitiesCompleted(99);
        dto.setForecastAccuracy(BigDecimal.ONE);
        assertEquals(99, dto.getDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(99, dto.getOpportunities());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(99, dto.getNewCustomers());
        assertEquals(BigDecimal.ONE, dto.getCustomerSatisfaction());
        assertEquals(BigDecimal.ONE, dto.getMarginPercentage());
        assertEquals(99, dto.getActivitiesCompleted());
        assertEquals(BigDecimal.ONE, dto.getForecastAccuracy());
    }

    @Test
    void testEqualsAndHashCode() {
        MetricRollup.RollupMetrics dto1 = MetricRollup.RollupMetrics.builder()
                        .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(BigDecimal.TEN)
            .newCustomers(42)
            .customerSatisfaction(BigDecimal.TEN)
            .margin(null)
            .marginPercentage(BigDecimal.TEN)
            .activitiesCompleted(42)
            .forecastAccuracy(BigDecimal.TEN)
            .build();
        MetricRollup.RollupMetrics dto2 = MetricRollup.RollupMetrics.builder()
                        .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(BigDecimal.TEN)
            .newCustomers(42)
            .customerSatisfaction(BigDecimal.TEN)
            .margin(null)
            .marginPercentage(BigDecimal.TEN)
            .activitiesCompleted(42)
            .forecastAccuracy(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MetricRollup.RollupMetrics dto = MetricRollup.RollupMetrics.builder()
                        .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(BigDecimal.TEN)
            .newCustomers(42)
            .customerSatisfaction(BigDecimal.TEN)
            .margin(null)
            .marginPercentage(BigDecimal.TEN)
            .activitiesCompleted(42)
            .forecastAccuracy(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}