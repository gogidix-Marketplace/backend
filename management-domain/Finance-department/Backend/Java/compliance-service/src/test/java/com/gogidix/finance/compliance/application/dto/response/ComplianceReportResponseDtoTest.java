package com.gogidix.finance.compliance.application.dto.response;

import com.gogidix.finance.compliance.application.dto.response.ComplianceReportResponseDto;
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
class ComplianceReportResponseDtoTest {

        @Test
    void testBuilder() {
        ComplianceReportResponseDto dto = ComplianceReportResponseDto.builder()
                        .id("test-id")
            .reportId("test-reportId")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .description("test-description")
            .reportType(ComplianceReportResponseDto.ReportTypeDto.DAILY_SUMMARY)
            .status(ComplianceReportResponseDto.ReportStatusDto.GENERATING)
            .reportPeriodStart(LocalDate.of(2025,1,15))
            .reportPeriodEnd(LocalDate.of(2025,1,15))
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .generatedByUserId("test-generatedByUserId")
            .summary(null)
            .sections(Collections.emptyList())
            .departmentIds(Collections.emptyList())
            .costCenterIds(Collections.emptyList())
            .ruleIds(Collections.emptyList())
            .checkIds(Collections.emptyList())
            .filters(Collections.emptyMap())
            .format("test-format")
            .fileUrl("test-fileUrl")
            .fileSize(42L)
            .totalRecords(42)
            .compliantCount(42)
            .nonCompliantCount(42)
            .warningCount(42)
            .notApplicableCount(42)
            .compliancePercentage(null)
            .complianceGrade("test-complianceGrade")
            .metrics(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isArchived(true)
            .violationSummaries(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-reportId", dto.getReportId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-reportName", dto.getReportName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(ComplianceReportResponseDto.ReportTypeDto.DAILY_SUMMARY, dto.getReportType());
        assertEquals(ComplianceReportResponseDto.ReportStatusDto.GENERATING, dto.getStatus());
        assertEquals(LocalDate.of(2025,1,15), dto.getReportPeriodStart());
        assertEquals(LocalDate.of(2025,1,15), dto.getReportPeriodEnd());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
        assertEquals("test-generatedByUserId", dto.getGeneratedByUserId());
        assertEquals("test-format", dto.getFormat());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals(42L, dto.getFileSize());
        assertEquals(42, dto.getTotalRecords());
        assertEquals(42, dto.getCompliantCount());
        assertEquals(42, dto.getNonCompliantCount());
        assertEquals(42, dto.getWarningCount());
        assertEquals(42, dto.getNotApplicableCount());
        assertEquals("test-complianceGrade", dto.getComplianceGrade());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertTrue(dto.getIsArchived());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceReportResponseDto dto = new ComplianceReportResponseDto();
        dto.setId("val-id");
        dto.setReportId("val-reportId");
        dto.setTenantId("val-tenantId");
        dto.setReportName("val-reportName");
        dto.setDescription("val-description");
        dto.setReportType(ComplianceReportResponseDto.ReportTypeDto.DAILY_SUMMARY);
        dto.setStatus(ComplianceReportResponseDto.ReportStatusDto.GENERATING);
        dto.setReportPeriodStart(LocalDate.of(2025,6,1));
        dto.setReportPeriodEnd(LocalDate.of(2025,6,1));
        dto.setGeneratedBy("val-generatedBy");
        dto.setGeneratedByUserId("val-generatedByUserId");
        dto.setFormat("val-format");
        dto.setFileUrl("val-fileUrl");
        dto.setTotalRecords(99);
        dto.setCompliantCount(99);
        dto.setNonCompliantCount(99);
        dto.setWarningCount(99);
        dto.setNotApplicableCount(99);
        dto.setComplianceGrade("val-complianceGrade");
        dto.setNotes("val-notes");
        dto.setCorrelationId("val-correlationId");
        dto.setIsArchived(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportName", dto.getReportName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(ComplianceReportResponseDto.ReportTypeDto.DAILY_SUMMARY, dto.getReportType());
        assertEquals(ComplianceReportResponseDto.ReportStatusDto.GENERATING, dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getReportPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getReportPeriodEnd());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
        assertEquals("val-generatedByUserId", dto.getGeneratedByUserId());
        assertEquals("val-format", dto.getFormat());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals(99, dto.getTotalRecords());
        assertEquals(99, dto.getCompliantCount());
        assertEquals(99, dto.getNonCompliantCount());
        assertEquals(99, dto.getWarningCount());
        assertEquals(99, dto.getNotApplicableCount());
        assertEquals("val-complianceGrade", dto.getComplianceGrade());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertTrue(dto.getIsArchived());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceReportResponseDto dto1 = ComplianceReportResponseDto.builder()
                        .id("test-id")
            .reportId("test-reportId")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .description("test-description")
            .reportType(ComplianceReportResponseDto.ReportTypeDto.DAILY_SUMMARY)
            .status(ComplianceReportResponseDto.ReportStatusDto.GENERATING)
            .reportPeriodStart(LocalDate.of(2025,1,15))
            .reportPeriodEnd(LocalDate.of(2025,1,15))
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .generatedByUserId("test-generatedByUserId")
            .summary(null)
            .sections(Collections.emptyList())
            .departmentIds(Collections.emptyList())
            .costCenterIds(Collections.emptyList())
            .ruleIds(Collections.emptyList())
            .checkIds(Collections.emptyList())
            .filters(Collections.emptyMap())
            .format("test-format")
            .fileUrl("test-fileUrl")
            .fileSize(42L)
            .totalRecords(42)
            .compliantCount(42)
            .nonCompliantCount(42)
            .warningCount(42)
            .notApplicableCount(42)
            .compliancePercentage(null)
            .complianceGrade("test-complianceGrade")
            .metrics(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isArchived(true)
            .violationSummaries(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ComplianceReportResponseDto dto2 = ComplianceReportResponseDto.builder()
                        .id("test-id")
            .reportId("test-reportId")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .description("test-description")
            .reportType(ComplianceReportResponseDto.ReportTypeDto.DAILY_SUMMARY)
            .status(ComplianceReportResponseDto.ReportStatusDto.GENERATING)
            .reportPeriodStart(LocalDate.of(2025,1,15))
            .reportPeriodEnd(LocalDate.of(2025,1,15))
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .generatedByUserId("test-generatedByUserId")
            .summary(null)
            .sections(Collections.emptyList())
            .departmentIds(Collections.emptyList())
            .costCenterIds(Collections.emptyList())
            .ruleIds(Collections.emptyList())
            .checkIds(Collections.emptyList())
            .filters(Collections.emptyMap())
            .format("test-format")
            .fileUrl("test-fileUrl")
            .fileSize(42L)
            .totalRecords(42)
            .compliantCount(42)
            .nonCompliantCount(42)
            .warningCount(42)
            .notApplicableCount(42)
            .compliancePercentage(null)
            .complianceGrade("test-complianceGrade")
            .metrics(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isArchived(true)
            .violationSummaries(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceReportResponseDto dto = ComplianceReportResponseDto.builder()
                        .id("test-id")
            .reportId("test-reportId")
            .tenantId("test-tenantId")
            .reportName("test-reportName")
            .description("test-description")
            .reportType(ComplianceReportResponseDto.ReportTypeDto.DAILY_SUMMARY)
            .status(ComplianceReportResponseDto.ReportStatusDto.GENERATING)
            .reportPeriodStart(LocalDate.of(2025,1,15))
            .reportPeriodEnd(LocalDate.of(2025,1,15))
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .generatedBy("test-generatedBy")
            .generatedByUserId("test-generatedByUserId")
            .summary(null)
            .sections(Collections.emptyList())
            .departmentIds(Collections.emptyList())
            .costCenterIds(Collections.emptyList())
            .ruleIds(Collections.emptyList())
            .checkIds(Collections.emptyList())
            .filters(Collections.emptyMap())
            .format("test-format")
            .fileUrl("test-fileUrl")
            .fileSize(42L)
            .totalRecords(42)
            .compliantCount(42)
            .nonCompliantCount(42)
            .warningCount(42)
            .notApplicableCount(42)
            .compliancePercentage(null)
            .complianceGrade("test-complianceGrade")
            .metrics(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .expiresAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isArchived(true)
            .violationSummaries(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}