package com.gogidix.finance.tax.application.dto.response;

import com.gogidix.finance.tax.application.dto.response.TaxFilingResponseDto;
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
class TaxFilingResponseDtoTest {

        @Test
    void testBuilder() {
        TaxFilingResponseDto dto = TaxFilingResponseDto.builder()
                        .id("test-id")
            .filingId("test-filingId")
            .tenantId("test-tenantId")
            .filingPeriod(null)
            .filingType(TaxFilingResponseDto.FilingTypeDto.MONTHLY_RETURN)
            .jurisdiction(TaxFilingResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxFilingResponseDto.TaxTypeDto.SALES_TAX)
            .currency("test-currency")
            .grossSales(BigDecimal.TEN)
            .taxableSales(BigDecimal.TEN)
            .exemptSales(BigDecimal.TEN)
            .totalTaxCollected(BigDecimal.TEN)
            .totalTaxPaid(BigDecimal.TEN)
            .taxDue(BigDecimal.TEN)
            .taxRefund(BigDecimal.TEN)
            .penalty(BigDecimal.TEN)
            .interest(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .status(TaxFilingResponseDto.FilingStatusDto.DRAFT)
            .submissionDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .filingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementNumber("test-acknowledgementNumber")
            .submittedBy("test-submittedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculationIds(Collections.emptyList())
            .adjustments(Collections.emptyList())
            .attachments(Collections.emptyList())
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .paymentReference("test-paymentReference")
            .paymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metadata(Collections.emptyMap())
            .isOverdue(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-filingId", dto.getFilingId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(TaxFilingResponseDto.FilingTypeDto.MONTHLY_RETURN, dto.getFilingType());
        assertEquals(TaxFilingResponseDto.JurisdictionDto.US_FEDERAL, dto.getJurisdiction());
        assertEquals(TaxFilingResponseDto.TaxTypeDto.SALES_TAX, dto.getTaxType());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getGrossSales());
        assertEquals(BigDecimal.TEN, dto.getTaxableSales());
        assertEquals(BigDecimal.TEN, dto.getExemptSales());
        assertEquals(BigDecimal.TEN, dto.getTotalTaxCollected());
        assertEquals(BigDecimal.TEN, dto.getTotalTaxPaid());
        assertEquals(BigDecimal.TEN, dto.getTaxDue());
        assertEquals(BigDecimal.TEN, dto.getTaxRefund());
        assertEquals(BigDecimal.TEN, dto.getPenalty());
        assertEquals(BigDecimal.TEN, dto.getInterest());
        assertEquals(BigDecimal.TEN, dto.getNetAmount());
        assertEquals(TaxFilingResponseDto.FilingStatusDto.DRAFT, dto.getStatus());
        assertEquals(LocalDate.of(2025,1,15), dto.getSubmissionDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getDueDate());
        assertEquals("test-acknowledgementNumber", dto.getAcknowledgementNumber());
        assertEquals("test-submittedBy", dto.getSubmittedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-internalNotes", dto.getInternalNotes());
        assertEquals("test-paymentReference", dto.getPaymentReference());
        assertTrue(dto.getIsOverdue());
    }

    @Test
    void testSettersAndGetters() {
        TaxFilingResponseDto dto = new TaxFilingResponseDto();
        dto.setId("val-id");
        dto.setFilingId("val-filingId");
        dto.setTenantId("val-tenantId");
        dto.setFilingType(TaxFilingResponseDto.FilingTypeDto.MONTHLY_RETURN);
        dto.setJurisdiction(TaxFilingResponseDto.JurisdictionDto.US_FEDERAL);
        dto.setTaxType(TaxFilingResponseDto.TaxTypeDto.SALES_TAX);
        dto.setCurrency("val-currency");
        dto.setGrossSales(BigDecimal.ONE);
        dto.setTaxableSales(BigDecimal.ONE);
        dto.setExemptSales(BigDecimal.ONE);
        dto.setTotalTaxCollected(BigDecimal.ONE);
        dto.setTotalTaxPaid(BigDecimal.ONE);
        dto.setTaxDue(BigDecimal.ONE);
        dto.setTaxRefund(BigDecimal.ONE);
        dto.setPenalty(BigDecimal.ONE);
        dto.setInterest(BigDecimal.ONE);
        dto.setNetAmount(BigDecimal.ONE);
        dto.setStatus(TaxFilingResponseDto.FilingStatusDto.DRAFT);
        dto.setSubmissionDate(LocalDate.of(2025,6,1));
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setAcknowledgementNumber("val-acknowledgementNumber");
        dto.setSubmittedBy("val-submittedBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setNotes("val-notes");
        dto.setInternalNotes("val-internalNotes");
        dto.setPaymentReference("val-paymentReference");
        dto.setIsOverdue(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-filingId", dto.getFilingId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(TaxFilingResponseDto.FilingTypeDto.MONTHLY_RETURN, dto.getFilingType());
        assertEquals(TaxFilingResponseDto.JurisdictionDto.US_FEDERAL, dto.getJurisdiction());
        assertEquals(TaxFilingResponseDto.TaxTypeDto.SALES_TAX, dto.getTaxType());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getGrossSales());
        assertEquals(BigDecimal.ONE, dto.getTaxableSales());
        assertEquals(BigDecimal.ONE, dto.getExemptSales());
        assertEquals(BigDecimal.ONE, dto.getTotalTaxCollected());
        assertEquals(BigDecimal.ONE, dto.getTotalTaxPaid());
        assertEquals(BigDecimal.ONE, dto.getTaxDue());
        assertEquals(BigDecimal.ONE, dto.getTaxRefund());
        assertEquals(BigDecimal.ONE, dto.getPenalty());
        assertEquals(BigDecimal.ONE, dto.getInterest());
        assertEquals(BigDecimal.ONE, dto.getNetAmount());
        assertEquals(TaxFilingResponseDto.FilingStatusDto.DRAFT, dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getSubmissionDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals("val-acknowledgementNumber", dto.getAcknowledgementNumber());
        assertEquals("val-submittedBy", dto.getSubmittedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-internalNotes", dto.getInternalNotes());
        assertEquals("val-paymentReference", dto.getPaymentReference());
        assertTrue(dto.getIsOverdue());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingResponseDto dto1 = TaxFilingResponseDto.builder()
                        .id("test-id")
            .filingId("test-filingId")
            .tenantId("test-tenantId")
            .filingPeriod(null)
            .filingType(TaxFilingResponseDto.FilingTypeDto.MONTHLY_RETURN)
            .jurisdiction(TaxFilingResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxFilingResponseDto.TaxTypeDto.SALES_TAX)
            .currency("test-currency")
            .grossSales(BigDecimal.TEN)
            .taxableSales(BigDecimal.TEN)
            .exemptSales(BigDecimal.TEN)
            .totalTaxCollected(BigDecimal.TEN)
            .totalTaxPaid(BigDecimal.TEN)
            .taxDue(BigDecimal.TEN)
            .taxRefund(BigDecimal.TEN)
            .penalty(BigDecimal.TEN)
            .interest(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .status(TaxFilingResponseDto.FilingStatusDto.DRAFT)
            .submissionDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .filingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementNumber("test-acknowledgementNumber")
            .submittedBy("test-submittedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculationIds(Collections.emptyList())
            .adjustments(Collections.emptyList())
            .attachments(Collections.emptyList())
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .paymentReference("test-paymentReference")
            .paymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metadata(Collections.emptyMap())
            .isOverdue(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TaxFilingResponseDto dto2 = TaxFilingResponseDto.builder()
                        .id("test-id")
            .filingId("test-filingId")
            .tenantId("test-tenantId")
            .filingPeriod(null)
            .filingType(TaxFilingResponseDto.FilingTypeDto.MONTHLY_RETURN)
            .jurisdiction(TaxFilingResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxFilingResponseDto.TaxTypeDto.SALES_TAX)
            .currency("test-currency")
            .grossSales(BigDecimal.TEN)
            .taxableSales(BigDecimal.TEN)
            .exemptSales(BigDecimal.TEN)
            .totalTaxCollected(BigDecimal.TEN)
            .totalTaxPaid(BigDecimal.TEN)
            .taxDue(BigDecimal.TEN)
            .taxRefund(BigDecimal.TEN)
            .penalty(BigDecimal.TEN)
            .interest(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .status(TaxFilingResponseDto.FilingStatusDto.DRAFT)
            .submissionDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .filingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementNumber("test-acknowledgementNumber")
            .submittedBy("test-submittedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculationIds(Collections.emptyList())
            .adjustments(Collections.emptyList())
            .attachments(Collections.emptyList())
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .paymentReference("test-paymentReference")
            .paymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metadata(Collections.emptyMap())
            .isOverdue(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxFilingResponseDto dto = TaxFilingResponseDto.builder()
                        .id("test-id")
            .filingId("test-filingId")
            .tenantId("test-tenantId")
            .filingPeriod(null)
            .filingType(TaxFilingResponseDto.FilingTypeDto.MONTHLY_RETURN)
            .jurisdiction(TaxFilingResponseDto.JurisdictionDto.US_FEDERAL)
            .taxType(TaxFilingResponseDto.TaxTypeDto.SALES_TAX)
            .currency("test-currency")
            .grossSales(BigDecimal.TEN)
            .taxableSales(BigDecimal.TEN)
            .exemptSales(BigDecimal.TEN)
            .totalTaxCollected(BigDecimal.TEN)
            .totalTaxPaid(BigDecimal.TEN)
            .taxDue(BigDecimal.TEN)
            .taxRefund(BigDecimal.TEN)
            .penalty(BigDecimal.TEN)
            .interest(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .status(TaxFilingResponseDto.FilingStatusDto.DRAFT)
            .submissionDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .filingDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementDate(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledgementNumber("test-acknowledgementNumber")
            .submittedBy("test-submittedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .calculationIds(Collections.emptyList())
            .adjustments(Collections.emptyList())
            .attachments(Collections.emptyList())
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .paymentReference("test-paymentReference")
            .paymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metadata(Collections.emptyMap())
            .isOverdue(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}