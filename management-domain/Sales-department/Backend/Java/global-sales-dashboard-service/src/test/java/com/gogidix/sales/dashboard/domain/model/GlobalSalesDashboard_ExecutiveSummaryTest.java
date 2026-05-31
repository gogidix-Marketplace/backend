package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
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
class GlobalSalesDashboard_ExecutiveSummaryTest {

        @Test
    void testBuilder() {
        GlobalSalesDashboard.ExecutiveSummary dto = GlobalSalesDashboard.ExecutiveSummary.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .recommendations(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-headline", dto.getHeadline());
        assertEquals("test-keyHighlight", dto.getKeyHighlight());
        assertEquals("test-overallSentiment", dto.getOverallSentiment());
        assertEquals(BigDecimal.TEN, dto.getRiskScore());
    }

    @Test
    void testSettersAndGetters() {
        GlobalSalesDashboard.ExecutiveSummary dto = new GlobalSalesDashboard.ExecutiveSummary();
        dto.setHeadline("val-headline");
        dto.setKeyHighlight("val-keyHighlight");
        dto.setOverallSentiment("val-overallSentiment");
        dto.setRiskScore(BigDecimal.ONE);
        assertEquals("val-headline", dto.getHeadline());
        assertEquals("val-keyHighlight", dto.getKeyHighlight());
        assertEquals("val-overallSentiment", dto.getOverallSentiment());
        assertEquals(BigDecimal.ONE, dto.getRiskScore());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalSalesDashboard.ExecutiveSummary dto1 = GlobalSalesDashboard.ExecutiveSummary.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .recommendations(Collections.emptyList())
            .build();
        GlobalSalesDashboard.ExecutiveSummary dto2 = GlobalSalesDashboard.ExecutiveSummary.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .recommendations(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalSalesDashboard.ExecutiveSummary dto = GlobalSalesDashboard.ExecutiveSummary.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .recommendations(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}