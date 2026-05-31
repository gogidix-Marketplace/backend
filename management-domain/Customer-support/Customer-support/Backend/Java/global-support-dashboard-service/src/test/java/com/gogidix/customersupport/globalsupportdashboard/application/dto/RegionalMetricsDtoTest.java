package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.RegionalMetricsDto;
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
class RegionalMetricsDtoTest {

        @Test
    void testBuilder() {
        RegionalMetricsDto dto = RegionalMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .totalTickets(42L)
            .openTickets(42L)
            .resolvedTickets(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .totalAgents(42L)
            .activeAgents(42L)
            .topCountryCode("test-topCountryCode")
            .topCountryTicketCount(42L)
            .ticketsLast24Hours(42L)
            .ticketsLast7Days(42L)
            .ticketsLast30Days(42L)
            .trendPercentage(null)
            .metricDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-regionName", dto.getRegionName());
        assertEquals(42L, dto.getTotalTickets());
        assertEquals(42L, dto.getOpenTickets());
        assertEquals(42L, dto.getResolvedTickets());
        assertEquals(42L, dto.getTotalCsatResponses());
        assertEquals(42L, dto.getTotalAgents());
        assertEquals(42L, dto.getActiveAgents());
        assertEquals("test-topCountryCode", dto.getTopCountryCode());
        assertEquals(42L, dto.getTopCountryTicketCount());
        assertEquals(42L, dto.getTicketsLast24Hours());
        assertEquals(42L, dto.getTicketsLast7Days());
        assertEquals(42L, dto.getTicketsLast30Days());
        assertEquals("test-aggregationType", dto.getAggregationType());
    }

    @Test
    void testSettersAndGetters() {
        RegionalMetricsDto dto = new RegionalMetricsDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setRegionCode("val-regionCode");
        dto.setRegionName("val-regionName");
        dto.setTopCountryCode("val-topCountryCode");
        dto.setAggregationType("val-aggregationType");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-regionName", dto.getRegionName());
        assertEquals("val-topCountryCode", dto.getTopCountryCode());
        assertEquals("val-aggregationType", dto.getAggregationType());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalMetricsDto dto1 = RegionalMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .totalTickets(42L)
            .openTickets(42L)
            .resolvedTickets(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .totalAgents(42L)
            .activeAgents(42L)
            .topCountryCode("test-topCountryCode")
            .topCountryTicketCount(42L)
            .ticketsLast24Hours(42L)
            .ticketsLast7Days(42L)
            .ticketsLast30Days(42L)
            .trendPercentage(null)
            .metricDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RegionalMetricsDto dto2 = RegionalMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .totalTickets(42L)
            .openTickets(42L)
            .resolvedTickets(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .totalAgents(42L)
            .activeAgents(42L)
            .topCountryCode("test-topCountryCode")
            .topCountryTicketCount(42L)
            .ticketsLast24Hours(42L)
            .ticketsLast7Days(42L)
            .ticketsLast30Days(42L)
            .trendPercentage(null)
            .metricDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalMetricsDto dto = RegionalMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .totalTickets(42L)
            .openTickets(42L)
            .resolvedTickets(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .totalAgents(42L)
            .activeAgents(42L)
            .topCountryCode("test-topCountryCode")
            .topCountryTicketCount(42L)
            .ticketsLast24Hours(42L)
            .ticketsLast7Days(42L)
            .ticketsLast30Days(42L)
            .trendPercentage(null)
            .metricDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}