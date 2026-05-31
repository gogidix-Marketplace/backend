package com.gogidix.finance.accountspayable.application.dto.response;

import com.gogidix.finance.accountspayable.application.dto.response.PaymentScheduleResponseDto;
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
class PaymentScheduleResponseDtoTest {

        @Test
    void testBuilder() {
        PaymentScheduleResponseDto dto = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.INVOICE_BASED)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .frequency(PaymentScheduleResponseDto.ScheduleFrequencyDto.DAILY)
            .installments(42)
            .installmentAmount(BigDecimal.TEN)
            .scheduledPayments(Collections.emptyList())
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .autoPaymentMethod("test-autoPaymentMethod")
            .bankAccountId("test-bankAccountId")
            .description("test-description")
            .notes("test-notes")
            .nextPaymentDate(LocalDate.of(2025,1,15))
            .remainingInstallments(42)
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .lastPaymentDate(LocalDate.of(2025,1,15))
            .progressPercentage(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-scheduleId", dto.getScheduleId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-vendorId", dto.getVendorId());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(PaymentScheduleResponseDto.ScheduleTypeDto.INVOICE_BASED, dto.getScheduleType());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(PaymentScheduleResponseDto.ScheduleFrequencyDto.DAILY, dto.getFrequency());
        assertEquals(42, dto.getInstallments());
        assertEquals(BigDecimal.TEN, dto.getInstallmentAmount());
        assertEquals(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING, dto.getStatus());
        assertEquals("test-autoPaymentMethod", dto.getAutoPaymentMethod());
        assertEquals("test-bankAccountId", dto.getBankAccountId());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(LocalDate.of(2025,1,15), dto.getNextPaymentDate());
        assertEquals(42, dto.getRemainingInstallments());
        assertEquals(BigDecimal.TEN, dto.getPaidAmount());
        assertEquals(BigDecimal.TEN, dto.getRemainingAmount());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getLastPaymentDate());
        assertEquals(42, dto.getProgressPercentage());
    }

    @Test
    void testSettersAndGetters() {
        PaymentScheduleResponseDto dto = new PaymentScheduleResponseDto();
        dto.setId("val-id");
        dto.setScheduleId("val-scheduleId");
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setVendorName("val-vendorName");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setScheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.INVOICE_BASED);
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setFrequency(PaymentScheduleResponseDto.ScheduleFrequencyDto.DAILY);
        dto.setInstallments(99);
        dto.setInstallmentAmount(BigDecimal.ONE);
        dto.setStatus(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING);
        dto.setAutoPaymentMethod("val-autoPaymentMethod");
        dto.setBankAccountId("val-bankAccountId");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setNextPaymentDate(LocalDate.of(2025,6,1));
        dto.setRemainingInstallments(99);
        dto.setPaidAmount(BigDecimal.ONE);
        dto.setRemainingAmount(BigDecimal.ONE);
        dto.setCreatedBy("val-createdBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setLastPaymentDate(LocalDate.of(2025,6,1));
        dto.setProgressPercentage(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-scheduleId", dto.getScheduleId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(PaymentScheduleResponseDto.ScheduleTypeDto.INVOICE_BASED, dto.getScheduleType());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(PaymentScheduleResponseDto.ScheduleFrequencyDto.DAILY, dto.getFrequency());
        assertEquals(99, dto.getInstallments());
        assertEquals(BigDecimal.ONE, dto.getInstallmentAmount());
        assertEquals(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING, dto.getStatus());
        assertEquals("val-autoPaymentMethod", dto.getAutoPaymentMethod());
        assertEquals("val-bankAccountId", dto.getBankAccountId());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextPaymentDate());
        assertEquals(99, dto.getRemainingInstallments());
        assertEquals(BigDecimal.ONE, dto.getPaidAmount());
        assertEquals(BigDecimal.ONE, dto.getRemainingAmount());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getLastPaymentDate());
        assertEquals(99, dto.getProgressPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentScheduleResponseDto dto1 = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.INVOICE_BASED)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .frequency(PaymentScheduleResponseDto.ScheduleFrequencyDto.DAILY)
            .installments(42)
            .installmentAmount(BigDecimal.TEN)
            .scheduledPayments(Collections.emptyList())
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .autoPaymentMethod("test-autoPaymentMethod")
            .bankAccountId("test-bankAccountId")
            .description("test-description")
            .notes("test-notes")
            .nextPaymentDate(LocalDate.of(2025,1,15))
            .remainingInstallments(42)
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .lastPaymentDate(LocalDate.of(2025,1,15))
            .progressPercentage(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        PaymentScheduleResponseDto dto2 = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.INVOICE_BASED)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .frequency(PaymentScheduleResponseDto.ScheduleFrequencyDto.DAILY)
            .installments(42)
            .installmentAmount(BigDecimal.TEN)
            .scheduledPayments(Collections.emptyList())
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .autoPaymentMethod("test-autoPaymentMethod")
            .bankAccountId("test-bankAccountId")
            .description("test-description")
            .notes("test-notes")
            .nextPaymentDate(LocalDate.of(2025,1,15))
            .remainingInstallments(42)
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .lastPaymentDate(LocalDate.of(2025,1,15))
            .progressPercentage(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PaymentScheduleResponseDto dto = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.INVOICE_BASED)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .frequency(PaymentScheduleResponseDto.ScheduleFrequencyDto.DAILY)
            .installments(42)
            .installmentAmount(BigDecimal.TEN)
            .scheduledPayments(Collections.emptyList())
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .autoPaymentMethod("test-autoPaymentMethod")
            .bankAccountId("test-bankAccountId")
            .description("test-description")
            .notes("test-notes")
            .nextPaymentDate(LocalDate.of(2025,1,15))
            .remainingInstallments(42)
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .lastPaymentDate(LocalDate.of(2025,1,15))
            .progressPercentage(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}