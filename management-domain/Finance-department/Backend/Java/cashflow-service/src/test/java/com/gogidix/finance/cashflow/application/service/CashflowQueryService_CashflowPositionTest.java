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
class CashflowQueryService_CashflowPositionTest {

        @Test
    void testBuilder() {
        CashflowQueryService.CashflowPosition dto = CashflowQueryService.CashflowPosition.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .settledInflow(BigDecimal.TEN)
            .settledOutflow(BigDecimal.TEN)
            .pendingInflow(BigDecimal.TEN)
            .pendingOutflow(BigDecimal.TEN)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netPosition(BigDecimal.TEN)
            .projectedNetPosition(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getAsOfDate());
        assertEquals(BigDecimal.TEN, dto.getSettledInflow());
        assertEquals(BigDecimal.TEN, dto.getSettledOutflow());
        assertEquals(BigDecimal.TEN, dto.getPendingInflow());
        assertEquals(BigDecimal.TEN, dto.getPendingOutflow());
        assertEquals(BigDecimal.TEN, dto.getTotalInflow());
        assertEquals(BigDecimal.TEN, dto.getTotalOutflow());
        assertEquals(BigDecimal.TEN, dto.getNetPosition());
        assertEquals(BigDecimal.TEN, dto.getProjectedNetPosition());
    }

    @Test
    void testBuilderWithValues() {
        CashflowQueryService.CashflowPosition dto = CashflowQueryService.CashflowPosition.builder()
            .asOfDate(LocalDate.of(2025,6,1))
            .settledInflow(BigDecimal.ONE)
            .settledOutflow(BigDecimal.ONE)
            .pendingInflow(BigDecimal.ONE)
            .pendingOutflow(BigDecimal.ONE)
            .totalInflow(BigDecimal.ONE)
            .totalOutflow(BigDecimal.ONE)
            .netPosition(BigDecimal.ONE)
            .projectedNetPosition(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQueryService.CashflowPosition dto1 = CashflowQueryService.CashflowPosition.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .settledInflow(BigDecimal.TEN)
            .settledOutflow(BigDecimal.TEN)
            .pendingInflow(BigDecimal.TEN)
            .pendingOutflow(BigDecimal.TEN)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netPosition(BigDecimal.TEN)
            .projectedNetPosition(BigDecimal.TEN)
            .build();
        CashflowQueryService.CashflowPosition dto2 = CashflowQueryService.CashflowPosition.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .settledInflow(BigDecimal.TEN)
            .settledOutflow(BigDecimal.TEN)
            .pendingInflow(BigDecimal.TEN)
            .pendingOutflow(BigDecimal.TEN)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netPosition(BigDecimal.TEN)
            .projectedNetPosition(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowQueryService.CashflowPosition dto = CashflowQueryService.CashflowPosition.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .settledInflow(BigDecimal.TEN)
            .settledOutflow(BigDecimal.TEN)
            .pendingInflow(BigDecimal.TEN)
            .pendingOutflow(BigDecimal.TEN)
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netPosition(BigDecimal.TEN)
            .projectedNetPosition(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}