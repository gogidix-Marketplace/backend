package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetTrackingSummaryDto;
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
class BudgetTrackingSummaryDto_SummaryMetricsDtoTest {

        @Test
    void testBuilder() {
        BudgetTrackingSummaryDto.SummaryMetricsDto dto = BudgetTrackingSummaryDto.SummaryMetricsDto.builder()
                        .averageUtilizationPercentage(BigDecimal.TEN)
            .medianVariance(BigDecimal.TEN)
            .highestUtilization(BigDecimal.TEN)
            .lowestUtilization(BigDecimal.TEN)
            .totalTransactions(42)
            .pendingApprovals(42)
            .alertsTriggered(42)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAverageUtilizationPercentage());
        assertEquals(BigDecimal.TEN, dto.getMedianVariance());
        assertEquals(BigDecimal.TEN, dto.getHighestUtilization());
        assertEquals(BigDecimal.TEN, dto.getLowestUtilization());
        assertEquals(42, dto.getTotalTransactions());
        assertEquals(42, dto.getPendingApprovals());
        assertEquals(42, dto.getAlertsTriggered());
    }

    @Test
    void testSettersAndGetters() {
        BudgetTrackingSummaryDto.SummaryMetricsDto dto = new BudgetTrackingSummaryDto.SummaryMetricsDto();
        dto.setAverageUtilizationPercentage(BigDecimal.ONE);
        dto.setMedianVariance(BigDecimal.ONE);
        dto.setHighestUtilization(BigDecimal.ONE);
        dto.setLowestUtilization(BigDecimal.ONE);
        dto.setTotalTransactions(99);
        dto.setPendingApprovals(99);
        dto.setAlertsTriggered(99);
        assertEquals(BigDecimal.ONE, dto.getAverageUtilizationPercentage());
        assertEquals(BigDecimal.ONE, dto.getMedianVariance());
        assertEquals(BigDecimal.ONE, dto.getHighestUtilization());
        assertEquals(BigDecimal.ONE, dto.getLowestUtilization());
        assertEquals(99, dto.getTotalTransactions());
        assertEquals(99, dto.getPendingApprovals());
        assertEquals(99, dto.getAlertsTriggered());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingSummaryDto.SummaryMetricsDto dto1 = BudgetTrackingSummaryDto.SummaryMetricsDto.builder()
                        .averageUtilizationPercentage(BigDecimal.TEN)
            .medianVariance(BigDecimal.TEN)
            .highestUtilization(BigDecimal.TEN)
            .lowestUtilization(BigDecimal.TEN)
            .totalTransactions(42)
            .pendingApprovals(42)
            .alertsTriggered(42)
            .build();
        BudgetTrackingSummaryDto.SummaryMetricsDto dto2 = BudgetTrackingSummaryDto.SummaryMetricsDto.builder()
                        .averageUtilizationPercentage(BigDecimal.TEN)
            .medianVariance(BigDecimal.TEN)
            .highestUtilization(BigDecimal.TEN)
            .lowestUtilization(BigDecimal.TEN)
            .totalTransactions(42)
            .pendingApprovals(42)
            .alertsTriggered(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetTrackingSummaryDto.SummaryMetricsDto dto = BudgetTrackingSummaryDto.SummaryMetricsDto.builder()
                        .averageUtilizationPercentage(BigDecimal.TEN)
            .medianVariance(BigDecimal.TEN)
            .highestUtilization(BigDecimal.TEN)
            .lowestUtilization(BigDecimal.TEN)
            .totalTransactions(42)
            .pendingApprovals(42)
            .alertsTriggered(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}