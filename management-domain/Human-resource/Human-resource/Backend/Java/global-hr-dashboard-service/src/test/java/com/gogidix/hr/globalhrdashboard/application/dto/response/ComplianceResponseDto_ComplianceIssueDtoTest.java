package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.ComplianceResponseDto;
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
class ComplianceResponseDto_ComplianceIssueDtoTest {

        @Test
    void testBuilder() {
        ComplianceResponseDto.ComplianceIssueDto dto = ComplianceResponseDto.ComplianceIssueDto.builder()
                        .issueId("test-issueId")
            .title("test-title")
            .description("test-description")
            .severity(null)
            .category("test-category")
            .identifiedDate(Instant.parse("2025-01-15T10:00:00Z"))
            .targetResolutionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedTo("test-assignedTo")
            .status(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-issueId", dto.getIssueId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-assignedTo", dto.getAssignedTo());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceResponseDto.ComplianceIssueDto dto = new ComplianceResponseDto.ComplianceIssueDto();
        dto.setIssueId("val-issueId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setCategory("val-category");
        dto.setAssignedTo("val-assignedTo");
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-assignedTo", dto.getAssignedTo());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceResponseDto.ComplianceIssueDto dto1 = ComplianceResponseDto.ComplianceIssueDto.builder()
                        .issueId("test-issueId")
            .title("test-title")
            .description("test-description")
            .severity(null)
            .category("test-category")
            .identifiedDate(Instant.parse("2025-01-15T10:00:00Z"))
            .targetResolutionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedTo("test-assignedTo")
            .status(null)
            .build();
        ComplianceResponseDto.ComplianceIssueDto dto2 = ComplianceResponseDto.ComplianceIssueDto.builder()
                        .issueId("test-issueId")
            .title("test-title")
            .description("test-description")
            .severity(null)
            .category("test-category")
            .identifiedDate(Instant.parse("2025-01-15T10:00:00Z"))
            .targetResolutionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedTo("test-assignedTo")
            .status(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceResponseDto.ComplianceIssueDto dto = ComplianceResponseDto.ComplianceIssueDto.builder()
                        .issueId("test-issueId")
            .title("test-title")
            .description("test-description")
            .severity(null)
            .category("test-category")
            .identifiedDate(Instant.parse("2025-01-15T10:00:00Z"))
            .targetResolutionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedTo("test-assignedTo")
            .status(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}