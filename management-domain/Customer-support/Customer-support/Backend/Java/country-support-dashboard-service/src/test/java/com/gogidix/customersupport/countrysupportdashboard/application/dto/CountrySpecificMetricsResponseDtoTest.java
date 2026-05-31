package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsResponseDto;
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
class CountrySpecificMetricsResponseDtoTest {

        @Test
    void testBuilder() {
        CountrySpecificMetricsResponseDto dto = CountrySpecificMetricsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .metricDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .activeAgents(42)
            .ticketVolumeByChannel(Collections.emptyMap())
            .ticketVolumeByPriority(Collections.emptyMap())
            .ticketVolumeByCategory(Collections.emptyMap())
            .slaComplianceRate(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .region("test-region")
            .language("test-language")
            .timezone("test-timezone")
            .businessHours(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals(LocalDate.of(2025,1,15), dto.getMetricDate());
        assertEquals(42, dto.getTotalTickets());
        assertEquals(42, dto.getOpenTickets());
        assertEquals(42, dto.getResolvedTickets());
        assertEquals(42, dto.getEscalatedTickets());
        assertEquals(42, dto.getActiveAgents());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-language", dto.getLanguage());
        assertEquals("test-timezone", dto.getTimezone());
    }

    @Test
    void testSettersAndGetters() {
        CountrySpecificMetricsResponseDto dto = new CountrySpecificMetricsResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setMetricDate(LocalDate.of(2025,6,1));
        dto.setTotalTickets(99);
        dto.setOpenTickets(99);
        dto.setResolvedTickets(99);
        dto.setEscalatedTickets(99);
        dto.setActiveAgents(99);
        dto.setRegion("val-region");
        dto.setLanguage("val-language");
        dto.setTimezone("val-timezone");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals(LocalDate.of(2025,6,1), dto.getMetricDate());
        assertEquals(99, dto.getTotalTickets());
        assertEquals(99, dto.getOpenTickets());
        assertEquals(99, dto.getResolvedTickets());
        assertEquals(99, dto.getEscalatedTickets());
        assertEquals(99, dto.getActiveAgents());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-language", dto.getLanguage());
        assertEquals("val-timezone", dto.getTimezone());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySpecificMetricsResponseDto dto1 = CountrySpecificMetricsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .metricDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .activeAgents(42)
            .ticketVolumeByChannel(Collections.emptyMap())
            .ticketVolumeByPriority(Collections.emptyMap())
            .ticketVolumeByCategory(Collections.emptyMap())
            .slaComplianceRate(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .region("test-region")
            .language("test-language")
            .timezone("test-timezone")
            .businessHours(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CountrySpecificMetricsResponseDto dto2 = CountrySpecificMetricsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .metricDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .activeAgents(42)
            .ticketVolumeByChannel(Collections.emptyMap())
            .ticketVolumeByPriority(Collections.emptyMap())
            .ticketVolumeByCategory(Collections.emptyMap())
            .slaComplianceRate(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .region("test-region")
            .language("test-language")
            .timezone("test-timezone")
            .businessHours(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySpecificMetricsResponseDto dto = CountrySpecificMetricsResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .metricDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .activeAgents(42)
            .ticketVolumeByChannel(Collections.emptyMap())
            .ticketVolumeByPriority(Collections.emptyMap())
            .ticketVolumeByCategory(Collections.emptyMap())
            .slaComplianceRate(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .region("test-region")
            .language("test-language")
            .timezone("test-timezone")
            .businessHours(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}