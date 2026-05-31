package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.port.in.BudgetTrackingQuery;
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
class BudgetTrackingQuery_SearchTransactionsQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.SearchTransactionsQuery dto = new BudgetTrackingQuery.SearchTransactionsQuery();
        dto.setTenantId("val-tenantId");
        dto.setSearchTerm("val-searchTerm");
        dto.setTransactionType("val-transactionType");
        dto.setStatus("val-status");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setMinAmount(BigDecimal.ONE);
        dto.setMaxAmount(BigDecimal.ONE);
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertEquals("val-transactionType", dto.getTransactionType());
        assertEquals("val-status", dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(BigDecimal.ONE, dto.getMinAmount());
        assertEquals(BigDecimal.ONE, dto.getMaxAmount());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.SearchTransactionsQuery dto1 = new BudgetTrackingQuery.SearchTransactionsQuery();
        BudgetTrackingQuery.SearchTransactionsQuery dto2 = new BudgetTrackingQuery.SearchTransactionsQuery();
        dto1.setTenantId("test");
        dto1.setSearchTerm("test");
        dto1.setTransactionType("test");
        dto1.setStatus("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setMinAmount(BigDecimal.TEN);
        dto1.setMaxAmount(BigDecimal.TEN);
        dto1.setCategory("test");
        dto1.setDepartment("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setSearchTerm("test");
        dto2.setTransactionType("test");
        dto2.setStatus("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setMinAmount(BigDecimal.TEN);
        dto2.setMaxAmount(BigDecimal.TEN);
        dto2.setCategory("test");
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
        BudgetTrackingQuery.SearchTransactionsQuery dto = new BudgetTrackingQuery.SearchTransactionsQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setTransactionType("test");
        dto.setStatus("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setMinAmount(BigDecimal.TEN);
        dto.setMaxAmount(BigDecimal.TEN);
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.SearchTransactionsQuery dto = new BudgetTrackingQuery.SearchTransactionsQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setTransactionType("test");
        dto.setStatus("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setMinAmount(BigDecimal.TEN);
        dto.setMaxAmount(BigDecimal.TEN);
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}