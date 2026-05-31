package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
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
class LedgerAccountQuery_AccountBalanceSummaryTest {

        @Test
    void testBuilder() {
        LedgerAccountQuery.AccountBalanceSummary dto = LedgerAccountQuery.AccountBalanceSummary.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
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
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccountQuery.AccountBalanceSummary dto = new LedgerAccountQuery.AccountBalanceSummary();
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setCurrentBalance(BigDecimal.ONE);
        dto.setDebitBalance(BigDecimal.ONE);
        dto.setCreditBalance(BigDecimal.ONE);
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getCurrentBalance());
        assertEquals(BigDecimal.ONE, dto.getDebitBalance());
        assertEquals(BigDecimal.ONE, dto.getCreditBalance());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountQuery.AccountBalanceSummary dto1 = LedgerAccountQuery.AccountBalanceSummary.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
            .build();
        LedgerAccountQuery.AccountBalanceSummary dto2 = LedgerAccountQuery.AccountBalanceSummary.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccountQuery.AccountBalanceSummary dto = LedgerAccountQuery.AccountBalanceSummary.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .currentBalance(BigDecimal.TEN)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .currency("test-currency")
            .asOfDate(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}