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
class MultiCurrencyAccount_AccountTransactionTest {

        @Test
    void testBuilder() {
        MultiCurrencyAccount.AccountTransaction dto = MultiCurrencyAccount.AccountTransaction.builder()
                        .transactionId("test-transactionId")
            .type("test-type")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .fromAmount(BigDecimal.TEN)
            .toAmount(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .status("test-status")
            .reference("test-reference")
            .description("test-description")
            .build();
        assertNotNull(dto);
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals("test-type", dto.getType());
        assertEquals("test-fromCurrency", dto.getFromCurrency());
        assertEquals("test-toCurrency", dto.getToCurrency());
        assertEquals(BigDecimal.TEN, dto.getFromAmount());
        assertEquals(BigDecimal.TEN, dto.getToAmount());
        assertEquals(BigDecimal.TEN, dto.getExchangeRate());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-reference", dto.getReference());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        MultiCurrencyAccount.AccountTransaction dto = new MultiCurrencyAccount.AccountTransaction();
        dto.setTransactionId("val-transactionId");
        dto.setType("val-type");
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setFromAmount(BigDecimal.ONE);
        dto.setToAmount(BigDecimal.ONE);
        dto.setExchangeRate(BigDecimal.ONE);
        dto.setStatus("val-status");
        dto.setReference("val-reference");
        dto.setDescription("val-description");
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-type", dto.getType());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals(BigDecimal.ONE, dto.getFromAmount());
        assertEquals(BigDecimal.ONE, dto.getToAmount());
        assertEquals(BigDecimal.ONE, dto.getExchangeRate());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        MultiCurrencyAccount.AccountTransaction dto1 = MultiCurrencyAccount.AccountTransaction.builder()
                        .transactionId("test-transactionId")
            .type("test-type")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .fromAmount(BigDecimal.TEN)
            .toAmount(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .status("test-status")
            .reference("test-reference")
            .description("test-description")
            .build();
        MultiCurrencyAccount.AccountTransaction dto2 = MultiCurrencyAccount.AccountTransaction.builder()
                        .transactionId("test-transactionId")
            .type("test-type")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .fromAmount(BigDecimal.TEN)
            .toAmount(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .status("test-status")
            .reference("test-reference")
            .description("test-description")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MultiCurrencyAccount.AccountTransaction dto = MultiCurrencyAccount.AccountTransaction.builder()
                        .transactionId("test-transactionId")
            .type("test-type")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .fromAmount(BigDecimal.TEN)
            .toAmount(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .status("test-status")
            .reference("test-reference")
            .description("test-description")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}