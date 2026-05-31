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
class CountryDashboardController_UpdateDashboardRequestTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardController.UpdateDashboardRequest dto = new CountryDashboardController.UpdateDashboardRequest();
        dto.setCountryName("val-countryName");
        assertEquals("val-countryName", dto.getCountryName());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardController.UpdateDashboardRequest dto1 = new CountryDashboardController.UpdateDashboardRequest();
        CountryDashboardController.UpdateDashboardRequest dto2 = new CountryDashboardController.UpdateDashboardRequest();
        dto1.setCountryName("test");
        dto1.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        dto2.setCountryName("test");
        dto2.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCountryName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardController.UpdateDashboardRequest dto = new CountryDashboardController.UpdateDashboardRequest();
        dto.setCountryName("test");
        dto.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardController.UpdateDashboardRequest dto = new CountryDashboardController.UpdateDashboardRequest();
        dto.setCountryName("test");
        dto.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}