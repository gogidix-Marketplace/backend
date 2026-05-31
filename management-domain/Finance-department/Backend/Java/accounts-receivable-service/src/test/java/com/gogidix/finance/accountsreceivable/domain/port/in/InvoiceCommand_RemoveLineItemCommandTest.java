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
class InvoiceCommand_RemoveLineItemCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.RemoveLineItemCommand dto = new InvoiceCommand.RemoveLineItemCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setLineItemId("val-lineItemId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-lineItemId", dto.getLineItemId());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.RemoveLineItemCommand dto1 = new InvoiceCommand.RemoveLineItemCommand();
        InvoiceCommand.RemoveLineItemCommand dto2 = new InvoiceCommand.RemoveLineItemCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setLineItemId("test");
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setLineItemId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.RemoveLineItemCommand dto = new InvoiceCommand.RemoveLineItemCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setLineItemId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.RemoveLineItemCommand dto = new InvoiceCommand.RemoveLineItemCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setLineItemId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}