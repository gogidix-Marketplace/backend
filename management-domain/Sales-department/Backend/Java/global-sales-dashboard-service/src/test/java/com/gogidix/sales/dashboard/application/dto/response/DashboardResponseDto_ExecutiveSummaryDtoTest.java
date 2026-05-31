package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_ExecutiveSummaryDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.ExecutiveSummaryDto dto = DashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(null)
            .recommendations(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-headline", dto.getHeadline());
        assertEquals("test-keyHighlight", dto.getKeyHighlight());
        assertEquals("test-overallSentiment", dto.getOverallSentiment());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.ExecutiveSummaryDto dto = new DashboardResponseDto.ExecutiveSummaryDto();
        dto.setHeadline("val-headline");
        dto.setKeyHighlight("val-keyHighlight");
        dto.setOverallSentiment("val-overallSentiment");
        assertEquals("val-headline", dto.getHeadline());
        assertEquals("val-keyHighlight", dto.getKeyHighlight());
        assertEquals("val-overallSentiment", dto.getOverallSentiment());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.ExecutiveSummaryDto dto1 = DashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(null)
            .recommendations(Collections.emptyList())
            .build();
        DashboardResponseDto.ExecutiveSummaryDto dto2 = DashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(null)
            .recommendations(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.ExecutiveSummaryDto dto = DashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformers(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(null)
            .recommendations(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}