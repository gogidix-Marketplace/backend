package com.gogidix.sales.countrydashboard.interfaces.rest;

import com.gogidix.sales.countrydashboard.interfaces.rest.CountryDashboardController;
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
class CountryDashboardController_AddTrendDataRequestTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardController.AddTrendDataRequest dto = new CountryDashboardController.AddTrendDataRequest();
        dto.setPeriod("val-period");
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setRevenue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setAverageDealSize(BigDecimal.ONE);
        dto.setNewCustomers(99);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setTerritory("val-territory");
        assertEquals("val-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(99, dto.getDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(BigDecimal.ONE, dto.getAverageDealSize());
        assertEquals(99, dto.getNewCustomers());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals("val-territory", dto.getTerritory());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardController.AddTrendDataRequest dto1 = new CountryDashboardController.AddTrendDataRequest();
        CountryDashboardController.AddTrendDataRequest dto2 = new CountryDashboardController.AddTrendDataRequest();
        dto1.setPeriod("test");
        dto1.setDate(LocalDate.of(2025,1,1));
        dto1.setRevenue(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setDeals(42);
        dto1.setWinRate(BigDecimal.TEN);
        dto1.setAverageDealSize(BigDecimal.TEN);
        dto1.setNewCustomers(42);
        dto1.setGrowthRate(BigDecimal.TEN);
        dto1.setTerritory("test");
        dto2.setPeriod("test");
        dto2.setDate(LocalDate.of(2025,1,1));
        dto2.setRevenue(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setDeals(42);
        dto2.setWinRate(BigDecimal.TEN);
        dto2.setAverageDealSize(BigDecimal.TEN);
        dto2.setNewCustomers(42);
        dto2.setGrowthRate(BigDecimal.TEN);
        dto2.setTerritory("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setPeriod(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardController.AddTrendDataRequest dto = new CountryDashboardController.AddTrendDataRequest();
        dto.setPeriod("test");
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setRevenue(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setDeals(42);
        dto.setWinRate(BigDecimal.TEN);
        dto.setAverageDealSize(BigDecimal.TEN);
        dto.setNewCustomers(42);
        dto.setGrowthRate(BigDecimal.TEN);
        dto.setTerritory("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardController.AddTrendDataRequest dto = new CountryDashboardController.AddTrendDataRequest();
        dto.setPeriod("test");
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setRevenue(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setDeals(42);
        dto.setWinRate(BigDecimal.TEN);
        dto.setAverageDealSize(BigDecimal.TEN);
        dto.setNewCustomers(42);
        dto.setGrowthRate(BigDecimal.TEN);
        dto.setTerritory("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}