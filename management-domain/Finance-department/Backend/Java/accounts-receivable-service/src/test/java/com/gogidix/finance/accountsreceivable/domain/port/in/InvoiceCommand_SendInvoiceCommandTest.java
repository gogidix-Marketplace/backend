package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.port.in.InvoiceCommand;
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
class InvoiceCommand_SendInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.SendInvoiceCommand dto = new InvoiceCommand.SendInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setRecipientEmail("val-recipientEmail");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-recipientEmail", dto.getRecipientEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.SendInvoiceCommand dto1 = new InvoiceCommand.SendInvoiceCommand();
        InvoiceCommand.SendInvoiceCommand dto2 = new InvoiceCommand.SendInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setRecipientEmail("test");
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setRecipientEmail("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.SendInvoiceCommand dto = new InvoiceCommand.SendInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setRecipientEmail("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.SendInvoiceCommand dto = new InvoiceCommand.SendInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setRecipientEmail("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}