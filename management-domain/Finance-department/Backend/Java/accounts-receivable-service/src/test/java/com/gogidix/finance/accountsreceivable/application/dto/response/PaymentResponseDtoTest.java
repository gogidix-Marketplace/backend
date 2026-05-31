package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.gogidix.finance.accountsreceivable.application.dto.response.PaymentResponseDto;
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
            .paymentNumber("test-paymentNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .paymentType(PaymentResponseDto.PaymentTypeDto.RECEIVED)
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate(LocalDate.of(2025,1,15))
            .paymentMethod("test-paymentMethod")
            .referenceNumber("test-referenceNumber")
            .bankAccount("test-bankAccount")
            .transactionId("test-transactionId")
            .checkNumber("test-checkNumber")
            .creditCardNumber("test-creditCardNumber")
            .description("test-description")
            .notes("test-notes")
            .depositDate(LocalDate.of(2025,1,15))
            .depositSlipNumber("test-depositSlipNumber")
            .batchId("test-batchId")
            .reconciled(true)
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciledBy("test-reconciledBy")
            .bankReconciliationId("test-bankReconciliationId")
            .clearedDate(LocalDate.of(2025,1,15))
            .autoApplied(true)
            .allocations(Collections.emptyList())
            .unappliedAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .gatewayTransactionId("test-gatewayTransactionId")
            .gatewayResponseCode("test-gatewayResponseCode")
            .gatewayResponseMessage("test-gatewayResponseMessage")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .refundedTo("test-refundedTo")
            .refundAmount(BigDecimal.TEN)
            .refundedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .refundReason("test-refundReason")
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-paymentId", dto.getPaymentId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-paymentNumber", dto.getPaymentNumber());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(PaymentResponseDto.PaymentTypeDto.RECEIVED, dto.getPaymentType());
        assertEquals(PaymentResponseDto.PaymentStatusDto.PENDING, dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaymentDate());
        assertEquals("test-paymentMethod", dto.getPaymentMethod());
        assertEquals("test-referenceNumber", dto.getReferenceNumber());
        assertEquals("test-bankAccount", dto.getBankAccount());
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals("test-checkNumber", dto.getCheckNumber());
        assertEquals("test-creditCardNumber", dto.getCreditCardNumber());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(LocalDate.of(2025,1,15), dto.getDepositDate());
        assertEquals("test-depositSlipNumber", dto.getDepositSlipNumber());
        assertEquals("test-batchId", dto.getBatchId());
        assertTrue(dto.getReconciled());
        assertEquals("test-reconciledBy", dto.getReconciledBy());
        assertEquals("test-bankReconciliationId", dto.getBankReconciliationId());
        assertEquals(LocalDate.of(2025,1,15), dto.getClearedDate());
        assertTrue(dto.getAutoApplied());
        assertEquals(BigDecimal.TEN, dto.getUnappliedAmount());
        assertEquals("test-exchangeRate", dto.getExchangeRate());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals(BigDecimal.TEN, dto.getBaseCurrencyAmount());
        assertEquals("test-gatewayTransactionId", dto.getGatewayTransactionId());
        assertEquals("test-gatewayResponseCode", dto.getGatewayResponseCode());
        assertEquals("test-gatewayResponseMessage", dto.getGatewayResponseMessage());
        assertEquals("test-processedBy", dto.getProcessedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertEquals("test-refundedTo", dto.getRefundedTo());
        assertEquals(BigDecimal.TEN, dto.getRefundAmount());
        assertEquals("test-refundReason", dto.getRefundReason());
        assertEquals("test-parentId", dto.getParentId());
        assertTrue(dto.getIsReversal());
    }

    @Test
    void testSettersAndGetters() {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId("val-id");
        dto.setPaymentId("val-paymentId");
        dto.setTenantId("val-tenantId");
        dto.setPaymentNumber("val-paymentNumber");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setPaymentType(PaymentResponseDto.PaymentTypeDto.RECEIVED);
        dto.setStatus(PaymentResponseDto.PaymentStatusDto.PENDING);
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setPaymentMethod("val-paymentMethod");
        dto.setReferenceNumber("val-referenceNumber");
        dto.setBankAccount("val-bankAccount");
        dto.setTransactionId("val-transactionId");
        dto.setCheckNumber("val-checkNumber");
        dto.setCreditCardNumber("val-creditCardNumber");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setDepositDate(LocalDate.of(2025,6,1));
        dto.setDepositSlipNumber("val-depositSlipNumber");
        dto.setBatchId("val-batchId");
        dto.setReconciled(true);
        dto.setReconciledBy("val-reconciledBy");
        dto.setBankReconciliationId("val-bankReconciliationId");
        dto.setClearedDate(LocalDate.of(2025,6,1));
        dto.setAutoApplied(true);
        dto.setUnappliedAmount(BigDecimal.ONE);
        dto.setExchangeRate("val-exchangeRate");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setBaseCurrencyAmount(BigDecimal.ONE);
        dto.setGatewayTransactionId("val-gatewayTransactionId");
        dto.setGatewayResponseCode("val-gatewayResponseCode");
        dto.setGatewayResponseMessage("val-gatewayResponseMessage");
        dto.setProcessedBy("val-processedBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setRejectionReason("val-rejectionReason");
        dto.setRefundedTo("val-refundedTo");
        dto.setRefundAmount(BigDecimal.ONE);
        dto.setRefundReason("val-refundReason");
        dto.setParentId("val-parentId");
        dto.setIsReversal(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentNumber", dto.getPaymentNumber());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(PaymentResponseDto.PaymentTypeDto.RECEIVED, dto.getPaymentType());
        assertEquals(PaymentResponseDto.PaymentStatusDto.PENDING, dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertEquals("val-referenceNumber", dto.getReferenceNumber());
        assertEquals("val-bankAccount", dto.getBankAccount());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-checkNumber", dto.getCheckNumber());
        assertEquals("val-creditCardNumber", dto.getCreditCardNumber());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(LocalDate.of(2025,6,1), dto.getDepositDate());
        assertEquals("val-depositSlipNumber", dto.getDepositSlipNumber());
        assertEquals("val-batchId", dto.getBatchId());
        assertTrue(dto.getReconciled());
        assertEquals("val-reconciledBy", dto.getReconciledBy());
        assertEquals("val-bankReconciliationId", dto.getBankReconciliationId());
        assertEquals(LocalDate.of(2025,6,1), dto.getClearedDate());
        assertTrue(dto.getAutoApplied());
        assertEquals(BigDecimal.ONE, dto.getUnappliedAmount());
        assertEquals("val-exchangeRate", dto.getExchangeRate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals(BigDecimal.ONE, dto.getBaseCurrencyAmount());
        assertEquals("val-gatewayTransactionId", dto.getGatewayTransactionId());
        assertEquals("val-gatewayResponseCode", dto.getGatewayResponseCode());
        assertEquals("val-gatewayResponseMessage", dto.getGatewayResponseMessage());
        assertEquals("val-processedBy", dto.getProcessedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertEquals("val-refundedTo", dto.getRefundedTo());
        assertEquals(BigDecimal.ONE, dto.getRefundAmount());
        assertEquals("val-refundReason", dto.getRefundReason());
        assertEquals("val-parentId", dto.getParentId());
        assertTrue(dto.getIsReversal());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentResponseDto dto1 = PaymentResponseDto.builder()
                        .id("test-id")
            .paymentId("test-paymentId")
            .tenantId("test-tenantId")
            .paymentNumber("test-paymentNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .paymentType(PaymentResponseDto.PaymentTypeDto.RECEIVED)
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate(LocalDate.of(2025,1,15))
            .paymentMethod("test-paymentMethod")
            .referenceNumber("test-referenceNumber")
            .bankAccount("test-bankAccount")
            .transactionId("test-transactionId")
            .checkNumber("test-checkNumber")
            .creditCardNumber("test-creditCardNumber")
            .description("test-description")
            .notes("test-notes")
            .depositDate(LocalDate.of(2025,1,15))
            .depositSlipNumber("test-depositSlipNumber")
            .batchId("test-batchId")
            .reconciled(true)
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciledBy("test-reconciledBy")
            .bankReconciliationId("test-bankReconciliationId")
            .clearedDate(LocalDate.of(2025,1,15))
            .autoApplied(true)
            .allocations(Collections.emptyList())
            .unappliedAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .gatewayTransactionId("test-gatewayTransactionId")
            .gatewayResponseCode("test-gatewayResponseCode")
            .gatewayResponseMessage("test-gatewayResponseMessage")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .refundedTo("test-refundedTo")
            .refundAmount(BigDecimal.TEN)
            .refundedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .refundReason("test-refundReason")
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        PaymentResponseDto dto2 = PaymentResponseDto.builder()
                        .id("test-id")
            .paymentId("test-paymentId")
            .tenantId("test-tenantId")
            .paymentNumber("test-paymentNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .paymentType(PaymentResponseDto.PaymentTypeDto.RECEIVED)
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate(LocalDate.of(2025,1,15))
            .paymentMethod("test-paymentMethod")
            .referenceNumber("test-referenceNumber")
            .bankAccount("test-bankAccount")
            .transactionId("test-transactionId")
            .checkNumber("test-checkNumber")
            .creditCardNumber("test-creditCardNumber")
            .description("test-description")
            .notes("test-notes")
            .depositDate(LocalDate.of(2025,1,15))
            .depositSlipNumber("test-depositSlipNumber")
            .batchId("test-batchId")
            .reconciled(true)
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciledBy("test-reconciledBy")
            .bankReconciliationId("test-bankReconciliationId")
            .clearedDate(LocalDate.of(2025,1,15))
            .autoApplied(true)
            .allocations(Collections.emptyList())
            .unappliedAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .gatewayTransactionId("test-gatewayTransactionId")
            .gatewayResponseCode("test-gatewayResponseCode")
            .gatewayResponseMessage("test-gatewayResponseMessage")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .refundedTo("test-refundedTo")
            .refundAmount(BigDecimal.TEN)
            .refundedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .refundReason("test-refundReason")
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
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
            .paymentNumber("test-paymentNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .paymentType(PaymentResponseDto.PaymentTypeDto.RECEIVED)
            .status(PaymentResponseDto.PaymentStatusDto.PENDING)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate(LocalDate.of(2025,1,15))
            .paymentMethod("test-paymentMethod")
            .referenceNumber("test-referenceNumber")
            .bankAccount("test-bankAccount")
            .transactionId("test-transactionId")
            .checkNumber("test-checkNumber")
            .creditCardNumber("test-creditCardNumber")
            .description("test-description")
            .notes("test-notes")
            .depositDate(LocalDate.of(2025,1,15))
            .depositSlipNumber("test-depositSlipNumber")
            .batchId("test-batchId")
            .reconciled(true)
            .reconciledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reconciledBy("test-reconciledBy")
            .bankReconciliationId("test-bankReconciliationId")
            .clearedDate(LocalDate.of(2025,1,15))
            .autoApplied(true)
            .allocations(Collections.emptyList())
            .unappliedAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .gatewayTransactionId("test-gatewayTransactionId")
            .gatewayResponseCode("test-gatewayResponseCode")
            .gatewayResponseMessage("test-gatewayResponseMessage")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .processedBy("test-processedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .refundedTo("test-refundedTo")
            .refundAmount(BigDecimal.TEN)
            .refundedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .refundReason("test-refundReason")
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}