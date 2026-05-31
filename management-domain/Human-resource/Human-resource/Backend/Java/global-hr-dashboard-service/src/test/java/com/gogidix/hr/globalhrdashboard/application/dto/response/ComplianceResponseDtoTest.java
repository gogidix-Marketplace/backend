package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.ComplianceResponseDto;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus;
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
class ComplianceResponseDtoTest {

        @Test
    void testBuilder() {
        ComplianceResponseDto dto = ComplianceResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .complianceScore(null)
            .totalRequirements(42)
            .passedChecks(42)
            .failedChecks(42)
            .criticalIssues(42)
            .period("test-period")
            .status(ComplianceStatus.COMPLIANT)
            .issues(Collections.emptyList())
            .pendingActions(Collections.emptyList())
            .lastAssessed(Instant.parse("2025-01-15T10:00:00Z"))
            .assessedBy("test-assessedBy")
            .notes("test-notes")
            .isActive(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals(42, dto.getTotalRequirements());
        assertEquals(42, dto.getPassedChecks());
        assertEquals(42, dto.getFailedChecks());
        assertEquals(42, dto.getCriticalIssues());
        assertEquals("test-period", dto.getPeriod());
        assertEquals("test-assessedBy", dto.getAssessedBy());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceResponseDto dto = new ComplianceResponseDto();
        dto.setId("val-id");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRegionCode("val-regionCode");
        dto.setTotalRequirements(99);
        dto.setPassedChecks(99);
        dto.setFailedChecks(99);
        dto.setCriticalIssues(99);
        dto.setPeriod("val-period");
        dto.setAssessedBy("val-assessedBy");
        dto.setNotes("val-notes");
        dto.setIsActive(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals(99, dto.getTotalRequirements());
        assertEquals(99, dto.getPassedChecks());
        assertEquals(99, dto.getFailedChecks());
        assertEquals(99, dto.getCriticalIssues());
        assertEquals("val-period", dto.getPeriod());
        assertEquals("val-assessedBy", dto.getAssessedBy());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceResponseDto dto1 = ComplianceResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .complianceScore(null)
            .totalRequirements(42)
            .passedChecks(42)
            .failedChecks(42)
            .criticalIssues(42)
            .period("test-period")
            .status(ComplianceStatus.COMPLIANT)
            .issues(Collections.emptyList())
            .pendingActions(Collections.emptyList())
            .lastAssessed(Instant.parse("2025-01-15T10:00:00Z"))
            .assessedBy("test-assessedBy")
            .notes("test-notes")
            .isActive(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ComplianceResponseDto dto2 = ComplianceResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .complianceScore(null)
            .totalRequirements(42)
            .passedChecks(42)
            .failedChecks(42)
            .criticalIssues(42)
            .period("test-period")
            .status(ComplianceStatus.COMPLIANT)
            .issues(Collections.emptyList())
            .pendingActions(Collections.emptyList())
            .lastAssessed(Instant.parse("2025-01-15T10:00:00Z"))
            .assessedBy("test-assessedBy")
            .notes("test-notes")
            .isActive(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceResponseDto dto = ComplianceResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .complianceScore(null)
            .totalRequirements(42)
            .passedChecks(42)
            .failedChecks(42)
            .criticalIssues(42)
            .period("test-period")
            .status(ComplianceStatus.COMPLIANT)
            .issues(Collections.emptyList())
            .pendingActions(Collections.emptyList())
            .lastAssessed(Instant.parse("2025-01-15T10:00:00Z"))
            .assessedBy("test-assessedBy")
            .notes("test-notes")
            .isActive(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}