package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary;
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
class RegionalSummary_ProductPerformanceTest {

        @Test
    void testBuilder() {
        RegionalSummary.ProductPerformance dto = RegionalSummary.ProductPerformance.builder()
                        .productCode("test-productCode")
            .productName("test-productName")
            .category("test-category")
            .revenue(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-productCode", dto.getProductCode());
        assertEquals("test-productName", dto.getProductName());
        assertEquals("test-category", dto.getCategory());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(42L, dto.getUnitsSold());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getMarketShare());
    }

    @Test
    void testSettersAndGetters() {
        RegionalSummary.ProductPerformance dto = new RegionalSummary.ProductPerformance();
        dto.setProductCode("val-productCode");
        dto.setProductName("val-productName");
        dto.setCategory("val-category");
        dto.setRevenue(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setMarketShare(BigDecimal.ONE);
        assertEquals("val-productCode", dto.getProductCode());
        assertEquals("val-productName", dto.getProductName());
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getMarketShare());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalSummary.ProductPerformance dto1 = RegionalSummary.ProductPerformance.builder()
                        .productCode("test-productCode")
            .productName("test-productName")
            .category("test-category")
            .revenue(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        RegionalSummary.ProductPerformance dto2 = RegionalSummary.ProductPerformance.builder()
                        .productCode("test-productCode")
            .productName("test-productName")
            .category("test-category")
            .revenue(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalSummary.ProductPerformance dto = RegionalSummary.ProductPerformance.builder()
                        .productCode("test-productCode")
            .productName("test-productName")
            .category("test-category")
            .revenue(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}