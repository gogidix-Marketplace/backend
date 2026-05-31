package com.gogidix.finance.exchangerate.application.dto;

import com.gogidix.finance.exchangerate.application.dto.ExchangeRateRequest;
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
class ExchangeRateRequestTest {

        @Test
    void testSettersAndGetters() {
        ExchangeRateRequest dto = new ExchangeRateRequest();
        dto.setBaseCurrency("val-baseCurrency");
        dto.setQuoteCurrency("val-quoteCurrency");
        dto.setRate(BigDecimal.ONE);
        dto.setSource("val-source");
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-quoteCurrency", dto.getQuoteCurrency());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals("val-source", dto.getSource());
    }

    @Test
    void testEqualsAndHashCode() {
        ExchangeRateRequest dto1 = new ExchangeRateRequest();
        ExchangeRateRequest dto2 = new ExchangeRateRequest();
        dto1.setBaseCurrency("test");
        dto1.setQuoteCurrency("test");
        dto1.setRate(BigDecimal.TEN);
        dto1.setSource("test");
        dto2.setBaseCurrency("test");
        dto2.setQuoteCurrency("test");
        dto2.setRate(BigDecimal.TEN);
        dto2.setSource("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setBaseCurrency(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ExchangeRateRequest dto = new ExchangeRateRequest();
        dto.setBaseCurrency("test");
        dto.setQuoteCurrency("test");
        dto.setRate(BigDecimal.TEN);
        dto.setSource("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ExchangeRateRequest dto = new ExchangeRateRequest();
        dto.setBaseCurrency("test");
        dto.setQuoteCurrency("test");
        dto.setRate(BigDecimal.TEN);
        dto.setSource("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}