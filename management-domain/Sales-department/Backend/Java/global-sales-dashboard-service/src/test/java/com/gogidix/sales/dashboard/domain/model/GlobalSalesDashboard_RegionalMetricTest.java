package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
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
class GlobalSalesDashboard_RegionalMetricTest {

        @Test
    void testBuilder() {
        GlobalSalesDashboard.RegionalMetric dto = GlobalSalesDashboard.RegionalMetric.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .growthRate(BigDecimal.TEN)
            .rank(42)
            .currency("test-currency")
            .build();
        assertNotNull(dto);
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-regionName", dto.getRegionName());
        assertEquals(BigDecimal.TEN, dto.getAchievementPercentage());
        assertEquals(42, dto.getDeals());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(42, dto.getRank());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        GlobalSalesDashboard.RegionalMetric dto = new GlobalSalesDashboard.RegionalMetric();
        dto.setRegionCode("val-regionCode");
        dto.setRegionName("val-regionName");
        dto.setAchievementPercentage(BigDecimal.ONE);
        dto.setDeals(99);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setRank(99);
        dto.setCurrency("val-currency");
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-regionName", dto.getRegionName());
        assertEquals(BigDecimal.ONE, dto.getAchievementPercentage());
        assertEquals(99, dto.getDeals());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(99, dto.getRank());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalSalesDashboard.RegionalMetric dto1 = GlobalSalesDashboard.RegionalMetric.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .growthRate(BigDecimal.TEN)
            .rank(42)
            .currency("test-currency")
            .build();
        GlobalSalesDashboard.RegionalMetric dto2 = GlobalSalesDashboard.RegionalMetric.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .growthRate(BigDecimal.TEN)
            .rank(42)
            .currency("test-currency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalSalesDashboard.RegionalMetric dto = GlobalSalesDashboard.RegionalMetric.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .growthRate(BigDecimal.TEN)
            .rank(42)
            .currency("test-currency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}