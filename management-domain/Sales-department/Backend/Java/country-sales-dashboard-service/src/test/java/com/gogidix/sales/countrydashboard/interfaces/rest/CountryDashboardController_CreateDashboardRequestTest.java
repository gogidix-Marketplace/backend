package com.gogidix.sales.countrydashboard.interfaces.rest;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
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
class CountryDashboardController_CreateDashboardRequestTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardController.CreateDashboardRequest dto = new CountryDashboardController.CreateDashboardRequest();
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setLocalCurrency("val-localCurrency");
        dto.setRefreshIntervalMinutes(99);
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-localCurrency", dto.getLocalCurrency());
        assertEquals(99, dto.getRefreshIntervalMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardController.CreateDashboardRequest dto1 = new CountryDashboardController.CreateDashboardRequest();
        CountryDashboardController.CreateDashboardRequest dto2 = new CountryDashboardController.CreateDashboardRequest();
        dto1.setCountryCode("test");
        dto1.setCountryName("test");
        dto1.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto1.setLocalCurrency("test");
        dto1.setEnabledTerritories(Collections.emptyList());
        dto1.setRefreshIntervalMinutes(42);
        dto2.setCountryCode("test");
        dto2.setCountryName("test");
        dto2.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto2.setLocalCurrency("test");
        dto2.setEnabledTerritories(Collections.emptyList());
        dto2.setRefreshIntervalMinutes(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCountryCode(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardController.CreateDashboardRequest dto = new CountryDashboardController.CreateDashboardRequest();
        dto.setCountryCode("test");
        dto.setCountryName("test");
        dto.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto.setLocalCurrency("test");
        dto.setEnabledTerritories(Collections.emptyList());
        dto.setRefreshIntervalMinutes(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardController.CreateDashboardRequest dto = new CountryDashboardController.CreateDashboardRequest();
        dto.setCountryCode("test");
        dto.setCountryName("test");
        dto.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto.setLocalCurrency("test");
        dto.setEnabledTerritories(Collections.emptyList());
        dto.setRefreshIntervalMinutes(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}