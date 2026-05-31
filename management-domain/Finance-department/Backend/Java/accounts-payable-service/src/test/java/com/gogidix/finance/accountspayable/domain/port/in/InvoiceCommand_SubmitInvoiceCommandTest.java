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
class InvoiceCommand_SubmitInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.SubmitInvoiceCommand dto = new InvoiceCommand.SubmitInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.SubmitInvoiceCommand dto1 = new InvoiceCommand.SubmitInvoiceCommand();
        InvoiceCommand.SubmitInvoiceCommand dto2 = new InvoiceCommand.SubmitInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.SubmitInvoiceCommand dto = new InvoiceCommand.SubmitInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.SubmitInvoiceCommand dto = new InvoiceCommand.SubmitInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}