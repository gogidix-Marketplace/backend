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
class CountryMetrics_SalesChannelMetricsTest {

        @Test
    void testBuilder() {
        CountryMetrics.SalesChannelMetrics dto = CountryMetrics.SalesChannelMetrics.builder()
                        .onlineRevenue(BigDecimal.TEN)
            .offlineRevenue(BigDecimal.TEN)
            .marketplaceRevenue(BigDecimal.TEN)
            .b2bRevenue(BigDecimal.TEN)
            .onlineContribution(BigDecimal.TEN)
            .offlineContribution(BigDecimal.TEN)
            .onlineOrders(42L)
            .offlineOrders(42L)
            .marketplaceOrders(42L)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getOnlineRevenue());
        assertEquals(BigDecimal.TEN, dto.getOfflineRevenue());
        assertEquals(BigDecimal.TEN, dto.getMarketplaceRevenue());
        assertEquals(BigDecimal.TEN, dto.getB2bRevenue());
        assertEquals(BigDecimal.TEN, dto.getOnlineContribution());
        assertEquals(BigDecimal.TEN, dto.getOfflineContribution());
        assertEquals(42L, dto.getOnlineOrders());
        assertEquals(42L, dto.getOfflineOrders());
        assertEquals(42L, dto.getMarketplaceOrders());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetrics.SalesChannelMetrics dto = new CountryMetrics.SalesChannelMetrics();
        dto.setOnlineRevenue(BigDecimal.ONE);
        dto.setOfflineRevenue(BigDecimal.ONE);
        dto.setMarketplaceRevenue(BigDecimal.ONE);
        dto.setB2bRevenue(BigDecimal.ONE);
        dto.setOnlineContribution(BigDecimal.ONE);
        dto.setOfflineContribution(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getOnlineRevenue());
        assertEquals(BigDecimal.ONE, dto.getOfflineRevenue());
        assertEquals(BigDecimal.ONE, dto.getMarketplaceRevenue());
        assertEquals(BigDecimal.ONE, dto.getB2bRevenue());
        assertEquals(BigDecimal.ONE, dto.getOnlineContribution());
        assertEquals(BigDecimal.ONE, dto.getOfflineContribution());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetrics.SalesChannelMetrics dto1 = CountryMetrics.SalesChannelMetrics.builder()
                        .onlineRevenue(BigDecimal.TEN)
            .offlineRevenue(BigDecimal.TEN)
            .marketplaceRevenue(BigDecimal.TEN)
            .b2bRevenue(BigDecimal.TEN)
            .onlineContribution(BigDecimal.TEN)
            .offlineContribution(BigDecimal.TEN)
            .onlineOrders(42L)
            .offlineOrders(42L)
            .marketplaceOrders(42L)
            .build();
        CountryMetrics.SalesChannelMetrics dto2 = CountryMetrics.SalesChannelMetrics.builder()
                        .onlineRevenue(BigDecimal.TEN)
            .offlineRevenue(BigDecimal.TEN)
            .marketplaceRevenue(BigDecimal.TEN)
            .b2bRevenue(BigDecimal.TEN)
            .onlineContribution(BigDecimal.TEN)
            .offlineContribution(BigDecimal.TEN)
            .onlineOrders(42L)
            .offlineOrders(42L)
            .marketplaceOrders(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetrics.SalesChannelMetrics dto = CountryMetrics.SalesChannelMetrics.builder()
                        .onlineRevenue(BigDecimal.TEN)
            .offlineRevenue(BigDecimal.TEN)
            .marketplaceRevenue(BigDecimal.TEN)
            .b2bRevenue(BigDecimal.TEN)
            .onlineContribution(BigDecimal.TEN)
            .offlineContribution(BigDecimal.TEN)
            .onlineOrders(42L)
            .offlineOrders(42L)
            .marketplaceOrders(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}