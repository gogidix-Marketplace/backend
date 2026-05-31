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
class CountryDashboardCommand_UpdateDashboardCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.UpdateDashboardCommand dto = new CountryDashboardCommand.UpdateDashboardCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.UpdateDashboardCommand dto1 = new CountryDashboardCommand.UpdateDashboardCommand();
        CountryDashboardCommand.UpdateDashboardCommand dto2 = new CountryDashboardCommand.UpdateDashboardCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.UpdateDashboardCommand dto = new CountryDashboardCommand.UpdateDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.UpdateDashboardCommand dto = new CountryDashboardCommand.UpdateDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setStatus(CountrySalesDashboard.DashboardStatus.ACTIVE);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}