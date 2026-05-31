package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.port.in.InvoiceQuery;
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
class InvoiceQuery_GetInvoiceByNumberQueryTest {

        @Test
    void testSettersAndGetters() {
        InvoiceQuery.GetInvoiceByNumberQuery dto = new InvoiceQuery.GetInvoiceByNumberQuery();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceNumber("val-invoiceNumber");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceQuery.GetInvoiceByNumberQuery dto1 = new InvoiceQuery.GetInvoiceByNumberQuery();
        InvoiceQuery.GetInvoiceByNumberQuery dto2 = new InvoiceQuery.GetInvoiceByNumberQuery();
        dto1.setTenantId("test");
        dto1.setInvoiceNumber("test");
        dto2.setTenantId("test");
        dto2.setInvoiceNumber("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceQuery.GetInvoiceByNumberQuery dto = new InvoiceQuery.GetInvoiceByNumberQuery();
        dto.setTenantId("test");
        dto.setInvoiceNumber("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceQuery.GetInvoiceByNumberQuery dto = new InvoiceQuery.GetInvoiceByNumberQuery();
        dto.setTenantId("test");
        dto.setInvoiceNumber("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}