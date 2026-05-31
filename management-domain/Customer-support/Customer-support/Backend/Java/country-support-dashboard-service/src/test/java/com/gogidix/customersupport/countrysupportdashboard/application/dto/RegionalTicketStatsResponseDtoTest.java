package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.RegionalTicketStatsResponseDto;
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
class RegionalTicketStatsResponseDtoTest {

        @Test
    void testBuilder() {
        RegionalTicketStatsResponseDto dto = RegionalTicketStatsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionName("test-regionName")
            .countryCodes(Collections.emptyList())
            .statDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .pendingTickets(42)
            .ticketsByCountry(Collections.emptyMap())
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .slaComplianceRate(null)
            .changePercentage(null)
            .activeAgents(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-regionName", dto.getRegionName());
        assertEquals(LocalDate.of(2025,1,15), dto.getStatDate());
        assertEquals(42, dto.getTotalTickets());
        assertEquals(42, dto.getNewTickets());
        assertEquals(42, dto.getClosedTickets());
        assertEquals(42, dto.getPendingTickets());
        assertEquals(42, dto.getActiveAgents());
    }

    @Test
    void testSettersAndGetters() {
        RegionalTicketStatsResponseDto dto = new RegionalTicketStatsResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setRegionName("val-regionName");
        dto.setStatDate(LocalDate.of(2025,6,1));
        dto.setTotalTickets(99);
        dto.setNewTickets(99);
        dto.setClosedTickets(99);
        dto.setPendingTickets(99);
        dto.setActiveAgents(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-regionName", dto.getRegionName());
        assertEquals(LocalDate.of(2025,6,1), dto.getStatDate());
        assertEquals(99, dto.getTotalTickets());
        assertEquals(99, dto.getNewTickets());
        assertEquals(99, dto.getClosedTickets());
        assertEquals(99, dto.getPendingTickets());
        assertEquals(99, dto.getActiveAgents());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalTicketStatsResponseDto dto1 = RegionalTicketStatsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionName("test-regionName")
            .countryCodes(Collections.emptyList())
            .statDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .pendingTickets(42)
            .ticketsByCountry(Collections.emptyMap())
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .slaComplianceRate(null)
            .changePercentage(null)
            .activeAgents(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RegionalTicketStatsResponseDto dto2 = RegionalTicketStatsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionName("test-regionName")
            .countryCodes(Collections.emptyList())
            .statDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .pendingTickets(42)
            .ticketsByCountry(Collections.emptyMap())
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .slaComplianceRate(null)
            .changePercentage(null)
            .activeAgents(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalTicketStatsResponseDto dto = RegionalTicketStatsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .regionName("test-regionName")
            .countryCodes(Collections.emptyList())
            .statDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .pendingTickets(42)
            .ticketsByCountry(Collections.emptyMap())
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .slaComplianceRate(null)
            .changePercentage(null)
            .activeAgents(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}