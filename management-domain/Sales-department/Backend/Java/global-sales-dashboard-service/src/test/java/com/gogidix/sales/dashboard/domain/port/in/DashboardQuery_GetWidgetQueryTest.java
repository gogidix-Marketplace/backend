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
class DashboardQuery_GetWidgetQueryTest {

        @Test
    void testSettersAndGetters() {
        DashboardQuery.GetWidgetQuery dto = new DashboardQuery.GetWidgetQuery();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setWidgetId("val-widgetId");
        dto.setIncludeHistoricalData(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-widgetId", dto.getWidgetId());
        assertTrue(dto.getIncludeHistoricalData());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardQuery.GetWidgetQuery dto1 = new DashboardQuery.GetWidgetQuery();
        DashboardQuery.GetWidgetQuery dto2 = new DashboardQuery.GetWidgetQuery();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setWidgetId("test");
        dto1.setIncludeHistoricalData(true);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setWidgetId("test");
        dto2.setIncludeHistoricalData(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardQuery.GetWidgetQuery dto = new DashboardQuery.GetWidgetQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setWidgetId("test");
        dto.setIncludeHistoricalData(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardQuery.GetWidgetQuery dto = new DashboardQuery.GetWidgetQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setWidgetId("test");
        dto.setIncludeHistoricalData(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}