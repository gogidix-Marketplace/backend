package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.CountryMetricsDto;
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
class CountryMetricsDto_CustomerMetricsDtoTest {

        @Test
    void testBuilder() {
        CountryMetricsDto.CustomerMetricsDto dto = CountryMetricsDto.CustomerMetricsDto.builder()
                        .averageAge(BigDecimal.TEN)
            .genderDistribution(Collections.emptyMap())
            .ageGroupDistribution(Collections.emptyMap())
            .satisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .repeatPurchaseRate(BigDecimal.TEN)
            .averageSessionDuration(BigDecimal.TEN)
            .averagePagesPerSession(42)
            .bounceRate(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAverageAge());
        assertEquals(BigDecimal.TEN, dto.getSatisfactionScore());
        assertEquals(BigDecimal.TEN, dto.getNetPromoterScore());
        assertEquals(BigDecimal.TEN, dto.getRepeatPurchaseRate());
        assertEquals(BigDecimal.TEN, dto.getAverageSessionDuration());
        assertEquals(42, dto.getAveragePagesPerSession());
        assertEquals(BigDecimal.TEN, dto.getBounceRate());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetricsDto.CustomerMetricsDto dto = new CountryMetricsDto.CustomerMetricsDto();
        dto.setAverageAge(BigDecimal.ONE);
        dto.setSatisfactionScore(BigDecimal.ONE);
        dto.setNetPromoterScore(BigDecimal.ONE);
        dto.setRepeatPurchaseRate(BigDecimal.ONE);
        dto.setAverageSessionDuration(BigDecimal.ONE);
        dto.setAveragePagesPerSession(99);
        dto.setBounceRate(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getAverageAge());
        assertEquals(BigDecimal.ONE, dto.getSatisfactionScore());
        assertEquals(BigDecimal.ONE, dto.getNetPromoterScore());
        assertEquals(BigDecimal.ONE, dto.getRepeatPurchaseRate());
        assertEquals(BigDecimal.ONE, dto.getAverageSessionDuration());
        assertEquals(99, dto.getAveragePagesPerSession());
        assertEquals(BigDecimal.ONE, dto.getBounceRate());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetricsDto.CustomerMetricsDto dto1 = CountryMetricsDto.CustomerMetricsDto.builder()
                        .averageAge(BigDecimal.TEN)
            .genderDistribution(Collections.emptyMap())
            .ageGroupDistribution(Collections.emptyMap())
            .satisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .repeatPurchaseRate(BigDecimal.TEN)
            .averageSessionDuration(BigDecimal.TEN)
            .averagePagesPerSession(42)
            .bounceRate(BigDecimal.TEN)
            .build();
        CountryMetricsDto.CustomerMetricsDto dto2 = CountryMetricsDto.CustomerMetricsDto.builder()
                        .averageAge(BigDecimal.TEN)
            .genderDistribution(Collections.emptyMap())
            .ageGroupDistribution(Collections.emptyMap())
            .satisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .repeatPurchaseRate(BigDecimal.TEN)
            .averageSessionDuration(BigDecimal.TEN)
            .averagePagesPerSession(42)
            .bounceRate(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetricsDto.CustomerMetricsDto dto = CountryMetricsDto.CustomerMetricsDto.builder()
                        .averageAge(BigDecimal.TEN)
            .genderDistribution(Collections.emptyMap())
            .ageGroupDistribution(Collections.emptyMap())
            .satisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .repeatPurchaseRate(BigDecimal.TEN)
            .averageSessionDuration(BigDecimal.TEN)
            .averagePagesPerSession(42)
            .bounceRate(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}