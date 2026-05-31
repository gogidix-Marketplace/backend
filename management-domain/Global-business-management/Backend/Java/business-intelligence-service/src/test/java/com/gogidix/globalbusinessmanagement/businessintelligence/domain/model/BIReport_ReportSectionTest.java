package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.BIReport;
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
class BIReport_ReportSectionTest {

        @Test
    void testBuilder() {
        BIReport.ReportSection dto = BIReport.ReportSection.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .type(null)
            .displayOrder(42)
            .content(null)
            .settings(null)
            .isVisible(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-sectionId", dto.getSectionId());
        assertEquals("test-title", dto.getTitle());
        assertEquals(42, dto.getDisplayOrder());
        assertTrue(dto.getIsVisible());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.ReportSection dto = new BIReport.ReportSection();
        dto.setSectionId("val-sectionId");
        dto.setTitle("val-title");
        dto.setDisplayOrder(99);
        dto.setIsVisible(true);
        assertEquals("val-sectionId", dto.getSectionId());
        assertEquals("val-title", dto.getTitle());
        assertEquals(99, dto.getDisplayOrder());
        assertTrue(dto.getIsVisible());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.ReportSection dto1 = BIReport.ReportSection.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .type(null)
            .displayOrder(42)
            .content(null)
            .settings(null)
            .isVisible(true)
            .build();
        BIReport.ReportSection dto2 = BIReport.ReportSection.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .type(null)
            .displayOrder(42)
            .content(null)
            .settings(null)
            .isVisible(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.ReportSection dto = BIReport.ReportSection.builder()
                        .sectionId("test-sectionId")
            .title("test-title")
            .type(null)
            .displayOrder(42)
            .content(null)
            .settings(null)
            .isVisible(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}