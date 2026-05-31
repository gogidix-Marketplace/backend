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
class DashboardCommand_RefreshDashboardCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.RefreshDashboardCommand dto = new DashboardCommand.RefreshDashboardCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setUserId("val-userId");
        dto.setForceRefresh(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-userId", dto.getUserId());
        assertTrue(dto.getForceRefresh());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.RefreshDashboardCommand dto1 = new DashboardCommand.RefreshDashboardCommand();
        DashboardCommand.RefreshDashboardCommand dto2 = new DashboardCommand.RefreshDashboardCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setUserId("test");
        dto1.setForceRefresh(true);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setUserId("test");
        dto2.setForceRefresh(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.RefreshDashboardCommand dto = new DashboardCommand.RefreshDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setUserId("test");
        dto.setForceRefresh(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.RefreshDashboardCommand dto = new DashboardCommand.RefreshDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setUserId("test");
        dto.setForceRefresh(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}