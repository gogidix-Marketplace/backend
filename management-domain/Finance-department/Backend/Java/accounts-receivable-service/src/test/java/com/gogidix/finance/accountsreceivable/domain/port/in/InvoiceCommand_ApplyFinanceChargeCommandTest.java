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
class InvoiceCommand_ApplyFinanceChargeCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.ApplyFinanceChargeCommand dto = new InvoiceCommand.ApplyFinanceChargeCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setRate(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals(BigDecimal.ONE, dto.getRate());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.ApplyFinanceChargeCommand dto1 = new InvoiceCommand.ApplyFinanceChargeCommand();
        InvoiceCommand.ApplyFinanceChargeCommand dto2 = new InvoiceCommand.ApplyFinanceChargeCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setRate(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setRate(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.ApplyFinanceChargeCommand dto = new InvoiceCommand.ApplyFinanceChargeCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setRate(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.ApplyFinanceChargeCommand dto = new InvoiceCommand.ApplyFinanceChargeCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setRate(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}