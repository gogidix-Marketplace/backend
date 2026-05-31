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
class InvoiceCommand_CancelInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.CancelInvoiceCommand dto = new InvoiceCommand.CancelInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.CancelInvoiceCommand dto1 = new InvoiceCommand.CancelInvoiceCommand();
        InvoiceCommand.CancelInvoiceCommand dto2 = new InvoiceCommand.CancelInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.CancelInvoiceCommand dto = new InvoiceCommand.CancelInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.CancelInvoiceCommand dto = new InvoiceCommand.CancelInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}