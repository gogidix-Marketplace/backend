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
class DashboardQuery_CompareRegionsQueryTest {

        @Test
    void testSettersAndGetters() {
        DashboardQuery.CompareRegionsQuery dto = new DashboardQuery.CompareRegionsQuery();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setMetric("val-metric");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-metric", dto.getMetric());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardQuery.CompareRegionsQuery dto1 = new DashboardQuery.CompareRegionsQuery();
        DashboardQuery.CompareRegionsQuery dto2 = new DashboardQuery.CompareRegionsQuery();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setRegionCodes(null);
        dto1.setBaseCurrency("test");
        dto1.setMetric("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setRegionCodes(null);
        dto2.setBaseCurrency("test");
        dto2.setMetric("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardQuery.CompareRegionsQuery dto = new DashboardQuery.CompareRegionsQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setRegionCodes(null);
        dto.setBaseCurrency("test");
        dto.setMetric("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardQuery.CompareRegionsQuery dto = new DashboardQuery.CompareRegionsQuery();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setRegionCodes(null);
        dto.setBaseCurrency("test");
        dto.setMetric("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}