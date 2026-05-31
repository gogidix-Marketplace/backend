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
class LedgerAccountQuery_AccountHierarchyTest {

        @Test
    void testBuilder() {
        LedgerAccountQuery.AccountHierarchy dto = LedgerAccountQuery.AccountHierarchy.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .currentBalance(BigDecimal.TEN)
            .currency("test-currency")
            .level(42)
            .children(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BigDecimal.TEN, dto.getCurrentBalance());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(42, dto.getLevel());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccountQuery.AccountHierarchy dto = new LedgerAccountQuery.AccountHierarchy();
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setCurrentBalance(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setLevel(99);
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getCurrentBalance());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(99, dto.getLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountQuery.AccountHierarchy dto1 = LedgerAccountQuery.AccountHierarchy.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .currentBalance(BigDecimal.TEN)
            .currency("test-currency")
            .level(42)
            .children(Collections.emptyList())
            .build();
        LedgerAccountQuery.AccountHierarchy dto2 = LedgerAccountQuery.AccountHierarchy.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .currentBalance(BigDecimal.TEN)
            .currency("test-currency")
            .level(42)
            .children(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccountQuery.AccountHierarchy dto = LedgerAccountQuery.AccountHierarchy.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccount.AccountType.ASSET)
            .accountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET)
            .status(LedgerAccount.AccountStatus.ACTIVE)
            .currentBalance(BigDecimal.TEN)
            .currency("test-currency")
            .level(42)
            .children(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}