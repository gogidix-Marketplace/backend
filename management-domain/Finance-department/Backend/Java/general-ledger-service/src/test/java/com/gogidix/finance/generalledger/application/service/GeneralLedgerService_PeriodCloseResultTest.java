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
class GeneralLedgerService_PeriodCloseResultTest {

        @Test
    void testBuilder() {
        GeneralLedgerService.PeriodCloseResult dto = GeneralLedgerService.PeriodCloseResult.builder()
                        .success(true)
            .closingEntryId("test-closingEntryId")
            .closingEntryNumber("test-closingEntryNumber")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .closedAt(null)
            .build();
        assertNotNull(dto);
        assertTrue(dto.isSuccess());
        assertEquals("test-closingEntryId", dto.getClosingEntryId());
        assertEquals("test-closingEntryNumber", dto.getClosingEntryNumber());
        assertEquals(42, dto.getFiscalYear());
        assertEquals(42, dto.getFiscalPeriod());
        assertEquals(BigDecimal.TEN, dto.getTotalRevenue());
        assertEquals(BigDecimal.TEN, dto.getTotalExpenses());
        assertEquals(BigDecimal.TEN, dto.getNetIncome());
    }

    @Test
    void testBuilderWithValues() {
        GeneralLedgerService.PeriodCloseResult dto = GeneralLedgerService.PeriodCloseResult.builder()
            .success(true)
            .closingEntryId("val-closingEntryId")
            .closingEntryNumber("val-closingEntryNumber")
            .fiscalYear(99)
            .fiscalPeriod(99)
            .totalRevenue(BigDecimal.ONE)
            .totalExpenses(BigDecimal.ONE)
            .netIncome(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerService.PeriodCloseResult dto1 = GeneralLedgerService.PeriodCloseResult.builder()
                        .success(true)
            .closingEntryId("test-closingEntryId")
            .closingEntryNumber("test-closingEntryNumber")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .closedAt(null)
            .build();
        GeneralLedgerService.PeriodCloseResult dto2 = GeneralLedgerService.PeriodCloseResult.builder()
                        .success(true)
            .closingEntryId("test-closingEntryId")
            .closingEntryNumber("test-closingEntryNumber")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .closedAt(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GeneralLedgerService.PeriodCloseResult dto = GeneralLedgerService.PeriodCloseResult.builder()
                        .success(true)
            .closingEntryId("test-closingEntryId")
            .closingEntryNumber("test-closingEntryNumber")
            .fiscalYear(42)
            .fiscalPeriod(42)
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .netIncome(BigDecimal.TEN)
            .closedAt(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}