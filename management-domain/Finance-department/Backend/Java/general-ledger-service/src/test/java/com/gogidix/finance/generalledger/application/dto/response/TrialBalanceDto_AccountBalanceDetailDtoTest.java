package com.gogidix.finance.generalledger.application.dto.response;

import com.gogidix.finance.generalledger.application.dto.response.LedgerAccountResponseDto;
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
class TrialBalanceDto_AccountBalanceDetailDtoTest {

        @Test
    void testBuilder() {
        TrialBalanceDto.AccountBalanceDetailDto dto = TrialBalanceDto.AccountBalanceDetailDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .netBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .build();
        assertNotNull(dto);
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BigDecimal.TEN, dto.getDebitBalance());
        assertEquals(BigDecimal.TEN, dto.getCreditBalance());
        assertEquals(BigDecimal.TEN, dto.getNetBalance());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        TrialBalanceDto.AccountBalanceDetailDto dto = new TrialBalanceDto.AccountBalanceDetailDto();
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setDebitBalance(BigDecimal.ONE);
        dto.setCreditBalance(BigDecimal.ONE);
        dto.setNetBalance(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getDebitBalance());
        assertEquals(BigDecimal.ONE, dto.getCreditBalance());
        assertEquals(BigDecimal.ONE, dto.getNetBalance());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        TrialBalanceDto.AccountBalanceDetailDto dto1 = TrialBalanceDto.AccountBalanceDetailDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .netBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .build();
        TrialBalanceDto.AccountBalanceDetailDto dto2 = TrialBalanceDto.AccountBalanceDetailDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .netBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrialBalanceDto.AccountBalanceDetailDto dto = TrialBalanceDto.AccountBalanceDetailDto.builder()
                        .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .accountName("test-accountName")
            .accountType(LedgerAccountResponseDto.AccountTypeDto.ASSET)
            .accountSubType(LedgerAccountResponseDto.AccountSubTypeDto.CURRENT_ASSET)
            .debitBalance(BigDecimal.TEN)
            .creditBalance(BigDecimal.TEN)
            .netBalance(BigDecimal.TEN)
            .currency("test-currency")
            .status(LedgerAccountResponseDto.AccountStatusDto.ACTIVE)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}