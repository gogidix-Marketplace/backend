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
class BudgetTrackingQuery_GetMonitorQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetMonitorQuery dto = new BudgetTrackingQuery.GetMonitorQuery();
        dto.setTenantId("val-tenantId");
        dto.setMonitorId("val-monitorId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-monitorId", dto.getMonitorId());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetMonitorQuery dto1 = new BudgetTrackingQuery.GetMonitorQuery();
        BudgetTrackingQuery.GetMonitorQuery dto2 = new BudgetTrackingQuery.GetMonitorQuery();
        dto1.setTenantId("test");
        dto1.setMonitorId("test");
        dto2.setTenantId("test");
        dto2.setMonitorId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetMonitorQuery dto = new BudgetTrackingQuery.GetMonitorQuery();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetMonitorQuery dto = new BudgetTrackingQuery.GetMonitorQuery();
        dto.setTenantId("test");
        dto.setMonitorId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}