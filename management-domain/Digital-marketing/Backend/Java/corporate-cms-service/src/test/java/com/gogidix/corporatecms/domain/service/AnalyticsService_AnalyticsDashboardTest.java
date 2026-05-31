package com.gogidix.corporatecms.domain.service;

import com.gogidix.corporatecms.domain.service.AnalyticsService;
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
class AnalyticsService_AnalyticsDashboardTest {

        @Test
    void testBuilder() {
        AnalyticsService.AnalyticsDashboard dto = AnalyticsService.AnalyticsDashboard.builder()
                        .contentStats(null)
            .mediaStats(null)
            .userStats(null)
            .productStats(null)
            .jobStats(null)
            .leadStats(null)
            .workflowStats(null)
            .recentActivity(Collections.emptyList())
            .build();
        assertNotNull(dto);

    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsService.AnalyticsDashboard dto1 = AnalyticsService.AnalyticsDashboard.builder()
                        .contentStats(null)
            .mediaStats(null)
            .userStats(null)
            .productStats(null)
            .jobStats(null)
            .leadStats(null)
            .workflowStats(null)
            .recentActivity(Collections.emptyList())
            .build();
        AnalyticsService.AnalyticsDashboard dto2 = AnalyticsService.AnalyticsDashboard.builder()
                        .contentStats(null)
            .mediaStats(null)
            .userStats(null)
            .productStats(null)
            .jobStats(null)
            .leadStats(null)
            .workflowStats(null)
            .recentActivity(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsService.AnalyticsDashboard dto = AnalyticsService.AnalyticsDashboard.builder()
                        .contentStats(null)
            .mediaStats(null)
            .userStats(null)
            .productStats(null)
            .jobStats(null)
            .leadStats(null)
            .workflowStats(null)
            .recentActivity(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}