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
class InvoiceCommand_CalculateDiscountCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.CalculateDiscountCommand dto = new InvoiceCommand.CalculateDiscountCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setDiscountPercentage(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals(BigDecimal.ONE, dto.getDiscountPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.CalculateDiscountCommand dto1 = new InvoiceCommand.CalculateDiscountCommand();
        InvoiceCommand.CalculateDiscountCommand dto2 = new InvoiceCommand.CalculateDiscountCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setDiscountPercentage(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setDiscountPercentage(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.CalculateDiscountCommand dto = new InvoiceCommand.CalculateDiscountCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setDiscountPercentage(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.CalculateDiscountCommand dto = new InvoiceCommand.CalculateDiscountCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setDiscountPercentage(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}