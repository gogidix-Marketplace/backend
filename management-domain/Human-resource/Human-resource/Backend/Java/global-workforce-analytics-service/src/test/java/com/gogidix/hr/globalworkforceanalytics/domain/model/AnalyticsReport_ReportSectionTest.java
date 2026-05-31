package com.gogidix.hr.globalworkforceanalytics.domain.model;

import com.gogidix.hr.globalworkforceanalytics.domain.model.AnalyticsReport;
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
class AnalyticsReport_ReportSectionTest {

        @Test
    void testSettersAndGetters() {
        AnalyticsReport.ReportSection dto = new AnalyticsReport.ReportSection();
        dto.setSectionId("val-sectionId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setOrder(99);
        dto.setSectionType("val-sectionType");
        dto.setIsVisible(true);
        assertEquals("val-sectionId", dto.getSectionId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getOrder());
        assertEquals("val-sectionType", dto.getSectionType());
        assertTrue(dto.getIsVisible());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReport.ReportSection dto1 = new AnalyticsReport.ReportSection();
        AnalyticsReport.ReportSection dto2 = new AnalyticsReport.ReportSection();
        dto1.setSectionId("test");
        dto1.setTitle("test");
        dto1.setDescription("test");
        dto1.setOrder(42);
        dto1.setSectionType("test");
        dto1.setContent(Collections.emptyMap());
        dto1.setChartIds(Collections.emptyList());
        dto1.setIsVisible(true);
        dto2.setSectionId("test");
        dto2.setTitle("test");
        dto2.setDescription("test");
        dto2.setOrder(42);
        dto2.setSectionType("test");
        dto2.setContent(Collections.emptyMap());
        dto2.setChartIds(Collections.emptyList());
        dto2.setIsVisible(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setSectionId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        AnalyticsReport.ReportSection dto = new AnalyticsReport.ReportSection();
        dto.setSectionId("test");
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setOrder(42);
        dto.setSectionType("test");
        dto.setContent(Collections.emptyMap());
        dto.setChartIds(Collections.emptyList());
        dto.setIsVisible(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        AnalyticsReport.ReportSection dto = new AnalyticsReport.ReportSection();
        dto.setSectionId("test");
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setOrder(42);
        dto.setSectionType("test");
        dto.setContent(Collections.emptyMap());
        dto.setChartIds(Collections.emptyList());
        dto.setIsVisible(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}