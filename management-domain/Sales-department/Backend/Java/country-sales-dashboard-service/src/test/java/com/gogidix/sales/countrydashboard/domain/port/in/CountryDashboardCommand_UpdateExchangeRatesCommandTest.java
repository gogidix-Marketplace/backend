package com.gogidix.sales.countrydashboard.domain.port.in;

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
class CountryDashboardCommand_UpdateExchangeRatesCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.UpdateExchangeRatesCommand dto = new CountryDashboardCommand.UpdateExchangeRatesCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.UpdateExchangeRatesCommand dto1 = new CountryDashboardCommand.UpdateExchangeRatesCommand();
        CountryDashboardCommand.UpdateExchangeRatesCommand dto2 = new CountryDashboardCommand.UpdateExchangeRatesCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setExchangeRates(Collections.emptyMap());
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setExchangeRates(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.UpdateExchangeRatesCommand dto = new CountryDashboardCommand.UpdateExchangeRatesCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setExchangeRates(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.UpdateExchangeRatesCommand dto = new CountryDashboardCommand.UpdateExchangeRatesCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setExchangeRates(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}