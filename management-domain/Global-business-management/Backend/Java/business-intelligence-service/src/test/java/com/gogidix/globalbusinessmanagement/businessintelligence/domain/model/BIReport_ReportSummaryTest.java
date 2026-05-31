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
class BIReport_ReportSummaryTest {

        @Test
    void testBuilder() {
        BIReport.ReportSummary dto = BIReport.ReportSummary.builder()
                        .executiveSummary("test-executiveSummary")
            .keyFindings(Collections.emptyList())
            .recommendations(Collections.emptyList())
            .risks(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .confidenceScore(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-executiveSummary", dto.getExecutiveSummary());
        assertEquals("test-overallSentiment", dto.getOverallSentiment());
        assertEquals(42, dto.getConfidenceScore());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.ReportSummary dto = new BIReport.ReportSummary();
        dto.setExecutiveSummary("val-executiveSummary");
        dto.setOverallSentiment("val-overallSentiment");
        dto.setConfidenceScore(99);
        assertEquals("val-executiveSummary", dto.getExecutiveSummary());
        assertEquals("val-overallSentiment", dto.getOverallSentiment());
        assertEquals(99, dto.getConfidenceScore());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.ReportSummary dto1 = BIReport.ReportSummary.builder()
                        .executiveSummary("test-executiveSummary")
            .keyFindings(Collections.emptyList())
            .recommendations(Collections.emptyList())
            .risks(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .confidenceScore(42)
            .build();
        BIReport.ReportSummary dto2 = BIReport.ReportSummary.builder()
                        .executiveSummary("test-executiveSummary")
            .keyFindings(Collections.emptyList())
            .recommendations(Collections.emptyList())
            .risks(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .confidenceScore(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.ReportSummary dto = BIReport.ReportSummary.builder()
                        .executiveSummary("test-executiveSummary")
            .keyFindings(Collections.emptyList())
            .recommendations(Collections.emptyList())
            .risks(Collections.emptyList())
            .opportunities(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .confidenceScore(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}