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
class DashboardQuery_GetTopPerformingRegionsQueryTest {

        @Test
    void testSettersAndGetters() {
        DashboardQuery.GetTopPerformingRegionsQuery dto = new DashboardQuery.GetTopPerformingRegionsQuery();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setLimit(99);
        dto.setSortBy("val-sortBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals(99, dto.getLimit());
        assertEquals("val-sortBy", dto.getSortBy());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardQuery.GetTopPerformingRegionsQuery dto1 = new DashboardQuery.GetTopPerformingRegionsQuery();
        DashboardQuery.GetTopPerformingRegionsQuery dto2 = new DashboardQuery.GetTopPerformingRegionsQuery();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setLimit(42);
        dto1.setSortBy("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setLimit(42);
        dto2.setSortBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardQuery.GetTopPerformingRegionsQuery dto = new DashboardQuery.GetTopPerformingRegionsQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setLimit(42);
        dto.setSortBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardQuery.GetTopPerformingRegionsQuery dto = new DashboardQuery.GetTopPerformingRegionsQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setLimit(42);
        dto.setSortBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}