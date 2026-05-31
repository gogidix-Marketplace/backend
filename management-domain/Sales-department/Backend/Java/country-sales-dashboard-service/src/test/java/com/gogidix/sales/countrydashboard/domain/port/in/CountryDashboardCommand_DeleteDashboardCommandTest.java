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
class CountryDashboardCommand_DeleteDashboardCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.DeleteDashboardCommand dto = new CountryDashboardCommand.DeleteDashboardCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.DeleteDashboardCommand dto1 = new CountryDashboardCommand.DeleteDashboardCommand();
        CountryDashboardCommand.DeleteDashboardCommand dto2 = new CountryDashboardCommand.DeleteDashboardCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.DeleteDashboardCommand dto = new CountryDashboardCommand.DeleteDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.DeleteDashboardCommand dto = new CountryDashboardCommand.DeleteDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}