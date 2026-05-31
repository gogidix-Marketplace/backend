package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.CountryDashboardResponseDto;
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
class CountryDashboardResponseDtoTest {

        @Test
    void testBuilder() {
        CountryDashboardResponseDto dto = CountryDashboardResponseDto.builder()
                        .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .region("test-region")
            .status("test-status")
            .type("test-type")
            .localCurrency("test-localCurrency")
            .baseCurrency("test-baseCurrency")
            .metrics(null)
            .territoriesCount(42)
            .kpisCount(42)
            .comparisonSummary(null)
            .quotaSummary(null)
            .executiveSummary(null)
            .dataFreshness(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-dashboardId", dto.getDashboardId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-type", dto.getType());
        assertEquals("test-localCurrency", dto.getLocalCurrency());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals(42, dto.getTerritoriesCount());
        assertEquals(42, dto.getKpisCount());
    }

    @Test
    void testSettersAndGetters() {
        CountryDashboardResponseDto dto = new CountryDashboardResponseDto();
        dto.setDashboardId("val-dashboardId");
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRegion("val-region");
        dto.setStatus("val-status");
        dto.setType("val-type");
        dto.setLocalCurrency("val-localCurrency");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setTerritoriesCount(99);
        dto.setKpisCount(99);
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-type", dto.getType());
        assertEquals("val-localCurrency", dto.getLocalCurrency());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals(99, dto.getTerritoriesCount());
        assertEquals(99, dto.getKpisCount());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardResponseDto dto1 = CountryDashboardResponseDto.builder()
                        .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .region("test-region")
            .status("test-status")
            .type("test-type")
            .localCurrency("test-localCurrency")
            .baseCurrency("test-baseCurrency")
            .metrics(null)
            .territoriesCount(42)
            .kpisCount(42)
            .comparisonSummary(null)
            .quotaSummary(null)
            .executiveSummary(null)
            .dataFreshness(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CountryDashboardResponseDto dto2 = CountryDashboardResponseDto.builder()
                        .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .region("test-region")
            .status("test-status")
            .type("test-type")
            .localCurrency("test-localCurrency")
            .baseCurrency("test-baseCurrency")
            .metrics(null)
            .territoriesCount(42)
            .kpisCount(42)
            .comparisonSummary(null)
            .quotaSummary(null)
            .executiveSummary(null)
            .dataFreshness(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDashboardResponseDto dto = CountryDashboardResponseDto.builder()
                        .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .region("test-region")
            .status("test-status")
            .type("test-type")
            .localCurrency("test-localCurrency")
            .baseCurrency("test-baseCurrency")
            .metrics(null)
            .territoriesCount(42)
            .kpisCount(42)
            .comparisonSummary(null)
            .quotaSummary(null)
            .executiveSummary(null)
            .dataFreshness(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}