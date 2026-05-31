package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.port.in.LedgerAccountQuery;
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
class LedgerAccountQuery_TrialBalanceTest {

        @Test
    void testBuilder() {
        LedgerAccountQuery.TrialBalance dto = LedgerAccountQuery.TrialBalance.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getAsOfDate());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getTotalDebits());
        assertEquals(BigDecimal.TEN, dto.getTotalCredits());
        assertTrue(dto.getIsBalanced());
        assertEquals(BigDecimal.TEN, dto.getDifference());
        assertEquals(42, dto.getFiscalYear());
        assertEquals(42, dto.getFiscalPeriod());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccountQuery.TrialBalance dto = new LedgerAccountQuery.TrialBalance();
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        dto.setCurrency("val-currency");
        dto.setTotalDebits(BigDecimal.ONE);
        dto.setTotalCredits(BigDecimal.ONE);
        dto.setIsBalanced(true);
        dto.setDifference(BigDecimal.ONE);
        dto.setFiscalYear(99);
        dto.setFiscalPeriod(99);
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getTotalDebits());
        assertEquals(BigDecimal.ONE, dto.getTotalCredits());
        assertTrue(dto.getIsBalanced());
        assertEquals(BigDecimal.ONE, dto.getDifference());
        assertEquals(99, dto.getFiscalYear());
        assertEquals(99, dto.getFiscalPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountQuery.TrialBalance dto1 = LedgerAccountQuery.TrialBalance.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .build();
        LedgerAccountQuery.TrialBalance dto2 = LedgerAccountQuery.TrialBalance.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccountQuery.TrialBalance dto = LedgerAccountQuery.TrialBalance.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}