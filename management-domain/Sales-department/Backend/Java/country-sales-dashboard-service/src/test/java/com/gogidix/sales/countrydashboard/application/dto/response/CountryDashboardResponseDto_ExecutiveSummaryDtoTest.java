package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.CountryDashboardResponseDto;
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
class CountryDashboardResponseDto_ExecutiveSummaryDtoTest {

        @Test
    void testBuilder() {
        CountryDashboardResponseDto.ExecutiveSummaryDto dto = CountryDashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformingTerritories(Collections.emptyList())
            .underperformingTerritories(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .primaryFocusArea("test-primaryFocusArea")
            .build();
        assertNotNull(dto);
        assertEquals("test-headline", dto.getHeadline());
        assertEquals("test-keyHighlight", dto.getKeyHighlight());
        assertEquals("test-overallSentiment", dto.getOverallSentiment());
        assertEquals(BigDecimal.TEN, dto.getRiskScore());
        assertEquals("test-primaryFocusArea", dto.getPrimaryFocusArea());
    }

    @Test
    void testSettersAndGetters() {
        CountryDashboardResponseDto.ExecutiveSummaryDto dto = new CountryDashboardResponseDto.ExecutiveSummaryDto();
        dto.setHeadline("val-headline");
        dto.setKeyHighlight("val-keyHighlight");
        dto.setOverallSentiment("val-overallSentiment");
        dto.setRiskScore(BigDecimal.ONE);
        dto.setPrimaryFocusArea("val-primaryFocusArea");
        assertEquals("val-headline", dto.getHeadline());
        assertEquals("val-keyHighlight", dto.getKeyHighlight());
        assertEquals("val-overallSentiment", dto.getOverallSentiment());
        assertEquals(BigDecimal.ONE, dto.getRiskScore());
        assertEquals("val-primaryFocusArea", dto.getPrimaryFocusArea());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardResponseDto.ExecutiveSummaryDto dto1 = CountryDashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformingTerritories(Collections.emptyList())
            .underperformingTerritories(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .primaryFocusArea("test-primaryFocusArea")
            .build();
        CountryDashboardResponseDto.ExecutiveSummaryDto dto2 = CountryDashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformingTerritories(Collections.emptyList())
            .underperformingTerritories(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .primaryFocusArea("test-primaryFocusArea")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDashboardResponseDto.ExecutiveSummaryDto dto = CountryDashboardResponseDto.ExecutiveSummaryDto.builder()
                        .headline("test-headline")
            .keyHighlight("test-keyHighlight")
            .topPerformingTerritories(Collections.emptyList())
            .underperformingTerritories(Collections.emptyList())
            .overallSentiment("test-overallSentiment")
            .riskScore(BigDecimal.TEN)
            .primaryFocusArea("test-primaryFocusArea")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}