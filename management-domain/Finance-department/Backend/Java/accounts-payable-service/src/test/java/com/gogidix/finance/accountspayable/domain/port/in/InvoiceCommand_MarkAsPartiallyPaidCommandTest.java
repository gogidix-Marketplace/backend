package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.port.in.InvoiceCommand;
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
class InvoiceCommand_MarkAsPartiallyPaidCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.MarkAsPartiallyPaidCommand dto = new InvoiceCommand.MarkAsPartiallyPaidCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setPaymentReference("val-paymentReference");
        dto.setAmountPaid(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-paymentReference", dto.getPaymentReference());
        assertEquals(BigDecimal.ONE, dto.getAmountPaid());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.MarkAsPartiallyPaidCommand dto1 = new InvoiceCommand.MarkAsPartiallyPaidCommand();
        InvoiceCommand.MarkAsPartiallyPaidCommand dto2 = new InvoiceCommand.MarkAsPartiallyPaidCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setPaymentReference("test");
        dto1.setAmountPaid(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setPaymentReference("test");
        dto2.setAmountPaid(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.MarkAsPartiallyPaidCommand dto = new InvoiceCommand.MarkAsPartiallyPaidCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setPaymentReference("test");
        dto.setAmountPaid(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.MarkAsPartiallyPaidCommand dto = new InvoiceCommand.MarkAsPartiallyPaidCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setPaymentReference("test");
        dto.setAmountPaid(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}