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
class LedgerAccountQuery_AccountBalanceDetailTest {

        @Test
    void testBuilder() {
        LedgerAccountQuery.AccountBalanceDetail dto = LedgerAccountQuery.AccountBalanceDetail.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .build();
        assertNotNull(dto);
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BigDecimal.TEN, dto.getDebitBalance());
        assertEquals(BigDecimal.TEN, dto.getCreditBalance());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccountQuery.AccountBalanceDetail dto = new LedgerAccountQuery.AccountBalanceDetail();
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setDebitBalance(BigDecimal.ONE);
        dto.setCreditBalance(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getDebitBalance());
        assertEquals(BigDecimal.ONE, dto.getCreditBalance());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountQuery.AccountBalanceDetail dto1 = LedgerAccountQuery.AccountBalanceDetail.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .build();
        LedgerAccountQuery.AccountBalanceDetail dto2 = LedgerAccountQuery.AccountBalanceDetail.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccountQuery.AccountBalanceDetail dto = LedgerAccountQuery.AccountBalanceDetail.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}