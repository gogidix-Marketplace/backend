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
class InvoiceCommand_CalculateTaxCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.CalculateTaxCommand dto = new InvoiceCommand.CalculateTaxCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setTaxRate(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.CalculateTaxCommand dto1 = new InvoiceCommand.CalculateTaxCommand();
        InvoiceCommand.CalculateTaxCommand dto2 = new InvoiceCommand.CalculateTaxCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setTaxRate(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setTaxRate(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.CalculateTaxCommand dto = new InvoiceCommand.CalculateTaxCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setTaxRate(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.CalculateTaxCommand dto = new InvoiceCommand.CalculateTaxCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setTaxRate(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}