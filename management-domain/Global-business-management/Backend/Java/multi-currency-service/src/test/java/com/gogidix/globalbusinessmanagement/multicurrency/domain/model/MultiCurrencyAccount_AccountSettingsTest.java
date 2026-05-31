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
class MultiCurrencyAccount_AccountSettingsTest {

        @Test
    void testBuilder() {
        MultiCurrencyAccount.AccountSettings dto = MultiCurrencyAccount.AccountSettings.builder()
                        .autoConversion(true)
            .autoConversionTargetCurrency("test-autoConversionTargetCurrency")
            .autoConversionThreshold(BigDecimal.TEN)
            .roundingEnabled(true)
            .roundingDecimalPlaces(42)
            .requireApprovalForTransfers(true)
            .allowNegativeBalances(true)
            .overdraftLimit(BigDecimal.TEN)
            .multiSigRequired(true)
            .requiredApprovals(42)
            .timeZone("test-timeZone")
            .notificationEmail("test-notificationEmail")
            .build();
        assertNotNull(dto);
        assertTrue(dto.getAutoConversion());
        assertEquals("test-autoConversionTargetCurrency", dto.getAutoConversionTargetCurrency());
        assertEquals(BigDecimal.TEN, dto.getAutoConversionThreshold());
        assertTrue(dto.getRoundingEnabled());
        assertEquals(42, dto.getRoundingDecimalPlaces());
        assertTrue(dto.getRequireApprovalForTransfers());
        assertTrue(dto.getAllowNegativeBalances());
        assertEquals(BigDecimal.TEN, dto.getOverdraftLimit());
        assertTrue(dto.getMultiSigRequired());
        assertEquals(42, dto.getRequiredApprovals());
        assertEquals("test-timeZone", dto.getTimeZone());
        assertEquals("test-notificationEmail", dto.getNotificationEmail());
    }

    @Test
    void testSettersAndGetters() {
        MultiCurrencyAccount.AccountSettings dto = new MultiCurrencyAccount.AccountSettings();
        dto.setAutoConversion(true);
        dto.setAutoConversionTargetCurrency("val-autoConversionTargetCurrency");
        dto.setAutoConversionThreshold(BigDecimal.ONE);
        dto.setRoundingEnabled(true);
        dto.setRoundingDecimalPlaces(99);
        dto.setRequireApprovalForTransfers(true);
        dto.setAllowNegativeBalances(true);
        dto.setOverdraftLimit(BigDecimal.ONE);
        dto.setMultiSigRequired(true);
        dto.setRequiredApprovals(99);
        dto.setTimeZone("val-timeZone");
        dto.setNotificationEmail("val-notificationEmail");
        assertTrue(dto.getAutoConversion());
        assertEquals("val-autoConversionTargetCurrency", dto.getAutoConversionTargetCurrency());
        assertEquals(BigDecimal.ONE, dto.getAutoConversionThreshold());
        assertTrue(dto.getRoundingEnabled());
        assertEquals(99, dto.getRoundingDecimalPlaces());
        assertTrue(dto.getRequireApprovalForTransfers());
        assertTrue(dto.getAllowNegativeBalances());
        assertEquals(BigDecimal.ONE, dto.getOverdraftLimit());
        assertTrue(dto.getMultiSigRequired());
        assertEquals(99, dto.getRequiredApprovals());
        assertEquals("val-timeZone", dto.getTimeZone());
        assertEquals("val-notificationEmail", dto.getNotificationEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        MultiCurrencyAccount.AccountSettings dto1 = MultiCurrencyAccount.AccountSettings.builder()
                        .autoConversion(true)
            .autoConversionTargetCurrency("test-autoConversionTargetCurrency")
            .autoConversionThreshold(BigDecimal.TEN)
            .roundingEnabled(true)
            .roundingDecimalPlaces(42)
            .requireApprovalForTransfers(true)
            .allowNegativeBalances(true)
            .overdraftLimit(BigDecimal.TEN)
            .multiSigRequired(true)
            .requiredApprovals(42)
            .timeZone("test-timeZone")
            .notificationEmail("test-notificationEmail")
            .build();
        MultiCurrencyAccount.AccountSettings dto2 = MultiCurrencyAccount.AccountSettings.builder()
                        .autoConversion(true)
            .autoConversionTargetCurrency("test-autoConversionTargetCurrency")
            .autoConversionThreshold(BigDecimal.TEN)
            .roundingEnabled(true)
            .roundingDecimalPlaces(42)
            .requireApprovalForTransfers(true)
            .allowNegativeBalances(true)
            .overdraftLimit(BigDecimal.TEN)
            .multiSigRequired(true)
            .requiredApprovals(42)
            .timeZone("test-timeZone")
            .notificationEmail("test-notificationEmail")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MultiCurrencyAccount.AccountSettings dto = MultiCurrencyAccount.AccountSettings.builder()
                        .autoConversion(true)
            .autoConversionTargetCurrency("test-autoConversionTargetCurrency")
            .autoConversionThreshold(BigDecimal.TEN)
            .roundingEnabled(true)
            .roundingDecimalPlaces(42)
            .requireApprovalForTransfers(true)
            .allowNegativeBalances(true)
            .overdraftLimit(BigDecimal.TEN)
            .multiSigRequired(true)
            .requiredApprovals(42)
            .timeZone("test-timeZone")
            .notificationEmail("test-notificationEmail")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}