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
class AnalyticsService_ContentStatsTest {

        @Test
    void testBuilder() {
        AnalyticsService.ContentStats dto = AnalyticsService.ContentStats.builder()
                        .total(42L)
            .published(42L)
            .draft(42L)
            .pendingReview(42L)
            .pendingApproval(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotal());
        assertEquals(42L, dto.getPublished());
        assertEquals(42L, dto.getDraft());
        assertEquals(42L, dto.getPendingReview());
        assertEquals(42L, dto.getPendingApproval());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsService.ContentStats dto1 = AnalyticsService.ContentStats.builder()
                        .total(42L)
            .published(42L)
            .draft(42L)
            .pendingReview(42L)
            .pendingApproval(42L)
            .build();
        AnalyticsService.ContentStats dto2 = AnalyticsService.ContentStats.builder()
                        .total(42L)
            .published(42L)
            .draft(42L)
            .pendingReview(42L)
            .pendingApproval(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsService.ContentStats dto = AnalyticsService.ContentStats.builder()
                        .total(42L)
            .published(42L)
            .draft(42L)
            .pendingReview(42L)
            .pendingApproval(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}