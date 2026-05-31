package com.gogidix.sales.dashboard.domain.port.in;

import com.gogidix.sales.dashboard.domain.port.in.DashboardQuery;
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
class DashboardQuery_GetDrillDownDataQueryTest {

        @Test
    void testSettersAndGetters() {
        DashboardQuery.GetDrillDownDataQuery dto = new DashboardQuery.GetDrillDownDataQuery();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setDrillDownId("val-drillDownId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-drillDownId", dto.getDrillDownId());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardQuery.GetDrillDownDataQuery dto1 = new DashboardQuery.GetDrillDownDataQuery();
        DashboardQuery.GetDrillDownDataQuery dto2 = new DashboardQuery.GetDrillDownDataQuery();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setDrillDownId("test");
        dto1.setFilters(null);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setDrillDownId("test");
        dto2.setFilters(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardQuery.GetDrillDownDataQuery dto = new DashboardQuery.GetDrillDownDataQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setDrillDownId("test");
        dto.setFilters(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardQuery.GetDrillDownDataQuery dto = new DashboardQuery.GetDrillDownDataQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setDrillDownId("test");
        dto.setFilters(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}