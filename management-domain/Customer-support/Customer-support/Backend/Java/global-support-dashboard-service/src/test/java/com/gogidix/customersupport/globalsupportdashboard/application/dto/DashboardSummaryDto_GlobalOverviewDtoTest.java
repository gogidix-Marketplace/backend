package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.DashboardSummaryDto;
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
class DashboardSummaryDto_GlobalOverviewDtoTest {

        @Test
    void testBuilder() {
        DashboardSummaryDto.GlobalOverviewDto dto = DashboardSummaryDto.GlobalOverviewDto.builder()
                        .totalTickets(42L)
            .openTickets(42L)
            .avgCsatScore(null)
            .slaCompliancePercentage(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .avgResolutionTimeMinutes(null)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalTickets());
        assertEquals(42L, dto.getOpenTickets());
        assertEquals(42L, dto.getTotalAgents());
        assertEquals(42L, dto.getActiveAgents());
    }

    @Test
    void testSettersAndGetters() {
        DashboardSummaryDto.GlobalOverviewDto dto = new DashboardSummaryDto.GlobalOverviewDto();


    }

    @Test
    void testEqualsAndHashCode() {
        DashboardSummaryDto.GlobalOverviewDto dto1 = DashboardSummaryDto.GlobalOverviewDto.builder()
                        .totalTickets(42L)
            .openTickets(42L)
            .avgCsatScore(null)
            .slaCompliancePercentage(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .avgResolutionTimeMinutes(null)
            .build();
        DashboardSummaryDto.GlobalOverviewDto dto2 = DashboardSummaryDto.GlobalOverviewDto.builder()
                        .totalTickets(42L)
            .openTickets(42L)
            .avgCsatScore(null)
            .slaCompliancePercentage(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .avgResolutionTimeMinutes(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardSummaryDto.GlobalOverviewDto dto = DashboardSummaryDto.GlobalOverviewDto.builder()
                        .totalTickets(42L)
            .openTickets(42L)
            .avgCsatScore(null)
            .slaCompliancePercentage(null)
            .totalAgents(42L)
            .activeAgents(42L)
            .avgResolutionTimeMinutes(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}