package com.gogidix.globalbusinessmanagement.currencyconversion.application.dto;

import com.gogidix.globalbusinessmanagement.currencyconversion.application.dto.CurrencyConversionResponseDto;
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
class CurrencyConversionResponseDtoTest {

        @Test
    void testBuilder() {
        CurrencyConversionResponseDto dto = CurrencyConversionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate("test-rate")
            .source("test-source")
            .effectiveDate("test-effectiveDate")
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-fromCurrency", dto.getFromCurrency());
        assertEquals("test-toCurrency", dto.getToCurrency());
        assertEquals("test-rate", dto.getRate());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-effectiveDate", dto.getEffectiveDate());
        assertEquals("test-status", dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        CurrencyConversionResponseDto dto = new CurrencyConversionResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setRate("val-rate");
        dto.setSource("val-source");
        dto.setEffectiveDate("val-effectiveDate");
        dto.setStatus("val-status");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals("val-rate", dto.getRate());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-effectiveDate", dto.getEffectiveDate());
        assertEquals("val-status", dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyConversionResponseDto dto1 = CurrencyConversionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate("test-rate")
            .source("test-source")
            .effectiveDate("test-effectiveDate")
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CurrencyConversionResponseDto dto2 = CurrencyConversionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate("test-rate")
            .source("test-source")
            .effectiveDate("test-effectiveDate")
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CurrencyConversionResponseDto dto = CurrencyConversionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate("test-rate")
            .source("test-source")
            .effectiveDate("test-effectiveDate")
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}