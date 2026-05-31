package com.gogidix.finance.tax.application.dto.response;

import com.gogidix.finance.tax.application.dto.response.TaxRateResponseDto;
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
class TaxRateResponseDtoTest {

        @Test
    void testBuilder() {
        TaxRateResponseDto dto = TaxRateResponseDto.builder()
                        .id("test-id")
            .taxRateId("test-taxRateId")
            .tenantId("test-tenantId")
            .jurisdiction(TaxRateResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxRateResponseDto.TaxTypeDto.SALES_TAX)
            .taxCode("test-taxCode")
            .ratePercentage(BigDecimal.TEN)
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isCompound(true)
            .isRecoverable(true)
            .recoveryRate(BigDecimal.TEN)
            .minThreshold(BigDecimal.TEN)
            .maxThreshold(BigDecimal.TEN)
            .status(TaxRateResponseDto.TaxRateStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .version(42)
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-taxRateId", dto.getTaxRateId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(TaxRateResponseDto.JurisdictionDto.US_FEDERAL, dto.getJurisdiction());
        assertEquals(TaxRateResponseDto.TaxTypeDto.SALES_TAX, dto.getTaxType());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.TEN, dto.getRatePercentage());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiryDate());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getIsCompound());
        assertTrue(dto.getIsRecoverable());
        assertEquals(BigDecimal.TEN, dto.getRecoveryRate());
        assertEquals(BigDecimal.TEN, dto.getMinThreshold());
        assertEquals(BigDecimal.TEN, dto.getMaxThreshold());
        assertEquals(TaxRateResponseDto.TaxRateStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(42, dto.getVersion());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        TaxRateResponseDto dto = new TaxRateResponseDto();
        dto.setId("val-id");
        dto.setTaxRateId("val-taxRateId");
        dto.setTenantId("val-tenantId");
        dto.setJurisdiction(TaxRateResponseDto.JurisdictionDto.US_FEDERAL);
        dto.setTaxType(TaxRateResponseDto.TaxTypeDto.SALES_TAX);
        dto.setTaxCode("val-taxCode");
        dto.setRatePercentage(BigDecimal.ONE);
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setExpiryDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setIsCompound(true);
        dto.setIsRecoverable(true);
        dto.setRecoveryRate(BigDecimal.ONE);
        dto.setMinThreshold(BigDecimal.ONE);
        dto.setMaxThreshold(BigDecimal.ONE);
        dto.setStatus(TaxRateResponseDto.TaxRateStatusDto.DRAFT);
        dto.setCreatedBy("val-createdBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setVersion(99);
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-taxRateId", dto.getTaxRateId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(TaxRateResponseDto.JurisdictionDto.US_FEDERAL, dto.getJurisdiction());
        assertEquals(TaxRateResponseDto.TaxTypeDto.SALES_TAX, dto.getTaxType());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getRatePercentage());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiryDate());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsCompound());
        assertTrue(dto.getIsRecoverable());
        assertEquals(BigDecimal.ONE, dto.getRecoveryRate());
        assertEquals(BigDecimal.ONE, dto.getMinThreshold());
        assertEquals(BigDecimal.ONE, dto.getMaxThreshold());
        assertEquals(TaxRateResponseDto.TaxRateStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(99, dto.getVersion());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateResponseDto dto1 = TaxRateResponseDto.builder()
                        .id("test-id")
            .taxRateId("test-taxRateId")
            .tenantId("test-tenantId")
            .jurisdiction(TaxRateResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxRateResponseDto.TaxTypeDto.SALES_TAX)
            .taxCode("test-taxCode")
            .ratePercentage(BigDecimal.TEN)
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isCompound(true)
            .isRecoverable(true)
            .recoveryRate(BigDecimal.TEN)
            .minThreshold(BigDecimal.TEN)
            .maxThreshold(BigDecimal.TEN)
            .status(TaxRateResponseDto.TaxRateStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .version(42)
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TaxRateResponseDto dto2 = TaxRateResponseDto.builder()
                        .id("test-id")
            .taxRateId("test-taxRateId")
            .tenantId("test-tenantId")
            .jurisdiction(TaxRateResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxRateResponseDto.TaxTypeDto.SALES_TAX)
            .taxCode("test-taxCode")
            .ratePercentage(BigDecimal.TEN)
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isCompound(true)
            .isRecoverable(true)
            .recoveryRate(BigDecimal.TEN)
            .minThreshold(BigDecimal.TEN)
            .maxThreshold(BigDecimal.TEN)
            .status(TaxRateResponseDto.TaxRateStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .version(42)
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxRateResponseDto dto = TaxRateResponseDto.builder()
                        .id("test-id")
            .taxRateId("test-taxRateId")
            .tenantId("test-tenantId")
            .jurisdiction(TaxRateResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxRateResponseDto.TaxTypeDto.SALES_TAX)
            .taxCode("test-taxCode")
            .ratePercentage(BigDecimal.TEN)
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isCompound(true)
            .isRecoverable(true)
            .recoveryRate(BigDecimal.TEN)
            .minThreshold(BigDecimal.TEN)
            .maxThreshold(BigDecimal.TEN)
            .status(TaxRateResponseDto.TaxRateStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .version(42)
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}