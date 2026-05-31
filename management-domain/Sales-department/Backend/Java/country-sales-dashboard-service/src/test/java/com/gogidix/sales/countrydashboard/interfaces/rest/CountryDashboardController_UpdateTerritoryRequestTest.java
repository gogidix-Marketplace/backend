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
class CountryDashboardController_UpdateTerritoryRequestTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardController.UpdateTerritoryRequest dto = new CountryDashboardController.UpdateTerritoryRequest();
        dto.setTerritoryName("val-territoryName");
        dto.setTerritoryCode("val-territoryCode");
        dto.setRevenue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setQuota(BigDecimal.ONE);
        dto.setDeals(99);
        dto.setWonDeals(99);
        dto.setGrowthRate(BigDecimal.ONE);
        assertEquals("val-territoryName", dto.getTerritoryName());
        assertEquals("val-territoryCode", dto.getTerritoryCode());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getQuota());
        assertEquals(99, dto.getDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardController.UpdateTerritoryRequest dto1 = new CountryDashboardController.UpdateTerritoryRequest();
        CountryDashboardController.UpdateTerritoryRequest dto2 = new CountryDashboardController.UpdateTerritoryRequest();
        dto1.setTerritoryName("test");
        dto1.setTerritoryCode("test");
        dto1.setRevenue(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setQuota(BigDecimal.TEN);
        dto1.setDeals(42);
        dto1.setWonDeals(42);
        dto1.setGrowthRate(BigDecimal.TEN);
        dto1.setAttributes(Collections.emptyMap());
        dto2.setTerritoryName("test");
        dto2.setTerritoryCode("test");
        dto2.setRevenue(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setQuota(BigDecimal.TEN);
        dto2.setDeals(42);
        dto2.setWonDeals(42);
        dto2.setGrowthRate(BigDecimal.TEN);
        dto2.setAttributes(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTerritoryName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardController.UpdateTerritoryRequest dto = new CountryDashboardController.UpdateTerritoryRequest();
        dto.setTerritoryName("test");
        dto.setTerritoryCode("test");
        dto.setRevenue(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setQuota(BigDecimal.TEN);
        dto.setDeals(42);
        dto.setWonDeals(42);
        dto.setGrowthRate(BigDecimal.TEN);
        dto.setAttributes(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardController.UpdateTerritoryRequest dto = new CountryDashboardController.UpdateTerritoryRequest();
        dto.setTerritoryName("test");
        dto.setTerritoryCode("test");
        dto.setRevenue(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setQuota(BigDecimal.TEN);
        dto.setDeals(42);
        dto.setWonDeals(42);
        dto.setGrowthRate(BigDecimal.TEN);
        dto.setAttributes(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}