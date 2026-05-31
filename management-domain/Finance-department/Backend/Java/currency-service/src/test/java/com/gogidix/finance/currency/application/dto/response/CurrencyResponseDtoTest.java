package com.gogidix.finance.currency.application.dto.response;

import com.gogidix.finance.currency.application.dto.response.CurrencyResponseDto;
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
class CurrencyResponseDtoTest {

        @Test
    void testBuilder() {
        CurrencyResponseDto dto = CurrencyResponseDto.builder()
                        .id("test-id")
            .currencyCode("test-currencyCode")
            .name("test-name")
            .symbol("test-symbol")
            .decimalPlaces(42)
            .isoNumericCode("test-isoNumericCode")
            .status(CurrencyResponseDto.CurrencyStatusDto.ACTIVE)
            .countryCodes(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-currencyCode", dto.getCurrencyCode());
        assertEquals("test-name", dto.getName());
        assertEquals("test-symbol", dto.getSymbol());
        assertEquals(42, dto.getDecimalPlaces());
        assertEquals("test-isoNumericCode", dto.getIsoNumericCode());
        assertEquals(CurrencyResponseDto.CurrencyStatusDto.ACTIVE, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        CurrencyResponseDto dto = new CurrencyResponseDto();
        dto.setId("val-id");
        dto.setCurrencyCode("val-currencyCode");
        dto.setName("val-name");
        dto.setSymbol("val-symbol");
        dto.setDecimalPlaces(99);
        dto.setIsoNumericCode("val-isoNumericCode");
        dto.setStatus(CurrencyResponseDto.CurrencyStatusDto.ACTIVE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-currencyCode", dto.getCurrencyCode());
        assertEquals("val-name", dto.getName());
        assertEquals("val-symbol", dto.getSymbol());
        assertEquals(99, dto.getDecimalPlaces());
        assertEquals("val-isoNumericCode", dto.getIsoNumericCode());
        assertEquals(CurrencyResponseDto.CurrencyStatusDto.ACTIVE, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyResponseDto dto1 = CurrencyResponseDto.builder()
                        .id("test-id")
            .currencyCode("test-currencyCode")
            .name("test-name")
            .symbol("test-symbol")
            .decimalPlaces(42)
            .isoNumericCode("test-isoNumericCode")
            .status(CurrencyResponseDto.CurrencyStatusDto.ACTIVE)
            .countryCodes(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CurrencyResponseDto dto2 = CurrencyResponseDto.builder()
                        .id("test-id")
            .currencyCode("test-currencyCode")
            .name("test-name")
            .symbol("test-symbol")
            .decimalPlaces(42)
            .isoNumericCode("test-isoNumericCode")
            .status(CurrencyResponseDto.CurrencyStatusDto.ACTIVE)
            .countryCodes(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CurrencyResponseDto dto = CurrencyResponseDto.builder()
                        .id("test-id")
            .currencyCode("test-currencyCode")
            .name("test-name")
            .symbol("test-symbol")
            .decimalPlaces(42)
            .isoNumericCode("test-isoNumericCode")
            .status(CurrencyResponseDto.CurrencyStatusDto.ACTIVE)
            .countryCodes(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}