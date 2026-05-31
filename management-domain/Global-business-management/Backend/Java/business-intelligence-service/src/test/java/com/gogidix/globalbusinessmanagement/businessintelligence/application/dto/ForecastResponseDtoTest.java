package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.ForecastResponseDto;
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
class ForecastResponseDtoTest {

        @Test
    void testBuilder() {
        ForecastResponseDto dto = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastName("test-forecastName")
            .metricCode("test-metricCode")
            .metricName("test-metricName")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .forecastType("test-forecastType")
            .forecastMethod("test-forecastMethod")
            .forecastPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .forecastPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .confidenceLevel(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-forecastName", dto.getForecastName());
        assertEquals("test-metricCode", dto.getMetricCode());
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-entityCode", dto.getEntityCode());
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-forecastType", dto.getForecastType());
        assertEquals("test-forecastMethod", dto.getForecastMethod());
        assertEquals("test-status", dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getConfidenceLevel());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
        assertEquals("test-modelVersion", dto.getModelVersion());
    }

    @Test
    void testSettersAndGetters() {
        ForecastResponseDto dto = new ForecastResponseDto();
        dto.setId("val-id");
        dto.setForecastName("val-forecastName");
        dto.setMetricCode("val-metricCode");
        dto.setMetricName("val-metricName");
        dto.setEntityCode("val-entityCode");
        dto.setEntityType("val-entityType");
        dto.setRegionCode("val-regionCode");
        dto.setForecastType("val-forecastType");
        dto.setForecastMethod("val-forecastMethod");
        dto.setStatus("val-status");
        dto.setConfidenceLevel(BigDecimal.ONE);
        dto.setGeneratedBy("val-generatedBy");
        dto.setModelVersion("val-modelVersion");
        assertEquals("val-id", dto.getId());
        assertEquals("val-forecastName", dto.getForecastName());
        assertEquals("val-metricCode", dto.getMetricCode());
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-entityCode", dto.getEntityCode());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-forecastType", dto.getForecastType());
        assertEquals("val-forecastMethod", dto.getForecastMethod());
        assertEquals("val-status", dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getConfidenceLevel());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
        assertEquals("val-modelVersion", dto.getModelVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastResponseDto dto1 = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastName("test-forecastName")
            .metricCode("test-metricCode")
            .metricName("test-metricName")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .forecastType("test-forecastType")
            .forecastMethod("test-forecastMethod")
            .forecastPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .forecastPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .confidenceLevel(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ForecastResponseDto dto2 = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastName("test-forecastName")
            .metricCode("test-metricCode")
            .metricName("test-metricName")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .forecastType("test-forecastType")
            .forecastMethod("test-forecastMethod")
            .forecastPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .forecastPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .confidenceLevel(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ForecastResponseDto dto = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastName("test-forecastName")
            .metricCode("test-metricCode")
            .metricName("test-metricName")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .regionCode("test-regionCode")
            .forecastType("test-forecastType")
            .forecastMethod("test-forecastMethod")
            .forecastPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .forecastPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .confidenceLevel(BigDecimal.TEN)
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .validUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}