package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.SupportMetricsDto;
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
class SupportMetricsDtoTest {

        @Test
    void testBuilder() {
        SupportMetricsDto dto = SupportMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .totalTickets(42L)
            .openTickets(42L)
            .inProgressTickets(42L)
            .resolvedTickets(42L)
            .closedTickets(42L)
            .escalatedTickets(42L)
            .avgFirstResponseTime(null)
            .avgResolutionTime(null)
            .avgFirstResponseTimeMinutes(42L)
            .avgResolutionTimeMinutes(42L)
            .ticketsWithinSla(42L)
            .ticketsBreachedSla(42L)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .npsScore(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .ticketsPerAgent(null)
            .avgAgentUtilization(null)
            .channelBreakdown(Collections.emptyMap())
            .regionalBreakdown(Collections.emptyMap())
            .priorityBreakdown(Collections.emptyMap())
            .avgQualityScore(null)
            .totalQualityReviews(42L)
            .metricStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metricEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .isRealTime(true)
            .isStale(true)
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(42L, dto.getTotalTickets());
        assertEquals(42L, dto.getOpenTickets());
        assertEquals(42L, dto.getInProgressTickets());
        assertEquals(42L, dto.getResolvedTickets());
        assertEquals(42L, dto.getClosedTickets());
        assertEquals(42L, dto.getEscalatedTickets());
        assertEquals(42L, dto.getAvgFirstResponseTimeMinutes());
        assertEquals(42L, dto.getAvgResolutionTimeMinutes());
        assertEquals(42L, dto.getTicketsWithinSla());
        assertEquals(42L, dto.getTicketsBreachedSla());
        assertEquals(42L, dto.getTotalCsatResponses());
        assertEquals(42L, dto.getTotalAgents());
        assertEquals(42L, dto.getActiveAgents());
        assertEquals(42L, dto.getTotalQualityReviews());
        assertEquals("test-aggregationType", dto.getAggregationType());
        assertEquals("test-dataSource", dto.getDataSource());
        assertTrue(dto.getIsRealTime());
        assertTrue(dto.getIsStale());
    }

    @Test
    void testSettersAndGetters() {
        SupportMetricsDto dto = new SupportMetricsDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setAggregationType("val-aggregationType");
        dto.setDataSource("val-dataSource");
        dto.setIsRealTime(true);
        dto.setIsStale(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-aggregationType", dto.getAggregationType());
        assertEquals("val-dataSource", dto.getDataSource());
        assertTrue(dto.getIsRealTime());
        assertTrue(dto.getIsStale());
    }

    @Test
    void testEqualsAndHashCode() {
        SupportMetricsDto dto1 = SupportMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .totalTickets(42L)
            .openTickets(42L)
            .inProgressTickets(42L)
            .resolvedTickets(42L)
            .closedTickets(42L)
            .escalatedTickets(42L)
            .avgFirstResponseTime(null)
            .avgResolutionTime(null)
            .avgFirstResponseTimeMinutes(42L)
            .avgResolutionTimeMinutes(42L)
            .ticketsWithinSla(42L)
            .ticketsBreachedSla(42L)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .npsScore(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .ticketsPerAgent(null)
            .avgAgentUtilization(null)
            .channelBreakdown(Collections.emptyMap())
            .regionalBreakdown(Collections.emptyMap())
            .priorityBreakdown(Collections.emptyMap())
            .avgQualityScore(null)
            .totalQualityReviews(42L)
            .metricStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metricEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .isRealTime(true)
            .isStale(true)
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        SupportMetricsDto dto2 = SupportMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .totalTickets(42L)
            .openTickets(42L)
            .inProgressTickets(42L)
            .resolvedTickets(42L)
            .closedTickets(42L)
            .escalatedTickets(42L)
            .avgFirstResponseTime(null)
            .avgResolutionTime(null)
            .avgFirstResponseTimeMinutes(42L)
            .avgResolutionTimeMinutes(42L)
            .ticketsWithinSla(42L)
            .ticketsBreachedSla(42L)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .npsScore(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .ticketsPerAgent(null)
            .avgAgentUtilization(null)
            .channelBreakdown(Collections.emptyMap())
            .regionalBreakdown(Collections.emptyMap())
            .priorityBreakdown(Collections.emptyMap())
            .avgQualityScore(null)
            .totalQualityReviews(42L)
            .metricStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metricEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .isRealTime(true)
            .isStale(true)
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SupportMetricsDto dto = SupportMetricsDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .totalTickets(42L)
            .openTickets(42L)
            .inProgressTickets(42L)
            .resolvedTickets(42L)
            .closedTickets(42L)
            .escalatedTickets(42L)
            .avgFirstResponseTime(null)
            .avgResolutionTime(null)
            .avgFirstResponseTimeMinutes(42L)
            .avgResolutionTimeMinutes(42L)
            .ticketsWithinSla(42L)
            .ticketsBreachedSla(42L)
            .slaCompliancePercentage(null)
            .avgCsatScore(null)
            .totalCsatResponses(42L)
            .npsScore(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .ticketsPerAgent(null)
            .avgAgentUtilization(null)
            .channelBreakdown(Collections.emptyMap())
            .regionalBreakdown(Collections.emptyMap())
            .priorityBreakdown(Collections.emptyMap())
            .avgQualityScore(null)
            .totalQualityReviews(42L)
            .metricStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .metricEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .isRealTime(true)
            .isStale(true)
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}