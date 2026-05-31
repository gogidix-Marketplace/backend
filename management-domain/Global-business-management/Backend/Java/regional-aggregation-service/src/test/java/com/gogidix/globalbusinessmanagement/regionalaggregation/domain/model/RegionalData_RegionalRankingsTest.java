package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.RegionalData;
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
class RegionalData_RegionalRankingsTest {

        @Test
    void testBuilder() {
        RegionalData.RegionalRankings dto = RegionalData.RegionalRankings.builder()
                        .revenueRank(42)
            .growthRank(42)
            .profitabilityRank(42)
            .customerSatisfactionRank(42)
            .marketShareRank(42)
            .overallRank(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRevenueRank());
        assertEquals(42, dto.getGrowthRank());
        assertEquals(42, dto.getProfitabilityRank());
        assertEquals(42, dto.getCustomerSatisfactionRank());
        assertEquals(42, dto.getMarketShareRank());
        assertEquals(42, dto.getOverallRank());
    }

    @Test
    void testSettersAndGetters() {
        RegionalData.RegionalRankings dto = new RegionalData.RegionalRankings();
        dto.setRevenueRank(99);
        dto.setGrowthRank(99);
        dto.setProfitabilityRank(99);
        dto.setCustomerSatisfactionRank(99);
        dto.setMarketShareRank(99);
        dto.setOverallRank(99);
        assertEquals(99, dto.getRevenueRank());
        assertEquals(99, dto.getGrowthRank());
        assertEquals(99, dto.getProfitabilityRank());
        assertEquals(99, dto.getCustomerSatisfactionRank());
        assertEquals(99, dto.getMarketShareRank());
        assertEquals(99, dto.getOverallRank());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalData.RegionalRankings dto1 = RegionalData.RegionalRankings.builder()
                        .revenueRank(42)
            .growthRank(42)
            .profitabilityRank(42)
            .customerSatisfactionRank(42)
            .marketShareRank(42)
            .overallRank(42)
            .build();
        RegionalData.RegionalRankings dto2 = RegionalData.RegionalRankings.builder()
                        .revenueRank(42)
            .growthRank(42)
            .profitabilityRank(42)
            .customerSatisfactionRank(42)
            .marketShareRank(42)
            .overallRank(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalData.RegionalRankings dto = RegionalData.RegionalRankings.builder()
                        .revenueRank(42)
            .growthRank(42)
            .profitabilityRank(42)
            .customerSatisfactionRank(42)
            .marketShareRank(42)
            .overallRank(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}