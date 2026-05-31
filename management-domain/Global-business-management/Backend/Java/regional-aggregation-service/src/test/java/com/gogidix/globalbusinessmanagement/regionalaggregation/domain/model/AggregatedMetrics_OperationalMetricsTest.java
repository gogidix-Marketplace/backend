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
class AggregatedMetrics_OperationalMetricsTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.OperationalMetrics dto = AggregatedMetrics.OperationalMetrics.builder()
                        .totalOrders(42L)
            .ordersPerCustomer(42L)
            .averageOrderValue(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .inventoryTurnover(42L)
            .fulfillmentCostPerOrder(BigDecimal.TEN)
            .totalTransactions(42L)
            .transactionSuccessRate(BigDecimal.TEN)
            .systemUptime(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalOrders());
        assertEquals(42L, dto.getOrdersPerCustomer());
        assertEquals(BigDecimal.TEN, dto.getAverageOrderValue());
        assertEquals(BigDecimal.TEN, dto.getOrderFulfillmentRate());
        assertEquals(BigDecimal.TEN, dto.getOnTimeDeliveryRate());
        assertEquals(BigDecimal.TEN, dto.getReturnRate());
        assertEquals(BigDecimal.TEN, dto.getRefundRate());
        assertEquals(42L, dto.getInventoryTurnover());
        assertEquals(BigDecimal.TEN, dto.getFulfillmentCostPerOrder());
        assertEquals(42L, dto.getTotalTransactions());
        assertEquals(BigDecimal.TEN, dto.getTransactionSuccessRate());
        assertEquals(BigDecimal.TEN, dto.getSystemUptime());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.OperationalMetrics dto = new AggregatedMetrics.OperationalMetrics();
        dto.setAverageOrderValue(BigDecimal.ONE);
        dto.setOrderFulfillmentRate(BigDecimal.ONE);
        dto.setOnTimeDeliveryRate(BigDecimal.ONE);
        dto.setReturnRate(BigDecimal.ONE);
        dto.setRefundRate(BigDecimal.ONE);
        dto.setFulfillmentCostPerOrder(BigDecimal.ONE);
        dto.setTransactionSuccessRate(BigDecimal.ONE);
        dto.setSystemUptime(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getAverageOrderValue());
        assertEquals(BigDecimal.ONE, dto.getOrderFulfillmentRate());
        assertEquals(BigDecimal.ONE, dto.getOnTimeDeliveryRate());
        assertEquals(BigDecimal.ONE, dto.getReturnRate());
        assertEquals(BigDecimal.ONE, dto.getRefundRate());
        assertEquals(BigDecimal.ONE, dto.getFulfillmentCostPerOrder());
        assertEquals(BigDecimal.ONE, dto.getTransactionSuccessRate());
        assertEquals(BigDecimal.ONE, dto.getSystemUptime());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.OperationalMetrics dto1 = AggregatedMetrics.OperationalMetrics.builder()
                        .totalOrders(42L)
            .ordersPerCustomer(42L)
            .averageOrderValue(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .inventoryTurnover(42L)
            .fulfillmentCostPerOrder(BigDecimal.TEN)
            .totalTransactions(42L)
            .transactionSuccessRate(BigDecimal.TEN)
            .systemUptime(BigDecimal.TEN)
            .build();
        AggregatedMetrics.OperationalMetrics dto2 = AggregatedMetrics.OperationalMetrics.builder()
                        .totalOrders(42L)
            .ordersPerCustomer(42L)
            .averageOrderValue(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .inventoryTurnover(42L)
            .fulfillmentCostPerOrder(BigDecimal.TEN)
            .totalTransactions(42L)
            .transactionSuccessRate(BigDecimal.TEN)
            .systemUptime(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.OperationalMetrics dto = AggregatedMetrics.OperationalMetrics.builder()
                        .totalOrders(42L)
            .ordersPerCustomer(42L)
            .averageOrderValue(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .inventoryTurnover(42L)
            .fulfillmentCostPerOrder(BigDecimal.TEN)
            .totalTransactions(42L)
            .transactionSuccessRate(BigDecimal.TEN)
            .systemUptime(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}