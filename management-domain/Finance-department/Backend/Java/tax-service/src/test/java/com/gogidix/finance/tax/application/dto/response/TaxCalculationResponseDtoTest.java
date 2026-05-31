package com.gogidix.finance.tax.application.dto.response;

import com.gogidix.finance.tax.application.dto.response.TaxCalculationResponseDto;
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
class TaxCalculationResponseDtoTest {

        @Test
    void testBuilder() {
        TaxCalculationResponseDto dto = TaxCalculationResponseDto.builder()
                        .id("test-id")
            .calculationId("test-calculationId")
            .tenantId("test-tenantId")
            .transactionId("test-transactionId")
            .transactionType(TaxCalculationResponseDto.TransactionTypeDto.SALES)
            .transactionDate(LocalDate.of(2025,1,15))
            .jurisdiction(TaxCalculationResponseDto.JurisdictionDto.US_FEDERAL)
            .currency("test-currency")
            .baseAmount(BigDecimal.TEN)
            .taxableAmount(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .taxBreakdown(Collections.emptyList())
            .exemptions(Collections.emptyList())
            .deductions(Collections.emptyList())
            .effectiveTaxRate(BigDecimal.TEN)
            .calculationMethod(TaxCalculationResponseDto.CalculationMethodDto.FLAT_RATE)
            .status(TaxCalculationResponseDto.CalculationStatusDto.PENDING)
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculatedBy("test-calculatedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .referenceNumber("test-referenceNumber")
            .notes("test-notes")
            .context(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-calculationId", dto.getCalculationId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals(TaxCalculationResponseDto.TransactionTypeDto.SALES, dto.getTransactionType());
        assertEquals(LocalDate.of(2025,1,15), dto.getTransactionDate());
        assertEquals(TaxCalculationResponseDto.JurisdictionDto.US_FEDERAL, dto.getJurisdiction());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getBaseAmount());
        assertEquals(BigDecimal.TEN, dto.getTaxableAmount());
        assertEquals(BigDecimal.TEN, dto.getTotalTax());
        assertEquals(BigDecimal.TEN, dto.getNetAmount());
        assertEquals(BigDecimal.TEN, dto.getEffectiveTaxRate());
        assertEquals(TaxCalculationResponseDto.CalculationMethodDto.FLAT_RATE, dto.getCalculationMethod());
        assertEquals(TaxCalculationResponseDto.CalculationStatusDto.PENDING, dto.getStatus());
        assertEquals("test-calculatedBy", dto.getCalculatedBy());
        assertEquals("test-verifiedBy", dto.getVerifiedBy());
        assertEquals("test-referenceNumber", dto.getReferenceNumber());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        TaxCalculationResponseDto dto = new TaxCalculationResponseDto();
        dto.setId("val-id");
        dto.setCalculationId("val-calculationId");
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setTransactionType(TaxCalculationResponseDto.TransactionTypeDto.SALES);
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setJurisdiction(TaxCalculationResponseDto.JurisdictionDto.US_FEDERAL);
        dto.setCurrency("val-currency");
        dto.setBaseAmount(BigDecimal.ONE);
        dto.setTaxableAmount(BigDecimal.ONE);
        dto.setTotalTax(BigDecimal.ONE);
        dto.setNetAmount(BigDecimal.ONE);
        dto.setEffectiveTaxRate(BigDecimal.ONE);
        dto.setCalculationMethod(TaxCalculationResponseDto.CalculationMethodDto.FLAT_RATE);
        dto.setStatus(TaxCalculationResponseDto.CalculationStatusDto.PENDING);
        dto.setCalculatedBy("val-calculatedBy");
        dto.setVerifiedBy("val-verifiedBy");
        dto.setReferenceNumber("val-referenceNumber");
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-calculationId", dto.getCalculationId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(TaxCalculationResponseDto.TransactionTypeDto.SALES, dto.getTransactionType());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals(TaxCalculationResponseDto.JurisdictionDto.US_FEDERAL, dto.getJurisdiction());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getBaseAmount());
        assertEquals(BigDecimal.ONE, dto.getTaxableAmount());
        assertEquals(BigDecimal.ONE, dto.getTotalTax());
        assertEquals(BigDecimal.ONE, dto.getNetAmount());
        assertEquals(BigDecimal.ONE, dto.getEffectiveTaxRate());
        assertEquals(TaxCalculationResponseDto.CalculationMethodDto.FLAT_RATE, dto.getCalculationMethod());
        assertEquals(TaxCalculationResponseDto.CalculationStatusDto.PENDING, dto.getStatus());
        assertEquals("val-calculatedBy", dto.getCalculatedBy());
        assertEquals("val-verifiedBy", dto.getVerifiedBy());
        assertEquals("val-referenceNumber", dto.getReferenceNumber());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationResponseDto dto1 = TaxCalculationResponseDto.builder()
                        .id("test-id")
            .calculationId("test-calculationId")
            .tenantId("test-tenantId")
            .transactionId("test-transactionId")
            .transactionType(TaxCalculationResponseDto.TransactionTypeDto.SALES)
            .transactionDate(LocalDate.of(2025,1,15))
            .jurisdiction(TaxCalculationResponseDto.JurisdictionDto.US_FEDERAL)
            .currency("test-currency")
            .baseAmount(BigDecimal.TEN)
            .taxableAmount(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .taxBreakdown(Collections.emptyList())
            .exemptions(Collections.emptyList())
            .deductions(Collections.emptyList())
            .effectiveTaxRate(BigDecimal.TEN)
            .calculationMethod(TaxCalculationResponseDto.CalculationMethodDto.FLAT_RATE)
            .status(TaxCalculationResponseDto.CalculationStatusDto.PENDING)
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculatedBy("test-calculatedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .referenceNumber("test-referenceNumber")
            .notes("test-notes")
            .context(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TaxCalculationResponseDto dto2 = TaxCalculationResponseDto.builder()
                        .id("test-id")
            .calculationId("test-calculationId")
            .tenantId("test-tenantId")
            .transactionId("test-transactionId")
            .transactionType(TaxCalculationResponseDto.TransactionTypeDto.SALES)
            .transactionDate(LocalDate.of(2025,1,15))
            .jurisdiction(TaxCalculationResponseDto.JurisdictionDto.US_FEDERAL)
            .currency("test-currency")
            .baseAmount(BigDecimal.TEN)
            .taxableAmount(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .taxBreakdown(Collections.emptyList())
            .exemptions(Collections.emptyList())
            .deductions(Collections.emptyList())
            .effectiveTaxRate(BigDecimal.TEN)
            .calculationMethod(TaxCalculationResponseDto.CalculationMethodDto.FLAT_RATE)
            .status(TaxCalculationResponseDto.CalculationStatusDto.PENDING)
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculatedBy("test-calculatedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .referenceNumber("test-referenceNumber")
            .notes("test-notes")
            .context(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxCalculationResponseDto dto = TaxCalculationResponseDto.builder()
                        .id("test-id")
            .calculationId("test-calculationId")
            .tenantId("test-tenantId")
            .transactionId("test-transactionId")
            .transactionType(TaxCalculationResponseDto.TransactionTypeDto.SALES)
            .transactionDate(LocalDate.of(2025,1,15))
            .jurisdiction(TaxCalculationResponseDto.JurisdictionDto.US_FEDERAL)
            .currency("test-currency")
            .baseAmount(BigDecimal.TEN)
            .taxableAmount(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .taxBreakdown(Collections.emptyList())
            .exemptions(Collections.emptyList())
            .deductions(Collections.emptyList())
            .effectiveTaxRate(BigDecimal.TEN)
            .calculationMethod(TaxCalculationResponseDto.CalculationMethodDto.FLAT_RATE)
            .status(TaxCalculationResponseDto.CalculationStatusDto.PENDING)
            .calculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculatedBy("test-calculatedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .referenceNumber("test-referenceNumber")
            .notes("test-notes")
            .context(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}