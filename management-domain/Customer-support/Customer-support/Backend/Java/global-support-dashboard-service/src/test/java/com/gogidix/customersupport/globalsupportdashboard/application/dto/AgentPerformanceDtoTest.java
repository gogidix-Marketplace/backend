package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.AgentPerformanceDto;
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
class AgentPerformanceDtoTest {

        @Test
    void testBuilder() {
        AgentPerformanceDto dto = AgentPerformanceDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .regionCode("test-regionCode")
            .totalTicketsAssigned(42L)
            .ticketsResolved(42L)
            .ticketsInProgress(42L)
            .ticketsReassigned(42L)
            .ticketsEscalated(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .avgHandlingTimeMinutes(42L)
            .avgCsatScore(null)
            .totalCsatReceived(42L)
            .qualityScore(null)
            .totalQaReviews(42L)
            .totalAvailableTimeMinutes(42L)
            .totalTalkTimeMinutes(42L)
            .totalAwayTimeMinutes(42L)
            .totalBreakTimeMinutes(42L)
            .utilizationPercentage(null)
            .status("test-status")
            .agentTier("test-agentTier")
            .statusLastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .ticketsResolvedLast7Days(42L)
            .ticketsResolvedLast30Days(42L)
            .performanceTrend(null)
            .regionalRank(42)
            .globalRank(42)
            .performancePeriodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .performancePeriodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-agentEmail", dto.getAgentEmail());
        assertEquals("test-teamId", dto.getTeamId());
        assertEquals("test-teamName", dto.getTeamName());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals(42L, dto.getTotalTicketsAssigned());
        assertEquals(42L, dto.getTicketsResolved());
        assertEquals(42L, dto.getTicketsInProgress());
        assertEquals(42L, dto.getTicketsReassigned());
        assertEquals(42L, dto.getTicketsEscalated());
        assertEquals(42L, dto.getAvgHandlingTimeMinutes());
        assertEquals(42L, dto.getTotalCsatReceived());
        assertEquals(42L, dto.getTotalQaReviews());
        assertEquals(42L, dto.getTotalAvailableTimeMinutes());
        assertEquals(42L, dto.getTotalTalkTimeMinutes());
        assertEquals(42L, dto.getTotalAwayTimeMinutes());
        assertEquals(42L, dto.getTotalBreakTimeMinutes());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-agentTier", dto.getAgentTier());
        assertEquals(42L, dto.getTicketsResolvedLast7Days());
        assertEquals(42L, dto.getTicketsResolvedLast30Days());
        assertEquals(42, dto.getRegionalRank());
        assertEquals(42, dto.getGlobalRank());
    }

    @Test
    void testSettersAndGetters() {
        AgentPerformanceDto dto = new AgentPerformanceDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setAgentEmail("val-agentEmail");
        dto.setTeamId("val-teamId");
        dto.setTeamName("val-teamName");
        dto.setRegionCode("val-regionCode");
        dto.setStatus("val-status");
        dto.setAgentTier("val-agentTier");
        dto.setRegionalRank(99);
        dto.setGlobalRank(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-agentEmail", dto.getAgentEmail());
        assertEquals("val-teamId", dto.getTeamId());
        assertEquals("val-teamName", dto.getTeamName());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-agentTier", dto.getAgentTier());
        assertEquals(99, dto.getRegionalRank());
        assertEquals(99, dto.getGlobalRank());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentPerformanceDto dto1 = AgentPerformanceDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .regionCode("test-regionCode")
            .totalTicketsAssigned(42L)
            .ticketsResolved(42L)
            .ticketsInProgress(42L)
            .ticketsReassigned(42L)
            .ticketsEscalated(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .avgHandlingTimeMinutes(42L)
            .avgCsatScore(null)
            .totalCsatReceived(42L)
            .qualityScore(null)
            .totalQaReviews(42L)
            .totalAvailableTimeMinutes(42L)
            .totalTalkTimeMinutes(42L)
            .totalAwayTimeMinutes(42L)
            .totalBreakTimeMinutes(42L)
            .utilizationPercentage(null)
            .status("test-status")
            .agentTier("test-agentTier")
            .statusLastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .ticketsResolvedLast7Days(42L)
            .ticketsResolvedLast30Days(42L)
            .performanceTrend(null)
            .regionalRank(42)
            .globalRank(42)
            .performancePeriodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .performancePeriodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AgentPerformanceDto dto2 = AgentPerformanceDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .regionCode("test-regionCode")
            .totalTicketsAssigned(42L)
            .ticketsResolved(42L)
            .ticketsInProgress(42L)
            .ticketsReassigned(42L)
            .ticketsEscalated(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .avgHandlingTimeMinutes(42L)
            .avgCsatScore(null)
            .totalCsatReceived(42L)
            .qualityScore(null)
            .totalQaReviews(42L)
            .totalAvailableTimeMinutes(42L)
            .totalTalkTimeMinutes(42L)
            .totalAwayTimeMinutes(42L)
            .totalBreakTimeMinutes(42L)
            .utilizationPercentage(null)
            .status("test-status")
            .agentTier("test-agentTier")
            .statusLastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .ticketsResolvedLast7Days(42L)
            .ticketsResolvedLast30Days(42L)
            .performanceTrend(null)
            .regionalRank(42)
            .globalRank(42)
            .performancePeriodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .performancePeriodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentPerformanceDto dto = AgentPerformanceDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .regionCode("test-regionCode")
            .totalTicketsAssigned(42L)
            .ticketsResolved(42L)
            .ticketsInProgress(42L)
            .ticketsReassigned(42L)
            .ticketsEscalated(42L)
            .avgFirstResponseTimeMinutes(null)
            .avgResolutionTimeMinutes(null)
            .avgHandlingTimeMinutes(42L)
            .avgCsatScore(null)
            .totalCsatReceived(42L)
            .qualityScore(null)
            .totalQaReviews(42L)
            .totalAvailableTimeMinutes(42L)
            .totalTalkTimeMinutes(42L)
            .totalAwayTimeMinutes(42L)
            .totalBreakTimeMinutes(42L)
            .utilizationPercentage(null)
            .status("test-status")
            .agentTier("test-agentTier")
            .statusLastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .ticketsResolvedLast7Days(42L)
            .ticketsResolvedLast30Days(42L)
            .performanceTrend(null)
            .regionalRank(42)
            .globalRank(42)
            .performancePeriodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .performancePeriodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}