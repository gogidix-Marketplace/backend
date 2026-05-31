package com.gogidix.finance.ledger.domain.model;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
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
class LedgerAccount_AccountBalanceTest {

        @Test
    void testBuilder() {
        LedgerAccount.AccountBalance dto = LedgerAccount.AccountBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .asOfDate(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getBalance());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccount.AccountBalance dto = new LedgerAccount.AccountBalance();
        dto.setCurrency("val-currency");
        dto.setBalance(BigDecimal.ONE);
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getBalance());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccount.AccountBalance dto1 = LedgerAccount.AccountBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .asOfDate(LocalDateTime.of(2025,1,15,10,0))
            .build();
        LedgerAccount.AccountBalance dto2 = LedgerAccount.AccountBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .asOfDate(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccount.AccountBalance dto = LedgerAccount.AccountBalance.builder()
                        .currency("test-currency")
            .balance(BigDecimal.TEN)
            .asOfDate(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}