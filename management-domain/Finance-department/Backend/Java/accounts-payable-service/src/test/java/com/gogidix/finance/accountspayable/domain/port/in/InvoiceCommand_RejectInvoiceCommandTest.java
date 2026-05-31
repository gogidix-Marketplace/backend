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
class InvoiceCommand_RejectInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.RejectInvoiceCommand dto = new InvoiceCommand.RejectInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setRejecter("val-rejecter");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-rejecter", dto.getRejecter());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.RejectInvoiceCommand dto1 = new InvoiceCommand.RejectInvoiceCommand();
        InvoiceCommand.RejectInvoiceCommand dto2 = new InvoiceCommand.RejectInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setRejecter("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setRejecter("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.RejectInvoiceCommand dto = new InvoiceCommand.RejectInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.RejectInvoiceCommand dto = new InvoiceCommand.RejectInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}