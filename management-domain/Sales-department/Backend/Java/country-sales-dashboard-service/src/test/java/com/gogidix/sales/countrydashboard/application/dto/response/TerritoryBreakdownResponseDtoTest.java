package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.TerritoryBreakdownResponseDto;
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
class TerritoryBreakdownResponseDtoTest {

        @Test
    void testBuilder() {
        TerritoryBreakdownResponseDto dto = TerritoryBreakdownResponseDto.builder()
                        .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .territoryCode("test-territoryCode")
            .revenue(BigDecimal.TEN)
            .currency("test-currency")
            .quota(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .rank(42)
            .previousRank(42)
            .growthRate(BigDecimal.TEN)
            .topPerformer("test-topPerformer")
            .performanceIndicator("test-performanceIndicator")
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .attributes(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-territoryId", dto.getTerritoryId());
        assertEquals("test-territoryName", dto.getTerritoryName());
        assertEquals("test-territoryCode", dto.getTerritoryCode());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getQuota());
        assertEquals(BigDecimal.TEN, dto.getAchievementPercentage());
        assertEquals(42, dto.getDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(BigDecimal.TEN, dto.getAverageDealSize());
        assertEquals(42, dto.getRank());
        assertEquals(42, dto.getPreviousRank());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals("test-topPerformer", dto.getTopPerformer());
        assertEquals("test-performanceIndicator", dto.getPerformanceIndicator());
    }

    @Test
    void testSettersAndGetters() {
        TerritoryBreakdownResponseDto dto = new TerritoryBreakdownResponseDto();
        dto.setTerritoryId("val-territoryId");
        dto.setTerritoryName("val-territoryName");
        dto.setTerritoryCode("val-territoryCode");
        dto.setRevenue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setQuota(BigDecimal.ONE);
        dto.setAchievementPercentage(BigDecimal.ONE);
        dto.setDeals(99);
        dto.setWonDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setAverageDealSize(BigDecimal.ONE);
        dto.setRank(99);
        dto.setPreviousRank(99);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setTopPerformer("val-topPerformer");
        dto.setPerformanceIndicator("val-performanceIndicator");
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-territoryName", dto.getTerritoryName());
        assertEquals("val-territoryCode", dto.getTerritoryCode());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getQuota());
        assertEquals(BigDecimal.ONE, dto.getAchievementPercentage());
        assertEquals(99, dto.getDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(BigDecimal.ONE, dto.getAverageDealSize());
        assertEquals(99, dto.getRank());
        assertEquals(99, dto.getPreviousRank());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals("val-topPerformer", dto.getTopPerformer());
        assertEquals("val-performanceIndicator", dto.getPerformanceIndicator());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryBreakdownResponseDto dto1 = TerritoryBreakdownResponseDto.builder()
                        .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .territoryCode("test-territoryCode")
            .revenue(BigDecimal.TEN)
            .currency("test-currency")
            .quota(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .rank(42)
            .previousRank(42)
            .growthRate(BigDecimal.TEN)
            .topPerformer("test-topPerformer")
            .performanceIndicator("test-performanceIndicator")
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .attributes(Collections.emptyMap())
            .build();
        TerritoryBreakdownResponseDto dto2 = TerritoryBreakdownResponseDto.builder()
                        .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .territoryCode("test-territoryCode")
            .revenue(BigDecimal.TEN)
            .currency("test-currency")
            .quota(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .rank(42)
            .previousRank(42)
            .growthRate(BigDecimal.TEN)
            .topPerformer("test-topPerformer")
            .performanceIndicator("test-performanceIndicator")
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .attributes(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryBreakdownResponseDto dto = TerritoryBreakdownResponseDto.builder()
                        .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .territoryCode("test-territoryCode")
            .revenue(BigDecimal.TEN)
            .currency("test-currency")
            .quota(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .deals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .rank(42)
            .previousRank(42)
            .growthRate(BigDecimal.TEN)
            .topPerformer("test-topPerformer")
            .performanceIndicator("test-performanceIndicator")
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .attributes(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}