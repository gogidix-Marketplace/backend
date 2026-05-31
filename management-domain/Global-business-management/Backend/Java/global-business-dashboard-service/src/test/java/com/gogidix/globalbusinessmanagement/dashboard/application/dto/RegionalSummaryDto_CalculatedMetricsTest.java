package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.RegionalSummaryDto;
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
class RegionalSummaryDto_CalculatedMetricsTest {

        @Test
    void testBuilder() {
        RegionalSummaryDto.CalculatedMetrics dto = RegionalSummaryDto.CalculatedMetrics.builder()
                        .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .netGrowthRate(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getRevenuePerCustomer());
        assertEquals(BigDecimal.TEN, dto.getOrdersPerCustomer());
        assertEquals(BigDecimal.TEN, dto.getCustomerGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.TEN, dto.getNetGrowthRate());
    }

    @Test
    void testSettersAndGetters() {
        RegionalSummaryDto.CalculatedMetrics dto = new RegionalSummaryDto.CalculatedMetrics();
        dto.setRevenuePerCustomer(BigDecimal.ONE);
        dto.setOrdersPerCustomer(BigDecimal.ONE);
        dto.setCustomerGrowthRate(BigDecimal.ONE);
        dto.setCustomerChurnRate(BigDecimal.ONE);
        dto.setNetGrowthRate(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getRevenuePerCustomer());
        assertEquals(BigDecimal.ONE, dto.getOrdersPerCustomer());
        assertEquals(BigDecimal.ONE, dto.getCustomerGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.ONE, dto.getNetGrowthRate());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalSummaryDto.CalculatedMetrics dto1 = RegionalSummaryDto.CalculatedMetrics.builder()
                        .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .netGrowthRate(BigDecimal.TEN)
            .build();
        RegionalSummaryDto.CalculatedMetrics dto2 = RegionalSummaryDto.CalculatedMetrics.builder()
                        .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .netGrowthRate(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalSummaryDto.CalculatedMetrics dto = RegionalSummaryDto.CalculatedMetrics.builder()
                        .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .netGrowthRate(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}