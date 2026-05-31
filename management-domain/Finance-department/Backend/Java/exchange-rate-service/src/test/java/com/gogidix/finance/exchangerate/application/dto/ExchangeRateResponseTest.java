package com.gogidix.finance.exchangerate.application.dto;

import com.gogidix.finance.exchangerate.application.dto.ExchangeRateResponse;
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
class ExchangeRateResponseTest {

        @Test
    void testBuilder() {
        ExchangeRateResponse dto = ExchangeRateResponse.builder()
                        .id("test-id")
            .baseCurrency("test-baseCurrency")
            .quoteCurrency("test-quoteCurrency")
            .rate(BigDecimal.TEN)
            .source("test-source")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals("test-quoteCurrency", dto.getQuoteCurrency());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals("test-source", dto.getSource());
    }

    @Test
    void testSettersAndGetters() {
        ExchangeRateResponse dto = new ExchangeRateResponse();
        dto.setId("val-id");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setQuoteCurrency("val-quoteCurrency");
        dto.setRate(BigDecimal.ONE);
        dto.setSource("val-source");
        assertEquals("val-id", dto.getId());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-quoteCurrency", dto.getQuoteCurrency());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals("val-source", dto.getSource());
    }

    @Test
    void testEqualsAndHashCode() {
        ExchangeRateResponse dto1 = ExchangeRateResponse.builder()
                        .id("test-id")
            .baseCurrency("test-baseCurrency")
            .quoteCurrency("test-quoteCurrency")
            .rate(BigDecimal.TEN)
            .source("test-source")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .build();
        ExchangeRateResponse dto2 = ExchangeRateResponse.builder()
                        .id("test-id")
            .baseCurrency("test-baseCurrency")
            .quoteCurrency("test-quoteCurrency")
            .rate(BigDecimal.TEN)
            .source("test-source")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ExchangeRateResponse dto = ExchangeRateResponse.builder()
                        .id("test-id")
            .baseCurrency("test-baseCurrency")
            .quoteCurrency("test-quoteCurrency")
            .rate(BigDecimal.TEN)
            .source("test-source")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}