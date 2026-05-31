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
class GeneralLedgerService_BalanceSheetResultTest {

        @Test
    void testBuilder() {
        GeneralLedgerService.BalanceSheetResult dto = GeneralLedgerService.BalanceSheetResult.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .liabilitiesPlusEquity(BigDecimal.TEN)
            .isBalanced(true)
            .assetAccounts(Collections.emptyList())
            .liabilityAccounts(Collections.emptyList())
            .equityAccounts(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getAsOfDate());
        assertEquals(BigDecimal.TEN, dto.getTotalAssets());
        assertEquals(BigDecimal.TEN, dto.getTotalLiabilities());
        assertEquals(BigDecimal.TEN, dto.getTotalEquity());
        assertEquals(BigDecimal.TEN, dto.getLiabilitiesPlusEquity());
        assertTrue(dto.isBalanced());
    }

    @Test
    void testBuilderWithValues() {
        GeneralLedgerService.BalanceSheetResult dto = GeneralLedgerService.BalanceSheetResult.builder()
            .asOfDate(LocalDate.of(2025,6,1))
            .totalAssets(BigDecimal.ONE)
            .totalLiabilities(BigDecimal.ONE)
            .totalEquity(BigDecimal.ONE)
            .liabilitiesPlusEquity(BigDecimal.ONE)
            .isBalanced(true)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerService.BalanceSheetResult dto1 = GeneralLedgerService.BalanceSheetResult.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .liabilitiesPlusEquity(BigDecimal.TEN)
            .isBalanced(true)
            .assetAccounts(Collections.emptyList())
            .liabilityAccounts(Collections.emptyList())
            .equityAccounts(Collections.emptyList())
            .build();
        GeneralLedgerService.BalanceSheetResult dto2 = GeneralLedgerService.BalanceSheetResult.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .liabilitiesPlusEquity(BigDecimal.TEN)
            .isBalanced(true)
            .assetAccounts(Collections.emptyList())
            .liabilityAccounts(Collections.emptyList())
            .equityAccounts(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GeneralLedgerService.BalanceSheetResult dto = GeneralLedgerService.BalanceSheetResult.builder()
                        .asOfDate(LocalDate.of(2025,1,15))
            .totalAssets(BigDecimal.TEN)
            .totalLiabilities(BigDecimal.TEN)
            .totalEquity(BigDecimal.TEN)
            .liabilitiesPlusEquity(BigDecimal.TEN)
            .isBalanced(true)
            .assetAccounts(Collections.emptyList())
            .liabilityAccounts(Collections.emptyList())
            .equityAccounts(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}