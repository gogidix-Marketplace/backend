package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.TrendAnalysisResponseDto;
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
class TrendAnalysisResponseDtoTest {

        @Test
    void testBuilder() {
        TrendAnalysisResponseDto dto = TrendAnalysisResponseDto.builder()
                        .id("test-id")
            .analysisName("test-analysisName")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .periodStart(LocalDateTime.of(2025,1,15,10,0))
            .periodEnd(LocalDateTime.of(2025,1,15,10,0))
            .analysisType("test-analysisType")
            .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendPattern("test-trendPattern")
            .analyzedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .analysisVersion("test-analysisVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-analysisName", dto.getAnalysisName());
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-metricCode", dto.getMetricCode());
        assertEquals("test-entityCode", dto.getEntityCode());
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-analysisType", dto.getAnalysisType());
        assertEquals("test-trendDirection", dto.getTrendDirection());
        assertEquals(BigDecimal.TEN, dto.getTrendStrength());
        assertEquals("test-trendPattern", dto.getTrendPattern());
        assertEquals("test-analysisVersion", dto.getAnalysisVersion());
    }

    @Test
    void testSettersAndGetters() {
        TrendAnalysisResponseDto dto = new TrendAnalysisResponseDto();
        dto.setId("val-id");
        dto.setAnalysisName("val-analysisName");
        dto.setMetricName("val-metricName");
        dto.setMetricCode("val-metricCode");
        dto.setEntityCode("val-entityCode");
        dto.setEntityType("val-entityType");
        dto.setRegionCode("val-regionCode");
        dto.setAnalysisType("val-analysisType");
        dto.setTrendDirection("val-trendDirection");
        dto.setTrendStrength(BigDecimal.ONE);
        dto.setTrendPattern("val-trendPattern");
        dto.setAnalysisVersion("val-analysisVersion");
        assertEquals("val-id", dto.getId());
        assertEquals("val-analysisName", dto.getAnalysisName());
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-metricCode", dto.getMetricCode());
        assertEquals("val-entityCode", dto.getEntityCode());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-analysisType", dto.getAnalysisType());
        assertEquals("val-trendDirection", dto.getTrendDirection());
        assertEquals(BigDecimal.ONE, dto.getTrendStrength());
        assertEquals("val-trendPattern", dto.getTrendPattern());
        assertEquals("val-analysisVersion", dto.getAnalysisVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        TrendAnalysisResponseDto dto1 = TrendAnalysisResponseDto.builder()
                        .id("test-id")
            .analysisName("test-analysisName")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .periodStart(LocalDateTime.of(2025,1,15,10,0))
            .periodEnd(LocalDateTime.of(2025,1,15,10,0))
            .analysisType("test-analysisType")
            .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendPattern("test-trendPattern")
            .analyzedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .analysisVersion("test-analysisVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TrendAnalysisResponseDto dto2 = TrendAnalysisResponseDto.builder()
                        .id("test-id")
            .analysisName("test-analysisName")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .periodStart(LocalDateTime.of(2025,1,15,10,0))
            .periodEnd(LocalDateTime.of(2025,1,15,10,0))
            .analysisType("test-analysisType")
            .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendPattern("test-trendPattern")
            .analyzedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .analysisVersion("test-analysisVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrendAnalysisResponseDto dto = TrendAnalysisResponseDto.builder()
                        .id("test-id")
            .analysisName("test-analysisName")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .periodStart(LocalDateTime.of(2025,1,15,10,0))
            .periodEnd(LocalDateTime.of(2025,1,15,10,0))
            .analysisType("test-analysisType")
            .trendDirection("test-trendDirection")
            .trendStrength(BigDecimal.TEN)
            .trendPattern("test-trendPattern")
            .analyzedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .analysisVersion("test-analysisVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}