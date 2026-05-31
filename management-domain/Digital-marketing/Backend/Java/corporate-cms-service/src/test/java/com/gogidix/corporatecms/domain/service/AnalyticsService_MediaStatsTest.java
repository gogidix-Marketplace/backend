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
class AnalyticsService_MediaStatsTest {

        @Test
    void testBuilder() {
        AnalyticsService.MediaStats dto = AnalyticsService.MediaStats.builder()
                        .total(42L)
            .images(42L)
            .videos(42L)
            .documents(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotal());
        assertEquals(42L, dto.getImages());
        assertEquals(42L, dto.getVideos());
        assertEquals(42L, dto.getDocuments());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsService.MediaStats dto1 = AnalyticsService.MediaStats.builder()
                        .total(42L)
            .images(42L)
            .videos(42L)
            .documents(42L)
            .build();
        AnalyticsService.MediaStats dto2 = AnalyticsService.MediaStats.builder()
                        .total(42L)
            .images(42L)
            .videos(42L)
            .documents(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsService.MediaStats dto = AnalyticsService.MediaStats.builder()
                        .total(42L)
            .images(42L)
            .videos(42L)
            .documents(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}