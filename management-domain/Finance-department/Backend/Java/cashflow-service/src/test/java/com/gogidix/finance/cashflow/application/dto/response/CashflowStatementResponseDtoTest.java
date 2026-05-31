package com.gogidix.finance.cashflow.application.dto.response;

import com.gogidix.finance.cashflow.application.dto.response.CashflowStatementResponseDto;
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
class CashflowStatementResponseDtoTest {

        @Test
    void testBuilder() {
        CashflowStatementResponseDto dto = CashflowStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .statementType(CashflowStatementResponseDto.StatementTypeDto.DIRECT)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowStatementResponseDto.StatementPeriodDto.DAILY)
            .status(CashflowStatementResponseDto.StatementStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .beginningCash(BigDecimal.TEN)
            .endingCash(BigDecimal.TEN)
            .netCashIncreaseDecrease(BigDecimal.TEN)
            .operatingActivities(null)
            .investingActivities(null)
            .financingActivities(null)
            .lineItems(Collections.emptyList())
            .currency("test-currency")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .isConsolidated(true)
            .subsidiaryIds(Collections.emptyList())
            .parentStatementId("test-parentStatementId")
            .priorPeriodCash(BigDecimal.TEN)
            .cashChangePercentage(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .approvalStatus("test-approvalStatus")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-statementId", dto.getStatementId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(CashflowStatementResponseDto.StatementTypeDto.DIRECT, dto.getStatementType());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(CashflowStatementResponseDto.StatementPeriodDto.DAILY, dto.getPeriod());
        assertEquals(CashflowStatementResponseDto.StatementStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
        assertEquals(BigDecimal.TEN, dto.getBeginningCash());
        assertEquals(BigDecimal.TEN, dto.getEndingCash());
        assertEquals(BigDecimal.TEN, dto.getNetCashIncreaseDecrease());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(42, dto.getFiscalYear());
        assertEquals(42, dto.getFiscalPeriod());
        assertTrue(dto.getIsConsolidated());
        assertEquals("test-parentStatementId", dto.getParentStatementId());
        assertEquals(BigDecimal.TEN, dto.getPriorPeriodCash());
        assertEquals(BigDecimal.TEN, dto.getCashChangePercentage());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-approvalStatus", dto.getApprovalStatus());
        assertEquals("test-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testSettersAndGetters() {
        CashflowStatementResponseDto dto = new CashflowStatementResponseDto();
        dto.setId("val-id");
        dto.setStatementId("val-statementId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setStatementType(CashflowStatementResponseDto.StatementTypeDto.DIRECT);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPeriod(CashflowStatementResponseDto.StatementPeriodDto.DAILY);
        dto.setStatus(CashflowStatementResponseDto.StatementStatusDto.DRAFT);
        dto.setGeneratedBy("val-generatedBy");
        dto.setBeginningCash(BigDecimal.ONE);
        dto.setEndingCash(BigDecimal.ONE);
        dto.setNetCashIncreaseDecrease(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setFiscalYear(99);
        dto.setFiscalPeriod(99);
        dto.setIsConsolidated(true);
        dto.setParentStatementId("val-parentStatementId");
        dto.setPriorPeriodCash(BigDecimal.ONE);
        dto.setCashChangePercentage(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setApprovalStatus("val-approvalStatus");
        dto.setApprovedBy("val-approvedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(CashflowStatementResponseDto.StatementTypeDto.DIRECT, dto.getStatementType());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(CashflowStatementResponseDto.StatementPeriodDto.DAILY, dto.getPeriod());
        assertEquals(CashflowStatementResponseDto.StatementStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
        assertEquals(BigDecimal.ONE, dto.getBeginningCash());
        assertEquals(BigDecimal.ONE, dto.getEndingCash());
        assertEquals(BigDecimal.ONE, dto.getNetCashIncreaseDecrease());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(99, dto.getFiscalYear());
        assertEquals(99, dto.getFiscalPeriod());
        assertTrue(dto.getIsConsolidated());
        assertEquals("val-parentStatementId", dto.getParentStatementId());
        assertEquals(BigDecimal.ONE, dto.getPriorPeriodCash());
        assertEquals(BigDecimal.ONE, dto.getCashChangePercentage());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-approvalStatus", dto.getApprovalStatus());
        assertEquals("val-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowStatementResponseDto dto1 = CashflowStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .statementType(CashflowStatementResponseDto.StatementTypeDto.DIRECT)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowStatementResponseDto.StatementPeriodDto.DAILY)
            .status(CashflowStatementResponseDto.StatementStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .beginningCash(BigDecimal.TEN)
            .endingCash(BigDecimal.TEN)
            .netCashIncreaseDecrease(BigDecimal.TEN)
            .operatingActivities(null)
            .investingActivities(null)
            .financingActivities(null)
            .lineItems(Collections.emptyList())
            .currency("test-currency")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .isConsolidated(true)
            .subsidiaryIds(Collections.emptyList())
            .parentStatementId("test-parentStatementId")
            .priorPeriodCash(BigDecimal.TEN)
            .cashChangePercentage(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .approvalStatus("test-approvalStatus")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CashflowStatementResponseDto dto2 = CashflowStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .statementType(CashflowStatementResponseDto.StatementTypeDto.DIRECT)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowStatementResponseDto.StatementPeriodDto.DAILY)
            .status(CashflowStatementResponseDto.StatementStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .beginningCash(BigDecimal.TEN)
            .endingCash(BigDecimal.TEN)
            .netCashIncreaseDecrease(BigDecimal.TEN)
            .operatingActivities(null)
            .investingActivities(null)
            .financingActivities(null)
            .lineItems(Collections.emptyList())
            .currency("test-currency")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .isConsolidated(true)
            .subsidiaryIds(Collections.emptyList())
            .parentStatementId("test-parentStatementId")
            .priorPeriodCash(BigDecimal.TEN)
            .cashChangePercentage(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .approvalStatus("test-approvalStatus")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowStatementResponseDto dto = CashflowStatementResponseDto.builder()
                        .id("test-id")
            .statementId("test-statementId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .statementType(CashflowStatementResponseDto.StatementTypeDto.DIRECT)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowStatementResponseDto.StatementPeriodDto.DAILY)
            .status(CashflowStatementResponseDto.StatementStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .beginningCash(BigDecimal.TEN)
            .endingCash(BigDecimal.TEN)
            .netCashIncreaseDecrease(BigDecimal.TEN)
            .operatingActivities(null)
            .investingActivities(null)
            .financingActivities(null)
            .lineItems(Collections.emptyList())
            .currency("test-currency")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .isConsolidated(true)
            .subsidiaryIds(Collections.emptyList())
            .parentStatementId("test-parentStatementId")
            .priorPeriodCash(BigDecimal.TEN)
            .cashChangePercentage(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .approvalStatus("test-approvalStatus")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}