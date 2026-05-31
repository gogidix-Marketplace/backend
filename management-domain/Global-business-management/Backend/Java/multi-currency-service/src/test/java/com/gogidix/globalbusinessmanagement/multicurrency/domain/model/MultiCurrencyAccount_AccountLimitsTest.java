package com.gogidix.globalbusinessmanagement.multicurrency.domain.model;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.MultiCurrencyAccount;
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
class MultiCurrencyAccount_AccountLimitsTest {

        @Test
    void testBuilder() {
        MultiCurrencyAccount.AccountLimits dto = MultiCurrencyAccount.AccountLimits.builder()
                        .maxBalancePerCurrency(BigDecimal.TEN)
            .maxTotalBalance(BigDecimal.TEN)
            .maxActiveCurrencies(42)
            .dailyTransferLimit(BigDecimal.TEN)
            .monthlyTransferLimit(BigDecimal.TEN)
            .dailyWithdrawalLimit(BigDecimal.TEN)
            .monthlyWithdrawalLimit(BigDecimal.TEN)
            .minTransferAmount(BigDecimal.TEN)
            .maxTransactionsPerDay(42)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getMaxBalancePerCurrency());
        assertEquals(BigDecimal.TEN, dto.getMaxTotalBalance());
        assertEquals(42, dto.getMaxActiveCurrencies());
        assertEquals(BigDecimal.TEN, dto.getDailyTransferLimit());
        assertEquals(BigDecimal.TEN, dto.getMonthlyTransferLimit());
        assertEquals(BigDecimal.TEN, dto.getDailyWithdrawalLimit());
        assertEquals(BigDecimal.TEN, dto.getMonthlyWithdrawalLimit());
        assertEquals(BigDecimal.TEN, dto.getMinTransferAmount());
        assertEquals(42, dto.getMaxTransactionsPerDay());
    }

    @Test
    void testSettersAndGetters() {
        MultiCurrencyAccount.AccountLimits dto = new MultiCurrencyAccount.AccountLimits();
        dto.setMaxBalancePerCurrency(BigDecimal.ONE);
        dto.setMaxTotalBalance(BigDecimal.ONE);
        dto.setMaxActiveCurrencies(99);
        dto.setDailyTransferLimit(BigDecimal.ONE);
        dto.setMonthlyTransferLimit(BigDecimal.ONE);
        dto.setDailyWithdrawalLimit(BigDecimal.ONE);
        dto.setMonthlyWithdrawalLimit(BigDecimal.ONE);
        dto.setMinTransferAmount(BigDecimal.ONE);
        dto.setMaxTransactionsPerDay(99);
        assertEquals(BigDecimal.ONE, dto.getMaxBalancePerCurrency());
        assertEquals(BigDecimal.ONE, dto.getMaxTotalBalance());
        assertEquals(99, dto.getMaxActiveCurrencies());
        assertEquals(BigDecimal.ONE, dto.getDailyTransferLimit());
        assertEquals(BigDecimal.ONE, dto.getMonthlyTransferLimit());
        assertEquals(BigDecimal.ONE, dto.getDailyWithdrawalLimit());
        assertEquals(BigDecimal.ONE, dto.getMonthlyWithdrawalLimit());
        assertEquals(BigDecimal.ONE, dto.getMinTransferAmount());
        assertEquals(99, dto.getMaxTransactionsPerDay());
    }

    @Test
    void testEqualsAndHashCode() {
        MultiCurrencyAccount.AccountLimits dto1 = MultiCurrencyAccount.AccountLimits.builder()
                        .maxBalancePerCurrency(BigDecimal.TEN)
            .maxTotalBalance(BigDecimal.TEN)
            .maxActiveCurrencies(42)
            .dailyTransferLimit(BigDecimal.TEN)
            .monthlyTransferLimit(BigDecimal.TEN)
            .dailyWithdrawalLimit(BigDecimal.TEN)
            .monthlyWithdrawalLimit(BigDecimal.TEN)
            .minTransferAmount(BigDecimal.TEN)
            .maxTransactionsPerDay(42)
            .build();
        MultiCurrencyAccount.AccountLimits dto2 = MultiCurrencyAccount.AccountLimits.builder()
                        .maxBalancePerCurrency(BigDecimal.TEN)
            .maxTotalBalance(BigDecimal.TEN)
            .maxActiveCurrencies(42)
            .dailyTransferLimit(BigDecimal.TEN)
            .monthlyTransferLimit(BigDecimal.TEN)
            .dailyWithdrawalLimit(BigDecimal.TEN)
            .monthlyWithdrawalLimit(BigDecimal.TEN)
            .minTransferAmount(BigDecimal.TEN)
            .maxTransactionsPerDay(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MultiCurrencyAccount.AccountLimits dto = MultiCurrencyAccount.AccountLimits.builder()
                        .maxBalancePerCurrency(BigDecimal.TEN)
            .maxTotalBalance(BigDecimal.TEN)
            .maxActiveCurrencies(42)
            .dailyTransferLimit(BigDecimal.TEN)
            .monthlyTransferLimit(BigDecimal.TEN)
            .dailyWithdrawalLimit(BigDecimal.TEN)
            .monthlyWithdrawalLimit(BigDecimal.TEN)
            .minTransferAmount(BigDecimal.TEN)
            .maxTransactionsPerDay(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}