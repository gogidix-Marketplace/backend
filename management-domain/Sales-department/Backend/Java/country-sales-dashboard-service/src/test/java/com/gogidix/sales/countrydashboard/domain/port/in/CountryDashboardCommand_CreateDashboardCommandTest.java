package com.gogidix.sales.countrydashboard.domain.port.in;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
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
class CountryDashboardCommand_CreateDashboardCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.CreateDashboardCommand dto = new CountryDashboardCommand.CreateDashboardCommand();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setLocalCurrency("val-localCurrency");
        dto.setRefreshIntervalMinutes(99);
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-localCurrency", dto.getLocalCurrency());
        assertEquals(99, dto.getRefreshIntervalMinutes());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.CreateDashboardCommand dto1 = new CountryDashboardCommand.CreateDashboardCommand();
        CountryDashboardCommand.CreateDashboardCommand dto2 = new CountryDashboardCommand.CreateDashboardCommand();
        dto1.setTenantId("test");
        dto1.setCountryCode("test");
        dto1.setCountryName("test");
        dto1.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto1.setLocalCurrency("test");
        dto1.setEnabledTerritories(Collections.emptyList());
        dto1.setRefreshIntervalMinutes(42);
        dto1.setCreatedBy("test");
        dto2.setTenantId("test");
        dto2.setCountryCode("test");
        dto2.setCountryName("test");
        dto2.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto2.setLocalCurrency("test");
        dto2.setEnabledTerritories(Collections.emptyList());
        dto2.setRefreshIntervalMinutes(42);
        dto2.setCreatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.CreateDashboardCommand dto = new CountryDashboardCommand.CreateDashboardCommand();
        dto.setTenantId("test");
        dto.setCountryCode("test");
        dto.setCountryName("test");
        dto.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto.setLocalCurrency("test");
        dto.setEnabledTerritories(Collections.emptyList());
        dto.setRefreshIntervalMinutes(42);
        dto.setCreatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.CreateDashboardCommand dto = new CountryDashboardCommand.CreateDashboardCommand();
        dto.setTenantId("test");
        dto.setCountryCode("test");
        dto.setCountryName("test");
        dto.setType(CountrySalesDashboard.DashboardType.EXECUTIVE);
        dto.setLocalCurrency("test");
        dto.setEnabledTerritories(Collections.emptyList());
        dto.setRefreshIntervalMinutes(42);
        dto.setCreatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}