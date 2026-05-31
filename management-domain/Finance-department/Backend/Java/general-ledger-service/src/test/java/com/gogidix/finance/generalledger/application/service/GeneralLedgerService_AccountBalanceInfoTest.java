package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.application.service.GeneralLedgerService;
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
class GeneralLedgerService_AccountBalanceInfoTest {

        @Test
    void testBuilder() {
        GeneralLedgerService.AccountBalanceInfo dto = GeneralLedgerService.AccountBalanceInfo.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .balance(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertNotNull(dto);
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BigDecimal.TEN, dto.getBalance());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testBuilderWithValues() {
        GeneralLedgerService.AccountBalanceInfo dto = GeneralLedgerService.AccountBalanceInfo.builder()
            .accountId("val-accountId")
            .accountNumber("val-accountNumber")
            .accountName("val-accountName")
            .balance(BigDecimal.ONE)
            .currency("val-currency")
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerService.AccountBalanceInfo dto1 = GeneralLedgerService.AccountBalanceInfo.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .balance(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        GeneralLedgerService.AccountBalanceInfo dto2 = GeneralLedgerService.AccountBalanceInfo.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .balance(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GeneralLedgerService.AccountBalanceInfo dto = GeneralLedgerService.AccountBalanceInfo.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .balance(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}