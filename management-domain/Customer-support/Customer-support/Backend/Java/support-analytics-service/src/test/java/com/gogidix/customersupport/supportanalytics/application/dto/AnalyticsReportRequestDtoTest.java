package com.gogidix.customersupport.supportanalytics.application.dto;

import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportRequestDto;
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
class AnalyticsReportRequestDtoTest {

        @Test
    void testBuilder() {
        AnalyticsReportRequestDto dto = AnalyticsReportRequestDto.builder()
                        .reportName("test-reportName")
            .reportType(AnalyticsReportRequestDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .generatedBy("test-generatedBy")
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-reportName", dto.getReportName());
        assertEquals(AnalyticsReportRequestDto.ReportTypeDto.DAILY, dto.getReportType());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsReportRequestDto dto = new AnalyticsReportRequestDto();
        dto.setReportName("val-reportName");
        dto.setReportType(AnalyticsReportRequestDto.ReportTypeDto.DAILY);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setGeneratedBy("val-generatedBy");
        assertEquals("val-reportName", dto.getReportName());
        assertEquals(AnalyticsReportRequestDto.ReportTypeDto.DAILY, dto.getReportType());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReportRequestDto dto1 = AnalyticsReportRequestDto.builder()
                        .reportName("test-reportName")
            .reportType(AnalyticsReportRequestDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .generatedBy("test-generatedBy")
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .build();
        AnalyticsReportRequestDto dto2 = AnalyticsReportRequestDto.builder()
                        .reportName("test-reportName")
            .reportType(AnalyticsReportRequestDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .generatedBy("test-generatedBy")
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsReportRequestDto dto = AnalyticsReportRequestDto.builder()
                        .reportName("test-reportName")
            .reportType(AnalyticsReportRequestDto.ReportTypeDto.DAILY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .generatedBy("test-generatedBy")
            .agentPerformanceMetrics(Collections.emptyMap())
            .channelPerformance(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}