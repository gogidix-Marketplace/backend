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
class InvoiceQuery_GetInvoicesByDepartmentQueryTest {

        @Test
    void testSettersAndGetters() {
        InvoiceQuery.GetInvoicesByDepartmentQuery dto = new InvoiceQuery.GetInvoicesByDepartmentQuery();
        dto.setTenantId("val-tenantId");
        dto.setDepartment("val-department");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-department", dto.getDepartment());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceQuery.GetInvoicesByDepartmentQuery dto1 = new InvoiceQuery.GetInvoicesByDepartmentQuery();
        InvoiceQuery.GetInvoicesByDepartmentQuery dto2 = new InvoiceQuery.GetInvoicesByDepartmentQuery();
        dto1.setTenantId("test");
        dto1.setDepartment("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setDepartment("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceQuery.GetInvoicesByDepartmentQuery dto = new InvoiceQuery.GetInvoicesByDepartmentQuery();
        dto.setTenantId("test");
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceQuery.GetInvoicesByDepartmentQuery dto = new InvoiceQuery.GetInvoicesByDepartmentQuery();
        dto.setTenantId("test");
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}