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
class BIReport_InsightReferenceTest {

        @Test
    void testBuilder() {
        BIReport.InsightReference dto = BIReport.InsightReference.builder()
                        .insightId("test-insightId")
            .title("test-title")
            .summary("test-summary")
            .relevance("test-relevance")
            .build();
        assertNotNull(dto);
        assertEquals("test-insightId", dto.getInsightId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-summary", dto.getSummary());
        assertEquals("test-relevance", dto.getRelevance());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.InsightReference dto = new BIReport.InsightReference();
        dto.setInsightId("val-insightId");
        dto.setTitle("val-title");
        dto.setSummary("val-summary");
        dto.setRelevance("val-relevance");
        assertEquals("val-insightId", dto.getInsightId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-relevance", dto.getRelevance());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.InsightReference dto1 = BIReport.InsightReference.builder()
                        .insightId("test-insightId")
            .title("test-title")
            .summary("test-summary")
            .relevance("test-relevance")
            .build();
        BIReport.InsightReference dto2 = BIReport.InsightReference.builder()
                        .insightId("test-insightId")
            .title("test-title")
            .summary("test-summary")
            .relevance("test-relevance")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.InsightReference dto = BIReport.InsightReference.builder()
                        .insightId("test-insightId")
            .title("test-title")
            .summary("test-summary")
            .relevance("test-relevance")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}