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
class BudgetTrackingQuery_GetTransactionsByBudgetQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetTransactionsByBudgetQuery dto = new BudgetTrackingQuery.GetTransactionsByBudgetQuery();
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetTransactionsByBudgetQuery dto1 = new BudgetTrackingQuery.GetTransactionsByBudgetQuery();
        BudgetTrackingQuery.GetTransactionsByBudgetQuery dto2 = new BudgetTrackingQuery.GetTransactionsByBudgetQuery();
        dto1.setTenantId("test");
        dto1.setBudgetId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setBudgetId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        dto2.setSortBy("test");
        dto2.setSortDirection("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetTransactionsByBudgetQuery dto = new BudgetTrackingQuery.GetTransactionsByBudgetQuery();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetTransactionsByBudgetQuery dto = new BudgetTrackingQuery.GetTransactionsByBudgetQuery();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}