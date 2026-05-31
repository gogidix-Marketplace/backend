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
class CountryMetricsDto_DataQualityScoreDtoTest {

        @Test
    void testBuilder() {
        CountryMetricsDto.DataQualityScoreDto dto = CountryMetricsDto.DataQualityScoreDto.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCompleteness());
        assertEquals(BigDecimal.TEN, dto.getAccuracy());
        assertEquals(BigDecimal.TEN, dto.getTimeliness());
        assertEquals(BigDecimal.TEN, dto.getConsistency());
        assertEquals(BigDecimal.TEN, dto.getOverallScore());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetricsDto.DataQualityScoreDto dto = new CountryMetricsDto.DataQualityScoreDto();
        dto.setCompleteness(BigDecimal.ONE);
        dto.setAccuracy(BigDecimal.ONE);
        dto.setTimeliness(BigDecimal.ONE);
        dto.setConsistency(BigDecimal.ONE);
        dto.setOverallScore(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getCompleteness());
        assertEquals(BigDecimal.ONE, dto.getAccuracy());
        assertEquals(BigDecimal.ONE, dto.getTimeliness());
        assertEquals(BigDecimal.ONE, dto.getConsistency());
        assertEquals(BigDecimal.ONE, dto.getOverallScore());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetricsDto.DataQualityScoreDto dto1 = CountryMetricsDto.DataQualityScoreDto.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .build();
        CountryMetricsDto.DataQualityScoreDto dto2 = CountryMetricsDto.DataQualityScoreDto.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetricsDto.DataQualityScoreDto dto = CountryMetricsDto.DataQualityScoreDto.builder()
                        .completeness(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .timeliness(BigDecimal.TEN)
            .consistency(BigDecimal.TEN)
            .overallScore(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}