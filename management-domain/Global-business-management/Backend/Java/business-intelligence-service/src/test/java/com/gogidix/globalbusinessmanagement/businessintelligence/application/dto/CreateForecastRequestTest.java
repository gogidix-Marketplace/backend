package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.CreateForecastRequest;
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
class CreateForecastRequestTest {

        @Test
    void testBuilder() {
        CreateForecastRequest dto = CreateForecastRequest.builder()
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
            .historicalPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .historicalPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .parameters(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-forecastName", dto.getForecastName());
        assertEquals("test-metricCode", dto.getMetricCode());
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-entityCode", dto.getEntityCode());
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-forecastType", dto.getForecastType());
        assertEquals("test-forecastMethod", dto.getForecastMethod());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
        assertEquals("test-modelVersion", dto.getModelVersion());
    }

    @Test
    void testSettersAndGetters() {
        CreateForecastRequest dto = new CreateForecastRequest();
        dto.setForecastName("val-forecastName");
        dto.setMetricCode("val-metricCode");
        dto.setMetricName("val-metricName");
        dto.setEntityCode("val-entityCode");
        dto.setEntityType("val-entityType");
        dto.setRegionCode("val-regionCode");
        dto.setForecastType("val-forecastType");
        dto.setForecastMethod("val-forecastMethod");
        dto.setGeneratedBy("val-generatedBy");
        dto.setModelVersion("val-modelVersion");
        assertEquals("val-forecastName", dto.getForecastName());
        assertEquals("val-metricCode", dto.getMetricCode());
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-entityCode", dto.getEntityCode());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-forecastType", dto.getForecastType());
        assertEquals("val-forecastMethod", dto.getForecastMethod());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
        assertEquals("val-modelVersion", dto.getModelVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateForecastRequest dto1 = CreateForecastRequest.builder()
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
            .historicalPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .historicalPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .parameters(Collections.emptyMap())
            .build();
        CreateForecastRequest dto2 = CreateForecastRequest.builder()
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
            .historicalPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .historicalPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .parameters(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreateForecastRequest dto = CreateForecastRequest.builder()
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
            .historicalPeriodStart(LocalDateTime.of(2025,1,15,10,0))
            .historicalPeriodEnd(LocalDateTime.of(2025,1,15,10,0))
            .generatedBy("test-generatedBy")
            .modelVersion("test-modelVersion")
            .parameters(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}