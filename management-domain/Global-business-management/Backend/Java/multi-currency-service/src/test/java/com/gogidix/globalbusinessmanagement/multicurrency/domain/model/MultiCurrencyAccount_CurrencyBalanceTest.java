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
class MultiCurrencyAccount_CurrencyBalanceTest {

        @Test
    void testBuilder() {
        MultiCurrencyAccount.CurrencyBalance dto = MultiCurrencyAccount.CurrencyBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .frozenBalance(BigDecimal.TEN)
            .pendingBalance(BigDecimal.TEN)
            .creditLimit(BigDecimal.TEN)
            .lastUpdatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .status(MultiCurrencyAccount.CurrencyBalance.BalanceStatus.ACTIVE)
            .isPrimary(true)
            .displayOrder(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getBalance());
        assertEquals(BigDecimal.TEN, dto.getAvailableBalance());
        assertEquals(BigDecimal.TEN, dto.getFrozenBalance());
        assertEquals(BigDecimal.TEN, dto.getPendingBalance());
        assertEquals(BigDecimal.TEN, dto.getCreditLimit());
        assertEquals(MultiCurrencyAccount.CurrencyBalance.BalanceStatus.ACTIVE, dto.getStatus());
        assertTrue(dto.getIsPrimary());
        assertEquals(42, dto.getDisplayOrder());
    }

    @Test
    void testSettersAndGetters() {
        MultiCurrencyAccount.CurrencyBalance dto = new MultiCurrencyAccount.CurrencyBalance();
        dto.setCurrency("val-currency");
        dto.setBalance(BigDecimal.ONE);
        dto.setAvailableBalance(BigDecimal.ONE);
        dto.setFrozenBalance(BigDecimal.ONE);
        dto.setPendingBalance(BigDecimal.ONE);
        dto.setCreditLimit(BigDecimal.ONE);
        dto.setStatus(MultiCurrencyAccount.CurrencyBalance.BalanceStatus.ACTIVE);
        dto.setIsPrimary(true);
        dto.setDisplayOrder(99);
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getBalance());
        assertEquals(BigDecimal.ONE, dto.getAvailableBalance());
        assertEquals(BigDecimal.ONE, dto.getFrozenBalance());
        assertEquals(BigDecimal.ONE, dto.getPendingBalance());
        assertEquals(BigDecimal.ONE, dto.getCreditLimit());
        assertEquals(MultiCurrencyAccount.CurrencyBalance.BalanceStatus.ACTIVE, dto.getStatus());
        assertTrue(dto.getIsPrimary());
        assertEquals(99, dto.getDisplayOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        MultiCurrencyAccount.CurrencyBalance dto1 = MultiCurrencyAccount.CurrencyBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .frozenBalance(BigDecimal.TEN)
            .pendingBalance(BigDecimal.TEN)
            .creditLimit(BigDecimal.TEN)
            .lastUpdatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .status(MultiCurrencyAccount.CurrencyBalance.BalanceStatus.ACTIVE)
            .isPrimary(true)
            .displayOrder(42)
            .build();
        MultiCurrencyAccount.CurrencyBalance dto2 = MultiCurrencyAccount.CurrencyBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .frozenBalance(BigDecimal.TEN)
            .pendingBalance(BigDecimal.TEN)
            .creditLimit(BigDecimal.TEN)
            .lastUpdatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .status(MultiCurrencyAccount.CurrencyBalance.BalanceStatus.ACTIVE)
            .isPrimary(true)
            .displayOrder(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MultiCurrencyAccount.CurrencyBalance dto = MultiCurrencyAccount.CurrencyBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .availableBalance(BigDecimal.TEN)
            .frozenBalance(BigDecimal.TEN)
            .pendingBalance(BigDecimal.TEN)
            .creditLimit(BigDecimal.TEN)
            .lastUpdatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .status(MultiCurrencyAccount.CurrencyBalance.BalanceStatus.ACTIVE)
            .isPrimary(true)
            .displayOrder(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}