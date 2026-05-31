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
class DashboardQuery_GetTrendDataQueryTest {

        @Test
    void testSettersAndGetters() {
        DashboardQuery.GetTrendDataQuery dto = new DashboardQuery.GetTrendDataQuery();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setRegion("val-region");
        dto.setPeriod("val-period");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-period", dto.getPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardQuery.GetTrendDataQuery dto1 = new DashboardQuery.GetTrendDataQuery();
        DashboardQuery.GetTrendDataQuery dto2 = new DashboardQuery.GetTrendDataQuery();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setRegion("test");
        dto1.setPeriod("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setRegion("test");
        dto2.setPeriod("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardQuery.GetTrendDataQuery dto = new DashboardQuery.GetTrendDataQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setRegion("test");
        dto.setPeriod("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardQuery.GetTrendDataQuery dto = new DashboardQuery.GetTrendDataQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setRegion("test");
        dto.setPeriod("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}