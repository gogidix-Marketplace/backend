package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.BIReportResponseDto;
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
class BIReportResponseDtoTest {

        @Test
    void testBuilder() {
        BIReportResponseDto dto = BIReportResponseDto.builder()
                        .id("test-id")
            .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .sectionIds(Collections.emptyList())
            .executiveSummary("test-executiveSummary")
            .confidenceScore(42)
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledFor(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-reportName", dto.getReportName());
        assertEquals("test-reportDescription", dto.getReportDescription());
        assertEquals("test-reportType", dto.getReportType());
        assertEquals("test-reportPeriod", dto.getReportPeriod());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-executiveSummary", dto.getExecutiveSummary());
        assertEquals(42, dto.getConfidenceScore());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-businessUnit", dto.getBusinessUnit());
    }

    @Test
    void testSettersAndGetters() {
        BIReportResponseDto dto = new BIReportResponseDto();
        dto.setId("val-id");
        dto.setReportName("val-reportName");
        dto.setReportDescription("val-reportDescription");
        dto.setReportType("val-reportType");
        dto.setReportPeriod("val-reportPeriod");
        dto.setStatus("val-status");
        dto.setCreatedBy("val-createdBy");
        dto.setExecutiveSummary("val-executiveSummary");
        dto.setConfidenceScore(99);
        dto.setRegionCode("val-regionCode");
        dto.setBusinessUnit("val-businessUnit");
        assertEquals("val-id", dto.getId());
        assertEquals("val-reportName", dto.getReportName());
        assertEquals("val-reportDescription", dto.getReportDescription());
        assertEquals("val-reportType", dto.getReportType());
        assertEquals("val-reportPeriod", dto.getReportPeriod());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-executiveSummary", dto.getExecutiveSummary());
        assertEquals(99, dto.getConfidenceScore());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-businessUnit", dto.getBusinessUnit());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReportResponseDto dto1 = BIReportResponseDto.builder()
                        .id("test-id")
            .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .sectionIds(Collections.emptyList())
            .executiveSummary("test-executiveSummary")
            .confidenceScore(42)
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledFor(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BIReportResponseDto dto2 = BIReportResponseDto.builder()
                        .id("test-id")
            .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .sectionIds(Collections.emptyList())
            .executiveSummary("test-executiveSummary")
            .confidenceScore(42)
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledFor(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReportResponseDto dto = BIReportResponseDto.builder()
                        .id("test-id")
            .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .status("test-status")
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .sectionIds(Collections.emptyList())
            .executiveSummary("test-executiveSummary")
            .confidenceScore(42)
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledFor(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}