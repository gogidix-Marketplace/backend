package com.gogidix.finance.generalledger.application.dto.response;

import com.gogidix.finance.generalledger.application.dto.response.TrialBalanceDto;
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
class TrialBalanceDtoTest {

        @Test
    void testBuilder() {
        TrialBalanceDto dto = TrialBalanceDto.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .periodName("test-periodName")
            .totalAccounts(42)
            .activeAccounts(42)
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
        assertEquals("test-periodName", dto.getPeriodName());
        assertEquals(42, dto.getTotalAccounts());
        assertEquals(42, dto.getActiveAccounts());
    }

    @Test
    void testSettersAndGetters() {
        TrialBalanceDto dto = new TrialBalanceDto();
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        dto.setCurrency("val-currency");
        dto.setTotalDebits(BigDecimal.ONE);
        dto.setTotalCredits(BigDecimal.ONE);
        dto.setIsBalanced(true);
        dto.setDifference(BigDecimal.ONE);
        dto.setFiscalYear(99);
        dto.setFiscalPeriod(99);
        dto.setPeriodName("val-periodName");
        dto.setTotalAccounts(99);
        dto.setActiveAccounts(99);
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getTotalDebits());
        assertEquals(BigDecimal.ONE, dto.getTotalCredits());
        assertTrue(dto.getIsBalanced());
        assertEquals(BigDecimal.ONE, dto.getDifference());
        assertEquals(99, dto.getFiscalYear());
        assertEquals(99, dto.getFiscalPeriod());
        assertEquals("val-periodName", dto.getPeriodName());
        assertEquals(99, dto.getTotalAccounts());
        assertEquals(99, dto.getActiveAccounts());
    }

    @Test
    void testEqualsAndHashCode() {
        TrialBalanceDto dto1 = TrialBalanceDto.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .periodName("test-periodName")
            .totalAccounts(42)
            .activeAccounts(42)
            .build();
        TrialBalanceDto dto2 = TrialBalanceDto.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .periodName("test-periodName")
            .totalAccounts(42)
            .activeAccounts(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrialBalanceDto dto = TrialBalanceDto.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .totalDebits(BigDecimal.TEN)
            .totalCredits(BigDecimal.TEN)
            .isBalanced(true)
            .difference(BigDecimal.TEN)
            .accountDetails(Collections.emptyList())
            .fiscalYear(42)
            .fiscalPeriod(42)
            .periodName("test-periodName")
            .totalAccounts(42)
            .activeAccounts(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}