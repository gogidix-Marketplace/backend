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
class DashboardQuery_GetWidgetsByCategoryQueryTest {

        @Test
    void testSettersAndGetters() {
        DashboardQuery.GetWidgetsByCategoryQuery dto = new DashboardQuery.GetWidgetsByCategoryQuery();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardQuery.GetWidgetsByCategoryQuery dto1 = new DashboardQuery.GetWidgetsByCategoryQuery();
        DashboardQuery.GetWidgetsByCategoryQuery dto2 = new DashboardQuery.GetWidgetsByCategoryQuery();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setCategory(null);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setCategory(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardQuery.GetWidgetsByCategoryQuery dto = new DashboardQuery.GetWidgetsByCategoryQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setCategory(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardQuery.GetWidgetsByCategoryQuery dto = new DashboardQuery.GetWidgetsByCategoryQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setCategory(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}