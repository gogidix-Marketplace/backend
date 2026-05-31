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
class DashboardQuery_GetRegionalMetricsQueryTest {

        @Test
    void testSettersAndGetters() {
        DashboardQuery.GetRegionalMetricsQuery dto = new DashboardQuery.GetRegionalMetricsQuery();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setRegionCode("val-regionCode");
        dto.setBaseCurrency("val-baseCurrency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardQuery.GetRegionalMetricsQuery dto1 = new DashboardQuery.GetRegionalMetricsQuery();
        DashboardQuery.GetRegionalMetricsQuery dto2 = new DashboardQuery.GetRegionalMetricsQuery();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setRegionCode("test");
        dto1.setBaseCurrency("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setRegionCode("test");
        dto2.setBaseCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardQuery.GetRegionalMetricsQuery dto = new DashboardQuery.GetRegionalMetricsQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setRegionCode("test");
        dto.setBaseCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardQuery.GetRegionalMetricsQuery dto = new DashboardQuery.GetRegionalMetricsQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setRegionCode("test");
        dto.setBaseCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}