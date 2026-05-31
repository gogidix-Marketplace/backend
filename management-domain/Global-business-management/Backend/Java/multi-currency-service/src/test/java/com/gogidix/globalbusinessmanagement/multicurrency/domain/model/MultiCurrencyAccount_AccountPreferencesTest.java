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
class MultiCurrencyAccount_AccountPreferencesTest {

        @Test
    void testBuilder() {
        MultiCurrencyAccount.AccountPreferences dto = MultiCurrencyAccount.AccountPreferences.builder()
                        .defaultDisplayCurrency("test-defaultDisplayCurrency")
            .showAllBalances(true)
            .hideZeroBalances(true)
            .language("test-language")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .favoriteCurrencies(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-defaultDisplayCurrency", dto.getDefaultDisplayCurrency());
        assertTrue(dto.getShowAllBalances());
        assertTrue(dto.getHideZeroBalances());
        assertEquals("test-language", dto.getLanguage());
        assertEquals("test-dateFormat", dto.getDateFormat());
        assertEquals("test-numberFormat", dto.getNumberFormat());
    }

    @Test
    void testSettersAndGetters() {
        MultiCurrencyAccount.AccountPreferences dto = new MultiCurrencyAccount.AccountPreferences();
        dto.setDefaultDisplayCurrency("val-defaultDisplayCurrency");
        dto.setShowAllBalances(true);
        dto.setHideZeroBalances(true);
        dto.setLanguage("val-language");
        dto.setDateFormat("val-dateFormat");
        dto.setNumberFormat("val-numberFormat");
        assertEquals("val-defaultDisplayCurrency", dto.getDefaultDisplayCurrency());
        assertTrue(dto.getShowAllBalances());
        assertTrue(dto.getHideZeroBalances());
        assertEquals("val-language", dto.getLanguage());
        assertEquals("val-dateFormat", dto.getDateFormat());
        assertEquals("val-numberFormat", dto.getNumberFormat());
    }

    @Test
    void testEqualsAndHashCode() {
        MultiCurrencyAccount.AccountPreferences dto1 = MultiCurrencyAccount.AccountPreferences.builder()
                        .defaultDisplayCurrency("test-defaultDisplayCurrency")
            .showAllBalances(true)
            .hideZeroBalances(true)
            .language("test-language")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .favoriteCurrencies(Collections.emptyList())
            .build();
        MultiCurrencyAccount.AccountPreferences dto2 = MultiCurrencyAccount.AccountPreferences.builder()
                        .defaultDisplayCurrency("test-defaultDisplayCurrency")
            .showAllBalances(true)
            .hideZeroBalances(true)
            .language("test-language")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .favoriteCurrencies(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MultiCurrencyAccount.AccountPreferences dto = MultiCurrencyAccount.AccountPreferences.builder()
                        .defaultDisplayCurrency("test-defaultDisplayCurrency")
            .showAllBalances(true)
            .hideZeroBalances(true)
            .language("test-language")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .favoriteCurrencies(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}