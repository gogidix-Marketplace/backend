package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import com.gogidix.finance.accountsreceivable.domain.port.in.PaymentCommand;
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
    void testSettersAndGetters() {
        PaymentCommand.CreatePaymentCommand dto = new PaymentCommand.CreatePaymentCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setPaymentMethod("val-paymentMethod");
        dto.setReferenceNumber("val-referenceNumber");
        dto.setBankAccount("val-bankAccount");
        dto.setCheckNumber("val-checkNumber");
        dto.setCreditCardNumber("val-creditCardNumber");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setDepositDate(LocalDate.of(2025,6,1));
        dto.setDepositSlipNumber("val-depositSlipNumber");
        dto.setBatchId("val-batchId");
        dto.setExchangeRate("val-exchangeRate");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setGatewayCustomerId("val-gatewayCustomerId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertEquals("val-referenceNumber", dto.getReferenceNumber());
        assertEquals("val-bankAccount", dto.getBankAccount());
        assertEquals("val-checkNumber", dto.getCheckNumber());
        assertEquals("val-creditCardNumber", dto.getCreditCardNumber());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(LocalDate.of(2025,6,1), dto.getDepositDate());
        assertEquals("val-depositSlipNumber", dto.getDepositSlipNumber());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-exchangeRate", dto.getExchangeRate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-gatewayCustomerId", dto.getGatewayCustomerId());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.CreatePaymentCommand dto1 = new PaymentCommand.CreatePaymentCommand();
        PaymentCommand.CreatePaymentCommand dto2 = new PaymentCommand.CreatePaymentCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setCustomerName("test");
        dto1.setInvoiceId("test");
        dto1.setInvoiceNumber("test");
        dto1.setPaymentType(Payment.PaymentType.RECEIVED);
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setPaymentDate(LocalDate.of(2025,1,1));
        dto1.setPaymentMethod("test");
        dto1.setReferenceNumber("test");
        dto1.setBankAccount("test");
        dto1.setCheckNumber("test");
        dto1.setCreditCardNumber("test");
        dto1.setDescription("test");
        dto1.setNotes("test");
        dto1.setDepositDate(LocalDate.of(2025,1,1));
        dto1.setDepositSlipNumber("test");
        dto1.setBatchId("test");
        dto1.setExchangeRate("test");
        dto1.setBaseCurrency("test");
        dto1.setGatewayCustomerId("test");
        dto1.setTags(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setCustomerName("test");
        dto2.setInvoiceId("test");
        dto2.setInvoiceNumber("test");
        dto2.setPaymentType(Payment.PaymentType.RECEIVED);
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setPaymentDate(LocalDate.of(2025,1,1));
        dto2.setPaymentMethod("test");
        dto2.setReferenceNumber("test");
        dto2.setBankAccount("test");
        dto2.setCheckNumber("test");
        dto2.setCreditCardNumber("test");
        dto2.setDescription("test");
        dto2.setNotes("test");
        dto2.setDepositDate(LocalDate.of(2025,1,1));
        dto2.setDepositSlipNumber("test");
        dto2.setBatchId("test");
        dto2.setExchangeRate("test");
        dto2.setBaseCurrency("test");
        dto2.setGatewayCustomerId("test");
        dto2.setTags(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.CreatePaymentCommand dto = new PaymentCommand.CreatePaymentCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setInvoiceId("test");
        dto.setInvoiceNumber("test");
        dto.setPaymentType(Payment.PaymentType.RECEIVED);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setPaymentDate(LocalDate.of(2025,1,1));
        dto.setPaymentMethod("test");
        dto.setReferenceNumber("test");
        dto.setBankAccount("test");
        dto.setCheckNumber("test");
        dto.setCreditCardNumber("test");
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setDepositDate(LocalDate.of(2025,1,1));
        dto.setDepositSlipNumber("test");
        dto.setBatchId("test");
        dto.setExchangeRate("test");
        dto.setBaseCurrency("test");
        dto.setGatewayCustomerId("test");
        dto.setTags(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.CreatePaymentCommand dto = new PaymentCommand.CreatePaymentCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setInvoiceId("test");
        dto.setInvoiceNumber("test");
        dto.setPaymentType(Payment.PaymentType.RECEIVED);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setPaymentDate(LocalDate.of(2025,1,1));
        dto.setPaymentMethod("test");
        dto.setReferenceNumber("test");
        dto.setBankAccount("test");
        dto.setCheckNumber("test");
        dto.setCreditCardNumber("test");
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setDepositDate(LocalDate.of(2025,1,1));
        dto.setDepositSlipNumber("test");
        dto.setBatchId("test");
        dto.setExchangeRate("test");
        dto.setBaseCurrency("test");
        dto.setGatewayCustomerId("test");
        dto.setTags(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}