package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.AggregatedMetrics;
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
class AggregatedMetrics_CustomerMetricsTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.CustomerMetrics dto = AggregatedMetrics.CustomerMetrics.builder()
                        .totalCustomers(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .customerAcquisitionRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .averageRevenuePerUser(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .supportTickets(42)
            .avgResolutionTime(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalCustomers());
        assertEquals(42L, dto.getActiveCustomers());
        assertEquals(42L, dto.getNewCustomers());
        assertEquals(42L, dto.getChurnedCustomers());
        assertEquals(42L, dto.getReturningCustomers());
        assertEquals(BigDecimal.TEN, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.TEN, dto.getCustomerAcquisitionRate());
        assertEquals(BigDecimal.TEN, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.TEN, dto.getCustomerLifetimeValue());
        assertEquals(BigDecimal.TEN, dto.getCustomerAcquisitionCost());
        assertEquals(BigDecimal.TEN, dto.getAverageRevenuePerUser());
        assertEquals(BigDecimal.TEN, dto.getCustomerSatisfactionScore());
        assertEquals(BigDecimal.TEN, dto.getNetPromoterScore());
        assertEquals(42, dto.getSupportTickets());
        assertEquals(BigDecimal.TEN, dto.getAvgResolutionTime());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.CustomerMetrics dto = new AggregatedMetrics.CustomerMetrics();
        dto.setCustomerRetentionRate(BigDecimal.ONE);
        dto.setCustomerAcquisitionRate(BigDecimal.ONE);
        dto.setCustomerChurnRate(BigDecimal.ONE);
        dto.setCustomerLifetimeValue(BigDecimal.ONE);
        dto.setCustomerAcquisitionCost(BigDecimal.ONE);
        dto.setAverageRevenuePerUser(BigDecimal.ONE);
        dto.setCustomerSatisfactionScore(BigDecimal.ONE);
        dto.setNetPromoterScore(BigDecimal.ONE);
        dto.setSupportTickets(99);
        dto.setAvgResolutionTime(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.ONE, dto.getCustomerAcquisitionRate());
        assertEquals(BigDecimal.ONE, dto.getCustomerChurnRate());
        assertEquals(BigDecimal.ONE, dto.getCustomerLifetimeValue());
        assertEquals(BigDecimal.ONE, dto.getCustomerAcquisitionCost());
        assertEquals(BigDecimal.ONE, dto.getAverageRevenuePerUser());
        assertEquals(BigDecimal.ONE, dto.getCustomerSatisfactionScore());
        assertEquals(BigDecimal.ONE, dto.getNetPromoterScore());
        assertEquals(99, dto.getSupportTickets());
        assertEquals(BigDecimal.ONE, dto.getAvgResolutionTime());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.CustomerMetrics dto1 = AggregatedMetrics.CustomerMetrics.builder()
                        .totalCustomers(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .customerAcquisitionRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .averageRevenuePerUser(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .supportTickets(42)
            .avgResolutionTime(BigDecimal.TEN)
            .build();
        AggregatedMetrics.CustomerMetrics dto2 = AggregatedMetrics.CustomerMetrics.builder()
                        .totalCustomers(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .customerAcquisitionRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .averageRevenuePerUser(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .supportTickets(42)
            .avgResolutionTime(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.CustomerMetrics dto = AggregatedMetrics.CustomerMetrics.builder()
                        .totalCustomers(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .customerAcquisitionRate(BigDecimal.TEN)
            .customerChurnRate(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .averageRevenuePerUser(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .netPromoterScore(BigDecimal.TEN)
            .supportTickets(42)
            .avgResolutionTime(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}