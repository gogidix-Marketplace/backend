package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto dto = DashboardResponseDto.builder()
                        .period("test-period")
            .lastRefreshed(Instant.parse("2025-01-15T10:00:00Z"))
            .headcountSummary(null)
            .complianceSummary(null)
            .diversitySummary(null)
            .retentionSummary(null)
            .regionalBreakdown(Collections.emptyMap())
            .topMetrics(null)
            .alerts(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-period", dto.getPeriod());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto dto = new DashboardResponseDto();
        dto.setPeriod("val-period");
        assertEquals("val-period", dto.getPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto dto1 = DashboardResponseDto.builder()
                        .period("test-period")
            .lastRefreshed(Instant.parse("2025-01-15T10:00:00Z"))
            .headcountSummary(null)
            .complianceSummary(null)
            .diversitySummary(null)
            .retentionSummary(null)
            .regionalBreakdown(Collections.emptyMap())
            .topMetrics(null)
            .alerts(null)
            .build();
        DashboardResponseDto dto2 = DashboardResponseDto.builder()
                        .period("test-period")
            .lastRefreshed(Instant.parse("2025-01-15T10:00:00Z"))
            .headcountSummary(null)
            .complianceSummary(null)
            .diversitySummary(null)
            .retentionSummary(null)
            .regionalBreakdown(Collections.emptyMap())
            .topMetrics(null)
            .alerts(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto dto = DashboardResponseDto.builder()
                        .period("test-period")
            .lastRefreshed(Instant.parse("2025-01-15T10:00:00Z"))
            .headcountSummary(null)
            .complianceSummary(null)
            .diversitySummary(null)
            .retentionSummary(null)
            .regionalBreakdown(Collections.emptyMap())
            .topMetrics(null)
            .alerts(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}