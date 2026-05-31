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
class BudgetTrackingQuery_GetMonitorsByPeriodQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetMonitorsByPeriodQuery dto = new BudgetTrackingQuery.GetMonitorsByPeriodQuery();
        dto.setTenantId("val-tenantId");
        dto.setStatus("val-status");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetMonitorsByPeriodQuery dto1 = new BudgetTrackingQuery.GetMonitorsByPeriodQuery();
        BudgetTrackingQuery.GetMonitorsByPeriodQuery dto2 = new BudgetTrackingQuery.GetMonitorsByPeriodQuery();
        dto1.setTenantId("test");
        dto1.setPeriod(null);
        dto1.setStatus("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setPeriod(null);
        dto2.setStatus("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetMonitorsByPeriodQuery dto = new BudgetTrackingQuery.GetMonitorsByPeriodQuery();
        dto.setTenantId("test");
        dto.setPeriod(null);
        dto.setStatus("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetMonitorsByPeriodQuery dto = new BudgetTrackingQuery.GetMonitorsByPeriodQuery();
        dto.setTenantId("test");
        dto.setPeriod(null);
        dto.setStatus("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}