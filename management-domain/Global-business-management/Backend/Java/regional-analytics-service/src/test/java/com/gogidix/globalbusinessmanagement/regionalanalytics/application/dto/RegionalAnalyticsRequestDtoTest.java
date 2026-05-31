package com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto;

import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsRequestDto;
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
class RegionalAnalyticsRequestDtoTest {

        @Test
    void testBuilder() {
        RegionalAnalyticsRequestDto dto = RegionalAnalyticsRequestDto.builder()
                        .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .build();
        assertNotNull(dto);
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
        RegionalAnalyticsRequestDto dto = new RegionalAnalyticsRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setMetricName("val-metricName");
        dto.setMetricValue("val-metricValue");
        dto.setRegion("val-region");
        dto.setCountry("val-country");
        dto.setPeriod("val-period");
        dto.setCategory("val-category");
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
        RegionalAnalyticsRequestDto dto1 = RegionalAnalyticsRequestDto.builder()
                        .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .build();
        RegionalAnalyticsRequestDto dto2 = RegionalAnalyticsRequestDto.builder()
                        .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalAnalyticsRequestDto dto = RegionalAnalyticsRequestDto.builder()
                        .tenantId("test-tenantId")
            .metricName("test-metricName")
            .metricValue("test-metricValue")
            .region("test-region")
            .country("test-country")
            .period("test-period")
            .category("test-category")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}