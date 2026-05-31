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
class CountryDashboardCommand_UpdateKPICommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.UpdateKPICommand dto = new CountryDashboardCommand.UpdateKPICommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setKpiId("val-kpiId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-kpiId", dto.getKpiId());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.UpdateKPICommand dto1 = new CountryDashboardCommand.UpdateKPICommand();
        CountryDashboardCommand.UpdateKPICommand dto2 = new CountryDashboardCommand.UpdateKPICommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setKpiId("test");
        dto1.setValue(null);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setKpiId("test");
        dto2.setValue(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.UpdateKPICommand dto = new CountryDashboardCommand.UpdateKPICommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setKpiId("test");
        dto.setValue(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.UpdateKPICommand dto = new CountryDashboardCommand.UpdateKPICommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setKpiId("test");
        dto.setValue(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}