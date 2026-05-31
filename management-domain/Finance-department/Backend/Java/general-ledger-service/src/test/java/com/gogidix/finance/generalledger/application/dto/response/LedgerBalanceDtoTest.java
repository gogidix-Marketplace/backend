package com.gogidix.finance.generalledger.application.dto.response;

import com.gogidix.finance.generalledger.application.dto.response.LedgerBalanceDto;
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
class LedgerBalanceDtoTest {

        @Test
    void testBuilder() {
        LedgerBalanceDto dto = LedgerBalanceDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
            .normalBalanceSide(42)
            .netChange(BigDecimal.TEN)
            .periodStartDate(LocalDate.of(2025,1,15))
            .periodEndDate(LocalDate.of(2025,1,15))
            .transactionCount(42)
            .lastTransactionAmount(BigDecimal.TEN)
            .isReconciled(true)
            .balanceStatus("test-balanceStatus")
            .build();
        assertNotNull(dto);
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BigDecimal.TEN, dto.getCurrentBalance());
        assertEquals(BigDecimal.TEN, dto.getDebitBalance());
        assertEquals(BigDecimal.TEN, dto.getCreditBalance());
        assertEquals(BigDecimal.TEN, dto.getOpeningBalance());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getAsOfDate());
        assertEquals(42, dto.getNormalBalanceSide());
        assertEquals(BigDecimal.TEN, dto.getNetChange());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodEndDate());
        assertEquals(42, dto.getTransactionCount());
        assertEquals(BigDecimal.TEN, dto.getLastTransactionAmount());
        assertTrue(dto.getIsReconciled());
        assertEquals("test-balanceStatus", dto.getBalanceStatus());
    }

    @Test
    void testSettersAndGetters() {
        LedgerBalanceDto dto = new LedgerBalanceDto();
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setCurrentBalance(BigDecimal.ONE);
        dto.setDebitBalance(BigDecimal.ONE);
        dto.setCreditBalance(BigDecimal.ONE);
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        dto.setNormalBalanceSide(99);
        dto.setNetChange(BigDecimal.ONE);
        dto.setPeriodStartDate(LocalDate.of(2025,6,1));
        dto.setPeriodEndDate(LocalDate.of(2025,6,1));
        dto.setTransactionCount(99);
        dto.setLastTransactionAmount(BigDecimal.ONE);
        dto.setIsReconciled(true);
        dto.setBalanceStatus("val-balanceStatus");
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getCurrentBalance());
        assertEquals(BigDecimal.ONE, dto.getDebitBalance());
        assertEquals(BigDecimal.ONE, dto.getCreditBalance());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
        assertEquals(99, dto.getNormalBalanceSide());
        assertEquals(BigDecimal.ONE, dto.getNetChange());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEndDate());
        assertEquals(99, dto.getTransactionCount());
        assertEquals(BigDecimal.ONE, dto.getLastTransactionAmount());
        assertTrue(dto.getIsReconciled());
        assertEquals("val-balanceStatus", dto.getBalanceStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerBalanceDto dto1 = LedgerBalanceDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
            .normalBalanceSide(42)
            .netChange(BigDecimal.TEN)
            .periodStartDate(LocalDate.of(2025,1,15))
            .periodEndDate(LocalDate.of(2025,1,15))
            .transactionCount(42)
            .lastTransactionAmount(BigDecimal.TEN)
            .isReconciled(true)
            .balanceStatus("test-balanceStatus")
            .build();
        LedgerBalanceDto dto2 = LedgerBalanceDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
            .normalBalanceSide(42)
            .netChange(BigDecimal.TEN)
            .periodStartDate(LocalDate.of(2025,1,15))
            .periodEndDate(LocalDate.of(2025,1,15))
            .transactionCount(42)
            .lastTransactionAmount(BigDecimal.TEN)
            .isReconciled(true)
            .balanceStatus("test-balanceStatus")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerBalanceDto dto = LedgerBalanceDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
            .normalBalanceSide(42)
            .netChange(BigDecimal.TEN)
            .periodStartDate(LocalDate.of(2025,1,15))
            .periodEndDate(LocalDate.of(2025,1,15))
            .transactionCount(42)
            .lastTransactionAmount(BigDecimal.TEN)
            .isReconciled(true)
            .balanceStatus("test-balanceStatus")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}