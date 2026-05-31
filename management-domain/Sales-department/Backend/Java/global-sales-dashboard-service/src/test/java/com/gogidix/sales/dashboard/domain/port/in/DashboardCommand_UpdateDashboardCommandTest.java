package com.gogidix.sales.dashboard.domain.port.in;

import com.gogidix.sales.dashboard.domain.port.in.DashboardCommand;
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
class DashboardCommand_UpdateDashboardCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.UpdateDashboardCommand dto = new DashboardCommand.UpdateDashboardCommand();
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
        DashboardCommand.UpdateDashboardCommand dto1 = new DashboardCommand.UpdateDashboardCommand();
        DashboardCommand.UpdateDashboardCommand dto2 = new DashboardCommand.UpdateDashboardCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setStatus(null);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setStatus(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.UpdateDashboardCommand dto = new DashboardCommand.UpdateDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setStatus(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.UpdateDashboardCommand dto = new DashboardCommand.UpdateDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setStatus(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}