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
class DashboardCommand_UpdateGlobalMetricsCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.UpdateGlobalMetricsCommand dto = new DashboardCommand.UpdateGlobalMetricsCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setTargetRevenue(BigDecimal.ONE);
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setAverageDealSize(BigDecimal.ONE);
        dto.setWeightedPipeline(BigDecimal.ONE);
        dto.setOpportunitiesInPipeline(99);
        dto.setYearOverYearGrowth(BigDecimal.ONE);
        dto.setMonthOverMonthGrowth(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals(BigDecimal.ONE, dto.getTargetRevenue());
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(BigDecimal.ONE, dto.getAverageDealSize());
        assertEquals(BigDecimal.ONE, dto.getWeightedPipeline());
        assertEquals(99, dto.getOpportunitiesInPipeline());
        assertEquals(BigDecimal.ONE, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.ONE, dto.getMonthOverMonthGrowth());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.UpdateGlobalMetricsCommand dto1 = new DashboardCommand.UpdateGlobalMetricsCommand();
        DashboardCommand.UpdateGlobalMetricsCommand dto2 = new DashboardCommand.UpdateGlobalMetricsCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setTotalRevenue(BigDecimal.TEN);
        dto1.setTargetRevenue(BigDecimal.TEN);
        dto1.setTotalDeals(42);
        dto1.setWonDeals(42);
        dto1.setAverageDealSize(BigDecimal.TEN);
        dto1.setWeightedPipeline(BigDecimal.TEN);
        dto1.setOpportunitiesInPipeline(42);
        dto1.setYearOverYearGrowth(BigDecimal.TEN);
        dto1.setMonthOverMonthGrowth(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setTotalRevenue(BigDecimal.TEN);
        dto2.setTargetRevenue(BigDecimal.TEN);
        dto2.setTotalDeals(42);
        dto2.setWonDeals(42);
        dto2.setAverageDealSize(BigDecimal.TEN);
        dto2.setWeightedPipeline(BigDecimal.TEN);
        dto2.setOpportunitiesInPipeline(42);
        dto2.setYearOverYearGrowth(BigDecimal.TEN);
        dto2.setMonthOverMonthGrowth(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.UpdateGlobalMetricsCommand dto = new DashboardCommand.UpdateGlobalMetricsCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setTargetRevenue(BigDecimal.TEN);
        dto.setTotalDeals(42);
        dto.setWonDeals(42);
        dto.setAverageDealSize(BigDecimal.TEN);
        dto.setWeightedPipeline(BigDecimal.TEN);
        dto.setOpportunitiesInPipeline(42);
        dto.setYearOverYearGrowth(BigDecimal.TEN);
        dto.setMonthOverMonthGrowth(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.UpdateGlobalMetricsCommand dto = new DashboardCommand.UpdateGlobalMetricsCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setTargetRevenue(BigDecimal.TEN);
        dto.setTotalDeals(42);
        dto.setWonDeals(42);
        dto.setAverageDealSize(BigDecimal.TEN);
        dto.setWeightedPipeline(BigDecimal.TEN);
        dto.setOpportunitiesInPipeline(42);
        dto.setYearOverYearGrowth(BigDecimal.TEN);
        dto.setMonthOverMonthGrowth(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}