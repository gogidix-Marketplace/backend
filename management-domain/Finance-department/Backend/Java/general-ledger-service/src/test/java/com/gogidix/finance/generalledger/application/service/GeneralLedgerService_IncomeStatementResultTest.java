package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.application.service.GeneralLedgerService;
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
class GeneralLedgerService_IncomeStatementResultTest {

        @Test
    void testBuilder() {
        GeneralLedgerService.IncomeStatementResult dto = GeneralLedgerService.IncomeStatementResult.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .revenueAccounts(Collections.emptyList())
            .expenseAccounts(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(BigDecimal.TEN, dto.getTotalRevenue());
        assertEquals(BigDecimal.TEN, dto.getTotalExpenses());
        assertEquals(BigDecimal.TEN, dto.getNetIncome());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
    }

    @Test
    void testBuilderWithValues() {
        GeneralLedgerService.IncomeStatementResult dto = GeneralLedgerService.IncomeStatementResult.builder()
            .startDate(LocalDate.of(2025,6,1))
            .endDate(LocalDate.of(2025,6,1))
            .totalRevenue(BigDecimal.ONE)
            .totalExpenses(BigDecimal.ONE)
            .netIncome(BigDecimal.ONE)
            .profitMargin(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerService.IncomeStatementResult dto1 = GeneralLedgerService.IncomeStatementResult.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .revenueAccounts(Collections.emptyList())
            .expenseAccounts(Collections.emptyList())
            .build();
        GeneralLedgerService.IncomeStatementResult dto2 = GeneralLedgerService.IncomeStatementResult.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .revenueAccounts(Collections.emptyList())
            .expenseAccounts(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GeneralLedgerService.IncomeStatementResult dto = GeneralLedgerService.IncomeStatementResult.builder()
                        .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .revenueAccounts(Collections.emptyList())
            .expenseAccounts(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}