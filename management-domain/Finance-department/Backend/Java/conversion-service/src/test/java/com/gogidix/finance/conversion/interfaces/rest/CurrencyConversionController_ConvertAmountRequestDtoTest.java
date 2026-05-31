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
class CurrencyConversionController_ConvertAmountRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CurrencyConversionController.ConvertAmountRequestDto dto = new CurrencyConversionController.ConvertAmountRequestDto();
        dto.setAmount(BigDecimal.ONE);
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setProvider("val-provider");
        dto.setApplyFee(true);
        dto.setFeePercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals("val-provider", dto.getProvider());
        assertTrue(dto.getApplyFee());
        assertEquals(BigDecimal.ONE, dto.getFeePercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyConversionController.ConvertAmountRequestDto dto1 = new CurrencyConversionController.ConvertAmountRequestDto();
        CurrencyConversionController.ConvertAmountRequestDto dto2 = new CurrencyConversionController.ConvertAmountRequestDto();
        dto1.setAmount(BigDecimal.TEN);
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto1.setProvider("test");
        dto1.setApplyFee(true);
        dto1.setFeePercentage(BigDecimal.TEN);
        dto2.setAmount(BigDecimal.TEN);
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        dto2.setProvider("test");
        dto2.setApplyFee(true);
        dto2.setFeePercentage(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAmount(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CurrencyConversionController.ConvertAmountRequestDto dto = new CurrencyConversionController.ConvertAmountRequestDto();
        dto.setAmount(BigDecimal.TEN);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setProvider("test");
        dto.setApplyFee(true);
        dto.setFeePercentage(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CurrencyConversionController.ConvertAmountRequestDto dto = new CurrencyConversionController.ConvertAmountRequestDto();
        dto.setAmount(BigDecimal.TEN);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setProvider("test");
        dto.setApplyFee(true);
        dto.setFeePercentage(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}