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
class BudgetTrackingSummaryDtoTest {

        @Test
    void testBuilder() {
        BudgetTrackingSummaryDto dto = BudgetTrackingSummaryDto.builder()
                        .tenantId("test-tenantId")
            .period("test-period")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .overallMetrics(null)
            .categorySummaries(Collections.emptyList())
            .departmentSummaries(Collections.emptyList())
            .budgetHealthStatus(Collections.emptyList())
            .totalBudgets(42)
            .onTrackCount(42)
            .attentionRequiredCount(42)
            .criticalCount(42)
            .exhaustedCount(42)
            .overBudgetCount(42)
            .totalAllocated(BigDecimal.TEN)
            .totalCommitted(BigDecimal.TEN)
            .totalActualExpenditure(BigDecimal.TEN)
            .totalAvailable(BigDecimal.TEN)
            .overallUtilizationPercentage(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(42, dto.getTotalBudgets());
        assertEquals(42, dto.getOnTrackCount());
        assertEquals(42, dto.getAttentionRequiredCount());
        assertEquals(42, dto.getCriticalCount());
        assertEquals(42, dto.getExhaustedCount());
        assertEquals(42, dto.getOverBudgetCount());
        assertEquals(BigDecimal.TEN, dto.getTotalAllocated());
        assertEquals(BigDecimal.TEN, dto.getTotalCommitted());
        assertEquals(BigDecimal.TEN, dto.getTotalActualExpenditure());
        assertEquals(BigDecimal.TEN, dto.getTotalAvailable());
        assertEquals(BigDecimal.TEN, dto.getOverallUtilizationPercentage());
    }

    @Test
    void testSettersAndGetters() {
        BudgetTrackingSummaryDto dto = new BudgetTrackingSummaryDto();
        dto.setTenantId("val-tenantId");
        dto.setPeriod("val-period");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setTotalBudgets(99);
        dto.setOnTrackCount(99);
        dto.setAttentionRequiredCount(99);
        dto.setCriticalCount(99);
        dto.setExhaustedCount(99);
        dto.setOverBudgetCount(99);
        dto.setTotalAllocated(BigDecimal.ONE);
        dto.setTotalCommitted(BigDecimal.ONE);
        dto.setTotalActualExpenditure(BigDecimal.ONE);
        dto.setTotalAvailable(BigDecimal.ONE);
        dto.setOverallUtilizationPercentage(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getTotalBudgets());
        assertEquals(99, dto.getOnTrackCount());
        assertEquals(99, dto.getAttentionRequiredCount());
        assertEquals(99, dto.getCriticalCount());
        assertEquals(99, dto.getExhaustedCount());
        assertEquals(99, dto.getOverBudgetCount());
        assertEquals(BigDecimal.ONE, dto.getTotalAllocated());
        assertEquals(BigDecimal.ONE, dto.getTotalCommitted());
        assertEquals(BigDecimal.ONE, dto.getTotalActualExpenditure());
        assertEquals(BigDecimal.ONE, dto.getTotalAvailable());
        assertEquals(BigDecimal.ONE, dto.getOverallUtilizationPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingSummaryDto dto1 = BudgetTrackingSummaryDto.builder()
                        .tenantId("test-tenantId")
            .period("test-period")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .overallMetrics(null)
            .categorySummaries(Collections.emptyList())
            .departmentSummaries(Collections.emptyList())
            .budgetHealthStatus(Collections.emptyList())
            .totalBudgets(42)
            .onTrackCount(42)
            .attentionRequiredCount(42)
            .criticalCount(42)
            .exhaustedCount(42)
            .overBudgetCount(42)
            .totalAllocated(BigDecimal.TEN)
            .totalCommitted(BigDecimal.TEN)
            .totalActualExpenditure(BigDecimal.TEN)
            .totalAvailable(BigDecimal.TEN)
            .overallUtilizationPercentage(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BudgetTrackingSummaryDto dto2 = BudgetTrackingSummaryDto.builder()
                        .tenantId("test-tenantId")
            .period("test-period")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .overallMetrics(null)
            .categorySummaries(Collections.emptyList())
            .departmentSummaries(Collections.emptyList())
            .budgetHealthStatus(Collections.emptyList())
            .totalBudgets(42)
            .onTrackCount(42)
            .attentionRequiredCount(42)
            .criticalCount(42)
            .exhaustedCount(42)
            .overBudgetCount(42)
            .totalAllocated(BigDecimal.TEN)
            .totalCommitted(BigDecimal.TEN)
            .totalActualExpenditure(BigDecimal.TEN)
            .totalAvailable(BigDecimal.TEN)
            .overallUtilizationPercentage(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetTrackingSummaryDto dto = BudgetTrackingSummaryDto.builder()
                        .tenantId("test-tenantId")
            .period("test-period")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .overallMetrics(null)
            .categorySummaries(Collections.emptyList())
            .departmentSummaries(Collections.emptyList())
            .budgetHealthStatus(Collections.emptyList())
            .totalBudgets(42)
            .onTrackCount(42)
            .attentionRequiredCount(42)
            .criticalCount(42)
            .exhaustedCount(42)
            .overBudgetCount(42)
            .totalAllocated(BigDecimal.TEN)
            .totalCommitted(BigDecimal.TEN)
            .totalActualExpenditure(BigDecimal.TEN)
            .totalAvailable(BigDecimal.TEN)
            .overallUtilizationPercentage(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}