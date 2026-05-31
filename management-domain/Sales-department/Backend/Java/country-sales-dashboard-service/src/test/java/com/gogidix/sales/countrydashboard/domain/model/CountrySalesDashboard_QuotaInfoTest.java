package com.gogidix.sales.countrydashboard.domain.model;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
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
class CountrySalesDashboard_QuotaInfoTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.QuotaInfo dto = CountrySalesDashboard.QuotaInfo.builder()
                        .annualQuota(null)
            .quarterlyQuota(null)
            .monthlyQuota(null)
            .yearToDateRevenue(null)
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(null)
            .projectedRevenue(null)
            .forecastAccuracy(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getYearToDateAchievement());
        assertEquals(BigDecimal.TEN, dto.getRemainingPercentage());
        assertEquals(BigDecimal.TEN, dto.getForecastAccuracy());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.QuotaInfo dto = new CountrySalesDashboard.QuotaInfo();
        dto.setYearToDateAchievement(BigDecimal.ONE);
        dto.setRemainingPercentage(BigDecimal.ONE);
        dto.setForecastAccuracy(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getYearToDateAchievement());
        assertEquals(BigDecimal.ONE, dto.getRemainingPercentage());
        assertEquals(BigDecimal.ONE, dto.getForecastAccuracy());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.QuotaInfo dto1 = CountrySalesDashboard.QuotaInfo.builder()
                        .annualQuota(null)
            .quarterlyQuota(null)
            .monthlyQuota(null)
            .yearToDateRevenue(null)
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(null)
            .projectedRevenue(null)
            .forecastAccuracy(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CountrySalesDashboard.QuotaInfo dto2 = CountrySalesDashboard.QuotaInfo.builder()
                        .annualQuota(null)
            .quarterlyQuota(null)
            .monthlyQuota(null)
            .yearToDateRevenue(null)
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(null)
            .projectedRevenue(null)
            .forecastAccuracy(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySalesDashboard.QuotaInfo dto = CountrySalesDashboard.QuotaInfo.builder()
                        .annualQuota(null)
            .quarterlyQuota(null)
            .monthlyQuota(null)
            .yearToDateRevenue(null)
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(null)
            .projectedRevenue(null)
            .forecastAccuracy(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}