package com.gogidix.finance.conversion.interfaces.rest;

import com.gogidix.finance.conversion.interfaces.rest.CurrencyConversionController;
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
class CurrencyConversionController_RefreshRateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CurrencyConversionController.RefreshRateRequestDto dto = new CurrencyConversionController.RefreshRateRequestDto();
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setProvider("val-provider");
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals("val-provider", dto.getProvider());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyConversionController.RefreshRateRequestDto dto1 = new CurrencyConversionController.RefreshRateRequestDto();
        CurrencyConversionController.RefreshRateRequestDto dto2 = new CurrencyConversionController.RefreshRateRequestDto();
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto1.setProvider("test");
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        dto2.setProvider("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFromCurrency(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CurrencyConversionController.RefreshRateRequestDto dto = new CurrencyConversionController.RefreshRateRequestDto();
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setProvider("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CurrencyConversionController.RefreshRateRequestDto dto = new CurrencyConversionController.RefreshRateRequestDto();
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setProvider("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}