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
class BudgetTrackingQuery_GetAlertsByBudgetQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetAlertsByBudgetQuery dto = new BudgetTrackingQuery.GetAlertsByBudgetQuery();
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        dto.setEnabledOnly(true);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
        assertTrue(dto.isEnabledOnly());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetAlertsByBudgetQuery dto1 = new BudgetTrackingQuery.GetAlertsByBudgetQuery();
        BudgetTrackingQuery.GetAlertsByBudgetQuery dto2 = new BudgetTrackingQuery.GetAlertsByBudgetQuery();
        dto1.setTenantId("test");
        dto1.setBudgetId("test");
        dto1.setEnabledOnly(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setBudgetId("test");
        dto2.setEnabledOnly(true);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetAlertsByBudgetQuery dto = new BudgetTrackingQuery.GetAlertsByBudgetQuery();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setEnabledOnly(true);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetAlertsByBudgetQuery dto = new BudgetTrackingQuery.GetAlertsByBudgetQuery();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setEnabledOnly(true);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}