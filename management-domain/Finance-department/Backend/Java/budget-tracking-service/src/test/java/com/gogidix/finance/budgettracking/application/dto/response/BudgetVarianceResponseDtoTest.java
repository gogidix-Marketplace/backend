package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetVarianceResponseDto;
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
class BudgetVarianceResponseDtoTest {

        @Test
    void testBuilder() {
        BudgetVarianceResponseDto dto = BudgetVarianceResponseDto.builder()
                        .id("test-id")
            .varianceId("test-varianceId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .period("test-period")
            .yearMonth(null)
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceStatus(BudgetVarianceResponseDto.VarianceStatusDto.FAVORABLE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .analysis("test-analysis")
            .breakdowns(Collections.emptyList())
            .contributingFactors(Collections.emptyList())
            .requiresInvestigation(true)
            .assignedTo("test-assignedTo")
            .investigationDueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .investigated(true)
            .investigatedBy("test-investigatedBy")
            .investigatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .investigationNotes("test-investigationNotes")
            .forecastedAmount(BigDecimal.TEN)
            .forecastVariance(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-varianceId", dto.getVarianceId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-budgetId", dto.getBudgetId());
        assertEquals("test-budgetCode", dto.getBudgetCode());
        assertEquals("test-budgetName", dto.getBudgetName());
        assertEquals("test-period", dto.getPeriod());
        assertEquals(BigDecimal.TEN, dto.getBudgetedAmount());
        assertEquals(BigDecimal.TEN, dto.getActualAmount());
        assertEquals(BigDecimal.TEN, dto.getCommittedAmount());
        assertEquals(BigDecimal.TEN, dto.getVarianceAmount());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals(BudgetVarianceResponseDto.VarianceStatusDto.FAVORABLE, dto.getVarianceStatus());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-fiscalYear", dto.getFiscalYear());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-lastUpdatedBy", dto.getLastUpdatedBy());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-analysis", dto.getAnalysis());
        assertTrue(dto.isRequiresInvestigation());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertTrue(dto.isInvestigated());
        assertEquals("test-investigatedBy", dto.getInvestigatedBy());
        assertEquals("test-investigationNotes", dto.getInvestigationNotes());
        assertEquals(BigDecimal.TEN, dto.getForecastedAmount());
        assertEquals(BigDecimal.TEN, dto.getForecastVariance());
    }

    @Test
    void testSettersAndGetters() {
        BudgetVarianceResponseDto dto = new BudgetVarianceResponseDto();
        dto.setId("val-id");
        dto.setVarianceId("val-varianceId");
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        dto.setBudgetCode("val-budgetCode");
        dto.setBudgetName("val-budgetName");
        dto.setPeriod("val-period");
        dto.setBudgetedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        dto.setCommittedAmount(BigDecimal.ONE);
        dto.setVarianceAmount(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setVarianceStatus(BudgetVarianceResponseDto.VarianceStatusDto.FAVORABLE);
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setFiscalYear("val-fiscalYear");
        dto.setCreatedBy("val-createdBy");
        dto.setLastUpdatedBy("val-lastUpdatedBy");
        dto.setDescription("val-description");
        dto.setAnalysis("val-analysis");
        dto.setRequiresInvestigation(true);
        dto.setAssignedTo("val-assignedTo");
        dto.setInvestigated(true);
        dto.setInvestigatedBy("val-investigatedBy");
        dto.setInvestigationNotes("val-investigationNotes");
        dto.setForecastedAmount(BigDecimal.ONE);
        dto.setForecastVariance(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-varianceId", dto.getVarianceId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
        assertEquals("val-budgetCode", dto.getBudgetCode());
        assertEquals("val-budgetName", dto.getBudgetName());
        assertEquals("val-period", dto.getPeriod());
        assertEquals(BigDecimal.ONE, dto.getBudgetedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
        assertEquals(BigDecimal.ONE, dto.getCommittedAmount());
        assertEquals(BigDecimal.ONE, dto.getVarianceAmount());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals(BudgetVarianceResponseDto.VarianceStatusDto.FAVORABLE, dto.getVarianceStatus());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-fiscalYear", dto.getFiscalYear());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-lastUpdatedBy", dto.getLastUpdatedBy());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-analysis", dto.getAnalysis());
        assertTrue(dto.isRequiresInvestigation());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertTrue(dto.isInvestigated());
        assertEquals("val-investigatedBy", dto.getInvestigatedBy());
        assertEquals("val-investigationNotes", dto.getInvestigationNotes());
        assertEquals(BigDecimal.ONE, dto.getForecastedAmount());
        assertEquals(BigDecimal.ONE, dto.getForecastVariance());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetVarianceResponseDto dto1 = BudgetVarianceResponseDto.builder()
                        .id("test-id")
            .varianceId("test-varianceId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .period("test-period")
            .yearMonth(null)
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceStatus(BudgetVarianceResponseDto.VarianceStatusDto.FAVORABLE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .analysis("test-analysis")
            .breakdowns(Collections.emptyList())
            .contributingFactors(Collections.emptyList())
            .requiresInvestigation(true)
            .assignedTo("test-assignedTo")
            .investigationDueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .investigated(true)
            .investigatedBy("test-investigatedBy")
            .investigatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .investigationNotes("test-investigationNotes")
            .forecastedAmount(BigDecimal.TEN)
            .forecastVariance(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BudgetVarianceResponseDto dto2 = BudgetVarianceResponseDto.builder()
                        .id("test-id")
            .varianceId("test-varianceId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .period("test-period")
            .yearMonth(null)
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceStatus(BudgetVarianceResponseDto.VarianceStatusDto.FAVORABLE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .analysis("test-analysis")
            .breakdowns(Collections.emptyList())
            .contributingFactors(Collections.emptyList())
            .requiresInvestigation(true)
            .assignedTo("test-assignedTo")
            .investigationDueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .investigated(true)
            .investigatedBy("test-investigatedBy")
            .investigatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .investigationNotes("test-investigationNotes")
            .forecastedAmount(BigDecimal.TEN)
            .forecastVariance(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetVarianceResponseDto dto = BudgetVarianceResponseDto.builder()
                        .id("test-id")
            .varianceId("test-varianceId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .period("test-period")
            .yearMonth(null)
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .varianceStatus(BudgetVarianceResponseDto.VarianceStatusDto.FAVORABLE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .fiscalYear("test-fiscalYear")
            .createdBy("test-createdBy")
            .lastUpdatedBy("test-lastUpdatedBy")
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .analysis("test-analysis")
            .breakdowns(Collections.emptyList())
            .contributingFactors(Collections.emptyList())
            .requiresInvestigation(true)
            .assignedTo("test-assignedTo")
            .investigationDueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .investigated(true)
            .investigatedBy("test-investigatedBy")
            .investigatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .investigationNotes("test-investigationNotes")
            .forecastedAmount(BigDecimal.TEN)
            .forecastVariance(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}