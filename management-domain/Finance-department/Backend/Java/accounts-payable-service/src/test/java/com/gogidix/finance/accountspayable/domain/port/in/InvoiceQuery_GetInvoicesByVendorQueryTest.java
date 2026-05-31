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
class InvoiceQuery_GetInvoicesByVendorQueryTest {

        @Test
    void testSettersAndGetters() {
        InvoiceQuery.GetInvoicesByVendorQuery dto = new InvoiceQuery.GetInvoicesByVendorQuery();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceQuery.GetInvoicesByVendorQuery dto1 = new InvoiceQuery.GetInvoicesByVendorQuery();
        InvoiceQuery.GetInvoicesByVendorQuery dto2 = new InvoiceQuery.GetInvoicesByVendorQuery();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceQuery.GetInvoicesByVendorQuery dto = new InvoiceQuery.GetInvoicesByVendorQuery();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceQuery.GetInvoicesByVendorQuery dto = new InvoiceQuery.GetInvoicesByVendorQuery();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}