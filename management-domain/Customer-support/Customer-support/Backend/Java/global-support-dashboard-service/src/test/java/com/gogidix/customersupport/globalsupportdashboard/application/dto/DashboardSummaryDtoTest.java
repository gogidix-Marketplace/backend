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
class DashboardSummaryDtoTest {

        @Test
    void testBuilder() {
        DashboardSummaryDto dto = DashboardSummaryDto.builder()
                        .tenantId("test-tenantId")
            .globalOverview(null)
            .regionalMetrics(Collections.emptyList())
            .topAgents(Collections.emptyList())
            .agentsNeedingAttention(Collections.emptyList())
            .trends(null)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isStale(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertTrue(dto.getIsStale());
    }

    @Test
    void testSettersAndGetters() {
        DashboardSummaryDto dto = new DashboardSummaryDto();
        dto.setTenantId("val-tenantId");
        dto.setIsStale(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertTrue(dto.getIsStale());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardSummaryDto dto1 = DashboardSummaryDto.builder()
                        .tenantId("test-tenantId")
            .globalOverview(null)
            .regionalMetrics(Collections.emptyList())
            .topAgents(Collections.emptyList())
            .agentsNeedingAttention(Collections.emptyList())
            .trends(null)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isStale(true)
            .build();
        DashboardSummaryDto dto2 = DashboardSummaryDto.builder()
                        .tenantId("test-tenantId")
            .globalOverview(null)
            .regionalMetrics(Collections.emptyList())
            .topAgents(Collections.emptyList())
            .agentsNeedingAttention(Collections.emptyList())
            .trends(null)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isStale(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardSummaryDto dto = DashboardSummaryDto.builder()
                        .tenantId("test-tenantId")
            .globalOverview(null)
            .regionalMetrics(Collections.emptyList())
            .topAgents(Collections.emptyList())
            .agentsNeedingAttention(Collections.emptyList())
            .trends(null)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isStale(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}