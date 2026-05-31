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
class AnalyticsService_WorkflowStatsTest {

        @Test
    void testBuilder() {
        AnalyticsService.WorkflowStats dto = AnalyticsService.WorkflowStats.builder()
                        .pending(42L)
            .approved(42L)
            .rejected(42L)
            .overdue(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getPending());
        assertEquals(42L, dto.getApproved());
        assertEquals(42L, dto.getRejected());
        assertEquals(42L, dto.getOverdue());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsService.WorkflowStats dto1 = AnalyticsService.WorkflowStats.builder()
                        .pending(42L)
            .approved(42L)
            .rejected(42L)
            .overdue(42L)
            .build();
        AnalyticsService.WorkflowStats dto2 = AnalyticsService.WorkflowStats.builder()
                        .pending(42L)
            .approved(42L)
            .rejected(42L)
            .overdue(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsService.WorkflowStats dto = AnalyticsService.WorkflowStats.builder()
                        .pending(42L)
            .approved(42L)
            .rejected(42L)
            .overdue(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}