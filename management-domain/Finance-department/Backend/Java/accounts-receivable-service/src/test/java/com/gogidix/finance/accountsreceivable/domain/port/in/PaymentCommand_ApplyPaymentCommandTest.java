package com.gogidix.finance.accountsreceivable.domain.port.in;

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
class PaymentCommand_ApplyPaymentCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.ApplyPaymentCommand dto = new PaymentCommand.ApplyPaymentCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setAmount(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.ONE, dto.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.ApplyPaymentCommand dto1 = new PaymentCommand.ApplyPaymentCommand();
        PaymentCommand.ApplyPaymentCommand dto2 = new PaymentCommand.ApplyPaymentCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setInvoiceId("test");
        dto1.setInvoiceNumber("test");
        dto1.setAmount(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setInvoiceId("test");
        dto2.setInvoiceNumber("test");
        dto2.setAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.ApplyPaymentCommand dto = new PaymentCommand.ApplyPaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setInvoiceId("test");
        dto.setInvoiceNumber("test");
        dto.setAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.ApplyPaymentCommand dto = new PaymentCommand.ApplyPaymentCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setInvoiceId("test");
        dto.setInvoiceNumber("test");
        dto.setAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}