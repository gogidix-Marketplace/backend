package com.gogidix.customersupport.supportanalytics.application.dto;

import com.gogidix.customersupport.supportanalytics.application.dto.TicketTrendResponseDto;
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
class TicketTrendResponseDtoTest {

        @Test
    void testBuilder() {
        TicketTrendResponseDto dto = TicketTrendResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .trendDate(LocalDate.of(2025,1,15))
            .periodType(TicketTrendResponseDto.PeriodTypeDto.HOURLY)
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .reopenedTickets(42)
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .ticketsByCategory(Collections.emptyMap())
            .changePercentage(null)
            .changeFromPreviousPeriod(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,1,15), dto.getTrendDate());
        assertEquals(TicketTrendResponseDto.PeriodTypeDto.HOURLY, dto.getPeriodType());
        assertEquals(42, dto.getTotalTickets());
        assertEquals(42, dto.getNewTickets());
        assertEquals(42, dto.getClosedTickets());
        assertEquals(42, dto.getReopenedTickets());
        assertEquals(42, dto.getChangeFromPreviousPeriod());
    }

    @Test
    void testSettersAndGetters() {
        TicketTrendResponseDto dto = new TicketTrendResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setTrendDate(LocalDate.of(2025,6,1));
        dto.setPeriodType(TicketTrendResponseDto.PeriodTypeDto.HOURLY);
        dto.setTotalTickets(99);
        dto.setNewTickets(99);
        dto.setClosedTickets(99);
        dto.setReopenedTickets(99);
        dto.setChangeFromPreviousPeriod(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTrendDate());
        assertEquals(TicketTrendResponseDto.PeriodTypeDto.HOURLY, dto.getPeriodType());
        assertEquals(99, dto.getTotalTickets());
        assertEquals(99, dto.getNewTickets());
        assertEquals(99, dto.getClosedTickets());
        assertEquals(99, dto.getReopenedTickets());
        assertEquals(99, dto.getChangeFromPreviousPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        TicketTrendResponseDto dto1 = TicketTrendResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .trendDate(LocalDate.of(2025,1,15))
            .periodType(TicketTrendResponseDto.PeriodTypeDto.HOURLY)
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .reopenedTickets(42)
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .ticketsByCategory(Collections.emptyMap())
            .changePercentage(null)
            .changeFromPreviousPeriod(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TicketTrendResponseDto dto2 = TicketTrendResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .trendDate(LocalDate.of(2025,1,15))
            .periodType(TicketTrendResponseDto.PeriodTypeDto.HOURLY)
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .reopenedTickets(42)
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .ticketsByCategory(Collections.emptyMap())
            .changePercentage(null)
            .changeFromPreviousPeriod(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TicketTrendResponseDto dto = TicketTrendResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .trendDate(LocalDate.of(2025,1,15))
            .periodType(TicketTrendResponseDto.PeriodTypeDto.HOURLY)
            .totalTickets(42)
            .newTickets(42)
            .closedTickets(42)
            .reopenedTickets(42)
            .ticketsByStatus(Collections.emptyMap())
            .ticketsByPriority(Collections.emptyMap())
            .ticketsByChannel(Collections.emptyMap())
            .ticketsByCategory(Collections.emptyMap())
            .changePercentage(null)
            .changeFromPreviousPeriod(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}