package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetMonitorResponseDto;
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
class BudgetMonitorResponseDtoTest {

        @Test
    void testBuilder() {
        BudgetMonitorResponseDto dto = BudgetMonitorResponseDto.builder()
                        .id("test-id")
            .monitorId("test-monitorId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .budgetPeriod("test-budgetPeriod")
            .period(null)
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualExpenditure(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status(BudgetMonitorResponseDto.MonitorStatusDto.ON_TRACK)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .thresholdStatuses(Collections.emptyList())
            .alertRecipients(Collections.emptyList())
            .currency("test-currency")
            .thresholdBreached(true)
            .warningCount(42)
            .criticalCount(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-monitorId", dto.getMonitorId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-budgetId", dto.getBudgetId());
        assertEquals("test-budgetCode", dto.getBudgetCode());
        assertEquals("test-budgetName", dto.getBudgetName());
        assertEquals("test-budgetPeriod", dto.getBudgetPeriod());
        assertEquals(BigDecimal.TEN, dto.getAllocatedAmount());
        assertEquals(BigDecimal.TEN, dto.getCommittedAmount());
        assertEquals(BigDecimal.TEN, dto.getActualExpenditure());
        assertEquals(BigDecimal.TEN, dto.getAvailableBalance());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(BigDecimal.TEN, dto.getUtilizationPercentage());
        assertEquals(BudgetMonitorResponseDto.MonitorStatusDto.ON_TRACK, dto.getStatus());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-fiscalYear", dto.getFiscalYear());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-lastUpdatedBy", dto.getLastUpdatedBy());
        assertEquals("test-currency", dto.getCurrency());
        assertTrue(dto.isThresholdBreached());
        assertEquals(42, dto.getWarningCount());
        assertEquals(42, dto.getCriticalCount());
    }

    @Test
    void testSettersAndGetters() {
        BudgetMonitorResponseDto dto = new BudgetMonitorResponseDto();
        dto.setId("val-id");
        dto.setMonitorId("val-monitorId");
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        dto.setBudgetCode("val-budgetCode");
        dto.setBudgetName("val-budgetName");
        dto.setBudgetPeriod("val-budgetPeriod");
        dto.setAllocatedAmount(BigDecimal.ONE);
        dto.setCommittedAmount(BigDecimal.ONE);
        dto.setActualExpenditure(BigDecimal.ONE);
        dto.setAvailableBalance(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setUtilizationPercentage(BigDecimal.ONE);
        dto.setStatus(BudgetMonitorResponseDto.MonitorStatusDto.ON_TRACK);
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setFiscalYear("val-fiscalYear");
        dto.setCreatedBy("val-createdBy");
        dto.setLastUpdatedBy("val-lastUpdatedBy");
        dto.setCurrency("val-currency");
        dto.setThresholdBreached(true);
        dto.setWarningCount(99);
        dto.setCriticalCount(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-monitorId", dto.getMonitorId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
        assertEquals("val-budgetCode", dto.getBudgetCode());
        assertEquals("val-budgetName", dto.getBudgetName());
        assertEquals("val-budgetPeriod", dto.getBudgetPeriod());
        assertEquals(BigDecimal.ONE, dto.getAllocatedAmount());
        assertEquals(BigDecimal.ONE, dto.getCommittedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualExpenditure());
        assertEquals(BigDecimal.ONE, dto.getAvailableBalance());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(BigDecimal.ONE, dto.getUtilizationPercentage());
        assertEquals(BudgetMonitorResponseDto.MonitorStatusDto.ON_TRACK, dto.getStatus());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-fiscalYear", dto.getFiscalYear());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-lastUpdatedBy", dto.getLastUpdatedBy());
        assertEquals("val-currency", dto.getCurrency());
        assertTrue(dto.isThresholdBreached());
        assertEquals(99, dto.getWarningCount());
        assertEquals(99, dto.getCriticalCount());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorResponseDto dto1 = BudgetMonitorResponseDto.builder()
                        .id("test-id")
            .monitorId("test-monitorId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .budgetPeriod("test-budgetPeriod")
            .period(null)
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualExpenditure(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status(BudgetMonitorResponseDto.MonitorStatusDto.ON_TRACK)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .thresholdStatuses(Collections.emptyList())
            .alertRecipients(Collections.emptyList())
            .currency("test-currency")
            .thresholdBreached(true)
            .warningCount(42)
            .criticalCount(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BudgetMonitorResponseDto dto2 = BudgetMonitorResponseDto.builder()
                        .id("test-id")
            .monitorId("test-monitorId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .budgetPeriod("test-budgetPeriod")
            .period(null)
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualExpenditure(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status(BudgetMonitorResponseDto.MonitorStatusDto.ON_TRACK)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .thresholdStatuses(Collections.emptyList())
            .alertRecipients(Collections.emptyList())
            .currency("test-currency")
            .thresholdBreached(true)
            .warningCount(42)
            .criticalCount(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetMonitorResponseDto dto = BudgetMonitorResponseDto.builder()
                        .id("test-id")
            .monitorId("test-monitorId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .budgetPeriod("test-budgetPeriod")
            .period(null)
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualExpenditure(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status(BudgetMonitorResponseDto.MonitorStatusDto.ON_TRACK)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .thresholdStatuses(Collections.emptyList())
            .alertRecipients(Collections.emptyList())
            .currency("test-currency")
            .thresholdBreached(true)
            .warningCount(42)
            .criticalCount(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}