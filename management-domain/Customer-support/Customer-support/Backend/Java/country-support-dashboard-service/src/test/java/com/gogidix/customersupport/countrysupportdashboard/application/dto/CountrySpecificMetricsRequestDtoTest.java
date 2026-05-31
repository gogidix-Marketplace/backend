package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsRequestDto;
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
class CountrySpecificMetricsRequestDtoTest {

        @Test
    void testBuilder() {
        CountrySpecificMetricsRequestDto dto = CountrySpecificMetricsRequestDto.builder()
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
            .build();
        assertNotNull(dto);
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
        CountrySpecificMetricsRequestDto dto = new CountrySpecificMetricsRequestDto();
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
        CountrySpecificMetricsRequestDto dto1 = CountrySpecificMetricsRequestDto.builder()
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
            .build();
        CountrySpecificMetricsRequestDto dto2 = CountrySpecificMetricsRequestDto.builder()
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
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySpecificMetricsRequestDto dto = CountrySpecificMetricsRequestDto.builder()
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
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}