package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
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
class InvoiceQuery_GetInvoicesByStatusQueryTest {

        @Test
    void testSettersAndGetters() {
        InvoiceQuery.GetInvoicesByStatusQuery dto = new InvoiceQuery.GetInvoicesByStatusQuery();
        dto.setTenantId("val-tenantId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceQuery.GetInvoicesByStatusQuery dto1 = new InvoiceQuery.GetInvoicesByStatusQuery();
        InvoiceQuery.GetInvoicesByStatusQuery dto2 = new InvoiceQuery.GetInvoicesByStatusQuery();
        dto1.setTenantId("test");
        dto1.setStatus(Invoice.InvoiceStatus.DRAFT);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setStatus(Invoice.InvoiceStatus.DRAFT);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceQuery.GetInvoicesByStatusQuery dto = new InvoiceQuery.GetInvoicesByStatusQuery();
        dto.setTenantId("test");
        dto.setStatus(Invoice.InvoiceStatus.DRAFT);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceQuery.GetInvoicesByStatusQuery dto = new InvoiceQuery.GetInvoicesByStatusQuery();
        dto.setTenantId("test");
        dto.setStatus(Invoice.InvoiceStatus.DRAFT);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}