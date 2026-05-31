package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.GlobalBusinessMetricsDto;
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
class GlobalBusinessMetricsDto_CalculatedMetricsTest {

        @Test
    void testBuilder() {
        GlobalBusinessMetricsDto.CalculatedMetrics dto = GlobalBusinessMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .netCustomerGrowthRate(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCustomerGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.TEN, dto.getRevenuePerCustomer());
        assertEquals(BigDecimal.TEN, dto.getOrdersPerCustomer());
        assertEquals(BigDecimal.TEN, dto.getNetCustomerGrowthRate());
    }

    @Test
    void testSettersAndGetters() {
        GlobalBusinessMetricsDto.CalculatedMetrics dto = new GlobalBusinessMetricsDto.CalculatedMetrics();
        dto.setCustomerGrowthRate(BigDecimal.ONE);
        dto.setCustomerChurnRate(BigDecimal.ONE);
        dto.setRevenuePerCustomer(BigDecimal.ONE);
        dto.setOrdersPerCustomer(BigDecimal.ONE);
        dto.setNetCustomerGrowthRate(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getCustomerGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.ONE, dto.getRevenuePerCustomer());
        assertEquals(BigDecimal.ONE, dto.getOrdersPerCustomer());
        assertEquals(BigDecimal.ONE, dto.getNetCustomerGrowthRate());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalBusinessMetricsDto.CalculatedMetrics dto1 = GlobalBusinessMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .netCustomerGrowthRate(BigDecimal.TEN)
            .build();
        GlobalBusinessMetricsDto.CalculatedMetrics dto2 = GlobalBusinessMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .netCustomerGrowthRate(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalBusinessMetricsDto.CalculatedMetrics dto = GlobalBusinessMetricsDto.CalculatedMetrics.builder()
                        .customerGrowthRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .ordersPerCustomer(BigDecimal.TEN)
            .netCustomerGrowthRate(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}