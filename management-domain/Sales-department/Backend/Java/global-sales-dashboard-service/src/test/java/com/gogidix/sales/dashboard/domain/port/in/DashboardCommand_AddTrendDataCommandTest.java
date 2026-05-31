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
class DashboardCommand_AddTrendDataCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.AddTrendDataCommand dto = new DashboardCommand.AddTrendDataCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setPeriod("val-period");
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setRevenue(BigDecimal.ONE);
        dto.setDeals(99);
        dto.setConversionRate(BigDecimal.ONE);
        dto.setRegion("val-region");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(99, dto.getDeals());
        assertEquals(BigDecimal.ONE, dto.getConversionRate());
        assertEquals("val-region", dto.getRegion());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.AddTrendDataCommand dto1 = new DashboardCommand.AddTrendDataCommand();
        DashboardCommand.AddTrendDataCommand dto2 = new DashboardCommand.AddTrendDataCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setPeriod("test");
        dto1.setDate(LocalDate.of(2025,1,1));
        dto1.setRevenue(BigDecimal.TEN);
        dto1.setDeals(42);
        dto1.setConversionRate(BigDecimal.TEN);
        dto1.setRegion("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setPeriod("test");
        dto2.setDate(LocalDate.of(2025,1,1));
        dto2.setRevenue(BigDecimal.TEN);
        dto2.setDeals(42);
        dto2.setConversionRate(BigDecimal.TEN);
        dto2.setRegion("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.AddTrendDataCommand dto = new DashboardCommand.AddTrendDataCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setPeriod("test");
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setRevenue(BigDecimal.TEN);
        dto.setDeals(42);
        dto.setConversionRate(BigDecimal.TEN);
        dto.setRegion("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.AddTrendDataCommand dto = new DashboardCommand.AddTrendDataCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setPeriod("test");
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setRevenue(BigDecimal.TEN);
        dto.setDeals(42);
        dto.setConversionRate(BigDecimal.TEN);
        dto.setRegion("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}