package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.interfaces.rest.ReportController;
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
class ReportController_UpdateReportRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ReportController.UpdateReportRequestDto dto = new ReportController.UpdateReportRequestDto();
        dto.setSummary("val-summary");
        dto.setCriticalIssue("val-criticalIssue");
        dto.setRecommendation("val-recommendation");
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-criticalIssue", dto.getCriticalIssue());
        assertEquals("val-recommendation", dto.getRecommendation());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportController.UpdateReportRequestDto dto1 = new ReportController.UpdateReportRequestDto();
        ReportController.UpdateReportRequestDto dto2 = new ReportController.UpdateReportRequestDto();
        dto1.setMetrics(null);
        dto1.setSummary("test");
        dto1.setCriticalIssue("test");
        dto1.setRecommendation("test");
        dto2.setMetrics(null);
        dto2.setSummary("test");
        dto2.setCriticalIssue("test");
        dto2.setRecommendation("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setSummary(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportController.UpdateReportRequestDto dto = new ReportController.UpdateReportRequestDto();
        dto.setMetrics(null);
        dto.setSummary("test");
        dto.setCriticalIssue("test");
        dto.setRecommendation("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportController.UpdateReportRequestDto dto = new ReportController.UpdateReportRequestDto();
        dto.setMetrics(null);
        dto.setSummary("test");
        dto.setCriticalIssue("test");
        dto.setRecommendation("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}