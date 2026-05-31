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
class BudgetTrackingQuery_GetUtilizationReportQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetUtilizationReportQuery dto = new BudgetTrackingQuery.GetUtilizationReportQuery();
        dto.setTenantId("val-tenantId");
        dto.setDepartment("val-department");
        dto.setCategory("val-category");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetUtilizationReportQuery dto1 = new BudgetTrackingQuery.GetUtilizationReportQuery();
        BudgetTrackingQuery.GetUtilizationReportQuery dto2 = new BudgetTrackingQuery.GetUtilizationReportQuery();
        dto1.setTenantId("test");
        dto1.setDepartment("test");
        dto1.setCategory("test");
        dto1.setFromPeriod(null);
        dto1.setToPeriod(null);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setDepartment("test");
        dto2.setCategory("test");
        dto2.setFromPeriod(null);
        dto2.setToPeriod(null);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetUtilizationReportQuery dto = new BudgetTrackingQuery.GetUtilizationReportQuery();
        dto.setTenantId("test");
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setFromPeriod(null);
        dto.setToPeriod(null);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetUtilizationReportQuery dto = new BudgetTrackingQuery.GetUtilizationReportQuery();
        dto.setTenantId("test");
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setFromPeriod(null);
        dto.setToPeriod(null);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}