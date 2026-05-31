package com.gogidix.finance.compliance.domain.model;

import com.gogidix.finance.compliance.domain.model.ComplianceReport;
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
class ComplianceReport_ReportSummaryTest {

        @Test
    void testBuilder() {
        ComplianceReport.ReportSummary dto = ComplianceReport.ReportSummary.builder()
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
        ComplianceReport.ReportSummary dto = new ComplianceReport.ReportSummary();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceReport.ReportSummary dto1 = ComplianceReport.ReportSummary.builder()
                        .title("test-title")
            .description("test-description")
            .data(Collections.emptyMap())
            .build();
        ComplianceReport.ReportSummary dto2 = ComplianceReport.ReportSummary.builder()
                        .title("test-title")
            .description("test-description")
            .data(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceReport.ReportSummary dto = ComplianceReport.ReportSummary.builder()
                        .title("test-title")
            .description("test-description")
            .data(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}