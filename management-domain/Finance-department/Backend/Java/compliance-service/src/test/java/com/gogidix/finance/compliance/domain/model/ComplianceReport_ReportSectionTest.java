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
class ComplianceReport_ReportSectionTest {

        @Test
    void testBuilder() {
        ComplianceReport.ReportSection dto = ComplianceReport.ReportSection.builder()
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
        ComplianceReport.ReportSection dto = new ComplianceReport.ReportSection();
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
        ComplianceReport.ReportSection dto1 = ComplianceReport.ReportSection.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .description("test-description")
            .order(42)
            .content(Collections.emptyMap())
            .build();
        ComplianceReport.ReportSection dto2 = ComplianceReport.ReportSection.builder()
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
        ComplianceReport.ReportSection dto = ComplianceReport.ReportSection.builder()
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