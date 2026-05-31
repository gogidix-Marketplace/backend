package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.port.in.PaymentCommand;
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
class PaymentCommand_CreatePaymentCommandTest {

        @Test
    void testBuilder() {
        PaymentCommand.CreatePaymentCommand dto = PaymentCommand.CreatePaymentCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceIds(Collections.emptyList())
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentMethod(Payment.PaymentMethod.BANK_TRANSFER)
            .paymentDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .notes("test-notes")
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .batchId("test-batchId")
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdBy("test-createdBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-vendorId", dto.getVendorId());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaymentDate());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("test-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("test-checkNumber", dto.getCheckNumber());
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals(BigDecimal.TEN, dto.getFeeAmount());
        assertEquals("test-exchangeRate", dto.getExchangeRate());
        assertEquals("test-originalCurrency", dto.getOriginalCurrency());
        assertEquals(BigDecimal.TEN, dto.getOriginalAmount());
        assertEquals("test-attachmentUrl", dto.getAttachmentUrl());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PaymentCommand.CreatePaymentCommand dto = new PaymentCommand.CreatePaymentCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setVendorName("val-vendorName");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setCheckNumber("val-checkNumber");
        dto.setBatchId("val-batchId");
        dto.setFeeAmount(BigDecimal.ONE);
        dto.setExchangeRate("val-exchangeRate");
        dto.setOriginalCurrency("val-originalCurrency");
        dto.setOriginalAmount(BigDecimal.ONE);
        dto.setAttachmentUrl("val-attachmentUrl");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-checkNumber", dto.getCheckNumber());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals(BigDecimal.ONE, dto.getFeeAmount());
        assertEquals("val-exchangeRate", dto.getExchangeRate());
        assertEquals("val-originalCurrency", dto.getOriginalCurrency());
        assertEquals(BigDecimal.ONE, dto.getOriginalAmount());
        assertEquals("val-attachmentUrl", dto.getAttachmentUrl());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.CreatePaymentCommand dto1 = PaymentCommand.CreatePaymentCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceIds(Collections.emptyList())
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentMethod(Payment.PaymentMethod.BANK_TRANSFER)
            .paymentDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .notes("test-notes")
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .batchId("test-batchId")
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdBy("test-createdBy")
            .build();
        PaymentCommand.CreatePaymentCommand dto2 = PaymentCommand.CreatePaymentCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceIds(Collections.emptyList())
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentMethod(Payment.PaymentMethod.BANK_TRANSFER)
            .paymentDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .notes("test-notes")
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .batchId("test-batchId")
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdBy("test-createdBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PaymentCommand.CreatePaymentCommand dto = PaymentCommand.CreatePaymentCommand.builder()
                        .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .invoiceIds(Collections.emptyList())
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .paymentMethod(Payment.PaymentMethod.BANK_TRANSFER)
            .paymentDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .notes("test-notes")
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .batchId("test-batchId")
            .feeAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .originalCurrency("test-originalCurrency")
            .originalAmount(BigDecimal.TEN)
            .attachmentUrl("test-attachmentUrl")
            .createdBy("test-createdBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}