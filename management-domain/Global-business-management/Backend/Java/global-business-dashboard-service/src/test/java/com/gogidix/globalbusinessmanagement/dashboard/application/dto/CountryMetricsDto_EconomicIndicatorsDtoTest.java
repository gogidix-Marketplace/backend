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
class CountryMetricsDto_EconomicIndicatorsDtoTest {

        @Test
    void testBuilder() {
        CountryMetricsDto.EconomicIndicatorsDto dto = CountryMetricsDto.EconomicIndicatorsDto.builder()
                        .gdpGrowth(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .consumerConfidenceIndex(BigDecimal.TEN)
            .purchasingPowerIndex(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getGdpGrowth());
        assertEquals(BigDecimal.TEN, dto.getInflationRate());
        assertEquals(BigDecimal.TEN, dto.getUnemploymentRate());
        assertEquals(BigDecimal.TEN, dto.getExchangeRate());
        assertEquals(BigDecimal.TEN, dto.getInterestRate());
        assertEquals(BigDecimal.TEN, dto.getConsumerConfidenceIndex());
        assertEquals(BigDecimal.TEN, dto.getPurchasingPowerIndex());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetricsDto.EconomicIndicatorsDto dto = new CountryMetricsDto.EconomicIndicatorsDto();
        dto.setGdpGrowth(BigDecimal.ONE);
        dto.setInflationRate(BigDecimal.ONE);
        dto.setUnemploymentRate(BigDecimal.ONE);
        dto.setExchangeRate(BigDecimal.ONE);
        dto.setInterestRate(BigDecimal.ONE);
        dto.setConsumerConfidenceIndex(BigDecimal.ONE);
        dto.setPurchasingPowerIndex(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getGdpGrowth());
        assertEquals(BigDecimal.ONE, dto.getInflationRate());
        assertEquals(BigDecimal.ONE, dto.getUnemploymentRate());
        assertEquals(BigDecimal.ONE, dto.getExchangeRate());
        assertEquals(BigDecimal.ONE, dto.getInterestRate());
        assertEquals(BigDecimal.ONE, dto.getConsumerConfidenceIndex());
        assertEquals(BigDecimal.ONE, dto.getPurchasingPowerIndex());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetricsDto.EconomicIndicatorsDto dto1 = CountryMetricsDto.EconomicIndicatorsDto.builder()
                        .gdpGrowth(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .consumerConfidenceIndex(BigDecimal.TEN)
            .purchasingPowerIndex(BigDecimal.TEN)
            .build();
        CountryMetricsDto.EconomicIndicatorsDto dto2 = CountryMetricsDto.EconomicIndicatorsDto.builder()
                        .gdpGrowth(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .consumerConfidenceIndex(BigDecimal.TEN)
            .purchasingPowerIndex(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetricsDto.EconomicIndicatorsDto dto = CountryMetricsDto.EconomicIndicatorsDto.builder()
                        .gdpGrowth(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .exchangeRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .consumerConfidenceIndex(BigDecimal.TEN)
            .purchasingPowerIndex(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}