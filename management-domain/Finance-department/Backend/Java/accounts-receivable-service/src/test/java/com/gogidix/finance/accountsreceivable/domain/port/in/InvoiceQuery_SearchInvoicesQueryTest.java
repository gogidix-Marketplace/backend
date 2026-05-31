package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.port.in.InvoiceQuery;
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
class InvoiceQuery_SearchInvoicesQueryTest {

        @Test
    void testSettersAndGetters() {
        InvoiceQuery.SearchInvoicesQuery dto = new InvoiceQuery.SearchInvoicesQuery();
        dto.setTenantId("val-tenantId");
        dto.setSearchTerm("val-searchTerm");
        dto.setCustomerId("val-customerId");
        dto.setStatus("val-status");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setMinAmount(BigDecimal.ONE);
        dto.setMaxAmount(BigDecimal.ONE);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-status", dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(BigDecimal.ONE, dto.getMinAmount());
        assertEquals(BigDecimal.ONE, dto.getMaxAmount());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceQuery.SearchInvoicesQuery dto1 = new InvoiceQuery.SearchInvoicesQuery();
        InvoiceQuery.SearchInvoicesQuery dto2 = new InvoiceQuery.SearchInvoicesQuery();
        dto1.setTenantId("test");
        dto1.setSearchTerm("test");
        dto1.setCustomerId("test");
        dto1.setStatus("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setMinAmount(BigDecimal.TEN);
        dto1.setMaxAmount(BigDecimal.TEN);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setSearchTerm("test");
        dto2.setCustomerId("test");
        dto2.setStatus("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setMinAmount(BigDecimal.TEN);
        dto2.setMaxAmount(BigDecimal.TEN);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceQuery.SearchInvoicesQuery dto = new InvoiceQuery.SearchInvoicesQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setCustomerId("test");
        dto.setStatus("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setMinAmount(BigDecimal.TEN);
        dto.setMaxAmount(BigDecimal.TEN);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceQuery.SearchInvoicesQuery dto = new InvoiceQuery.SearchInvoicesQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setCustomerId("test");
        dto.setStatus("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setMinAmount(BigDecimal.TEN);
        dto.setMaxAmount(BigDecimal.TEN);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}