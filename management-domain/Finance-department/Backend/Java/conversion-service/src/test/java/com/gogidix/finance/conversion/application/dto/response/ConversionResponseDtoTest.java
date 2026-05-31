package com.gogidix.finance.conversion.application.dto.response;

import com.gogidix.finance.conversion.application.dto.response.ConversionResponseDto;
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
class ConversionResponseDtoTest {

        @Test
    void testBuilder() {
        ConversionResponseDto dto = ConversionResponseDto.builder()
                        .id("test-id")
            .conversionId("test-conversionId")
            .tenantId("test-tenantId")
            .requestedBy("test-requestedBy")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .status(ConversionResponseDto.ConversionStatusDto.PENDING)
            .provider("test-provider")
            .conversionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reference("test-reference")
            .correlationId("test-correlationId")
            .failureReason("test-failureReason")
            .fee(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reversible(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-conversionId", dto.getConversionId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-requestedBy", dto.getRequestedBy());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-fromCurrency", dto.getFromCurrency());
        assertEquals("test-toCurrency", dto.getToCurrency());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals(BigDecimal.TEN, dto.getConvertedAmount());
        assertEquals(ConversionResponseDto.ConversionStatusDto.PENDING, dto.getStatus());
        assertEquals("test-provider", dto.getProvider());
        assertEquals("test-reference", dto.getReference());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-failureReason", dto.getFailureReason());
        assertEquals(BigDecimal.TEN, dto.getFee());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertTrue(dto.getReversible());
    }

    @Test
    void testSettersAndGetters() {
        ConversionResponseDto dto = new ConversionResponseDto();
        dto.setId("val-id");
        dto.setConversionId("val-conversionId");
        dto.setTenantId("val-tenantId");
        dto.setRequestedBy("val-requestedBy");
        dto.setAmount(BigDecimal.ONE);
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setRate(BigDecimal.ONE);
        dto.setConvertedAmount(BigDecimal.ONE);
        dto.setStatus(ConversionResponseDto.ConversionStatusDto.PENDING);
        dto.setProvider("val-provider");
        dto.setReference("val-reference");
        dto.setCorrelationId("val-correlationId");
        dto.setFailureReason("val-failureReason");
        dto.setFee(BigDecimal.ONE);
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setReversible(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-conversionId", dto.getConversionId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestedBy", dto.getRequestedBy());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals(BigDecimal.ONE, dto.getConvertedAmount());
        assertEquals(ConversionResponseDto.ConversionStatusDto.PENDING, dto.getStatus());
        assertEquals("val-provider", dto.getProvider());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-failureReason", dto.getFailureReason());
        assertEquals(BigDecimal.ONE, dto.getFee());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertTrue(dto.getReversible());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionResponseDto dto1 = ConversionResponseDto.builder()
                        .id("test-id")
            .conversionId("test-conversionId")
            .tenantId("test-tenantId")
            .requestedBy("test-requestedBy")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .status(ConversionResponseDto.ConversionStatusDto.PENDING)
            .provider("test-provider")
            .conversionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reference("test-reference")
            .correlationId("test-correlationId")
            .failureReason("test-failureReason")
            .fee(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reversible(true)
            .build();
        ConversionResponseDto dto2 = ConversionResponseDto.builder()
                        .id("test-id")
            .conversionId("test-conversionId")
            .tenantId("test-tenantId")
            .requestedBy("test-requestedBy")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .status(ConversionResponseDto.ConversionStatusDto.PENDING)
            .provider("test-provider")
            .conversionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reference("test-reference")
            .correlationId("test-correlationId")
            .failureReason("test-failureReason")
            .fee(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reversible(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConversionResponseDto dto = ConversionResponseDto.builder()
                        .id("test-id")
            .conversionId("test-conversionId")
            .tenantId("test-tenantId")
            .requestedBy("test-requestedBy")
            .amount(BigDecimal.TEN)
            .fromCurrency("test-fromCurrency")
            .toCurrency("test-toCurrency")
            .rate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .status(ConversionResponseDto.ConversionStatusDto.PENDING)
            .provider("test-provider")
            .conversionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reference("test-reference")
            .correlationId("test-correlationId")
            .failureReason("test-failureReason")
            .fee(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reversible(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}