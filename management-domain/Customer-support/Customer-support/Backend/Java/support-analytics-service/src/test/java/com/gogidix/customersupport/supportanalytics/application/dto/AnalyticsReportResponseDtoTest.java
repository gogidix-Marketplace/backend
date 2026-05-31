package com.gogidix.customersupport.supportanalytics.application.dto;

import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportResponseDto;
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
class AnalyticsReportResponseDtoTest {

        @Test
    void testBuilder() {
        AnalyticsReportResponseDto dto = AnalyticsReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .reportType(AnalyticsReportResponseDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .resolvedTickets(42)
            .openTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-reportName", dto.getReportName());
        assertEquals(AnalyticsReportResponseDto.ReportTypeDto.DAILY, dto.getReportType());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(42, dto.getTotalTickets());
        assertEquals(42, dto.getResolvedTickets());
        assertEquals(42, dto.getOpenTickets());
        assertEquals(42, dto.getEscalatedTickets());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsReportResponseDto dto = new AnalyticsReportResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setReportName("val-reportName");
        dto.setReportType(AnalyticsReportResponseDto.ReportTypeDto.DAILY);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setTotalTickets(99);
        dto.setResolvedTickets(99);
        dto.setOpenTickets(99);
        dto.setEscalatedTickets(99);
        dto.setGeneratedBy("val-generatedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportName", dto.getReportName());
        assertEquals(AnalyticsReportResponseDto.ReportTypeDto.DAILY, dto.getReportType());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getTotalTickets());
        assertEquals(99, dto.getResolvedTickets());
        assertEquals(99, dto.getOpenTickets());
        assertEquals(99, dto.getEscalatedTickets());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReportResponseDto dto1 = AnalyticsReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .reportType(AnalyticsReportResponseDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .resolvedTickets(42)
            .openTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AnalyticsReportResponseDto dto2 = AnalyticsReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .reportType(AnalyticsReportResponseDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .resolvedTickets(42)
            .openTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsReportResponseDto dto = AnalyticsReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .reportType(AnalyticsReportResponseDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalTickets(42)
            .resolvedTickets(42)
            .openTickets(42)
            .escalatedTickets(42)
            .averageResolutionTimeMinutes(null)
            .averageResponseTimeMinutes(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}