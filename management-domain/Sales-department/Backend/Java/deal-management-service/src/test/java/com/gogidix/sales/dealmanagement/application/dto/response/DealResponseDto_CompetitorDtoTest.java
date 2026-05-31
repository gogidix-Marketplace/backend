package com.gogidix.sales.dealmanagement.application.dto.response;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
import com.gogidix.sales.dealmanagement.domain.model.Competitor;
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
class DealResponseDto_CompetitorDtoTest {

        @Test
    void testBuilder() {
        DealResponseDto.CompetitorDto dto = DealResponseDto.CompetitorDto.builder()
                        .competitorId("test-competitorId")
            .competitorName("test-competitorName")
            .strength(Competitor.StrengthLevel.VERY_WEAK)
            .threat(Competitor.ThreatLevel.VERY_LOW)
            .probabilityOfWin(42)
            .competingProduct("test-competingProduct")
            .build();
        assertNotNull(dto);
        assertEquals("test-competitorId", dto.getCompetitorId());
        assertEquals("test-competitorName", dto.getCompetitorName());
        assertEquals(42, dto.getProbabilityOfWin());
        assertEquals("test-competingProduct", dto.getCompetingProduct());
    }

    @Test
    void testSettersAndGetters() {
        DealResponseDto.CompetitorDto dto = new DealResponseDto.CompetitorDto();
        dto.setCompetitorId("val-competitorId");
        dto.setCompetitorName("val-competitorName");
        dto.setProbabilityOfWin(99);
        dto.setCompetingProduct("val-competingProduct");
        assertEquals("val-competitorId", dto.getCompetitorId());
        assertEquals("val-competitorName", dto.getCompetitorName());
        assertEquals(99, dto.getProbabilityOfWin());
        assertEquals("val-competingProduct", dto.getCompetingProduct());
    }

    @Test
    void testEqualsAndHashCode() {
        DealResponseDto.CompetitorDto dto1 = DealResponseDto.CompetitorDto.builder()
                        .competitorId("test-competitorId")
            .competitorName("test-competitorName")
            .strength(Competitor.StrengthLevel.VERY_WEAK)
            .threat(Competitor.ThreatLevel.VERY_LOW)
            .probabilityOfWin(42)
            .competingProduct("test-competingProduct")
            .build();
        DealResponseDto.CompetitorDto dto2 = DealResponseDto.CompetitorDto.builder()
                        .competitorId("test-competitorId")
            .competitorName("test-competitorName")
            .strength(Competitor.StrengthLevel.VERY_WEAK)
            .threat(Competitor.ThreatLevel.VERY_LOW)
            .probabilityOfWin(42)
            .competingProduct("test-competingProduct")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealResponseDto.CompetitorDto dto = DealResponseDto.CompetitorDto.builder()
                        .competitorId("test-competitorId")
            .competitorName("test-competitorName")
            .strength(Competitor.StrengthLevel.VERY_WEAK)
            .threat(Competitor.ThreatLevel.VERY_LOW)
            .probabilityOfWin(42)
            .competingProduct("test-competingProduct")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}