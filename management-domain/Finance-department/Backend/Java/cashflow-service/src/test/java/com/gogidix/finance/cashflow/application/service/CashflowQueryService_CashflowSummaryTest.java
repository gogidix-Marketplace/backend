package com.gogidix.finance.cashflow.application.service;

import com.gogidix.finance.cashflow.application.service.CashflowQueryService;
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
class CashflowQueryService_CashflowSummaryTest {

        @Test
    void testBuilder() {
        CashflowQueryService.CashflowSummary dto = CashflowQueryService.CashflowSummary.builder()
                        .totalCount(42L)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .pendingCount(42L)
            .settledCount(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalCount());
        assertEquals(BigDecimal.TEN, dto.getTotalInflow());
        assertEquals(BigDecimal.TEN, dto.getTotalOutflow());
        assertEquals(BigDecimal.TEN, dto.getNetCashflow());
        assertEquals(42L, dto.getPendingCount());
        assertEquals(42L, dto.getSettledCount());
    }

    @Test
    void testBuilderWithValues() {
        CashflowQueryService.CashflowSummary dto = CashflowQueryService.CashflowSummary.builder()
            .totalInflow(BigDecimal.ONE)
            .totalOutflow(BigDecimal.ONE)
            .netCashflow(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQueryService.CashflowSummary dto1 = CashflowQueryService.CashflowSummary.builder()
                        .totalCount(42L)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .pendingCount(42L)
            .settledCount(42L)
            .build();
        CashflowQueryService.CashflowSummary dto2 = CashflowQueryService.CashflowSummary.builder()
                        .totalCount(42L)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .pendingCount(42L)
            .settledCount(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowQueryService.CashflowSummary dto = CashflowQueryService.CashflowSummary.builder()
                        .totalCount(42L)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .pendingCount(42L)
            .settledCount(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}