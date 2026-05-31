package com.gogidix.finance.accountspayable.application.dto.response;

import com.gogidix.finance.accountspayable.application.dto.response.PaymentResponseDto;
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
class PaymentResponseDtoTest {

        @Test
    void testBuilder() {
        PaymentResponseDto dto = PaymentResponseDto.builder()
                        .id("test-id")
            .paymentId("test-paymentId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .paymentMethod(PaymentResponseDto.PaymentMethodDto.BANK_TRANSFER)
            .paymentReference("test-paymentReference")
            .paymentDate(LocalDate.of(2025,1,15))
            .scheduledDate(LocalDate.of(2025,1,15))
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .transactionReference("test-transactionReference")
            .description("test-description")
            .notes("test-notes")
            .batchId("test-batchId")
            .approvalReference("test-approvalReference")
            .rejectionReason("test-rejectionReason")
            .cancelledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .cancelledBy("test-cancelledBy")
            .cancellationReason("test-cancellationReason")
            .invoiceIds(Collections.emptyList())
            .allocations(Collections.emptyList())
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalAmount(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-paymentId", dto.getPaymentId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-vendorId", dto.getVendorId());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(PaymentResponseDto.PaymentStatusDto.PENDING, dto.getStatus());
        assertEquals(PaymentResponseDto.PaymentMethodDto.BANK_TRANSFER, dto.getPaymentMethod());
        assertEquals("test-paymentReference", dto.getPaymentReference());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaymentDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getScheduledDate());
        assertEquals("test-processedBy", dto.getProcessedBy());
        assertEquals("test-transactionReference", dto.getTransactionReference());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals("test-approvalReference", dto.getApprovalReference());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertEquals("test-cancelledBy", dto.getCancelledBy());
        assertEquals("test-cancellationReason", dto.getCancellationReason());
        assertEquals(BigDecimal.TEN, dto.getFeeAmount());
        assertEquals("test-exchangeRate", dto.getExchangeRate());
        assertEquals("test-originalCurrency", dto.getOriginalCurrency());
        assertEquals(BigDecimal.TEN, dto.getOriginalAmount());
        assertEquals("test-attachmentUrl", dto.getAttachmentUrl());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
    }

    @Test
    void testSettersAndGetters() {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId("val-id");
        dto.setPaymentId("val-paymentId");
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setVendorName("val-vendorName");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setStatus(PaymentResponseDto.PaymentStatusDto.PENDING);
        dto.setPaymentMethod(PaymentResponseDto.PaymentMethodDto.BANK_TRANSFER);
        dto.setPaymentReference("val-paymentReference");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setScheduledDate(LocalDate.of(2025,6,1));
        dto.setProcessedBy("val-processedBy");
        dto.setTransactionReference("val-transactionReference");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setBatchId("val-batchId");
        dto.setApprovalReference("val-approvalReference");
        dto.setRejectionReason("val-rejectionReason");
        dto.setCancelledBy("val-cancelledBy");
        dto.setCancellationReason("val-cancellationReason");
        dto.setFeeAmount(BigDecimal.ONE);
        dto.setExchangeRate("val-exchangeRate");
        dto.setOriginalCurrency("val-originalCurrency");
        dto.setOriginalAmount(BigDecimal.ONE);
        dto.setAttachmentUrl("val-attachmentUrl");
        dto.setTotalAmount(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(PaymentResponseDto.PaymentStatusDto.PENDING, dto.getStatus());
        assertEquals(PaymentResponseDto.PaymentMethodDto.BANK_TRANSFER, dto.getPaymentMethod());
        assertEquals("val-paymentReference", dto.getPaymentReference());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getScheduledDate());
        assertEquals("val-processedBy", dto.getProcessedBy());
        assertEquals("val-transactionReference", dto.getTransactionReference());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-approvalReference", dto.getApprovalReference());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertEquals("val-cancelledBy", dto.getCancelledBy());
        assertEquals("val-cancellationReason", dto.getCancellationReason());
        assertEquals(BigDecimal.ONE, dto.getFeeAmount());
        assertEquals("val-exchangeRate", dto.getExchangeRate());
        assertEquals("val-originalCurrency", dto.getOriginalCurrency());
        assertEquals(BigDecimal.ONE, dto.getOriginalAmount());
        assertEquals("val-attachmentUrl", dto.getAttachmentUrl());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentResponseDto dto1 = PaymentResponseDto.builder()
                        .id("test-id")
            .paymentId("test-paymentId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .paymentMethod(PaymentResponseDto.PaymentMethodDto.BANK_TRANSFER)
            .paymentReference("test-paymentReference")
            .paymentDate(LocalDate.of(2025,1,15))
            .scheduledDate(LocalDate.of(2025,1,15))
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .transactionReference("test-transactionReference")
            .description("test-description")
            .notes("test-notes")
            .batchId("test-batchId")
            .approvalReference("test-approvalReference")
            .rejectionReason("test-rejectionReason")
            .cancelledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .cancelledBy("test-cancelledBy")
            .cancellationReason("test-cancellationReason")
            .invoiceIds(Collections.emptyList())
            .allocations(Collections.emptyList())
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalAmount(BigDecimal.TEN)
            .build();
        PaymentResponseDto dto2 = PaymentResponseDto.builder()
                        .id("test-id")
            .paymentId("test-paymentId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .paymentMethod(PaymentResponseDto.PaymentMethodDto.BANK_TRANSFER)
            .paymentReference("test-paymentReference")
            .paymentDate(LocalDate.of(2025,1,15))
            .scheduledDate(LocalDate.of(2025,1,15))
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .transactionReference("test-transactionReference")
            .description("test-description")
            .notes("test-notes")
            .batchId("test-batchId")
            .approvalReference("test-approvalReference")
            .rejectionReason("test-rejectionReason")
            .cancelledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .cancelledBy("test-cancelledBy")
            .cancellationReason("test-cancellationReason")
            .invoiceIds(Collections.emptyList())
            .allocations(Collections.emptyList())
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalAmount(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PaymentResponseDto dto = PaymentResponseDto.builder()
                        .id("test-id")
            .paymentId("test-paymentId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .paymentMethod(PaymentResponseDto.PaymentMethodDto.BANK_TRANSFER)
            .paymentReference("test-paymentReference")
            .paymentDate(LocalDate.of(2025,1,15))
            .scheduledDate(LocalDate.of(2025,1,15))
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .transactionReference("test-transactionReference")
            .description("test-description")
            .notes("test-notes")
            .batchId("test-batchId")
            .approvalReference("test-approvalReference")
            .rejectionReason("test-rejectionReason")
            .cancelledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .cancelledBy("test-cancelledBy")
            .cancellationReason("test-cancellationReason")
            .invoiceIds(Collections.emptyList())
            .allocations(Collections.emptyList())
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalAmount(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}