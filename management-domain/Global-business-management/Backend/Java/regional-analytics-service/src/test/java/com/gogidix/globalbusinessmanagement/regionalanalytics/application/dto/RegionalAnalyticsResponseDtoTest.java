package com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto;

import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsResponseDto;
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
class RegionalAnalyticsResponseDtoTest {

        @Test
    void testBuilder() {
        RegionalAnalyticsResponseDto dto = RegionalAnalyticsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-metricValue", dto.getMetricValue());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-country", dto.getCountry());
        assertEquals("test-period", dto.getPeriod());
        assertEquals("test-category", dto.getCategory());
    }

    @Test
    void testSettersAndGetters() {
        RegionalAnalyticsResponseDto dto = new RegionalAnalyticsResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setMetricName("val-metricName");
        dto.setMetricValue("val-metricValue");
        dto.setRegion("val-region");
        dto.setCountry("val-country");
        dto.setPeriod("val-period");
        dto.setCategory("val-category");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-metricValue", dto.getMetricValue());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-country", dto.getCountry());
        assertEquals("val-period", dto.getPeriod());
        assertEquals("val-category", dto.getCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalAnalyticsResponseDto dto1 = RegionalAnalyticsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RegionalAnalyticsResponseDto dto2 = RegionalAnalyticsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalAnalyticsResponseDto dto = RegionalAnalyticsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}