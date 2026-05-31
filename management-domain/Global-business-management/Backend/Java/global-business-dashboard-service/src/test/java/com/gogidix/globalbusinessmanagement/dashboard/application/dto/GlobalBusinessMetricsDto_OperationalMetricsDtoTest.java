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
class GlobalBusinessMetricsDto_OperationalMetricsDtoTest {

        @Test
    void testBuilder() {
        GlobalBusinessMetricsDto.OperationalMetricsDto dto = GlobalBusinessMetricsDto.OperationalMetricsDto.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .averageFulfillmentTime(BigDecimal.TEN)
            .activeProducts(42L)
            .discontinuedProducts(42L)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getInventoryTurnover());
        assertEquals(BigDecimal.TEN, dto.getOrderFulfillmentRate());
        assertEquals(BigDecimal.TEN, dto.getReturnRate());
        assertEquals(BigDecimal.TEN, dto.getAverageFulfillmentTime());
        assertEquals(42L, dto.getActiveProducts());
        assertEquals(42L, dto.getDiscontinuedProducts());
    }

    @Test
    void testSettersAndGetters() {
        GlobalBusinessMetricsDto.OperationalMetricsDto dto = new GlobalBusinessMetricsDto.OperationalMetricsDto();
        dto.setInventoryTurnover(BigDecimal.ONE);
        dto.setOrderFulfillmentRate(BigDecimal.ONE);
        dto.setReturnRate(BigDecimal.ONE);
        dto.setAverageFulfillmentTime(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getInventoryTurnover());
        assertEquals(BigDecimal.ONE, dto.getOrderFulfillmentRate());
        assertEquals(BigDecimal.ONE, dto.getReturnRate());
        assertEquals(BigDecimal.ONE, dto.getAverageFulfillmentTime());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalBusinessMetricsDto.OperationalMetricsDto dto1 = GlobalBusinessMetricsDto.OperationalMetricsDto.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .averageFulfillmentTime(BigDecimal.TEN)
            .activeProducts(42L)
            .discontinuedProducts(42L)
            .build();
        GlobalBusinessMetricsDto.OperationalMetricsDto dto2 = GlobalBusinessMetricsDto.OperationalMetricsDto.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .averageFulfillmentTime(BigDecimal.TEN)
            .activeProducts(42L)
            .discontinuedProducts(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalBusinessMetricsDto.OperationalMetricsDto dto = GlobalBusinessMetricsDto.OperationalMetricsDto.builder()
                        .inventoryTurnover(BigDecimal.TEN)
            .orderFulfillmentRate(BigDecimal.TEN)
            .returnRate(BigDecimal.TEN)
            .averageFulfillmentTime(BigDecimal.TEN)
            .activeProducts(42L)
            .discontinuedProducts(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}