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
class ComplianceReportResponseDto_ReportSectionDtoTest {

        @Test
    void testBuilder() {
        ComplianceReportResponseDto.ReportSectionDto dto = ComplianceReportResponseDto.ReportSectionDto.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .description("test-description")
            .order(42)
            .content(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-sectionId", dto.getSectionId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getOrder());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceReportResponseDto.ReportSectionDto dto = new ComplianceReportResponseDto.ReportSectionDto();
        dto.setSectionId("val-sectionId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setOrder(99);
        assertEquals("val-sectionId", dto.getSectionId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceReportResponseDto.ReportSectionDto dto1 = ComplianceReportResponseDto.ReportSectionDto.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .description("test-description")
            .order(42)
            .content(Collections.emptyMap())
            .build();
        ComplianceReportResponseDto.ReportSectionDto dto2 = ComplianceReportResponseDto.ReportSectionDto.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .description("test-description")
            .order(42)
            .content(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceReportResponseDto.ReportSectionDto dto = ComplianceReportResponseDto.ReportSectionDto.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .description("test-description")
            .order(42)
            .content(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}