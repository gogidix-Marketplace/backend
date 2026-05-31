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
class ComplianceReportResponseDto_ReportSummaryDtoTest {

        @Test
    void testBuilder() {
        ComplianceReportResponseDto.ReportSummaryDto dto = ComplianceReportResponseDto.ReportSummaryDto.builder()
                        .title("test-title")
            .description("test-description")
            .data(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceReportResponseDto.ReportSummaryDto dto = new ComplianceReportResponseDto.ReportSummaryDto();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceReportResponseDto.ReportSummaryDto dto1 = ComplianceReportResponseDto.ReportSummaryDto.builder()
                        .title("test-title")
            .description("test-description")
            .data(Collections.emptyMap())
            .build();
        ComplianceReportResponseDto.ReportSummaryDto dto2 = ComplianceReportResponseDto.ReportSummaryDto.builder()
                        .title("test-title")
            .description("test-description")
            .data(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceReportResponseDto.ReportSummaryDto dto = ComplianceReportResponseDto.ReportSummaryDto.builder()
                        .title("test-title")
            .description("test-description")
            .data(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}