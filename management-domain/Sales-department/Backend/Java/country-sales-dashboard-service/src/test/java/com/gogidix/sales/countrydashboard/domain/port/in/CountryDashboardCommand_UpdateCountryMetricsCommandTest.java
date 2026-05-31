package com.gogidix.sales.countrydashboard.domain.port.in;

import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
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
class CountryDashboardCommand_UpdateCountryMetricsCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.UpdateCountryMetricsCommand dto = new CountryDashboardCommand.UpdateCountryMetricsCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setTargetRevenue(BigDecimal.ONE);
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setLostDeals(99);
        dto.setAverageDealSize(BigDecimal.ONE);
        dto.setWeightedPipeline(BigDecimal.ONE);
        dto.setOpportunitiesInPipeline(99);
        dto.setNewCustomers(99);
        dto.setChurnedCustomers(99);
        dto.setRetentionRate(BigDecimal.ONE);
        dto.setNpsScore(BigDecimal.ONE);
        dto.setActiveSalesReps(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals(BigDecimal.ONE, dto.getTargetRevenue());
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(99, dto.getLostDeals());
        assertEquals(BigDecimal.ONE, dto.getAverageDealSize());
        assertEquals(BigDecimal.ONE, dto.getWeightedPipeline());
        assertEquals(99, dto.getOpportunitiesInPipeline());
        assertEquals(99, dto.getNewCustomers());
        assertEquals(99, dto.getChurnedCustomers());
        assertEquals(BigDecimal.ONE, dto.getRetentionRate());
        assertEquals(BigDecimal.ONE, dto.getNpsScore());
        assertEquals(99, dto.getActiveSalesReps());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.UpdateCountryMetricsCommand dto1 = new CountryDashboardCommand.UpdateCountryMetricsCommand();
        CountryDashboardCommand.UpdateCountryMetricsCommand dto2 = new CountryDashboardCommand.UpdateCountryMetricsCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setTotalRevenue(BigDecimal.TEN);
        dto1.setTargetRevenue(BigDecimal.TEN);
        dto1.setTotalDeals(42);
        dto1.setWonDeals(42);
        dto1.setLostDeals(42);
        dto1.setAverageDealSize(BigDecimal.TEN);
        dto1.setWeightedPipeline(BigDecimal.TEN);
        dto1.setOpportunitiesInPipeline(42);
        dto1.setNewCustomers(42);
        dto1.setChurnedCustomers(42);
        dto1.setRetentionRate(BigDecimal.TEN);
        dto1.setNpsScore(BigDecimal.TEN);
        dto1.setActiveSalesReps(42);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setTotalRevenue(BigDecimal.TEN);
        dto2.setTargetRevenue(BigDecimal.TEN);
        dto2.setTotalDeals(42);
        dto2.setWonDeals(42);
        dto2.setLostDeals(42);
        dto2.setAverageDealSize(BigDecimal.TEN);
        dto2.setWeightedPipeline(BigDecimal.TEN);
        dto2.setOpportunitiesInPipeline(42);
        dto2.setNewCustomers(42);
        dto2.setChurnedCustomers(42);
        dto2.setRetentionRate(BigDecimal.TEN);
        dto2.setNpsScore(BigDecimal.TEN);
        dto2.setActiveSalesReps(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.UpdateCountryMetricsCommand dto = new CountryDashboardCommand.UpdateCountryMetricsCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setTargetRevenue(BigDecimal.TEN);
        dto.setTotalDeals(42);
        dto.setWonDeals(42);
        dto.setLostDeals(42);
        dto.setAverageDealSize(BigDecimal.TEN);
        dto.setWeightedPipeline(BigDecimal.TEN);
        dto.setOpportunitiesInPipeline(42);
        dto.setNewCustomers(42);
        dto.setChurnedCustomers(42);
        dto.setRetentionRate(BigDecimal.TEN);
        dto.setNpsScore(BigDecimal.TEN);
        dto.setActiveSalesReps(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.UpdateCountryMetricsCommand dto = new CountryDashboardCommand.UpdateCountryMetricsCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setTargetRevenue(BigDecimal.TEN);
        dto.setTotalDeals(42);
        dto.setWonDeals(42);
        dto.setLostDeals(42);
        dto.setAverageDealSize(BigDecimal.TEN);
        dto.setWeightedPipeline(BigDecimal.TEN);
        dto.setOpportunitiesInPipeline(42);
        dto.setNewCustomers(42);
        dto.setChurnedCustomers(42);
        dto.setRetentionRate(BigDecimal.TEN);
        dto.setNpsScore(BigDecimal.TEN);
        dto.setActiveSalesReps(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}