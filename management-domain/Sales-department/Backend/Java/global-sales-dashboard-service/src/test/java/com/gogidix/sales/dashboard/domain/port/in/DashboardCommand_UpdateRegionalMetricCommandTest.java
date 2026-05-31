package com.gogidix.sales.dashboard.domain.port.in;

import com.gogidix.sales.dashboard.domain.port.in.DashboardCommand;
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
class DashboardCommand_UpdateRegionalMetricCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.UpdateRegionalMetricCommand dto = new DashboardCommand.UpdateRegionalMetricCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setRegionCode("val-regionCode");
        dto.setRegionName("val-regionName");
        dto.setRevenue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setTarget(BigDecimal.ONE);
        dto.setDeals(99);
        dto.setGrowthRate(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-regionName", dto.getRegionName());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getTarget());
        assertEquals(99, dto.getDeals());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.UpdateRegionalMetricCommand dto1 = new DashboardCommand.UpdateRegionalMetricCommand();
        DashboardCommand.UpdateRegionalMetricCommand dto2 = new DashboardCommand.UpdateRegionalMetricCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setRegionCode("test");
        dto1.setRegionName("test");
        dto1.setRevenue(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setTarget(BigDecimal.TEN);
        dto1.setDeals(42);
        dto1.setGrowthRate(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setRegionCode("test");
        dto2.setRegionName("test");
        dto2.setRevenue(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setTarget(BigDecimal.TEN);
        dto2.setDeals(42);
        dto2.setGrowthRate(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.UpdateRegionalMetricCommand dto = new DashboardCommand.UpdateRegionalMetricCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setRegionCode("test");
        dto.setRegionName("test");
        dto.setRevenue(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setTarget(BigDecimal.TEN);
        dto.setDeals(42);
        dto.setGrowthRate(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.UpdateRegionalMetricCommand dto = new DashboardCommand.UpdateRegionalMetricCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setRegionCode("test");
        dto.setRegionName("test");
        dto.setRevenue(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setTarget(BigDecimal.TEN);
        dto.setDeals(42);
        dto.setGrowthRate(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}