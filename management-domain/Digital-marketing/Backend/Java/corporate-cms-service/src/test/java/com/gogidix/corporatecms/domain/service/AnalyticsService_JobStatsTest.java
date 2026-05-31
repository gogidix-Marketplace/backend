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
class AnalyticsService_JobStatsTest {

        @Test
    void testBuilder() {
        AnalyticsService.JobStats dto = AnalyticsService.JobStats.builder()
                        .total(42L)
            .open(42L)
            .closed(42L)
            .draft(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotal());
        assertEquals(42L, dto.getOpen());
        assertEquals(42L, dto.getClosed());
        assertEquals(42L, dto.getDraft());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsService.JobStats dto1 = AnalyticsService.JobStats.builder()
                        .total(42L)
            .open(42L)
            .closed(42L)
            .draft(42L)
            .build();
        AnalyticsService.JobStats dto2 = AnalyticsService.JobStats.builder()
                        .total(42L)
            .open(42L)
            .closed(42L)
            .draft(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsService.JobStats dto = AnalyticsService.JobStats.builder()
                        .total(42L)
            .open(42L)
            .closed(42L)
            .draft(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}