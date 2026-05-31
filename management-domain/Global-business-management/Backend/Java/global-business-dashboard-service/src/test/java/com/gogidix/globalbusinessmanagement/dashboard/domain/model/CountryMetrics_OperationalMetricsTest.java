package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics;
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
class CountryMetrics_OperationalMetricsTest {

        @Test
    void testBuilder() {
        CountryMetrics.OperationalMetrics dto = CountryMetrics.OperationalMetrics.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .fulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .averageFulfillmentTime(42L)
            .averageResponseTime(42L)
            .firstContactResolution(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getInventoryTurnover());
        assertEquals(BigDecimal.TEN, dto.getFulfillmentRate());
        assertEquals(BigDecimal.TEN, dto.getOnTimeDeliveryRate());
        assertEquals(BigDecimal.TEN, dto.getReturnRate());
        assertEquals(BigDecimal.TEN, dto.getRefundRate());
        assertEquals(42L, dto.getAverageFulfillmentTime());
        assertEquals(42L, dto.getAverageResponseTime());
        assertEquals(BigDecimal.TEN, dto.getFirstContactResolution());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetrics.OperationalMetrics dto = new CountryMetrics.OperationalMetrics();
        dto.setInventoryTurnover(BigDecimal.ONE);
        dto.setFulfillmentRate(BigDecimal.ONE);
        dto.setOnTimeDeliveryRate(BigDecimal.ONE);
        dto.setReturnRate(BigDecimal.ONE);
        dto.setRefundRate(BigDecimal.ONE);
        dto.setFirstContactResolution(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getInventoryTurnover());
        assertEquals(BigDecimal.ONE, dto.getFulfillmentRate());
        assertEquals(BigDecimal.ONE, dto.getOnTimeDeliveryRate());
        assertEquals(BigDecimal.ONE, dto.getReturnRate());
        assertEquals(BigDecimal.ONE, dto.getRefundRate());
        assertEquals(BigDecimal.ONE, dto.getFirstContactResolution());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetrics.OperationalMetrics dto1 = CountryMetrics.OperationalMetrics.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .fulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .averageFulfillmentTime(42L)
            .averageResponseTime(42L)
            .firstContactResolution(BigDecimal.TEN)
            .build();
        CountryMetrics.OperationalMetrics dto2 = CountryMetrics.OperationalMetrics.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .fulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .averageFulfillmentTime(42L)
            .averageResponseTime(42L)
            .firstContactResolution(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetrics.OperationalMetrics dto = CountryMetrics.OperationalMetrics.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .fulfillmentRate(BigDecimal.TEN)
            .onTimeDeliveryRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .refundRate(BigDecimal.TEN)
            .averageFulfillmentTime(42L)
            .averageResponseTime(42L)
            .firstContactResolution(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}