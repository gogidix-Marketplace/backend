package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary;
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
class RegionalSummary_RegionalDemographicsTest {

        @Test
    void testBuilder() {
        RegionalSummary.RegionalDemographics dto = RegionalSummary.RegionalDemographics.builder()
                        .totalPopulation(42L)
            .targetMarketSize(42L)
            .urbanizationRate(BigDecimal.TEN)
            .averageIncome(BigDecimal.TEN)
            .dominantLanguage("test-dominantLanguage")
            .supportedLanguages(Collections.emptyList())
            .primaryCurrency("test-primaryCurrency")
            .acceptedCurrencies(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalPopulation());
        assertEquals(42L, dto.getTargetMarketSize());
        assertEquals(BigDecimal.TEN, dto.getUrbanizationRate());
        assertEquals(BigDecimal.TEN, dto.getAverageIncome());
        assertEquals("test-dominantLanguage", dto.getDominantLanguage());
        assertEquals("test-primaryCurrency", dto.getPrimaryCurrency());
    }

    @Test
    void testSettersAndGetters() {
        RegionalSummary.RegionalDemographics dto = new RegionalSummary.RegionalDemographics();
        dto.setUrbanizationRate(BigDecimal.ONE);
        dto.setAverageIncome(BigDecimal.ONE);
        dto.setDominantLanguage("val-dominantLanguage");
        dto.setPrimaryCurrency("val-primaryCurrency");
        assertEquals(BigDecimal.ONE, dto.getUrbanizationRate());
        assertEquals(BigDecimal.ONE, dto.getAverageIncome());
        assertEquals("val-dominantLanguage", dto.getDominantLanguage());
        assertEquals("val-primaryCurrency", dto.getPrimaryCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalSummary.RegionalDemographics dto1 = RegionalSummary.RegionalDemographics.builder()
                        .totalPopulation(42L)
            .targetMarketSize(42L)
            .urbanizationRate(BigDecimal.TEN)
            .averageIncome(BigDecimal.TEN)
            .dominantLanguage("test-dominantLanguage")
            .supportedLanguages(Collections.emptyList())
            .primaryCurrency("test-primaryCurrency")
            .acceptedCurrencies(Collections.emptyList())
            .build();
        RegionalSummary.RegionalDemographics dto2 = RegionalSummary.RegionalDemographics.builder()
                        .totalPopulation(42L)
            .targetMarketSize(42L)
            .urbanizationRate(BigDecimal.TEN)
            .averageIncome(BigDecimal.TEN)
            .dominantLanguage("test-dominantLanguage")
            .supportedLanguages(Collections.emptyList())
            .primaryCurrency("test-primaryCurrency")
            .acceptedCurrencies(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalSummary.RegionalDemographics dto = RegionalSummary.RegionalDemographics.builder()
                        .totalPopulation(42L)
            .targetMarketSize(42L)
            .urbanizationRate(BigDecimal.TEN)
            .averageIncome(BigDecimal.TEN)
            .dominantLanguage("test-dominantLanguage")
            .supportedLanguages(Collections.emptyList())
            .primaryCurrency("test-primaryCurrency")
            .acceptedCurrencies(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}