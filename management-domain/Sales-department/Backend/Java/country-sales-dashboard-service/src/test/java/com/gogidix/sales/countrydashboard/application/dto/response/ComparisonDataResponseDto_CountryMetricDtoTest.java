package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.ComparisonDataResponseDto;
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
class ComparisonDataResponseDto_CountryMetricDtoTest {

        @Test
    void testBuilder() {
        ComparisonDataResponseDto.CountryMetricDto dto = ComparisonDataResponseDto.CountryMetricDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .growth(BigDecimal.TEN)
            .rank(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(BigDecimal.TEN, dto.getAchievement());
        assertEquals(BigDecimal.TEN, dto.getGrowth());
        assertEquals(42, dto.getRank());
    }

    @Test
    void testSettersAndGetters() {
        ComparisonDataResponseDto.CountryMetricDto dto = new ComparisonDataResponseDto.CountryMetricDto();
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRevenue(BigDecimal.ONE);
        dto.setAchievement(BigDecimal.ONE);
        dto.setGrowth(BigDecimal.ONE);
        dto.setRank(99);
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getAchievement());
        assertEquals(BigDecimal.ONE, dto.getGrowth());
        assertEquals(99, dto.getRank());
    }

    @Test
    void testEqualsAndHashCode() {
        ComparisonDataResponseDto.CountryMetricDto dto1 = ComparisonDataResponseDto.CountryMetricDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .growth(BigDecimal.TEN)
            .rank(42)
            .build();
        ComparisonDataResponseDto.CountryMetricDto dto2 = ComparisonDataResponseDto.CountryMetricDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .growth(BigDecimal.TEN)
            .rank(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComparisonDataResponseDto.CountryMetricDto dto = ComparisonDataResponseDto.CountryMetricDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .achievement(BigDecimal.TEN)
            .growth(BigDecimal.TEN)
            .rank(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}