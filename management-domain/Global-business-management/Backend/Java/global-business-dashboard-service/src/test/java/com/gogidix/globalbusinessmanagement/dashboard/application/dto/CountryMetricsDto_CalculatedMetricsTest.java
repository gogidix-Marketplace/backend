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
class CountryMetricsDto_CalculatedMetricsTest {

        @Test
    void testBuilder() {
        CountryMetricsDto.CalculatedMetrics dto = CountryMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerRetentionRate(BigDecimal.TEN)
            .ltvToCacRatio(BigDecimal.TEN)
            .paybackPeriod(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCustomerGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.TEN, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.TEN, dto.getLtvToCacRatio());
        assertEquals(BigDecimal.TEN, dto.getPaybackPeriod());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetricsDto.CalculatedMetrics dto = new CountryMetricsDto.CalculatedMetrics();
        dto.setCustomerGrowthRate(BigDecimal.ONE);
        dto.setCustomerChurnRate(BigDecimal.ONE);
        dto.setCustomerRetentionRate(BigDecimal.ONE);
        dto.setLtvToCacRatio(BigDecimal.ONE);
        dto.setPaybackPeriod(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getCustomerGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.ONE, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.ONE, dto.getLtvToCacRatio());
        assertEquals(BigDecimal.ONE, dto.getPaybackPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetricsDto.CalculatedMetrics dto1 = CountryMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerRetentionRate(BigDecimal.TEN)
            .ltvToCacRatio(BigDecimal.TEN)
            .paybackPeriod(BigDecimal.TEN)
            .build();
        CountryMetricsDto.CalculatedMetrics dto2 = CountryMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerRetentionRate(BigDecimal.TEN)
            .ltvToCacRatio(BigDecimal.TEN)
            .paybackPeriod(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetricsDto.CalculatedMetrics dto = CountryMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerRetentionRate(BigDecimal.TEN)
            .ltvToCacRatio(BigDecimal.TEN)
            .paybackPeriod(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}