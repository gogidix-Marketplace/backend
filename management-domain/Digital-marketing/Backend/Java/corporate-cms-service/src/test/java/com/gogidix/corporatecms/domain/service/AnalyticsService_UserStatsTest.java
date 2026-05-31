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
class AnalyticsService_UserStatsTest {

        @Test
    void testBuilder() {
        AnalyticsService.UserStats dto = AnalyticsService.UserStats.builder()
                        .total(42L)
            .active(42L)
            .byRole(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotal());
        assertEquals(42L, dto.getActive());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsService.UserStats dto1 = AnalyticsService.UserStats.builder()
                        .total(42L)
            .active(42L)
            .byRole(Collections.emptyMap())
            .build();
        AnalyticsService.UserStats dto2 = AnalyticsService.UserStats.builder()
                        .total(42L)
            .active(42L)
            .byRole(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsService.UserStats dto = AnalyticsService.UserStats.builder()
                        .total(42L)
            .active(42L)
            .byRole(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}