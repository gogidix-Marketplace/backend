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
class BudgetTrackingQuery_GetMonitorsByDepartmentQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetMonitorsByDepartmentQuery dto = new BudgetTrackingQuery.GetMonitorsByDepartmentQuery();
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
        BudgetTrackingQuery.GetMonitorsByDepartmentQuery dto1 = new BudgetTrackingQuery.GetMonitorsByDepartmentQuery();
        BudgetTrackingQuery.GetMonitorsByDepartmentQuery dto2 = new BudgetTrackingQuery.GetMonitorsByDepartmentQuery();
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
        BudgetTrackingQuery.GetMonitorsByDepartmentQuery dto = new BudgetTrackingQuery.GetMonitorsByDepartmentQuery();
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
        BudgetTrackingQuery.GetMonitorsByDepartmentQuery dto = new BudgetTrackingQuery.GetMonitorsByDepartmentQuery();
        dto.setTenantId("test");
        dto.setDepartment("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}